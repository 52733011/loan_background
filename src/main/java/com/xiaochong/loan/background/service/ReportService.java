package com.xiaochong.loan.background.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaochong.loan.background.component.HtmlToPdfComponent;
import com.xiaochong.loan.background.component.OssComponent;
import com.xiaochong.loan.background.controller.ReportController;
import com.xiaochong.loan.background.dao.OrderDao;
import com.xiaochong.loan.background.dao.OrderUserInfoDao;
import com.xiaochong.loan.background.dao.ReportDao;
import com.xiaochong.loan.background.dao.UserDao;
import com.xiaochong.loan.background.entity.po.Order;
import com.xiaochong.loan.background.entity.po.OrderUserInfo;
import com.xiaochong.loan.background.entity.po.Report;
import com.xiaochong.loan.background.entity.po.User;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.ReportResultVo;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.exception.FileException;
import com.xiaochong.loan.background.exception.OKhttpException;
import com.xiaochong.loan.background.utils.BusinessConstantsUtils;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.OkHttpUtils;
import com.xiaochong.loan.background.utils.ParamsUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinxin on 2017/8/18.
 */
@Service
public class ReportService {

    private Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderUserInfoDao orderUserInfoDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HtmlToPdfComponent htmlToPdfComponent;

    @Autowired
    private OssComponent ossComponent;

    @Value("${report.url}")
    private String reportUrl;

    @Value("${report.oss.path}")
    private String reportOssPath;
    /**
     * 根据订单号查询
     * @param orderNo
     * @return
     */
    public BusinessVo<ReportResultVo> selectByOrderNo(String orderNo) {
        BusinessVo<ReportResultVo> businessVo = new BusinessVo<>();
        Report report = reportDao.selectByOrderNo(orderNo);
        ReportResultVo reportVo = new ReportResultVo();
        reportConvert(reportVo ,report);
        businessVo.setData(reportVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return  businessVo;
    }

    @Value("${risk.app_account}")
    private String appAccount;

    @Value("${risk.secret_key}")
    private String riskSecretKey;

    @Value("${risk.credit.report.url}")
    private String credit_report_url;

    @Value("${risk.yys.call_count_url}")
    private String yys_call_count_url;

    public BusinessVo<String> selectOldByOrderNo(String orderNo) throws OKhttpException {
        BusinessVo<String> businessVo = new BusinessVo<>();
        Order order = orderDao.selectOrderByOrdeNum(orderNo);

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("idCard", order.getIdCard());
        paramsMap.put("order_num", orderNo);
        paramsMap.put("app_account", appAccount);
        paramsMap.put("risk_secret_key",riskSecretKey);

        String post = OkHttpUtils.post(credit_report_url, paramsMap);
        JSONObject json = JSON.parseObject(post);
        String code = json.getString("code");
        String data = json.getString("data");
        String message = json.getString("msg");
        businessVo.setCode(code);
        businessVo.setMessage(message);
        businessVo.setData(data);
        return businessVo;
    }



    public BusinessVo<String> getYysCallCount(String orderNo) throws OKhttpException {
        BusinessVo<String> businessVo = new BusinessVo<>();
        Order order = orderDao.selectOrderByOrdeNum(orderNo);

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("idCard", order.getIdCard());
        paramsMap.put("app_account", appAccount);
        paramsMap.put("risk_secret_key",riskSecretKey);
        String result= OkHttpUtils.post(yys_call_count_url, paramsMap);
        JSONObject json = JSON.parseObject(result);
        String code = json.getString("code");
        String data = json.getString("data");
        String message = json.getString("msg");
        businessVo.setCode(code);
        businessVo.setMessage(message);
        businessVo.setData(data);
        return businessVo;
    }

    /**
     * 类转换
     * @param reportVo
     * @param report
     */
    private void reportConvert(ReportResultVo reportVo, Report report) {
        if(report==null){
            return;
        }
        reportVo.setId(report.getId());
        reportVo.setCreateTime(DateUtils.format(report.getCreateTime(),DateUtils.yyyyMMdd_format));
        reportVo.setOrderTime(DateUtils.format(report.getOrderTime(),DateUtils.yyyyMMdd_format));
        reportVo.setOrderNo(report.getOrderNo());
        reportVo.setReportContent(report.getReportContent());
        reportVo.setReportNo(report.getReportNo());
        reportVo.setMerName(report.getMerName());
    }

    /**
     * 下载成功返回报告
     * @param orderNo
     * @param token
     * @return
     */
    public BusinessVo<String> getReportUrl(String orderNo, String token,Integer requestType) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        String code = BusinessConstantsUtils.SUCCESS_CODE;
        String message = BusinessConstantsUtils.SUCCESS_DESC;
        Order order= orderDao.selectOrderByOrdeNum(orderNo);
        //数据库有链接直接返回
        if(StringUtils.isNotBlank(order.getReportUrl())){
            businessVo.setCode(code);
            businessVo.setMessage(message);
            businessVo.setData(order.getReportUrl());
            return businessVo;
        }
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("num",orderNo.toString());
        paramsMap.put("requestType",requestType.toString());
        paramsMap.put("token",token);
        String urlParams = "";
        try {
            urlParams = ParamsUtils.mapToString(paramsMap);
        } catch (DataDisposeException e) {
            code = BusinessConstantsUtils.REQUEST_REPORT_DEFEAT_CODE;
            message = BusinessConstantsUtils.REQUEST_REPORT_DEFEAT_DESC;
            logger.error(e.getMessage(),e);
        }
        String url = reportUrl+"?"+urlParams;
//        String url="test.xcbd.cn/report.html?type=935&token=1b6a154347fd407890a5ad0c5e725819";
//        Order order = orderDao.selectOrderByOrdeNum(orderNo);
        User byId = userDao.getById(order.getUserId());
        String destPath = byId.getRealname().trim()+order.getOrderNo()+".pdf";

        File reportFile = htmlToPdfComponent.convert(url, destPath);
        logger.info("filePath is {}",reportFile.getPath());

        String ossUrl = "";
        try {
            ossUrl = ossComponent.uploadFile(reportOssPath + "/" + reportFile.getName(), new FileInputStream(reportFile));
            logger.info("下载地址：{}",ossUrl);
//            if(reportFile.exists()){
//                reportFile.delete();
//            }
            businessVo.setData(ossUrl);
        } catch (FileException | FileNotFoundException e) {
            logger.error(e.getMessage(),e);
            code = BusinessConstantsUtils.READ_FILE_DEFEAT_CODE;
            message = BusinessConstantsUtils.READ_FILE_DEFEAT_DESC;
        }
        if (StringUtils.isNotBlank(ossUrl)){
            Order orderForUpdate = new Order();
            orderForUpdate.setId(order.getId());
            orderForUpdate.setReportUrl(ossUrl);
            orderDao.update(orderForUpdate);
        }else {
            code = BusinessConstantsUtils.REPORT_UPLOAD_OSS_DEFEAT_CODE;
            message = BusinessConstantsUtils.REPORT_UPLOAD_OSS_DEFEAT_DESC;
        }
        businessVo.setCode(code);
        businessVo.setMessage(message);
        return businessVo;
    }

}
