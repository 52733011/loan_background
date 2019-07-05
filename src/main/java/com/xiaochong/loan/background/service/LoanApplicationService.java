package com.xiaochong.loan.background.service;

import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by jinxin on 2017/9/11.
 */
@Service
public class LoanApplicationService {

    private Logger logger = LoggerFactory.getLogger(LoanApplicationService.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private BankCardDao bankCardDao;

    @Autowired
    private SessionComponent sessionComponent;

    @Autowired
    private ProxyUserDao proxyUserDao;

    @Autowired
    private RateTemplateDao rateTemplateDao;

    @Autowired
    private MerchDataTemplateDao merchDataTemplateDao;

    @Autowired
    private LoanApplicationDao loanApplicationDao;

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private BorrowerBalanceDao borrowerBalanceDao;

    @Autowired
    private BorrowerDao borrowerDao;

    /**
     * 根据订单号查询被调人信息
     * @param orderNo
     * @param token
     * @return
     */
    public BusinessVo<UserInfoVo> selectBorrowerInfoByOrderNo(String orderNo, String token) {
        BusinessVo<UserInfoVo> businessVo = new BusinessVo<>();
        UserInfoVo userInfoVo= new UserInfoVo();
        Order order = orderDao.selectOrderByOrdeNum(orderNo);
        ProxyUser proxyUserByToken = getProxyUserByToken(token);
        Integer merchId = proxyUserByToken.getMerchId();
        //运营要求暂时解除商户限制
//        if(order==null||!ObjectUtils.equals(merchId,order.getMerId())){
//            logger.error("订单号不存在：orderNo：{}",orderNo);
//            businessVo.setCode(BusinessConstantsUtils.ORDER_NO_NOT_EXISTS_CODE);
//            businessVo.setMessage(BusinessConstantsUtils.ORDER_NO_NOT_EXISTS_DESC);
//            return businessVo;
//        }else
        if(order==null){
            businessVo.setCode(BusinessConstantsUtils.ORDER_NO_NOT_EXISTS_CODE);
            businessVo.setMessage(BusinessConstantsUtils.ORDER_NO_NOT_EXISTS_DESC);
            return  businessVo;
        }else
        if(!OrderStatusEnum.FINISH.getType().equals(order.getStatus())){
            businessVo.setCode(BusinessConstantsUtils.REPORT_NOT_FINISH__CODE);
            businessVo.setMessage(BusinessConstantsUtils.REPORT_NOT_FINISH__DESC);
            return  businessVo;
        }else {
            userInfoVo.setIdCard(order.getIdCard());
            userInfoVo.setUserName(order.getRealname());
            userInfoVo.setPhone(order.getPhone());
        }
        Report report = reportDao.selectByOrderNo(orderNo);
        if(report!=null){
            userInfoVo.setReportNo(report.getReportNo());
            userInfoVo.setReportTime(DateUtils.format(report.getOrderTime(),DateUtils.ymdhms_format));
        }
        BankCard bankCard = new BankCard();
        bankCard.setOrderNo(orderNo);
        BankCard byBankCard = bankCardDao.getByBankCard(bankCard);
        if(byBankCard!=null){
            userInfoVo.setBankCard(byBankCard.getBankCard());
        }
        businessVo.setData(userInfoVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 根据类型查询费率列表
     * @param rateType
     * @param token
     * @return
     */
    public BusinessVo<List<RateTemplateVo>> selectRateTemplateByType(Integer rateType, String token) {
        BusinessVo<List<RateTemplateVo>> businessVo = new BusinessVo<>();
        ProxyUser proxyUser = getProxyUserByToken(token);
        Integer merchId = proxyUser.getMerchId();
        LogTraceUtils.info("根据放款类型查询费率列表","merchId"+merchId);
        RateTemplate rateTemplate = new RateTemplate();
        rateTemplate.setMerId(merchId);
        rateTemplate.setStagingType(rateType);
        List<RateTemplate> rateTemplateList = rateTemplateDao.selectRateTemplate(rateTemplate);
        List<RateTemplateVo> rateTemplateVoList =  new ArrayList<>();
        if(CollectionUtils.isNotEmpty(rateTemplateList)){
            rateTemplateListConvert(rateTemplateVoList,rateTemplateList);
        }else {
            businessVo.setCode(BusinessConstantsUtils.MERCHANT_RATE_TEMPLATE_IS_NULL_CODE);
            businessVo.setMessage(BusinessConstantsUtils.MERCHANT_RATE_TEMPLATE_IS_NULL_DESC);
            return businessVo;
        }
        businessVo.setData(rateTemplateVoList);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * rateList 集合类转换
     * @param rateTemplateVoList
     * @param rateTemplateList
     */
    private void rateTemplateListConvert(List<RateTemplateVo> rateTemplateVoList, List<RateTemplate> rateTemplateList) {
        rateTemplateList.forEach(rateTemplate -> {
            RateTemplateVo rateTemplateVo = new RateTemplateVo();
            rateTemplateConvert(rateTemplateVo,rateTemplate);
            rateTemplateVoList.add(rateTemplateVo);
        });
    }

    /**
     * rate类转换
     * @param rateTemplateVo
     * @param rateTemplate
     */
    private void rateTemplateConvert(RateTemplateVo rateTemplateVo, RateTemplate rateTemplate) {
        rateTemplateVo.setId(rateTemplate.getId().toString());
        rateTemplateVo.setRateName(rateTemplate.getRateName());
        BigDecimal depositMoney = rateTemplate.getDepositMoney();
        Integer depositType = rateTemplate.getDepositType();
        if (Objects.equals(DepositTypeEnum.ACTUAL.getType(), depositType)){
            rateTemplateVo.setDepositMoney(depositMoney+"元");
        }else {
            rateTemplateVo.setDepositMoney(depositMoney+"%");
        }
        String interestType;
        if (Objects.equals(StagingTypeEnum.MONTH.getType(), rateTemplate.getStagingType())){
            interestType = "/月";
        }else {
            interestType = "/次";
        }
        rateTemplateVo.setInterestRate(rateTemplate.getInterestRate().toString()+interestType);
        rateTemplateVo.setOverdueRate(rateTemplate.getOverdueRate()+"%/日");
        Integer overdueType = rateTemplate.getOverdueType();
        String overdueTypeName = "全部待还";
        if (Objects.equals(OverdueTypeEnum.CURRENT.getType(), overdueType)){
            overdueTypeName = "当期待还";
        }
        rateTemplateVo.setOverdueType(overdueTypeName+OverdueMoneyTypeEnum.getNameByType(rateTemplate.getOverdueMoneyType()));
        rateTemplateVo.setServiceCharge(rateTemplate.getServiceCharge().toString()+ "%");
        rateTemplateVo.setStagingType(rateTemplate.getStagingType().toString());
    }

    /**
     * 根据token获取proxyUser对象
     * @param token
     * @return
     */
    private ProxyUser getProxyUserByToken(String token){
        String idString = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        return proxyUserDao.getById(Integer.parseInt(idString));
    }

    /**
     * 查询商户模板信息
     * @param token
     * @return
     */
    public BusinessVo<List<MerchDataTemplateVo>> selectDataTemplateByMerchId(String token) {
        BusinessVo<List<MerchDataTemplateVo>> businessVo = new BusinessVo<>();
        ProxyUser proxyUser = getProxyUserByToken(token);
        MerchDataTemplate merchDataTemplate = new MerchDataTemplate();
        Integer merchId = proxyUser.getMerchId();
        LogTraceUtils.info("查询商户数据模板接口","merchId"+merchId);
        merchDataTemplate.setMerchId(merchId);
        merchDataTemplate.setStatus(MerchDataTemplateStatusEnum.USING.getType());
        List<MerchDataTemplate> merchDataTemplateList = merchDataTemplateDao.searchMerchDataTemplate(merchDataTemplate);
        List<MerchDataTemplateVo> merchDataTemplateVoList =  new ArrayList<>();
        if(CollectionUtils.isNotEmpty(merchDataTemplateList)){
            merchDataTemplateListConvert(merchDataTemplateVoList,merchDataTemplateList);
        }else {
            businessVo.setCode(BusinessConstantsUtils.MERCHANT_DATA_TEMPLATE_IS_NULL_CODE);
            businessVo.setMessage(BusinessConstantsUtils.MERCHANT_DATA_TEMPLATE_IS_NULL_DESC);
            return businessVo;
        }
        businessVo.setData(merchDataTemplateVoList);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 集合类转换
     * @param merchDataTemplateVoList
     * @param result
     */
    private void merchDataTemplateListConvert(List<MerchDataTemplateVo> merchDataTemplateVoList, List<MerchDataTemplate> result) {
        result.forEach(merchDataTemplate -> {
            MerchDataTemplateVo merchDataTemplateVo = new MerchDataTemplateVo();
            merchDataTemplateConvert(merchDataTemplateVo,merchDataTemplate);
            merchDataTemplateVoList.add(merchDataTemplateVo);
        });
    }

    /**
     * 模板类转换
     * @param merchDataTemplateVo
     * @param merchDataTemplate
     */
    private void merchDataTemplateConvert(MerchDataTemplateVo merchDataTemplateVo, MerchDataTemplate merchDataTemplate) {
        merchDataTemplateVo.setId(merchDataTemplate.getId());
        merchDataTemplateVo.setMerchId(merchDataTemplate.getMerchId());
        merchDataTemplateVo.setTemplateName(merchDataTemplate.getTemplateName());
    }

    /**
     * 生成放款订单
     * @param loanApplication
     * @param token
     * @return
     */
    @Transactional
    public BusinessVo<String> sponsorLoanApplication(LoanApplication loanApplication, String token) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        //期数限制
        String stageType = loanApplication.getStageType();
        Integer stageLimit = loanApplication.getStageLimit();

        if(StagingTypeEnum.MONTH.getType().toString().equals(stageType)){
            if(stageLimit>240){
                businessVo.setCode(BusinessConstantsUtils.STAGE_OUT_OF_MONTHS__CODE);
                businessVo.setMessage(BusinessConstantsUtils.STAGE_OUT_OF_MONTHS__DESC);
                return businessVo;
            }
        }else if(StagingTypeEnum.DAY.getType().toString().equals(stageType)){
            if(stageLimit>90){
                businessVo.setCode(BusinessConstantsUtils.STAGE_OUT_OF_DAYS__CODE);
                businessVo.setMessage(BusinessConstantsUtils.STAGE_OUT_OF_DAYS__DESC);
                return businessVo;
            }
        }
        ProxyUser proxyUserByToken = getProxyUserByToken(token);
        Integer merId = proxyUserByToken.getMerchId();
        String orderNo = loanApplication.getOrderNo();
        Order order = orderDao.selectOrderByOrdeNum(orderNo);
        //运营要求暂时解除商户限制
        //        if(order==null||!ObjectUtils.equals(merchId,order.getMerId())){
//            logger.error("订单号不存在：orderNo：{}",orderNo);
//            businessVo.setCode(BusinessConstantsUtils.ORDER_NO_NOT_EXISTS_CODE);
//            businessVo.setMessage(BusinessConstantsUtils.ORDER_NO_NOT_EXISTS_DESC);
//            return businessVo;
//        } else
        if(order==null){
            businessVo.setCode(BusinessConstantsUtils.ORDER_NO_NOT_EXISTS_CODE);
            businessVo.setMessage(BusinessConstantsUtils.ORDER_NO_NOT_EXISTS_DESC);
            return  businessVo;
        }else if(!OrderStatusEnum.FINISH.getType().equals(order.getStatus())){
            businessVo.setCode(BusinessConstantsUtils.REPORT_NOT_FINISH__CODE);
            businessVo.setMessage(BusinessConstantsUtils.REPORT_NOT_FINISH__DESC);
            return  businessVo;
        }
//        LoanApplication loanApplicationForSearch = new LoanApplication();
//        loanApplicationForSearch.setOrderNo(orderNo);
//        List<LoanApplication> loanApplicationList = loanApplicationDao.selectLoanApplication(loanApplication);
//        if(CollectionUtils.isNotEmpty(loanApplicationList)){
//            for (LoanApplication l: loanApplicationList) {
//                if(!LoanApplicationStatusEnum.REFUSED.getType().equals(l.getStatus())){
//                    businessVo.setCode(BusinessConstantsUtils.ORDER_BEEN_USED_CODE);
//                    businessVo.setMessage(BusinessConstantsUtils.ORDER_BEEN_USED_DESC);
//                    return businessVo;
//                }
//            }
//        }

        String proxyuser = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        int createBy = Integer.parseInt(proxyuser);
        ProxyUser byId = proxyUserDao.getById(createBy);
        if(byId!=null){
            Merchantinfo merchantinfoByToid = merchantDao.findMerchantinfoByToid(byId.getMerchId());
            String auditType = merchantinfoByToid.getAuditType();
            if(StringUtils.isNotBlank(auditType)){
                loanApplication.setMerchId(merId);
                loanApplication.setAuditType(auditType);
            }else {
                businessVo.setCode(BusinessConstantsUtils.LIMITED_AUTHORITY_ERROR_CODE);
                businessVo.setMessage(BusinessConstantsUtils.LIMITED_AUTHORITY_ERROR_DESC);
                return businessVo;
            }
        }
        //插入 借贷人信息
        Date now = new Date();
        Borrower borrower = new Borrower();
        borrower.setMerchId(merId);
        borrower.setIdCard(order.getIdCard());
        borrower.setPhone(order.getPhone());
        List<Borrower> borrowerList = borrowerDao.selectBorrowerByPhoneOrIdCard(borrower);
        Borrower borrowerForLoan;

        if(CollectionUtils.isEmpty(borrowerList)){
            borrower.setName(order.getRealname());
            borrower.setPhone(order.getPhone());
            borrower.setMerchId(merId);
            borrower.setIdCard(order.getIdCard());
            borrower.setCreateBy(createBy);
            borrower.setCreateTime(now);
            borrowerDao.insertBorrower(borrower);
            borrowerForLoan=borrower;
            //插入余额
            BorrowerBalance borrowerBalance= new BorrowerBalance();
            borrowerBalance.setBalance(BigDecimal.ZERO);
            borrowerBalance.setBorrowerId(borrower.getId());
            borrowerBalance.setCreateBy(createBy);
            borrowerBalance.setCreateTime(now);
            borrowerBalanceDao.insert(borrowerBalance);
        }else {
            borrowerForLoan=borrowerList.get(0);
        }
        Integer borrowerForLoanId = borrowerForLoan.getId();
        loanApplication.setBorrowerId(borrowerForLoanId);
        loanApplication.setBorrowerName(borrowerForLoan.getName());
        loanApplication.setBorrowerPhone(borrowerForLoan.getPhone());
        loanApplication.setBorrowerIdCard(borrowerForLoan.getIdCard());
        if(DataSubmitByTypeEnum.BORROWER.getType().equals(loanApplication.getSubmitByType())){
            loanApplication.setSubmitBy(borrowerForLoanId);
        }
        //计算打款金额
        BigDecimal loanMoney = loanApplication.getLoanMoney();
        RateTemplate rateTemplate = rateTemplateDao.selectRateTemplateById(loanApplication.getRateId());
        if(rateTemplate!=null){
            BigDecimal subtract = BigDecimalOperationUtils.sub(100, rateTemplate.getServiceCharge()).subtract(rateTemplate.getDepositMoney());
            BigDecimal remitMoney = loanMoney.multiply(subtract);
            loanApplication.setRemitMoney(BigDecimalOperationUtils.divBigDecimal(remitMoney,100,2));
            logger.info("打款金额：appID：{}，remitMoney：{}",loanApplication.getId(),remitMoney);
        }
        loanApplication.setStatus(LoanApplicationStatusEnum.WAITING_SUBMIT.getType());
        loanApplication.setCreateBy(createBy);
        loanApplication.setCreateTime(now);
        loanApplicationDao.insert(loanApplication);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return  businessVo;
    }
}
