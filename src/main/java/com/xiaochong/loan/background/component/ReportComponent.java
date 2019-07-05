package com.xiaochong.loan.background.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaochong.loan.background.component.akka.ComponentActor;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.ExtendInfoBo;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.AttributionInfoService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReportComponent extends ComponentActor {

    private Logger logger = LoggerFactory.getLogger(ReportComponent.class);

    private RiskDataComponent riskDataComponent = (RiskDataComponent) SpringContextUtil.getBean("riskDataComponent");

    private OrderDao orderDao = (OrderDao) SpringContextUtil.getBean("orderDao");

    private MerchantFlowOptionDao merchantFlowOptionDao = (MerchantFlowOptionDao) SpringContextUtil.getBean("merchantFlowOptionDao");

    private ReportUserInfoDao reportUserInfoDao = (ReportUserInfoDao) SpringContextUtil.getBean("reportUserInfoDao");

    private ReportEducationInfoDao reportEducationInfoDao = (ReportEducationInfoDao) SpringContextUtil.getBean("reportEducationInfoDao");

    private ReportZhimaInfoDao reportZhimaInfoDao = (ReportZhimaInfoDao) SpringContextUtil.getBean("reportZhimaInfoDao");

    private ReportZhimaExtendDao reportZhimaExtendDao = (ReportZhimaExtendDao) SpringContextUtil.getBean("reportZhimaExtendDao");

    private ContactInfoDao contactInfoDao = (ContactInfoDao) SpringContextUtil.getBean("contactInfoDao");

    private ReportContactRecordDao reportContactRecordDao = (ReportContactRecordDao) SpringContextUtil.getBean("reportContactRecord");

    private ReportCallRecordDao reportCallRecordDao = (ReportCallRecordDao) SpringContextUtil.getBean("reportCallRecordDao");

    private ReportTongdunRuleDao reportTongdunRuleDao = (ReportTongdunRuleDao) SpringContextUtil.getBean("reportTongdunRuleDao");

    private ReportLoanInfoDao reportLoanInfoDao = (ReportLoanInfoDao) SpringContextUtil.getBean("reportLoanInfoDao");

    private ReportBlackInfoDao reportBlackInfoDao = (ReportBlackInfoDao) SpringContextUtil.getBean("reportBlackInfoDao");

    private FlowOptionDao flowOptionDao = (FlowOptionDao) SpringContextUtil.getBean("flowOptionDao");

    private MerchantDao merchantDao = (MerchantDao) SpringContextUtil.getBean("merchantDao");

    private IncrementComponent incrementComponent = (IncrementComponent) SpringContextUtil.getBean("incrementComponent");

    private ReportDao reportDao = (ReportDao) SpringContextUtil.getBean("reportDao");

    private ReportYysReportDao reportYysReportDao = (ReportYysReportDao) SpringContextUtil.getBean("reportYysReportDao");

    private AttributionInfoService attributionInfoService = (AttributionInfoService) SpringContextUtil.getBean("attributionInfoService");

    private OrderUserInfoDao orderUserInfoDao = (OrderUserInfoDao) SpringContextUtil.getBean("orderUserInfoDao");

    private AccountRecordDao accountRecordDao = (AccountRecordDao) SpringContextUtil.getBean("accountRecordDao");

    private ReportLbsDao reportLbsDao = (ReportLbsDao) SpringContextUtil.getBean("reportLbsDao");

    private RiskReportDao riskReportDao = (RiskReportDao) SpringContextUtil.getBean("riskReportDao");

    private BaiduApiComponent baiduApiComponent = (BaiduApiComponent) SpringContextUtil.getBean("baiduApiComponent");

    @Override
    @Transactional
    protected void process(Object message) {
        AkkaRiskOrderParamsVo paramsVo = (AkkaRiskOrderParamsVo) message;
        logger.info("报告异步任务开始：{}", paramsVo.getOrderNum());
        RiskResultVo<String> creditReport = riskDataComponent.getCreditReport(paramsVo.getIdCard(), paramsVo.getOrderNum());
        RiskResultVo<String> yysCallCount = riskDataComponent.getYysCallCount(paramsVo.getIdCard());
        /*Order order = orderDao.selectOrderByOrdeNum(paramsVo.getOrderNum());
        assemblyReport(order.getMerId(), order.getOrderNo());*/
        if (Objects.equals(creditReport.getCode(), "100")) {
            //risk报告源数据入库
            RiskReport riskReport = new RiskReport();
            riskReport.setCreateTime(new Date());
            riskReport.setCreditreport(creditReport.getData());
            riskReport.setOrderNo(paramsVo.getOrderNum());
            riskReport.setIdCard(paramsVo.getIdCard());
            riskReportDao.insert(riskReport);

            //解析数据
            JSONObject reportInfo = JSON.parseObject(creditReport.getData());
            List<ReportYysInfo> reportYysInfos = JSON.parseArray(yysCallCount.getData(), ReportYysInfo.class);
            //解析数据
            Order order = orderDao.selectOrderByOrdeNum(paramsVo.getOrderNum());
            //各项数据入库
            String orderNo = order.getOrderNo();
            analysisUserInfo(reportInfo, orderNo);
            analysisEducationInfo(reportInfo, orderNo);
            analysisTongdun(reportInfo, orderNo);
            analysisZhima(reportInfo, orderNo);
            analysisYysReport(reportInfo, orderNo);
            analysisLbs(reportInfo, orderNo);
            if (CollectionUtils.isNotEmpty(reportYysInfos)) {
                analysisCallRecord(reportYysInfos, orderNo);
                analysisContacts(orderNo, reportYysInfos);
            }
            assemblyReport(order.getMerId(), orderNo);
            logger.info("报告异步完成：{}", paramsVo.getOrderNum());
        } else {
            logger.error("risk报告获取失败：code" + creditReport.getCode() + "message：" + creditReport.getMessage(), new Exception());
        }
    }



    private void assemblyReport(Integer merId, String orderNum) {
        List<ReportVo> reportVoList = new ArrayList<>();
        MerchantFlowOption searchObject = new MerchantFlowOption();
        searchObject.setMerchId(merId);
        List<MerchantFlowOption> merchantFlowOptions = merchantFlowOptionDao.selectMerchantFlowOption(searchObject);
        Map<String, List<MerchantFlowOption>> flowOptionMap = merchantFlowOptions.stream().collect(Collectors.groupingBy(MerchantFlowOption::getFlowNo));
        Order order = orderDao.selectOrderByOrdeNum(orderNum);
        OrderUserInfo orderUserInfo = new OrderUserInfo();
        orderUserInfo.setOrderNo(orderNum);
        orderUserInfo.setMerchId(merId);
        orderUserInfo = orderUserInfoDao.getByOrderUserInfo(orderUserInfo);
        flowOptionMap.forEach((k, v) -> {
            //用户基本信息
            if (MerchFlowNoEnum.BASE_INFO.getFlowNo().equals(k)) {
                List<Map<String, String>> userInfoList = new ArrayList<>();
                for (MerchantFlowOption merchantFlowOption : v) {
                    String flowOptionNo = merchantFlowOption.getFlowOptionNo();
                    FlowOption flowOption = flowOptionDao.selectByFlowOptionNo(flowOptionNo);
                    Map<String, String> userInfoMap = new HashMap<>();
                    String name = flowOption.getOptionDesc();
                    ReportUserInfo reportUserInfo = reportUserInfoDao.selectByOrderNumAndFiledName(orderNum, flowOption.getOptionName());
                    String value = "-";
                    if (Objects.nonNull(reportUserInfo)) {
                        value = StringUtils.isBlank(reportUserInfo.getFiledValue()) ? "-" : reportUserInfo.getFiledValue();
                    }
                    userInfoMap.put("name", name);
                    userInfoMap.put("value", value);
                    userInfoList.add(userInfoMap);
                }
                Map<String, String> userInfoMap = new HashMap<>();
                userInfoMap.put("name", "被调人手机号");
                userInfoMap.put("value", order.getPhone());
                userInfoList.add(userInfoMap);

                ReportVo<List<Map<String, String>>> reportVo = new ReportVo<>();
                reportVo.setOptionType(FlowOptionTypeEnum.USER_INFO.getType());
                reportVo.setTable(false);
                reportVo.setOptionData(userInfoList);
                reportVoList.add(reportVo);
            }
            //学信
            if (MerchFlowNoEnum.EDUCATION.getFlowNo().equals(k) ||
                    MerchFlowNoEnum.EDUCATION_NEW.getFlowNo().equals(k) ||
                    MerchFlowNoEnum.EDUCATION_TONGDUN.getFlowNo().equals(k) ) {
                List<Map<String, String>> educationList = new ArrayList<>();
                for (MerchantFlowOption merchantFlowOption : v) {
                    String flowOptionNo = merchantFlowOption.getFlowOptionNo();
                    FlowOption flowOption = flowOptionDao.selectByFlowOptionNo(flowOptionNo);
                    Map<String, String> educationMap = new HashMap<>();
                    String name = flowOption.getOptionDesc();
                    ReportEducationInfo reportEducationInfo = reportEducationInfoDao.selectByOrderNumAndFiledName(orderNum, flowOption.getOptionName());
                    String value = "-";
                    if (Objects.nonNull(reportEducationInfo)) {
                        value = StringUtils.isBlank(reportEducationInfo.getFiledValue()) ? "-" : reportEducationInfo.getFiledValue();
                        if("schoolName".equals(reportEducationInfo.getFiledName())){
                            JSONObject jsonObject = baiduApiComponent.placeSuggestion("全国", reportEducationInfo.getFiledValue());
                            if("0".equals(jsonObject.getString("status"))){
                                JSONObject location = jsonObject.getJSONArray("result").
                                        getJSONObject(0).getJSONObject("location");
                                Map<String, String> lat = new HashMap<>();
                                lat.put("name", "lat");
                                lat.put("value", location.getString("lat"));
                                educationList.add(lat);
                                Map<String, String> lng = new HashMap<>();
                                lng.put("name", "lng");
                                lng.put("value", location.getString("lng"));
                                educationList.add(lng);
                            }
                        }
                    }

                    educationMap.put("name", name);
                    educationMap.put("value", value);
                    educationList.add(educationMap);
                }
                ReportVo<List<Map<String, String>>> reportVo = new ReportVo<>();
                reportVo.setOptionType(FlowOptionTypeEnum.EDUCATION_INFO.getType());
                reportVo.setTable(false);
                reportVo.setOptionData(educationList);
                reportVoList.add(reportVo);
            }
            //芝麻
            if (MerchFlowNoEnum.ZHIMA.getFlowNo().equals(k)) {
                List<ReportZhimaInfo> reportZhimaInfos = reportZhimaInfoDao.selectByOrderNum(orderNum);
                List<ReportTableRemarkVo> reportTableRemarkVos = new ArrayList<>();
                for (ReportZhimaInfo reportZhimaInfo : reportZhimaInfos) {
                    String code = reportZhimaInfo.getCode();
                    String type = reportZhimaInfo.getType();
                    String bizCode = reportZhimaInfo.getBizCode();
                    String level = reportZhimaInfo.getLevel();
                    List<ReportZhimaExtend> reportZhimaExtends = reportZhimaExtendDao.selectByZhimaId(reportZhimaInfo.getId());
                    List<String> extendInfo = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(reportZhimaExtends)) {
                        for (ReportZhimaExtend reportZhimaExtend : reportZhimaExtends) {
                            String zhimaExtendInfo = "描述：" + reportZhimaExtend.getDescription() + " 结果：" + reportZhimaExtend.getValue();
                            extendInfo.add(zhimaExtendInfo);
                        }
                    }
                    ReportTableRemarkVo reportTableRemarkVo = new ReportTableRemarkVo();
                    reportTableRemarkVo.setName(bizCode + " " + type + " " + code);
                    reportTableRemarkVo.setValue(level);
                    reportTableRemarkVo.setRemark(extendInfo);

                    reportTableRemarkVos.add(reportTableRemarkVo);

                }
                ReportVo<List<ReportTableRemarkVo>> reportVo = new ReportVo<>();
                reportVo.setOptionType(FlowOptionTypeEnum.WATCH_DETAILS.getType());
                reportVo.setTable(true);
                reportVo.setTableType(TableTypeEnum.remark.getType());
                reportVo.setOptionData(reportTableRemarkVos);

                reportVoList.add(reportVo);
            }
            //运营商
            if (MerchFlowNoEnum.OPERATOR.getFlowNo().equals(k)) {

                List<String> flowOptionNoList = new ArrayList<>();
                for (MerchantFlowOption merchantFlowOption : v) {
                    flowOptionNoList.add(merchantFlowOption.getFlowOptionNo());
                }
                List<FlowOption> flowOptions = flowOptionDao.selectByFlowOptionNoList(flowOptionNoList);

                Map<String, List<FlowOption>> groupByType = flowOptions.stream().collect(Collectors.groupingBy(FlowOption::getOptionType));

                groupByType.forEach((type, flowOptionList) -> {
                    if (type.equals(FlowOptionTypeEnum.LINKMAN_LIST_OF_LAST_SIX_MONTH.getType())) {
                        //通话记录
                        List<ReportCallRecord> reportCallRecords = reportCallRecordDao.selectByOrderNum(orderNum);
                        List<CallRecordVo> callRecordVoList = new ArrayList<>();
                        for (ReportCallRecord reportCallRecord : reportCallRecords) {
                            CallRecordVo callRecordVo = new CallRecordVo();
                            callRecordVo.setPhone(reportCallRecord.getPhone());
                            callRecordVo.setCalledCount(reportCallRecord.getCalledCount());
                            callRecordVo.setDialingCount(reportCallRecord.getDialingCount());
                            Long durationLong = StringUtils.isNotBlank(reportCallRecord.getDuration())?
                                    Long.valueOf(reportCallRecord.getDuration()):new Long(0);
                            Long duration = durationLong.longValue()/60L;
                            callRecordVo.setDuration(duration.longValue()==0L&&durationLong>0L?1+"":duration.toString());
                            callRecordVo.setFrequency(reportCallRecord.getFrequency());
                            callRecordVo.setLocation(reportCallRecord.getLocation());
                            callRecordVo.setTag(reportCallRecord.getTag());
                            callRecordVoList.add(callRecordVo);
                        }
                        ReportVo<List<CallRecordVo>> callRecordReportVo = new ReportVo<>();
                        callRecordReportVo.setOptionType(FlowOptionTypeEnum.LINKMAN_LIST_OF_LAST_SIX_MONTH.getType());
                        callRecordReportVo.setTableType(TableTypeEnum.callRecord.getType());
                        callRecordReportVo.setTable(true);
                        callRecordReportVo.setOptionData(callRecordVoList);

                        reportVoList.add(callRecordReportVo);
                    }
                    if (type.equals(FlowOptionTypeEnum.LINKMAN_INFO.getType())) {
                        //联系人通话记录
                        List<ReportContactRecord> reportContactRecords = reportContactRecordDao.selectByOrderNum(orderNum);
                        List<CallRecordVo> contactRecordVoList = new ArrayList<>();
                        for (ReportContactRecord reportContactRecord : reportContactRecords) {
                            CallRecordVo callRecordVo = new CallRecordVo();
                            callRecordVo.setPhone(reportContactRecord.getPhone());
                            callRecordVo.setCalledCount(reportContactRecord.getCalledCount());
                            callRecordVo.setDialingCount(reportContactRecord.getDialingCount());
                            Long durationLong = StringUtils.isNotBlank(reportContactRecord.getDuration())?
                                    Long.valueOf(reportContactRecord.getDuration()):new Long(0);
                            Long duration = durationLong.longValue()/60L;

                            callRecordVo.setDuration(duration.longValue()==0L&&durationLong>0L?1+"":duration.toString());
                            callRecordVo.setFrequency(reportContactRecord.getFrequency());
                            callRecordVo.setLocation(reportContactRecord.getLocation());
                            callRecordVo.setTag(reportContactRecord.getTag());
                            contactRecordVoList.add(callRecordVo);
                        }
                        ReportVo<List<CallRecordVo>> contactRecordReportVo = new ReportVo<>();
                        contactRecordReportVo.setOptionType(FlowOptionTypeEnum.LINKMAN_INFO.getType());
                        contactRecordReportVo.setTableType(TableTypeEnum.callRecord.getType());
                        contactRecordReportVo.setTable(true);
                        contactRecordReportVo.setOptionData(contactRecordVoList);
                        reportVoList.add(contactRecordReportVo);
                    }
                    if (type.equals(FlowOptionTypeEnum.PHONE_INFO.getType())) {
                        //手机信息
                        List<Map<String, String>> mobileInfoList = new ArrayList<>();
                        for (FlowOption flowOption : flowOptionList) {
                            Map<String, String> mobileInfo = new HashMap<>();
                            String filedName = flowOption.getOptionName();
                            String name = flowOption.getOptionDesc();
                            ReportYysReportInfo reportYysReportInfo = reportYysReportDao.selectByFiledNameAndOrderNum(orderNum, filedName);
                            String value = "-";
                            if (Objects.nonNull(reportYysReportInfo)) {
                                value = StringUtils.isBlank(reportYysReportInfo.getFiledValue()) ? "-" : reportYysReportInfo.getFiledValue();
                            }
                            mobileInfo.put("name", name);
                            mobileInfo.put("value", value);
                            mobileInfoList.add(mobileInfo);
                            if("姓名和手机号是否在金融机构黑名单".equals(name)){
                                for (int i = 0; i < reportVoList.size(); i++) {
                                    if(reportVoList.get(i)!=null &&
                                            FlowOptionTypeEnum.USER_INFO.getType().equals(reportVoList.get(i).getOptionType())){
                                        Map<String, String> map = new HashMap<>();
                                        map.put("name", name);
                                        map.put("value", value);
                                        ReportVo reportVo = reportVoList.get(i);
                                        List<Map<String, String>> optionData =
                                                (List<Map<String, String>>)reportVo.getOptionData();
                                        optionData.add(map);
                                        reportVo.setOptionData(optionData);
                                        reportVoList.set(i,reportVo);
                                        break;
                                    }
                                }
                            }
                        }

                        ReportVo<List<Map<String, String>>> reportVo = new ReportVo<>();
                        reportVo.setOptionData(mobileInfoList);
                        reportVo.setTable(false);
                        reportVo.setOptionType(FlowOptionTypeEnum.PHONE_INFO.getType());
                        reportVoList.add(reportVo);
                    }
                    if (type.equals(FlowOptionTypeEnum.USER_QUERY_INFO.getType())) {
                        //用户查询信息
                        List<Map<String, String>> userSearchInfoList = new ArrayList<>();
                        for (FlowOption flowOption : flowOptionList) {
                            Map<String, String> userSearchInfo = new HashMap<>();
                            String filedName = flowOption.getOptionName();
                            String name = flowOption.getOptionDesc();
                            ReportYysReportInfo reportYysReportInfo = reportYysReportDao.selectByFiledNameAndOrderNum(orderNum, filedName);
                            String value = "-";
                            if (Objects.nonNull(reportYysReportInfo)) {
                                value = StringUtils.isBlank(reportYysReportInfo.getFiledValue()) ? "-" : reportYysReportInfo.getFiledValue();
                            }
                            userSearchInfo.put("name", name);
                            userSearchInfo.put("value", value);
                            userSearchInfoList.add(userSearchInfo);
                        }

                        ReportVo<List<Map<String, String>>> reportVo = new ReportVo<>();
                        reportVo.setOptionData(userSearchInfoList);
                        reportVo.setTable(false);
                        reportVo.setOptionType(FlowOptionTypeEnum.USER_QUERY_INFO.getType());
                        reportVoList.add(reportVo);
                    }
                    if (type.equals(FlowOptionTypeEnum.COMMUNICATION_BLACKLIST_INFO.getType())) {
                        //通讯录黑名单
                        List<Map<String, String>> contactBlackInfoList = new ArrayList<>();
                        for (FlowOption flowOption : flowOptionList) {
                            Map<String, String> contactBlackInfo = new HashMap<>();
                            String filedName = flowOption.getOptionName();
                            String name = flowOption.getOptionDesc();
                            ReportYysReportInfo reportYysReportInfo = reportYysReportDao.selectByFiledNameAndOrderNum(orderNum, filedName);
                            String value = "-";
                            if (Objects.nonNull(reportYysReportInfo)) {
                                value = StringUtils.isBlank(reportYysReportInfo.getFiledValue()) ? "-" : reportYysReportInfo.getFiledValue();
                            }
                            contactBlackInfo.put("name", name);
                            contactBlackInfo.put("value", value);
                            contactBlackInfoList.add(contactBlackInfo);
                        }
                        ReportVo<List<Map<String, String>>> reportVo = new ReportVo<>();
                        reportVo.setOptionData(contactBlackInfoList);
                        reportVo.setTable(false);
                        reportVo.setOptionType(FlowOptionTypeEnum.COMMUNICATION_BLACKLIST_INFO.getType());
                        reportVoList.add(reportVo);
                    }
                });
            }
        });

        //同盾数据是必然会有不在配置中
        List<ReportTongdunRule> reportTongdunRules = reportTongdunRuleDao.selectByOrderNum(orderNum);
        Map<Integer, List<ReportTongdunRule>> tongdunRuleMap = reportTongdunRules.stream().collect(Collectors.groupingBy(ReportTongdunRule::getType));
        tongdunRuleMap.forEach((type, list) -> {
            if (type == 0) {
                List<ReportTableRemarkVo> tongdunLoan = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(list)) {
                    for (ReportTongdunRule reportTongdunRule : list) {
                        List<ReportLoanInfo> reportLoanInfos = reportLoanInfoDao.selectByRuleId(reportTongdunRule.getId());
                        String name = reportTongdunRule.getItemname();
                        String value = reportTongdunRule.getCount();
                        List<String> loanInfo = new ArrayList<>();
                        if (CollectionUtils.isNotEmpty(reportLoanInfos)) {
                            for (ReportLoanInfo reportLoanInfo : reportLoanInfos) {
                                String remark = reportLoanInfo.getCount() + "  " + reportLoanInfo.getName();
                                loanInfo.add(remark);
                            }
                        }
                        ReportTableRemarkVo reportTableRemarkVo = new ReportTableRemarkVo();
                        if(Pattern.matches("7天内申请人在多个平台申请借款", name) ||
                                Pattern.matches("1个月内申请人在多个平台申请借款", name) ||
                                Pattern.matches("3个月内申请人在多个平台申请借款", name) ||
                                Pattern.matches("6个月内申请人在多个平台申请借款", name) ||
                                Pattern.matches("与贷款类号码联系情况", name) ||
                                Pattern.matches("与银行类号码联系情况", name) ||
                                Pattern.matches("与信用卡类号码联系情况", name) ||
                                Pattern.matches("与催收类号码联系情况", name)
                                ){
                            reportTableRemarkVo.setName(name);
                            reportTableRemarkVo.setValue(value);
                            reportTableRemarkVo.setRemark(loanInfo);
                            tongdunLoan.add(reportTableRemarkVo);
                        }

                    }
                }
                ReportVo<List<ReportTableRemarkVo>> reportVo = new ReportVo<>();
                reportVo.setOptionType(FlowOptionTypeEnum.DEBIT_AND_CREDIT_RECORD.getType());
                reportVo.setTable(true);
                reportVo.setTableType(TableTypeEnum.remark.getType());
                reportVo.setOptionData(tongdunLoan);
                reportVoList.add(reportVo);
            }
            if (type == 1) {
                List<ReportTableRemarkVo> tongdunLoan = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(list)) {
                    for (ReportTongdunRule reportTongdunRule : list) {
                        List<ReportBlackInfo> reportBlackInfos = reportBlackInfoDao.selectByRuleId(reportTongdunRule.getId());
                        String name = reportTongdunRule.getItemname();
                        String value = reportTongdunRule.getLevel();
                        List<String> loanInfo = new ArrayList<>();
                        if (CollectionUtils.isNotEmpty(reportBlackInfos)) {
                            for (ReportBlackInfo reportBlackInfo : reportBlackInfos) {
                                String remark = "详情：" + reportBlackInfo.getName() + " 结果：" + reportBlackInfo.getLevel();
                                loanInfo.add(remark);
                            }
                        }
                        ReportTableRemarkVo reportTableRemarkVo = new ReportTableRemarkVo();
                        if(Pattern.matches("身份证命中.*", name)){
                            reportTableRemarkVo.setTitle("身份证号不良信息扫描");
                        }else if(Pattern.matches("手机号命中.*", name)){
                            reportTableRemarkVo.setTitle("手机号不良信息扫描");
                        }else if(Pattern.matches("身份证_姓名.*", name)){
                            reportTableRemarkVo.setTitle("姓名+身份证号模糊检测");
                        }else{
                            reportTableRemarkVo.setTitle("用户行为检测");
                        }
                        reportTableRemarkVo.setName(name);
                        reportTableRemarkVo.setValue(value);
                        reportTableRemarkVo.setRemark(loanInfo);
                        tongdunLoan.add(reportTableRemarkVo);
                    }
                }
                ReportVo<List<ReportTableRemarkVo>> reportVo = new ReportVo<>();
                reportVo.setOptionType(FlowOptionTypeEnum.BADNESS_SCAN_RECORD.getType());
                reportVo.setTable(true);
                reportVo.setTableType(TableTypeEnum.remark.getType());
                reportVo.setOptionData(tongdunLoan);
                reportVoList.add(reportVo);
            }
        });

        if (tongdunRuleMap.get(0) == null){
            ReportVo<List<ReportTableRemarkVo>> reportVo = new ReportVo<>();
            reportVo.setOptionType(FlowOptionTypeEnum.DEBIT_AND_CREDIT_RECORD.getType());
            reportVo.setTable(true);
            reportVo.setTableType(TableTypeEnum.remark.getType());
            reportVo.setOptionData(new ArrayList<>());
            reportVoList.add(reportVo);
        }else if (tongdunRuleMap.get(1) == null){
            ReportVo<List<ReportTableRemarkVo>> reportVo = new ReportVo<>();
            reportVo.setOptionType(FlowOptionTypeEnum.BADNESS_SCAN_RECORD.getType());
            reportVo.setTable(true);
            reportVo.setTableType(TableTypeEnum.remark.getType());
            reportVo.setOptionData(new ArrayList<>());
            reportVoList.add(reportVo);
        }

        //位置信息
        ReportLbs searchReportLbs = new ReportLbs();
        searchReportLbs.setOrderNo(orderNum);
        List<ReportLbs> reportLbsList = reportLbsDao.listByReportLbs(searchReportLbs);
        logger.info("reportLbsDao.listByReportLbs【orderNum={}】【{}】",orderNum,reportLbsList);
        List<Map<String, String>> lbsMapList = new ArrayList<>();
        for (ReportLbs reportLbs:reportLbsList) {
            Map<String, String> lbsMap = new HashMap<>();
            lbsMap.put("key",reportLbs.getFiledName());
            lbsMap.put("name",reportLbs.getFiledDesc());
            lbsMap.put("value",StringUtils.isNotBlank(reportLbs.getFiledValue())?reportLbs.getFiledValue():"-");
            lbsMapList.add(lbsMap);
        }

        ReportEducationInfo reportEducationInfo =
                reportEducationInfoDao.selectByOrderNumAndFiledName(orderNum, "schoolName");
        if(reportEducationInfo!=null){
            JSONObject jsonObject = baiduApiComponent.placeSuggestion(reportEducationInfo.getFiledValue(), "全国");
            Map<String, String> schoolName = new HashMap<>();
            schoolName.put("key", "schoolName");
            schoolName.put("name", "学校名称");
            schoolName.put("value", reportEducationInfo.getFiledValue());
            lbsMapList.add(schoolName);
            if("0".equals(jsonObject.getString("status"))){
                JSONObject result = jsonObject.getJSONArray("result").
                        getJSONObject(0);
                JSONObject location = result.getJSONObject("location");
                Map<String, String> edulocation = new HashMap<>();
                edulocation.put("key", "edu_location");
                edulocation.put("name", "学校经纬度");
                edulocation.put("value", location.toJSONString());
                lbsMapList.add(edulocation);
                JSONObject geocoder = baiduApiComponent.geocoder(
                        location.getString("lat"), location.getString("lng"), BaiduCoordTypeEnum.BAIDU);
                if("0".equals(geocoder.getString("status"))){
                    String address = geocoder.getJSONObject("result").
                            getString("formatted_address");
                    Map<String, String> eduAddress = new HashMap<>();
                    eduAddress.put("key", "eduAddress");
                    eduAddress.put("name", "学校地址");
                    eduAddress.put("value", address);
                    lbsMapList.add(eduAddress);
                }
            }
        }
        if(orderUserInfo!=null){

            if(orderUserInfo.getAuthStartTime()!=null){
                Map<String, String> lbsMap = new HashMap<>();
                lbsMap.put("key","authStartTime");
                lbsMap.put("name","认证开始时间");
                lbsMap.put("value",DateUtils.format(orderUserInfo.getAuthStartTime(),DateUtils.ymdhms_format));
                lbsMapList.add(lbsMap);
            }

            if(orderUserInfo.getAuthEndTime()!=null){
                Map<String, String> lbsMap = new HashMap<>();
                lbsMap.put("key","authEndTime");
                lbsMap.put("name","认证结束时间");
                lbsMap.put("value",DateUtils.format(orderUserInfo.getAuthEndTime(),DateUtils.ymdhms_format));
                lbsMapList.add(lbsMap);
            }
            if(orderUserInfo.getAuthStartTime()!=null&&orderUserInfo.getAuthEndTime()!=null){
                LocalDateTime start = DateUtils.dateToLocalDateTime(orderUserInfo.getAuthStartTime());
                LocalDateTime end = DateUtils.dateToLocalDateTime(orderUserInfo.getAuthEndTime());

                Duration duration = Duration.between(start,end);
                System.out.println(duration.toDays()+"-"+duration.toHours()+"-"+duration.toMinutes());
                long d = 0;
                long h = 0;
                long m = 0;
                if(duration.toMinutes()>=60){
                    h = duration.toMinutes()/60;
                    m = duration.toMinutes() - h*60;
                    if(h>=24){
                        d = h/24;
                        h = h - d*24;
                    }

                }else{
                    duration.toMinutes();
                }
                if(d==0L && h==0L && m==0L){
                    m=1;
                }
                Map<String, String> lbsMap = new HashMap<>();
                lbsMap.put("key","authConsuming");
                lbsMap.put("name","认证耗时");
                lbsMap.put("value",d+"天"+h+"小时"+m+"分钟");
                lbsMapList.add(lbsMap);
            }
            if(StringUtils.isNotBlank(orderUserInfo.getLatitude()) && StringUtils.isNotBlank(orderUserInfo.getLongitude())){
                JSONObject geoconvJson = baiduApiComponent.geoconv(orderUserInfo.getLatitude(), orderUserInfo.getLongitude());
                if("0".equals(geoconvJson.getString("status"))){
                    JSONObject result = geoconvJson.getJSONArray("result").getJSONObject(0);
                    Map<String, String> lbsMap = new HashMap<>();
                    lbsMap.put("key","baiduGisLocation");
                    lbsMap.put("name","百度格式经纬度");
                    lbsMap.put("value",result.toJSONString());
                    lbsMapList.add(lbsMap);
                }
            }


            if(StringUtils.isNotBlank(orderUserInfo.getEndLatitude()) && StringUtils.isNotBlank(orderUserInfo.getEndLongitude())){
                Map<String, String> endLocation = new HashMap<>();
                endLocation.put("key","endLocation");
                endLocation.put("name","订单授权结束时的经纬度");
                JSONObject location = new JSONObject();
                location.put("latitude",orderUserInfo.getEndLatitude());
                location.put("longitude",orderUserInfo.getEndLongitude());
                endLocation.put("value",location.toJSONString());
                lbsMapList.add(endLocation);
                JSONObject geoconvJson = baiduApiComponent.geoconv(orderUserInfo.getEndLatitude(), orderUserInfo.getEndLongitude());
                if("0".equals(geoconvJson.getString("status"))){
                    JSONObject result = geoconvJson.getJSONArray("result").getJSONObject(0);
                    Map<String, String> lbsMap = new HashMap<>();
                    lbsMap.put("key","baiduEndLocation");
                    lbsMap.put("name","百度格式订单授权结束时的经纬度");
                    lbsMap.put("value",result.toJSONString());
                    lbsMapList.add(lbsMap);
                }

                JSONObject geocoder = baiduApiComponent.geocoder(
                        orderUserInfo.getEndLatitude(), orderUserInfo.getEndLongitude(),BaiduCoordTypeEnum.GPS);
                if("0".equals(geocoder.getString("status"))){
                    Map<String, String> lbsMap = new HashMap<>();
                    String address = geocoder.getJSONObject("result").
                            getString("formatted_address");
                    lbsMap.put("key","address");
                    lbsMap.put("name","订单授权结束时的地址");
                    lbsMap.put("value",address);
                    lbsMapList.add(lbsMap);
                }
            }

        }
        ReportVo<List<Map<String, String>>> reportVo = new ReportVo<>();
        reportVo.setOptionType(FlowOptionTypeEnum.LBS_INFO.getType());
        reportVo.setTable(false);
        reportVo.setOptionData(lbsMapList);
        reportVoList.add(reportVo);



        Merchantinfo merchantinfo = merchantDao.findMerchantinfoByToid(merId);
        //报告入库
        Report report = new Report();
        report.setCreateTime(new Date());
        report.setOrderTime(order.getCreatetime());
        report.setOrderNo(order.getOrderNo());
        report.setMerName(merchantinfo.getMerchantName());
        report.setReportContent(JSON.toJSONString(reportVoList));
        report.setReportNo(merchantinfo.getEnName() + orderNum);
        String reportNo = DateUtils.getCurrentDateyymd() + incrementComponent.getCountNo(CountTypeEnum.REPORT);
        report.setReportNo(merchantinfo.getEnName() + reportNo);
        //数据入库
        reportDao.insertOrUpdateByOrderNum(report);

        //修改报告状态
        order.setStatus(OrderStatusEnum.FINISH.getType());
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setOrderNo(order.getOrderNo());
        accountRecord.setOrderStatus(order.getStatus());
        accountRecord.setTransactType(TransactTypeEnum.EXPEND.getType());
        accountRecordDao.updateAccountRecordByOrderNo(accountRecord);
        orderDao.update(order);
        logger.info("产出报告完成，更改订单状态{}", order);
    }


    private void analysisLbs(JSONObject reportInfo, String orderNo) {
        JSONObject riskOrderLbsBo = reportInfo.getJSONObject("riskOrderLbsBo");
        logger.info("解析地理位置信息:riskOrderLbsBo:{}",riskOrderLbsBo.toJSONString());
        Set<String> keySet = riskOrderLbsBo.keySet();
        Date now = new Date();
        for (String key: keySet) {
            String desc = null;
            if("orderIporderIp".equals(key)){desc = "下单IP地址";}
            else if("orderIpProvince".equals(key)){desc = "IP对应的省份";}
            else if("orderIpCity".equals(key)){desc = "IP对应的市";}
            else if("gisLongitude".equals(key)){desc = "订单提交时的经度";}
            else if("gisLatitude".equals(key)){desc = "订单提交时的纬度";}
            else if("gisProvince".equals(key)){desc = "经纬度对应的省";}
            else if("gisCity".equals(key)){desc = "经纬度对应的市";}
            else if("createTime".equals(key)){desc = "创建时间";}
            else if("formattedAddress".equals(key)){desc = "格式化地址";}
            else if("business".equals(key)){desc = "商圈信息";}
            //else if("evtInfo".equals(key)){desc = "周边环境信息";}
            else if("gisDistrict".equals(key)){desc = "gis区";}
            if(desc==null){continue;}

            ReportLbs reportLbs = new ReportLbs();
            reportLbs.setOrderNo(orderNo);
            reportLbs.setFiledName(key);
            reportLbs = reportLbsDao.getByReportLbs(reportLbs);
            if(reportLbs==null){
                reportLbs = new ReportLbs();
                reportLbs.setFiledName(key);
                reportLbs.setFiledValue(riskOrderLbsBo.getString(key));
                reportLbs.setFiledDesc(desc);
                reportLbs.setOrderNo(orderNo);
                reportLbs.setCreateTime(now);

                logger.info("reportLbs.insert:【{}】【{}】",reportLbsDao.insert(reportLbs),reportLbs);
            }else{
                reportLbs.setFiledName(key);
                reportLbs.setFiledValue(riskOrderLbsBo.getString(key));
                reportLbs.setFiledDesc(desc);
                reportLbs.setCreateTime(now);
                logger.info("reportLbs.update:【{}】【{}】",reportLbsDao.update(reportLbs),reportLbs);
            }


        }
    }

    /**
     * 用户基本信息
     *
     * @param reportInfo 报告信息
     * @param orderNum   订单编号
     */
    private void analysisUserInfo(JSONObject reportInfo, String orderNum) {
        //用户基本信息数据
        String zmScore = reportInfo.getString("zmScore");
        String finalScore = reportInfo.getString("finalScore");

        JSONObject userInfo = reportInfo.getJSONObject("userInfo");
        JSONObject work = reportInfo.getJSONObject("work");
        if (userInfo != null) {
            String realName = userInfo.getString("realName");
            String idCard = userInfo.getString("idCard");

            String sex = "-";
            String age = "-";
            if (StringUtils.isNotBlank(idCard)) {
                sex = IdCardUtil.getSexByIdCard(idCard).equals("man") ? "男" : "女";
                age = IdCardUtil.getAgeByIdCard(idCard) != null ? IdCardUtil.getAgeByIdCard(idCard).toString() : "-";
            }
            String mobile = userInfo.getString("mobile");
            String familyPhone = userInfo.getString("familyPhone");
            String identity = userInfo.getString("identity");
            String address = userInfo.getString("address");
            String qq = userInfo.getString("qq");
            String wx = userInfo.getString("wx");
            String companyName = "-";
            String companPhone = "-";
            String companyAddress = "-";
            if (work != null) {
                companyName = work.getString("companyName");
                companPhone = work.getString("companPhone");
                companyAddress = work.getString("companyAddress");
            }

            Map<String, String> userInfoMap = new HashMap<>();
            userInfoMap.put("zmScore", zmScore);
            userInfoMap.put("finalScore", finalScore);
            userInfoMap.put("realName", realName);
            userInfoMap.put("idCard", idCard);
            userInfoMap.put("age", age);
            userInfoMap.put("sex", sex);
            userInfoMap.put("mobile", mobile);
            userInfoMap.put("familyPhone", familyPhone);
            switch (identity) {
                case "1":
                    userInfoMap.put("identity", "学生");
                    break;
                case "2":
                    userInfoMap.put("identity", "职业人");
                    break;
                case "3":
                    userInfoMap.put("identity", "无业");
                    break;
                default:
                    userInfoMap.put("identity", "-");
            }
            userInfoMap.put("qq", qq);
            userInfoMap.put("wx", wx);
            userInfoMap.put("companyName", companyName);
            userInfoMap.put("companPhone", companPhone);
            userInfoMap.put("companyAddress", companyAddress);

            OrderUserInfo searchObj = new OrderUserInfo();
            searchObj.setOrderNo(orderNum);
            OrderUserInfo orderUserInfo = orderUserInfoDao.getByOrderUserInfo(searchObj);
            if (Objects.nonNull(orderUserInfo)) {
                userInfoMap.put("cityName", StringUtils.isBlank(orderUserInfo.getPermanentAddress()) ? "-" : orderUserInfo.getPermanentAddress());
                String province = orderUserInfo.getHomeAddressProvince();
                String city = orderUserInfo.getHomeAddressCity();
                String area = orderUserInfo.getHomeAddressArea();
                String homeAddress = orderUserInfo.getHomeAddress();
                if (StringUtils.isNotBlank(province) || StringUtils.isNotBlank(city) || StringUtils.isNotBlank(area) || StringUtils.isNotBlank(homeAddress)) {
                    address = province + city + area + homeAddress;
                }
                userInfoMap.put("address", address);
            }


            Date date = new Date();
            userInfoMap.forEach((k, v) -> {
                ReportUserInfo reportUserInfo = new ReportUserInfo();
                reportUserInfo.setFiledName(k);
                reportUserInfo.setFiledValue(v);
                reportUserInfo.setOrderNo(orderNum);
                reportUserInfo.setCreateTime(date);
                reportUserInfoDao.insertOrUpdateByOrderNum(reportUserInfo);
            });
        }
    }


    /**
     * 学信网数据
     *
     * @param reportInfo 报告json
     * @param orderNum   订单编号
     */
    private void analysisEducationInfo(JSONObject reportInfo, String orderNum) {
        JSONObject student = reportInfo.getJSONObject("student");
        if (student != null) {
            String nation = student.getString("nation");
            String schoolName = student.getString("schoolName");
            String educationLevel = student.getString("educationLevel");
            String majorName = student.getString("majorName");
            String studyType = student.getString("studyType");
            String dateGraduation = student.getString("dateGraduation");
            String dateEnrollment = student.getString("dateEnrollment");
            String schoolStatus = student.getString("schoolStatus");
            Map<String, String> educationInfoMap = new HashMap<>();
            educationInfoMap.put("nation", nation);
            educationInfoMap.put("schoolName", schoolName);
            educationInfoMap.put("educationLevel", educationLevel);
            educationInfoMap.put("majorName", majorName);
            educationInfoMap.put("studyType", studyType);
            educationInfoMap.put("dateGraduation", dateGraduation);
            educationInfoMap.put("dateEnrollment",dateEnrollment);
            educationInfoMap.put("schoolStatus",schoolStatus);

            Date date = new Date();
            educationInfoMap.forEach((k, v) -> {
                if (k.equals("nation")) {
                    ReportUserInfo reportUserInfo = new ReportUserInfo();
                    reportUserInfo.setCreateTime(date);
                    reportUserInfo.setOrderNo(orderNum);
                    reportUserInfo.setFiledValue(v);
                    reportUserInfo.setFiledName(k);
                    reportUserInfoDao.insertOrUpdateByOrderNum(reportUserInfo);
                } else {
                    ReportEducationInfo reportEducationInfo = new ReportEducationInfo();
                    reportEducationInfo.setFiledName(k);
                    reportEducationInfo.setFiledValue(v);
                    reportEducationInfo.setOrderNo(orderNum);
                    reportEducationInfo.setCreateTime(date);
                    reportEducationInfoDao.insertOrUpdateByOrderNum(reportEducationInfo);
                }
            });
        }
    }

    /**
     * 芝麻行业关注名单
     *
     * @param reportInfo 报告实体
     * @param orderNum   订单编号
     */
    private void analysisZhima(JSONObject reportInfo, String orderNum) {
        Date date = new Date();
        JSONObject zhimaIvs = reportInfo.getJSONObject("ivsOrIndustryBo");
        if (zhimaIvs != null) {
            String ivsScore = zhimaIvs.getString("ivsScore");
            ReportUserInfo reportUserInfo = new ReportUserInfo();
            reportUserInfo.setFiledName("ivsScore");
            reportUserInfo.setFiledValue(StringUtils.isBlank(ivsScore)?"-":ivsScore);
            reportUserInfo.setOrderNo(orderNum);
            reportUserInfo.setCreateTime(date);
            reportUserInfoDao.insertOrUpdateByOrderNum(reportUserInfo);
            if (StringUtils.isNotBlank(zhimaIvs.getString("watchDetails"))){
                String watchDetails = zhimaIvs.getString("watchDetails");
                List<WatchDetailsVo> watchDetailsVoList = JSON.parseArray(watchDetails, WatchDetailsVo.class);
                for (WatchDetailsVo watchDetailsVo : watchDetailsVoList) {
                    String biz_code = watchDetailsVo.getBizCode();
                    String level = watchDetailsVo.getLevel();
                    String type = watchDetailsVo.getType();
                    String code = watchDetailsVo.getCode();
                    String refresh_time = watchDetailsVo.getRefreshTime();
                    String settlement = watchDetailsVo.getSettlement();
                    String status = watchDetailsVo.getStatus();
                    String statement = watchDetailsVo.getStatement();
                    //芝麻行业名单数据入库
                    ReportZhimaInfo reportZhimaInfo = new ReportZhimaInfo();
                    reportZhimaInfo.setBizCode(ZhimaListContrastComponent.industryMap.get(biz_code));
                    reportZhimaInfo.setCode(ZhimaListContrastComponent.codeMap.get(code));
                    reportZhimaInfo.setLevel(ZhimaLevelEmum.getNameByType(level));
                    reportZhimaInfo.setRefreshTime(refresh_time);
                    reportZhimaInfo.setType(ZhimaListContrastComponent.typeMap.get(type));
                    reportZhimaInfo.setSettlement(settlement);
                    reportZhimaInfo.setStatus(status);
                    reportZhimaInfo.setStatement(statement);
                    reportZhimaInfo.setOrderNo(orderNum);
                    reportZhimaInfo.setCreateTime(date);
                    reportZhimaInfoDao.insertOrUpdateByOrderNoAndCode(reportZhimaInfo);

                    Integer zhimaInfoId = reportZhimaInfo.getId();

                    if (CollectionUtils.isNotEmpty(watchDetailsVo.getExtendInfo())) {
                        for (ExtendInfoBo extendInfoBo : watchDetailsVo.getExtendInfo()) {
                            String key = extendInfoBo.getKey();
                            String value = extendInfoBo.getValue();
                            String description = extendInfoBo.getDescription();

                            ReportZhimaExtend reportZhimaExtend = new ReportZhimaExtend();
                            reportZhimaExtend.setDescription(description);
                            reportZhimaExtend.setKey(key);
                            if (key.equals("event_max_amt_code")) {
                                reportZhimaExtend.setValue(ZhimaListContrastComponent.amtCodeMap.get(value));
                            } else {
                                reportZhimaExtend.setValue(value);
                            }
                            reportZhimaExtend.setZhimaId(zhimaInfoId);
                            reportZhimaExtendDao.insertOrUpdateByZhimaIdAndDesc(reportZhimaExtend);
                        }
                    }
                }

            }
        }
    }

    /**
     * 同盾数据解析
     *
     * @param reportInfo 报告json
     * @param orderNum   订单编号
     */
    private void analysisTongdun(JSONObject reportInfo, String orderNum) {
        JSONObject longBorrowing = reportInfo.getJSONObject("longBorrowing");
        if (longBorrowing != null) {
            if (StringUtils.isNotBlank(longBorrowing.getString("oneOrSevenOrThreeDaysItemBoList"))) {
                JSONArray oneOrSevenOrThreeDaysItemBoList = longBorrowing.getJSONArray("oneOrSevenOrThreeDaysItemBoList");
                for (int i = 0; i < oneOrSevenOrThreeDaysItemBoList.size(); i++) {
                    JSONObject itemBoListJSONObject = oneOrSevenOrThreeDaysItemBoList.getJSONObject(i);
                    String itemNmae = itemBoListJSONObject.getString("itemNmae");
                    String conditions = itemBoListJSONObject.getString("conditions");
                    if (StringUtils.isNotBlank(conditions)) {
                        JSONArray conditionsArray = JSON.parseArray(conditions);
                        for (int c = 0; c < conditionsArray.size(); c++) {
                            JSONObject conditionObject = conditionsArray.getJSONObject(c);
                            if (Objects.nonNull(conditionObject)) {
                                ReportTongdunRule reportTongdunRule = new ReportTongdunRule();
                                reportTongdunRule.setItemname(itemNmae);
                                reportTongdunRule.setOrderNo(orderNum);
                                reportTongdunRule.setCreateTime(new Date());
                                reportTongdunRule.setType(0);
                                reportTongdunRuleDao.insertOrUpdateByOrderNum(reportTongdunRule);

                                int ruleId = reportTongdunRule.getId();

                                JSONArray hits = conditionObject.getJSONArray("hits");
                                String result = conditionObject.getString("result");
                                for (int h = 0; h < hits.size(); h++) {
                                    JSONObject hitObject = hits.getJSONObject(h);
                                    String count = hitObject.getString("count");
                                    String name = hitObject.getString("industry_display_name");

                                    ReportLoanInfo reportLoanInfo = new ReportLoanInfo();
                                    reportLoanInfo.setCount(count);
                                    reportLoanInfo.setName(name);
                                    reportLoanInfo.setRuleId(ruleId);
                                    reportLoanInfo.setCreateTime(new Date());
                                    reportLoanInfoDao.insertOrUpdateByOrderNumAndName(reportLoanInfo);
                                    //更新策略集命中次数
                                    ReportTongdunRule updateRule = new ReportTongdunRule();
                                    updateRule.setId(ruleId);
                                    updateRule.setCount(result);
                                    updateRule.setType(TongdunRuleTypeEnum.loan.getType());
                                    reportTongdunRuleDao.updateSelective(updateRule);
                                }
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotBlank(longBorrowing.getString("itemBoList"))) {
                JSONArray itemBoList = longBorrowing.getJSONArray("itemBoList");
                for (int i = 0; i < itemBoList.size(); i++) {
                    JSONObject item = itemBoList.getJSONObject(i);
                    String itemNmae = item.getString("itemNmae");
                    String conditions = item.getString("conditions");
                    if (StringUtils.isNotBlank(conditions)) {
                        JSONArray conditionsArray = JSON.parseArray(conditions);
                        //根据不同类型解析不良时间和贷款事件
                        String level = "低";
                        for (int c = 0; c < conditionsArray.size(); c++) {
                            JSONObject conditionObject = conditionsArray.getJSONObject(c);
                            String type = conditionObject.getString("type");
                            if (type.equals("grey_list") || type.equals("black_list") || type.equals("fuzzy_black_list")) {
                                //策略入库
                                ReportTongdunRule reportTongdunRule = new ReportTongdunRule();
                                reportTongdunRule.setItemname(itemNmae);
                                reportTongdunRule.setOrderNo(orderNum);
                                reportTongdunRule.setCreateTime(new Date());
                                reportTongdunRule.setType(1);
                                reportTongdunRuleDao.insertOrUpdateByOrderNum(reportTongdunRule);

                                int ruleId = reportTongdunRule.getId();

                                JSONArray hits = conditionObject.getJSONArray("hits");
                                for (int h = 0; h < hits.size(); h++) {
                                    JSONObject hitObject = hits.getJSONObject(h);
                                    if (Objects.nonNull(hitObject)) {
                                        String hitName = hitObject.getString("fraud_type_display_name");
                                        String hitLevel = hitObject.getString("risk_level_display_name");
                                        if (StringUtils.isNotBlank(hitLevel)){
                                            if (hitLevel.equals("高")) {
                                                level = hitLevel;
                                            } else if (hitLevel.equals("中") && !hitLevel.equals("高")) {
                                                level = hitLevel;
                                            }
                                        }
                                        //规则详情入库
                                        ReportBlackInfo reportBlackInfo = new ReportBlackInfo();
                                        reportBlackInfo.setCreateTime(new Date());
                                        reportBlackInfo.setLevel(hitLevel);
                                        reportBlackInfo.setName(hitName);
                                        reportBlackInfo.setRuleId(ruleId);
                                        reportBlackInfoDao.insertOrUpdateByOrderNumAndName(reportBlackInfo);

                                        //更新策略集风险级别
                                        ReportTongdunRule updateRule = new ReportTongdunRule();
                                        updateRule.setId(ruleId);
                                        updateRule.setLevel(level);
                                        updateRule.setType(TongdunRuleTypeEnum.black.getType());
                                        reportTongdunRuleDao.updateSelective(updateRule);
                                    }
                                }
                            } else if (type.equals("association_partner") || type.equals("cross_partner")) {
                                //策略入库
                                ReportTongdunRule reportTongdunRule = new ReportTongdunRule();
                                reportTongdunRule.setItemname(itemNmae);
                                reportTongdunRule.setOrderNo(orderNum);
                                reportTongdunRule.setCreateTime(new Date());
                                reportTongdunRule.setType(0);
                                reportTongdunRuleDao.insertOrUpdateByOrderNum(reportTongdunRule);

                                int ruleId = reportTongdunRule.getId();

                                JSONArray hits = conditionObject.getJSONArray("hits");
                                String result = conditionObject.getString("result");
                                for (int h = 0; h < hits.size(); h++) {
                                    JSONObject hitObject = hits.getJSONObject(h);
                                    String count = hitObject.getString("count");
                                    String name = hitObject.getString("industry_display_name");

                                    ReportLoanInfo reportLoanInfo = new ReportLoanInfo();
                                    reportLoanInfo.setCount(count);
                                    reportLoanInfo.setName(name);
                                    reportLoanInfo.setRuleId(ruleId);
                                    reportLoanInfo.setCreateTime(new Date());
                                    reportLoanInfoDao.insertOrUpdateByOrderNumAndName(reportLoanInfo);
                                    //更新策略集命中次数
                                    ReportTongdunRule updateRule = new ReportTongdunRule();
                                    updateRule.setId(ruleId);
                                    updateRule.setCount(result);
                                    updateRule.setType(TongdunRuleTypeEnum.loan.getType());
                                    reportTongdunRuleDao.updateSelective(updateRule);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 解析联系人通话记录
     *
     * @param orderNum       订单编号
     * @param reportYysInfos 运营商详细信息
     */
    private void analysisContacts(String orderNum, List<ReportYysInfo> reportYysInfos) {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setOrderNo(orderNum);
        List<ContactInfo> contactInfos = contactInfoDao.listByContactInfo(contactInfo);

        Date now = new Date();
        for (ContactInfo info : contactInfos) {
            String mobile = info.getPhone();
            List<ReportYysInfo> collect = reportYysInfos.stream().filter(reportYysInfo ->
                    reportYysInfo.getCallothernumber().equals(mobile)).collect(Collectors.toList());
            for (ReportYysInfo reportYysInfo : collect) {
                ReportContactRecord reportContactRecord = new ReportContactRecord();
                reportContactRecord.setPhone(reportYysInfo.getCallothernumber());
                reportContactRecord.setOrderNo(orderNum);
                reportContactRecord.setCalledCount(reportYysInfo.getDialcount());
                reportContactRecord.setDialingCount(reportYysInfo.getDialedcount());
                int dialcallTime = Integer.parseInt(reportYysInfo.getDialcalltime());
                int dialedcallTime = Integer.parseInt(reportYysInfo.getDialedcalltime());
                Integer duration = dialcallTime + dialedcallTime;
                reportContactRecord.setDuration(duration.toString());
                int dialingCount = Integer.parseInt(reportYysInfo.getDialedcount());
                int calledCount = Integer.parseInt(reportYysInfo.getDialcount());
                Integer frequency = dialingCount + calledCount;
                reportContactRecord.setFrequency(frequency.toString());
                reportContactRecord.setTag(info.getRelationName());
                AttributionInfo attributionInfo = attributionInfoService.attributionQuery(reportYysInfo.getCallothernumber());
                if (Objects.nonNull(attributionInfo)) {
                    reportContactRecord.setLocation(attributionInfo.getCity() + "-" + attributionInfo.getArea());
                } else {
                    reportContactRecord.setLocation("-");
                }
                reportContactRecord.setCreateTime(now);

                reportContactRecordDao.insertOrUpdateOrderNumAndPhone(reportContactRecord);
            }
        }
    }


    /**
     * 运营商通话记录
     *
     * @param reportYysInfos 运营商通话记录
     * @param orderNum       订单编号
     */
    private void analysisCallRecord(List<ReportYysInfo> reportYysInfos, String orderNum) {

        //联系人信息
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setOrderNo(orderNum);
        List<ContactInfo> contactInfos = contactInfoDao.listByContactInfo(contactInfo);

        //通话记录排序 根据主叫被叫次数之和
        reportYysInfos.sort((o1, o2) -> ((Integer.parseInt(o2.getDialcount()) + Integer.parseInt(o2.getDialedcount()))) - (Integer.parseInt(o1.getDialcount()) + Integer.parseInt(o1.getDialedcount())));

        //取top50
        List<ReportYysInfo> subReportYysInfo;
        if (reportYysInfos.size() > 50) {
            subReportYysInfo = reportYysInfos.subList(0, 50);
        } else {
            subReportYysInfo = reportYysInfos;
        }

        Date now = new Date();
        for (ReportYysInfo reportYysInfo : subReportYysInfo) {
            String tag = "";
            for (ContactInfo info : contactInfos) {
                if (reportYysInfo.getCallothernumber().equals(info.getPhone())) {
                    tag = info.getRelationName();
                }
            }
            ReportCallRecord reportCallRecord = new ReportCallRecord();
            reportCallRecord.setPhone(reportYysInfo.getCallothernumber());
            reportCallRecord.setOrderNo(orderNum);
            reportCallRecord.setCalledCount(reportYysInfo.getDialcount());
            reportCallRecord.setDialingCount(reportYysInfo.getDialedcount());
            int dialcallTime = Integer.parseInt(reportYysInfo.getDialcalltime());
            int dialedcallTime = Integer.parseInt(reportYysInfo.getDialedcalltime());
            Integer duration = dialcallTime + dialedcallTime;
            reportCallRecord.setDuration(duration.toString());
            int dialingCount = Integer.parseInt(reportYysInfo.getDialedcount());
            int calledCount = Integer.parseInt(reportYysInfo.getDialcount());
            Integer frequency = dialingCount + calledCount;
            reportCallRecord.setFrequency(frequency.toString());
            AttributionInfo attributionInfo = attributionInfoService.attributionQuery(reportYysInfo.getCallothernumber());
            if (Objects.nonNull(attributionInfo)) {
                reportCallRecord.setLocation(attributionInfo.getCity() + "-" + attributionInfo.getArea());
            } else {
                reportCallRecord.setLocation("-");
            }
            reportCallRecord.setTag(tag);
            reportCallRecord.setCreateTime(now);
            reportCallRecordDao.insertOrUpdateByOrderNumAndPhone(reportCallRecord);
        }
    }

    private void analysisYysReport(JSONObject reportInfo, String orderNum) {
        Date now = new Date();
        List<ReportYysReportInfo> reportYysReportInfoList = new ArrayList<>();

        JSONObject yysRepor = reportInfo.getJSONObject("yysRepor");
        if (Objects.nonNull(yysRepor)) {
            String sourceNameZh = yysRepor.getString("sourceNameZh");
            //数据入库
            ReportYysReportInfo reportYysReportInfo = new ReportYysReportInfo();
            reportYysReportInfo.setFiledName("sourceNameZh");
            reportYysReportInfo.setFiledValue(sourceNameZh);
            reportYysReportInfo.setCreateTime(now);
            reportYysReportInfo.setOrderNo(orderNum);
            reportYysReportInfoList.add(reportYysReportInfo);

            JSONObject cellPhoneBasic = yysRepor.getJSONObject("cellPhoneBasic");
            if (Objects.nonNull(cellPhoneBasic)) {
                Map<String, String> cellPhoneBasicMap = new HashMap<>();
                String mobile = cellPhoneBasic.getString("mobile");
                String carrierName = cellPhoneBasic.getString("carrierName");
                String carrierIdcard = cellPhoneBasic.getString("carrierIdcard");
                String regTime = cellPhoneBasic.getString("regTime");
                String inTime = cellPhoneBasic.getString("inTime");
                String email = cellPhoneBasic.getString("email");
                String reliability = cellPhoneBasic.getString("reliability");
                String phoneAttribution = cellPhoneBasic.getString("phoneAttribution");
                cellPhoneBasicMap.put("mobile", mobile);
                cellPhoneBasicMap.put("carrierName", carrierName);
                cellPhoneBasicMap.put("carrierIdcard", carrierIdcard);
                cellPhoneBasicMap.put("regTime", regTime);
                cellPhoneBasicMap.put("inTime", inTime);
                cellPhoneBasicMap.put("email", email);
                cellPhoneBasicMap.put("reliability", reliability);
                cellPhoneBasicMap.put("phoneAttribution", phoneAttribution);

                cellPhoneBasicMap.forEach((k, v) -> {

                    ReportYysReportInfo cellPhoneBasicRport = new ReportYysReportInfo();
                    cellPhoneBasicRport.setOrderNo(orderNum);
                    cellPhoneBasicRport.setCreateTime(now);
                    cellPhoneBasicRport.setFiledValue(v);
                    cellPhoneBasicRport.setFiledName(k);
                    reportYysReportInfoList.add(cellPhoneBasicRport);
                });

            }
            String basicCheckItemsList = yysRepor.getString("basicCheckItemsList");
            if (StringUtils.isNotBlank(basicCheckItemsList)) {
                JSONArray basicCheckItemsListArray = yysRepor.getJSONArray("basicCheckItemsList");
                for (int i = 0; i < basicCheckItemsListArray.size(); i++) {
                    JSONObject checkItemObj = basicCheckItemsListArray.getJSONObject(i);
                    String checkItem = checkItemObj.getString("checkItem");
                    String checkItemDesc = checkItemObj.getString("checkItemDesc");
                    String result = checkItemObj.getString("result");
                    if (checkItem.equals("idcard_check") || checkItem.equals("is_name_and_idcard_in_court_black")
                            || checkItem.equals("is_name_and_idcard_in_finance_black")) {
                        FlowOption searchObj = new FlowOption();
                        searchObj.setOptionName(checkItem);
                        searchObj.setOptionType(FlowOptionTypeEnum.USER_INFO.getType());
                        FlowOption flowOption = flowOptionDao.selectOneFlowOption(searchObj);
                        ReportUserInfo reportUserInfo = new ReportUserInfo();

                        reportUserInfo.setCreateTime(now);
                        reportUserInfo.setFiledName(flowOption.getOptionName());
                        reportUserInfo.setFiledValue(result);
                        reportUserInfo.setFiledDesc(flowOption.getOptionDesc());
                        reportUserInfo.setOrderNo(orderNum);
                        reportUserInfoDao.insertOrUpdateByOrderNum(reportUserInfo);

                    } else {
                        ReportYysReportInfo checkItemReport = new ReportYysReportInfo();
                        checkItemReport.setFiledName(checkItem);
                        checkItemReport.setFiledDesc(checkItemDesc);
                        checkItemReport.setFiledValue(result);
                        checkItemReport.setCreateTime(now);
                        checkItemReport.setOrderNo(orderNum);
                        reportYysReportInfoList.add(checkItemReport);
                    }
                }
            }
            String behaviorCheckList = yysRepor.getString("behaviorCheckList");
            if (StringUtils.isNotBlank(behaviorCheckList)) {
                JSONArray behaviorCheckListArray = yysRepor.getJSONArray("behaviorCheckList");
                for (int i = 0; i < behaviorCheckListArray.size(); i++) {
                    JSONObject behaviorCheck = behaviorCheckListArray.getJSONObject(i);
                    if (Objects.nonNull(behaviorCheck)) {
                        String evidence = behaviorCheck.getString("evidence");
                        String checkPoint = behaviorCheck.getString("checkPoint");
                        String checkPointCn = behaviorCheck.getString("checkPointCn");
                        String result = behaviorCheck.getString("result");

                        if (checkPoint.equals("contact_loan") || checkPoint.equals("contact_bank")
                                || checkPoint.equals("contact_credit_card") || checkPoint.equals("contact_collection")) {
                            ReportTongdunRule reportTongdunRule = new ReportTongdunRule();
                            reportTongdunRule.setType(0);
                            reportTongdunRule.setCreateTime(now);
                            reportTongdunRule.setOrderNo(orderNum);
                            reportTongdunRule.setItemname(checkPointCn);
                            String count;
                            List<String> tongdunLoanList = new ArrayList<>();
                            if (evidence.contains("联系列表：")) {
                                String[] split = evidence.split("联系列表：");
                                count = split[0].replace("[总计]", "");
                                String[] tongdunLoan = split[1].split("，");
                                tongdunLoanList = Arrays.asList(tongdunLoan);
                            } else {
                                count = evidence.replace("[总计]", "");
                            }
                            reportTongdunRule.setCount(count);
                            reportTongdunRuleDao.insertOrUpdateByOrderNum(reportTongdunRule);

                            Integer ruleId = reportTongdunRule.getId();
                            for (String s : tongdunLoanList) {
                                ReportLoanInfo reportLoanInfo = new ReportLoanInfo();
                                reportLoanInfo.setCreateTime(now);
                                reportLoanInfo.setRuleId(ruleId);
                                reportLoanInfo.setName(s.substring(0, s.lastIndexOf("]") + 1));
                                reportLoanInfo.setCount(s.substring(s.lastIndexOf("]") + 1, s.length()));
                                reportLoanInfoDao.insertOrUpdateByOrderNumAndName(reportLoanInfo);
                            }

                        } else {
                            ReportYysReportInfo behaviorCheckReport = new ReportYysReportInfo();
                            behaviorCheckReport.setFiledName(checkPoint);
                            behaviorCheckReport.setFiledDesc(checkPointCn);
                            behaviorCheckReport.setFiledValue(result);
                            behaviorCheckReport.setCreateTime(now);
                            behaviorCheckReport.setOrderNo(orderNum);
                            behaviorCheckReport.setRemark(evidence);
                            reportYysReportInfoList.add(behaviorCheckReport);
                        }
                    }
                }
            }
            String userInfoCheckList = yysRepor.getString("userInfoCheckList");
            if (StringUtils.isNotBlank(userInfoCheckList)) {
                JSONArray userInfoCheckListArray = yysRepor.getJSONArray("userInfoCheckList");
                for (int i = 0; i < userInfoCheckListArray.size(); i++) {
                    JSONObject userInfoCheck = userInfoCheckListArray.getJSONObject(i);
                    if (Objects.nonNull(userInfoCheck)) {
                        JSONObject checkSearchInfo = userInfoCheck.getJSONObject("checkSearchInfo");

                        if (Objects.nonNull(checkSearchInfo)) {
                            Map<String, String> checkSearchInfoMap = new HashMap<>();

                            String searchedOrgCnt = checkSearchInfo.getString("searchedOrgCnt");
                            String searchedOrgType = checkSearchInfo.getString("searchedOrgType");
                            String searchType = "-";
                            if (StringUtils.isNotBlank(searchedOrgType)){
                                List<String> searchedOrgTypeList = JSON.parseArray(searchedOrgType, String.class);
                                searchType = YYSUserInfoTypeUtil.getSearchOrgType(searchedOrgTypeList);
                            }
                            String idcardWithOtherNames = checkSearchInfo.getString("idcardWithOtherNames");
                            String idcardWithOtherPhones = checkSearchInfo.getString("idcardWithOtherPhones");
                            String phoneWithOtherNames = checkSearchInfo.getString("phoneWithOtherNames");
                            String phoneWithOtherIdcards = checkSearchInfo.getString("phoneWithOtherIdcards");
                            String registerOrgCnt = checkSearchInfo.getString("registerOrgCnt");
                            String registerOrgType = checkSearchInfo.getString("registerOrgType");
                            String registerType = "-";
                            if (StringUtils.isNotBlank(registerOrgType)){
                                List<String> registerOrgTypeList = JSON.parseArray(registerOrgType, String.class);
                                registerType = YYSUserInfoTypeUtil.getSearchOrgType(registerOrgTypeList);
                            }
                            String arisedOpenWeb = checkSearchInfo.getString("arisedOpenWeb");

                            checkSearchInfoMap.put("searchedOrgCnt", searchedOrgCnt);
                            checkSearchInfoMap.put("searchedOrgType", searchType);
                            checkSearchInfoMap.put("idcardWithOtherNames", idcardWithOtherNames);
                            checkSearchInfoMap.put("idcardWithOtherPhones", idcardWithOtherPhones);
                            checkSearchInfoMap.put("phoneWithOtherNames", phoneWithOtherNames);
                            checkSearchInfoMap.put("phoneWithOtherIdcards", phoneWithOtherIdcards);
                            checkSearchInfoMap.put("registerOrgCnt", registerOrgCnt);
                            checkSearchInfoMap.put("registerOrgType", registerType);
                            checkSearchInfoMap.put("arisedOpenWeb", arisedOpenWeb);
                            checkSearchInfoMap.forEach((k, v) -> {
                                String value;
                                if (v.startsWith("[")) {
                                    value = v.substring(1, v.length() - 1);
                                } else {
                                    value = v;
                                }
                                ReportYysReportInfo checkSearchInfoReport = new ReportYysReportInfo();
                                checkSearchInfoReport.setOrderNo(orderNum);
                                checkSearchInfoReport.setCreateTime(now);
                                checkSearchInfoReport.setFiledValue(value);
                                checkSearchInfoReport.setFiledName(k);
                                reportYysReportInfoList.add(checkSearchInfoReport);
                            });
                        }

                        JSONObject checkBlackInfo = userInfoCheck.getJSONObject("checkBlackInfo");
                        if (Objects.nonNull(checkBlackInfo)) {
                            Map<String, String> checkBlacInfoMap = new HashMap<>();

                            String phoneGrayScore = checkBlackInfo.getString("phoneGrayScore");
                            String contactsClass1BlacklistNnt = checkBlackInfo.getString("contactsClass1BlacklistNnt");
                            String contactsClass2BlacklistNnt = checkBlackInfo.getString("contactsClass2BlacklistNnt");
                            String contactsClass1Cnt = checkBlackInfo.getString("contactsClass1Cnt");
                            String contactsRouterCnt = checkBlackInfo.getString("contactsRouterCnt");
                            String contactsRouterRatio = checkBlackInfo.getString("contactsRouterRatio");

                            checkBlacInfoMap.put("phoneGrayScore", phoneGrayScore);
                            checkBlacInfoMap.put("contactsClass1BlacklistNnt", contactsClass1BlacklistNnt);
                            checkBlacInfoMap.put("contactsClass2BlacklistNnt", contactsClass2BlacklistNnt);
                            checkBlacInfoMap.put("contactsClass1Cnt", contactsClass1Cnt);
                            checkBlacInfoMap.put("contactsRouterCnt", contactsRouterCnt);
                            checkBlacInfoMap.put("contactsRouterRatio", contactsRouterRatio);

                            checkBlacInfoMap.forEach((k, v) -> {
                                ReportYysReportInfo checkBlackInfoReport = new ReportYysReportInfo();
                                checkBlackInfoReport.setOrderNo(orderNum);
                                checkBlackInfoReport.setCreateTime(now);
                                checkBlackInfoReport.setFiledValue(v);
                                checkBlackInfoReport.setFiledName(k);
                                reportYysReportInfoList.add(checkBlackInfoReport);
                            });

                        }
                    }
                }
            }
            reportYysReportInfoList.forEach(item -> reportYysReportDao.inserOrUpdateByNameAndOrderNum(item));
        }
    }
}
