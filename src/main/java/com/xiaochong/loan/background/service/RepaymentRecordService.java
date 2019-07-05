package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.BorrowerBanlanceComponent;
import com.xiaochong.loan.background.component.SMSComponent;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jinxin on 2017/8/15.
 */
@Service
public class RepaymentRecordService extends BaseService{

    private Logger logger = LoggerFactory.getLogger(RepaymentRecordService.class);

    @Autowired
    private LoanApplicationDao loanApplicationDao;

    @Autowired
    private SessionComponent sessionComponent;

    @Autowired
    private ProxyUserDao proxyUserDao;

    @Autowired
    private  BorrowerBalanceDao borrowerBalanceDao;

    @Autowired
    private RepaymentPlanDao repaymentPlanDao;

    @Autowired
    private BorrowerBanlanceComponent borrowerBanlanceComponent;

    @Autowired
    private SMSComponent smsComponent;

    @Autowired
    private  SmsTemplateDao smsTemplateDao;

    @Value("${webapp.billingInfoUrl}")
    private String billing_info_url;

    @Autowired
    private BorrowerAccountRecordDao borrowerAccountRecordDao;

    @Autowired
    private  MerchantDao merchantDao;

    @Autowired
    private UrgeOverdueDao urgeOverdueDao;

    @Autowired
    private BorrowerDao borrowerDao;

    Lock lock =  new ReentrantLock();

    /**
     * 还款信息查询
     * @param token
     * @param status
     * @param searchStatus
     * @param searchContent
     * @param pageNum
     * @param pageSize
     * @return
     */
    public BusinessVo<RepaymentRecordVo> searchRepaymentRecord(String token, String status, String searchStatus, String searchContent, Integer pageNum, Integer pageSize) {
        BusinessVo<RepaymentRecordVo> businessVo = new BusinessVo<>();
        RepaymentRecordVo repaymentRecordVo= new RepaymentRecordVo();
        String proxyuser = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        int createBy = Integer.parseInt(proxyuser);
        ProxyUser byId = proxyUserDao.getById(createBy);
        Integer merchId = byId.getMerchId();
        List<Integer> loanApplicationIds= new ArrayList<>();
        if(StringUtils.isNotBlank(searchContent)){
            LoanApplication loanApplication = new LoanApplication();
            if("1".equals(searchStatus)){
                loanApplication.setBorrowerPhone(searchContent);
            }else  if("2".equals(searchStatus)){
                loanApplication.setBorrowerIdCard(searchContent);
            }
            loanApplication.setMerchId(merchId);
            List<LoanApplication> loanApplicationList = loanApplicationDao.selectLoanApplication(loanApplication);
            if(CollectionUtils.isNotEmpty(loanApplicationList)){
                loanApplicationList.forEach(l->{
                    loanApplicationIds.add(l.getId());
                });
                LoanApplication application = loanApplicationList.get(0);
                repaymentRecordVo.setBorrowerName(application.getBorrowerName());
                repaymentRecordVo.setBorrowerIdCard(application.getBorrowerIdCard());
                repaymentRecordVo.setBorrowerPhone(application.getBorrowerPhone());
                BorrowerBalance borrowerBalance = borrowerBalanceDao.selectBorrowerBalanceByBorrowerId(application.getBorrowerId());
                repaymentRecordVo.setBorrowerBanlance(borrowerBalance.getBalance());

                int allPendingrePaymentNum=0;
                BigDecimal allPendingRepaymentMoney= BigDecimal.ZERO;
                int overdueRepaymentNum=0;
                BigDecimal overdueRepaymentMoney= BigDecimal.ZERO;
                List<RepaymentPlan> repaymentPlanList=repaymentPlanDao.selectByLoanApplicationIds(loanApplicationIds);
                if(CollectionUtils.isNotEmpty(repaymentPlanList)){
                    for (RepaymentPlan repaymentPlan : repaymentPlanList) {
                        BigDecimal wholeMoney = repaymentPlan.getWholeMoney();
                        if(!RepaymentPlanStatusEnum.PAY.getType().equals(repaymentPlan.getStatus())){
                            allPendingRepaymentMoney=allPendingRepaymentMoney.add(wholeMoney);
                            allPendingrePaymentNum++;
                            if(RepaymentPlanStatusEnum.OVERDUE.getType().equals(repaymentPlan.getStatus())){
                                overdueRepaymentMoney=overdueRepaymentMoney.add(wholeMoney);
                                overdueRepaymentNum++;
                            }
                        }
                    }
                }
                repaymentRecordVo.setAllPendingRepaymentMoney(allPendingRepaymentMoney);
                repaymentRecordVo.setAllPendingrePaymentNum(allPendingrePaymentNum);
                repaymentRecordVo.setOverdueRepaymentMoney(overdueRepaymentMoney);
                repaymentRecordVo.setOverdueRepaymentNum(overdueRepaymentNum);
            }else {
                loanApplicationIds.add(-1);
            }
        }
        RepaymentPlan repaymentPlanForSearch = new RepaymentPlan();
        repaymentPlanForSearch.setMerchId(merchId);
        repaymentPlanForSearch.setStatus(status);
        int size = loanApplicationIds.size();
        if(size ==1){
            repaymentPlanForSearch.setApplicationId(loanApplicationIds.get(0));
        }else if(size>1) {
            repaymentPlanForSearch.setApplicationIds(loanApplicationIds);
        }
        PageHelper.startPage(pageNum, pageSize, true);
        Page<RepaymentPlan> page = (Page<RepaymentPlan>) repaymentPlanDao.selectListByRepaymentPlanRecord(repaymentPlanForSearch);
        PageInfo<RepaymentPlan> repaymentPlanPageInfo = page.toPageInfo();
        BasePageInfoVo<RepaymentPlanVo> basePageInfoVo = assemblyBasePageInfo(repaymentPlanPageInfo);
        List<RepaymentPlanVo> merchDataTemplateVoList = new ArrayList<>();
        List<RepaymentPlan> result = page.getResult();
        if(CollectionUtils.isNotEmpty(result)){
            repaymentPlanVoListConvert(merchDataTemplateVoList,result);
        }
        basePageInfoVo.setResultList(merchDataTemplateVoList);
        repaymentRecordVo.setBasePageInfoVo(basePageInfoVo);

        businessVo.setData(repaymentRecordVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 集合类转换
     * @param repaymentPlanVoList
     * @param result
     */
    private void repaymentPlanVoListConvert(List<RepaymentPlanVo> repaymentPlanVoList, List<RepaymentPlan> result) {
        result.forEach(repaymentPlan -> {
            RepaymentPlanVo repaymentPlanVo= new RepaymentPlanVo();
            repaymentPlanVoConvert(repaymentPlanVo, repaymentPlan);
            repaymentPlanVoList.add(repaymentPlanVo);
        });
    }


    /**
     * 类转换
     * @param repaymentPlanVo
     * @param repaymentPlan
     */
    private void repaymentPlanVoConvert(RepaymentPlanVo repaymentPlanVo, RepaymentPlan repaymentPlan) {
        repaymentPlanVo.setId(repaymentPlan.getId());
        repaymentPlanVo.setOverdueDays(repaymentPlan.getOverdueDays());
        repaymentPlanVo.setApplicationId(repaymentPlan.getApplicationId());
        repaymentPlanVo.setCreateTime(DateUtils.format(repaymentPlan.getCreateTime(),DateUtils.yyyyMMdd_format));
        repaymentPlanVo.setUpdateTime(DateUtils.format(repaymentPlan.getUpdateTime(),DateUtils.yyyyMMdd_format));
        repaymentPlanVo.setInterest(repaymentPlan.getInterest());
        repaymentPlanVo.setPracticalTime(DateUtils.format(repaymentPlan.getPracticalTime(),DateUtils.yyyyMMddHHmmss_hanziformat));
        repaymentPlanVo.setPrincipalMoney(repaymentPlan.getPrincipalMoney());
        repaymentPlanVo.setPunishInterest(repaymentPlan.getPunishInterest());
        repaymentPlanVo.setReturnTime(DateUtils.format(repaymentPlan.getReturnTime(),DateUtils.yyyyMMdd_hanziformat));
        repaymentPlanVo.setStageNo(repaymentPlan.getStageNo());
        repaymentPlanVo.setWholeMoney(repaymentPlan.getWholeMoney());
        repaymentPlanVo.setStatus(repaymentPlan.getStatus());
    }


    /**
     * 还款成功
     * @param token
     * @param repaymentPlanId
     * @param reductiveMoney
     * @return
     */
    @Transactional
    public BusinessVo<String>  repayment(String token, Integer repaymentPlanId, BigDecimal reductiveMoney) {
        BusinessVo<String> businessVo = new BusinessVo();
        Date now = new Date();
        //减免金额，短信，还款记录，余额减去
        Integer proxyUser = UserTokenUtils.getProxyUserIdByToken(token);
        if(repaymentPlanId==null){
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            return businessVo;
        }
        try {
            lock.lock();
            RepaymentPlan repaymentPlan;
            logger.info("repaymentPlanId正在还款:{}",repaymentPlanId);
            repaymentPlan= repaymentPlanDao.selectById(repaymentPlanId);
            if(repaymentPlan==null){
                businessVo.setData("无此还款计划！");
                businessVo.setCode(BusinessConstantsUtils.REPAYMENT_ID_ERROR_CODE);
                businessVo.setMessage(BusinessConstantsUtils.REPAYMENT_ID_ERROR_DESC);
                return businessVo;
            }else
            if(RepaymentPlanStatusEnum.PAY.getType().equals(repaymentPlan.getStatus())){
                businessVo.setCode(BusinessConstantsUtils.REPAYMENTED_ERROR_CODE);
                businessVo.setMessage(BusinessConstantsUtils.REPAYMENTED_ERROR_DESC);
                return businessVo;
            }
            LoanApplication loanApplication = loanApplicationDao.selectLoanApplicationById(repaymentPlan.getApplicationId());
            if(repaymentPlan!=null){
                BigDecimal wholeMoney = repaymentPlan.getWholeMoney();
                BigDecimal subtract = reductiveMoney.subtract(wholeMoney);
                Integer borrowerId = loanApplication.getBorrowerId();
                if(subtract.compareTo(BigDecimal.ZERO)>0){
                    //减免金额大于所还总金额报错返回
                    businessVo.setCode(BusinessConstantsUtils.REDUCTIVE_MONEY_ERROR_CODE);
                    businessVo.setMessage(BusinessConstantsUtils.REDUCTIVE_MONEY_ERROR_DESC);
                    return businessVo;
                }else {
                    Map<String, Object> resultMap = borrowerBanlanceComponent.updateBalance(subtract, proxyUser, borrowerId);
                    if("less".equals(resultMap.get("result"))){
                        businessVo.setCode(BusinessConstantsUtils.LESS_BANLANCE_CODE);
                        businessVo.setMessage(BusinessConstantsUtils.LESS_BANLANCE_DESC);
                        return businessVo;
                    }else
                    if("success".equals(resultMap.get("result"))){
                        //更新
                        RepaymentPlan repaymentPlanForUpdate= new RepaymentPlan();
                        repaymentPlanForUpdate.setStatus(RepaymentPlanStatusEnum.PAY.getType());
                        repaymentPlanForUpdate.setUpdateTime(now);
                        repaymentPlanForUpdate.setUpdateBy(proxyUser);
                        repaymentPlanForUpdate.setRepaymentTime(now);
                        repaymentPlanForUpdate.setPracticalTime(now);
                        repaymentPlanForUpdate.setRepaymentUser(proxyUser);
                        repaymentPlanForUpdate.setId(repaymentPlanId);
                        repaymentPlanDao.update(repaymentPlanForUpdate);

                        //发送短信
                        repaymentSms(loanApplication,repaymentPlan);
                        //还款记录
                        BorrowerAccountRecord borrowerAccountRecord = new BorrowerAccountRecord();
                        BorrowerBalance borrowerBalance = borrowerBalanceDao.selectBorrowerBalanceByBorrowerId(borrowerId);
                        borrowerAccountRecord.setBorrowerBanlanceId(borrowerBalance.getId());
                        borrowerAccountRecord.setCapitalMoney(new BigDecimal("0").subtract(subtract));
                        borrowerAccountRecord.setCapitalBeforeMoney((BigDecimal) resultMap.get("before"));
                        BigDecimal after=(BigDecimal) resultMap.get("after");
                        borrowerAccountRecord.setCapitalAfterMoney(after);
                        businessVo.setData(after.toString());
                        borrowerAccountRecord.setOperateSerialNo(UUID.randomUUID().toString());
                        borrowerAccountRecord.setCapitalType(CapitalTypeEnum.REPAYMENT.getType());
                        borrowerAccountRecord.setRepaymentId(repaymentPlanId);
                        borrowerAccountRecord.setResult(RepaymentResultTypeEnum.SUCCESS.getType());
                        borrowerAccountRecord.setMark("还款成功");
                        borrowerAccountRecord.setCreateUser(proxyUser);
                        borrowerAccountRecord.setCreateTime(now);
                        borrowerAccountRecord.setSource(BorrowerAccountRecordSourceTypeEnum.COMMERCIAL_TENANT.getType());
                        borrowerAccountRecordDao.insert(borrowerAccountRecord);
                    }else {
                        businessVo.setCode(BusinessConstantsUtils.REPAYMENT_DEFEATE_CODE);
                        businessVo.setMessage(BusinessConstantsUtils.REPAYMENT_DEFEATE_DESC);
                        return businessVo;
                    }
                }
            }
        }catch (Exception e){
            logger.error("还款报错：{}",e);
            businessVo.setCode(BusinessConstantsUtils.REPAYMENT_DEFEATE_CODE);
            businessVo.setMessage(BusinessConstantsUtils.REPAYMENT_DEFEATE_DESC);
            return businessVo;
        }finally {
            lock.unlock();
        }
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 还款成功发送短信
     * @param loanApplication
     * @param repaymentPlan
     * @return
     */
    private boolean repaymentSms(LoanApplication loanApplication,RepaymentPlan repaymentPlan){
        //发送短信
        SmsTemplate smsTemplate= new SmsTemplate();
        smsTemplate.setMerchId(loanApplication.getMerchId());
        String stageType = loanApplication.getStageType();
        Integer merchId = loanApplication.getMerchId();
        Integer stageLimit = loanApplication.getStageLimit();
        List<LoanSmsTemplate> loanSmsTemplates = new ArrayList<>();
        LoanSmsTemplate companyName = new LoanSmsTemplate();
        //公司名
        companyName.setSmaTagEnum(SmaTagEnum.COMPANY_NAME);
        Merchantinfo merchantinfoByToid = merchantDao.findMerchantinfoByToid(merchId);
        companyName.setParam(merchantinfoByToid.getMerchantName());
        loanSmsTemplates.add(companyName);
        LoanSmsTemplate borrweInfo = new LoanSmsTemplate();
        borrweInfo.setSmaTagEnum(SmaTagEnum.BORROWING_INFORMATION);
        StringBuilder stringBuilder = new StringBuilder(loanApplication.getLoanMoney().toString());
        String stageUnit;
        if(StagingTypeEnum.MONTH.getType().toString().equals(stageType)){
            stageUnit="个月";
        }else {
            stageUnit="天,1期";
        }
        stringBuilder.append("元").append("分期").append(stageLimit).append(stageUnit);
        borrweInfo.setParam(stringBuilder);
        loanSmsTemplates.add(borrweInfo);
        //下一期还款信息
//            第一期332.43元已还款成功，第二期金额为332.43元，
        //非最后一期
        Integer stageNo = repaymentPlan.getStageNo();
        if(!stageLimit.equals(stageNo)&&StagingTypeEnum.MONTH.getType().toString().equals(loanApplication.getStageType())){
            RepaymentPlan repaymentPlanForSearch = new RepaymentPlan();
            repaymentPlanForSearch.setApplicationId(loanApplication.getId());
            repaymentPlanForSearch.setStageNo(stageNo+1);
            List<RepaymentPlan> repaymentPlanList = repaymentPlanDao.selectRepaymentPlan(repaymentPlanForSearch);
            if(CollectionUtils.isNotEmpty(repaymentPlanList)){
                RepaymentPlan next = repaymentPlanList.get(0);
                StringBuilder repaymentInfo = new StringBuilder("第");
                repaymentInfo.append(stageNo).append("期金额为").append(repaymentPlan.getWholeMoney()).
                        append("元");
                LoanSmsTemplate repayInfo = new LoanSmsTemplate();
                repayInfo.setSmaTagEnum(SmaTagEnum.REPAYMENT);
                repayInfo.setParam(repaymentInfo);
                loanSmsTemplates.add(repayInfo);
                LoanSmsTemplate nextRepayInfo = new LoanSmsTemplate();
                StringBuilder nextinfo = new StringBuilder("下一期金额为");
                nextinfo.append(next.getWholeMoney()).append("元，最后还款日为")
                        .append(DateUtils.format(next.getReturnTime(),DateUtils.yyyyMMdd_hanziformat));
                nextRepayInfo.setSmaTagEnum(SmaTagEnum.NEXT_REPAYMENT);
                nextRepayInfo.setParam(nextinfo);
                loanSmsTemplates.add(nextRepayInfo);
            }
            //账单信息
            LoanSmsTemplate billInfo = new LoanSmsTemplate();
            billInfo.setSmaTagEnum(SmaTagEnum.BILLING_INFORMATION);
            //TODO 账单信息待配置
            StringBuilder info = new StringBuilder("查看账单：");
            info.append(billing_info_url);
            billInfo.setParam(info.toString());
            loanSmsTemplates.add(billInfo);
            smsTemplate.setSendStatus(SmsSendStatueEnum.REPAYMENT_SUCCESS.getType());
        }else {
//                        最后一期332.43元已还款成功，借款已还清。
            StringBuilder repaymentInfo = new StringBuilder("最后一期");
            repaymentInfo.append(repaymentPlan.getWholeMoney()).
                    append("元已还款成功，借款已还清");
            LoanSmsTemplate nextRepayInfo = new LoanSmsTemplate();
            nextRepayInfo.setSmaTagEnum(SmaTagEnum.REPAYMENT);
            nextRepayInfo.setParam(repaymentInfo);
            loanSmsTemplates.add(nextRepayInfo);
            smsTemplate.setSendStatus(SmsSendStatueEnum.LAST_REPAYMENT_SUCCESS.getType());
        }
        smsTemplate.setStatus("1");
        SmsTemplate bySmsTemplate = smsTemplateDao.getBySmsTemplate(smsTemplate);
        if (bySmsTemplate!=null){
            try {
                logger.info("短信标签集合：{}",loanSmsTemplates);
                smsComponent.sendLoanSms(loanSmsTemplates,bySmsTemplate,loanApplication.getBorrowerPhone());
                return true;
            } catch (Exception e) {
                logger.error("还款成功短信发送失败：{}",e);
            }
        }
        return  false;
    }

    /**
     *自动扫描还款
     * @return
     */
    @Transactional
    public List<Integer> autoDebitScheduled() {
        List<Integer> repaymentIds = new ArrayList<>();
        List<RepaymentPlan> repaymentPlans=repaymentPlanDao.selectOverDueInfos();
        repaymentPlans.forEach(repaymentPlan -> {
            Date date = new Date();
            Integer borrowerId = repaymentPlan.getBorrowerId();
            BigDecimal wholeMoney = repaymentPlan.getWholeMoney();
            BorrowerBalance borrowerBalance = borrowerBalanceDao.selectBorrowerBalanceByBorrowerId(borrowerId);
            if(borrowerBalance.getBalance().compareTo(wholeMoney)>=0){
                Integer repaymentPlanId = repaymentPlan.getId();
                Map<String, Object> resultMap = borrowerBanlanceComponent.updateBalance(BigDecimal.ZERO.subtract(wholeMoney), -1, borrowerId);
                if("success".equals(resultMap.get("result"))){
                    //更新状态
                    RepaymentPlan repaymentPlanForUpdate= new RepaymentPlan();
                    repaymentPlanForUpdate.setStatus(RepaymentPlanStatusEnum.PAY.getType());
                    repaymentPlanForUpdate.setId(repaymentPlanId);
                    repaymentPlanForUpdate.setRepaymentUser(-1);
                    repaymentPlanForUpdate.setRepaymentTime(date);
                    repaymentPlanForUpdate.setPracticalTime(date);
                    repaymentPlanForUpdate.setUpdateBy(-1);
                    repaymentPlanForUpdate.setUpdateTime(date);
                    repaymentPlanDao.update(repaymentPlanForUpdate);
                    //短信
                    LoanApplication loanApplication = loanApplicationDao.selectLoanApplicationById(repaymentPlan.getApplicationId());
                    if(loanApplication!=null){
                        this.repaymentSms(loanApplication,repaymentPlan);
                    }
                    //还款记录
                    BorrowerAccountRecord borrowerAccountRecord = new BorrowerAccountRecord();
                    borrowerAccountRecord.setBorrowerBanlanceId(borrowerBalance.getId());
                    borrowerAccountRecord.setCapitalMoney(new BigDecimal("0").subtract(wholeMoney));
                    borrowerAccountRecord.setCapitalBeforeMoney((BigDecimal) resultMap.get("before"));
                    borrowerAccountRecord.setCapitalAfterMoney((BigDecimal) resultMap.get("after"));
                    borrowerAccountRecord.setOperateSerialNo(UUID.randomUUID().toString());
                    borrowerAccountRecord.setCapitalType(CapitalTypeEnum.REPAYMENT.getType());
                    borrowerAccountRecord.setResult(RepaymentResultTypeEnum.SUCCESS.getType());
                    borrowerAccountRecord.setRepaymentId(repaymentPlanId);
                    borrowerAccountRecord.setMark("系统自动扣款成功");
                    borrowerAccountRecord.setCreateUser(-1);
                    borrowerAccountRecord.setCreateTime(date);
                    borrowerAccountRecord.setSource(BorrowerAccountRecordSourceTypeEnum.SYSTEM_AUTO.getType());
                    borrowerAccountRecordDao.insert(borrowerAccountRecord);
                    repaymentIds.add(repaymentPlanId);
                }
            }
        });
        return repaymentIds;
    }


    @Transactional
    public void overDueStatisticsScheduled() {
        List<Map<String, Object>> maps = repaymentPlanDao.selectOverDueStatistics();
        for (Map<String, Object> map: maps) {
           Integer borrowerId= (Integer)map.get("borrower_id");
           Integer applicationId= (Integer)map.get("application_id");
            UrgeOverdue urgeOverdue = new UrgeOverdue();
            urgeOverdue.setBorrowerId(borrowerId);
            urgeOverdue.setOverdueAllMoney(new BigDecimal(map.get("over_due_whole_money").toString()) );
            urgeOverdue.setOverdueNum(Integer.valueOf(map.get("over_due_number").toString()));
            urgeOverdue.setMaxOverdueDays((Integer)map.get("max_overdue_days") );
            UrgeOverdue urgeOverdueSelect=urgeOverdueDao.selectByApplicationId(applicationId);
            urgeOverdue.setStatus(UrgeOverDueStatusEnum.WAITING_URGE.getType());
            Date now = new Date();
            urgeOverdue.setApplicationId(applicationId);
            if(urgeOverdueSelect!=null){
                urgeOverdue.setId(urgeOverdueSelect.getId());
                urgeOverdue.setUpdateTime(now);
                urgeOverdue.setUpdateUser(-1);
                urgeOverdueDao.update(urgeOverdue);
                logger.info("更新催收表：{}",urgeOverdue);
            }else {
                urgeOverdue.setCreateTime(now);
                urgeOverdue.setCreateUser(-1);
                Borrower borrower = borrowerDao.selectBorrowerById(borrowerId);
                urgeOverdue.setBorrowerPhone(borrower.getPhone());
                urgeOverdue.setBorrowerName(borrower.getName());
                urgeOverdue.setMerchId(borrower.getMerchId());
                urgeOverdueDao.insert(urgeOverdue);
                logger.info("添加催收表：{}",urgeOverdue);
            }
        }
    }

    /**
     * 催收逾期表状态
     */
    @Transactional
    public void updateUrgeOverDueStatusScheduled() {
        //更新催收逾期表状态,只扫描今日已更进的
        List<Integer> applicationIds=urgeOverdueDao.selectApplicationIdsByStatus(UrgeOverDueStatusEnum.URGEED_TODAY.getType());
        applicationIds.forEach(aId->{
            RepaymentPlan repaymentPlan = new RepaymentPlan();
            repaymentPlan.setApplicationId(aId);
            repaymentPlan.setStatus(RepaymentPlanStatusEnum.OVERDUE.getType());
            Integer count= repaymentPlanDao.selectCountByRepaymentPlan(repaymentPlan);
            UrgeOverdue urgeOverdueForUpdate= new UrgeOverdue();
            if(count>0){
                urgeOverdueForUpdate.setStatus(UrgeOverDueStatusEnum.WAITING_URGE.getType());
            }else {
                urgeOverdueForUpdate.setStatus(UrgeOverDueStatusEnum.OFF_THE_STOCKS.getType());
            }
            urgeOverdueForUpdate.setApplicationId(aId);
            urgeOverdueForUpdate.setUpdateUser(-1);
            urgeOverdueForUpdate.setUpdateTime(new Date());
            urgeOverdueDao.updateByApplicationId(urgeOverdueForUpdate);
            logger.info("更新催收逾期表：{}",urgeOverdueForUpdate);
        });
    }
}
