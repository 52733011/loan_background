package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.SMSComponent;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.controller.webapp.SubmitDataPageController;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jinxin on 2017/9/7.
 */
@Service
public class LoanApplicationAuditService extends BaseService{

    private Logger logger = LoggerFactory.getLogger(SubmitDataPageController.class);

    @Autowired
    private MerchantAuditDataDao merchantAuditDataDao;

    @Autowired
    private SessionComponent sessionComponent;

    @Autowired
    private LoanApplicationDao loanApplicationDao;

    @Autowired
    private RateTemplateDao rateTemplateDao;

    @Autowired
    private RepaymentPlanDao repaymentPlanDao;

    @Autowired
    private ProxyUserDao proxyUserDao;

    @Autowired
    private BorrowerBalanceDao borrowerBalanceDao;

    @Autowired
    private SMSComponent smsComponent;

    @Autowired
    private  MerchantDao merchantDao;

    @Autowired
    private  SmsTemplateDao smsTemplateDao;

    @Autowired
    private  BankCardDao bankCardDao;

    @Value("${webapp.billingInfoUrl}")
    private String billing_info_url;

    /**
     * 审核或者重新提交
     * 可以多次重复提交，每次都会保存，以最后一次为准
     *  审核只能一次确认放款后无法再次审核
     * @param merchantAuditData
     * @param token
     * @param type
     * @return
     */
    @Transactional
    public BusinessVo<String> auditOrResubmit(MerchantAuditData merchantAuditData, String token, String type) {
        BusinessVo<String>  businessVo = new BusinessVo<>();
        Integer applicationId = merchantAuditData.getApplicationId();
        LoanApplication applicationById = loanApplicationDao.selectLoanApplicationById(applicationId);
        Integer merchId = applicationById.getMerchId();

        List<MerchantAuditData> merchantAuditDataList = merchantAuditDataDao.selectMerchantAuditDataByApplicationId(applicationId);
        if(CollectionUtils.isNotEmpty(merchantAuditDataList)){
            MerchantAuditData auditDataOld = merchantAuditDataList.get(0);
            String status = auditDataOld.getStatus();
            if(LoanApplicationAuditStatusEnum.AUDIT_PASS.getType().equals(status)){
                businessVo.setData("禁止重复审核！");
                businessVo.setCode(BusinessConstantsUtils.LOAN_APPLICATION_AUDITED_CODE);
                businessVo.setMessage(BusinessConstantsUtils.LOAN_APPLICATION_AUDITED_DESC);
                return businessVo;
            }
        }

        int proxyUserIdInteger;
        if(LoanAuditTypeEnum.XIAOCHONG_AUDIT.getType().equals(type)){
            StringBuilder key = new StringBuilder(UserLoginTypeEnum.MANAGE.getType()).append("-").append(token);
            ManageAdmin  manageAdmin = sessionComponent.getAttributeManageAdmin(key.toString());
            proxyUserIdInteger=manageAdmin.getId();
        }else {
            String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
            proxyUserIdInteger = Integer.parseInt(proxyUserId);
        }
        merchantAuditData.setAuditBy(proxyUserIdInteger);
        Date date = new Date();
        merchantAuditData.setAuditTime(date);
        merchantAuditDataDao.insertMerchantAuditData(merchantAuditData);
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setId(applicationId);
        loanApplication.setUpdateTime(date);
        loanApplication.setUpdateBy(proxyUserIdInteger);
        String auditStatus = merchantAuditData.getStatus();
        if(LoanApplicationAuditStatusEnum.SUBMIT_AGAIN.getType().equals(auditStatus)){
            loanApplication.setStatus(LoanApplicationStatusEnum.RESUBMIT.getType());
            loanApplicationDao.updateById(loanApplication);
            businessVo.setData("重新提交成功！");
            businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
            businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
            return businessVo;
        }else {
            loanApplication.setStatus(LoanApplicationStatusEnum.LOANED.getType());
        }
        loanApplicationDao.updateById(loanApplication);

        //生成还款计划
        Date loanTime=applicationById.getLoanTime() ;
        if(loanTime==null){
            //放款时间未设置直接使用当前时间,并更新数据库
            loanTime=date;
            LoanApplication loanApplicationForUpdate = new LoanApplication();
            loanApplicationForUpdate.setId(applicationId);
            loanApplicationForUpdate.setLoanTime(loanTime);
            loanApplicationDao.updateById(loanApplicationForUpdate);
        }
        loanTime=DateUtils.getDate(loanTime,DateUtils.yyyyMMdd_format);
        long l = DateUtils.addDay(loanTime, 1).getTime() - 1000;
        loanTime = new Date(l);
        if(LoanApplicationAuditStatusEnum.AUDIT_PASS.getType().equals(auditStatus)){
            BigDecimal loanMoney = applicationById.getLoanMoney();
            String stageType = applicationById.getStageType();
            Integer stageLimit = applicationById.getStageLimit();
            RateTemplate rateTemplate = rateTemplateDao.selectRateTemplateById(applicationById.getRateId());
            BigDecimal interestRate =rateTemplate.getInterestRate().divide(new BigDecimal("100")) ;
            BigDecimal firstStage;
            Integer borrowerId = applicationById.getBorrowerId();
            if(StagingTypeEnum.MONTH.getType().toString().equals(stageType)){
                BigDecimal principalMoney = BigDecimalOperationUtils.divBigDecimal(loanMoney,stageLimit,2);
                firstStage=principalMoney;
                for (int i = 1; i <=stageLimit ; i++) {
                    if(i==stageLimit){
                        principalMoney=loanMoney.subtract(BigDecimalOperationUtils.mul(principalMoney,stageLimit-1));
                    }
                    RepaymentPlan repaymentPlan=createRepaymentPlan(borrowerId,merchId,loanMoney,proxyUserIdInteger,DateUtils.addMonth(loanTime,i),applicationId,principalMoney,interestRate,i);

                    logger.info("还款计划：{}",repaymentPlan);
                    repaymentPlanDao.insertRepaymentPlan(repaymentPlan);
                }
            }else {
                //按天放款
                firstStage=loanMoney;
                RepaymentPlan repaymentPlan=
                createRepaymentPlan(borrowerId,merchId,loanMoney,proxyUserIdInteger,DateUtils.addDay(loanTime,stageLimit),applicationId,loanMoney,interestRate,1);
                logger.info("还款计划：{}",repaymentPlan);
                repaymentPlanDao.insertRepaymentPlan(repaymentPlan);
            }

            //审核成功发送短信
            List<LoanSmsTemplate> loanSmsTemplates = new ArrayList<>();
            LoanSmsTemplate companyName = new LoanSmsTemplate();
            //公司名
            companyName.setSmaTagEnum(SmaTagEnum.COMPANY_NAME);

            Merchantinfo merchantinfoByToid = merchantDao.findMerchantinfoByToid(merchId);
            companyName.setParam(merchantinfoByToid.getMerchantName());
            loanSmsTemplates.add(companyName);
            LoanSmsTemplate borrweInfo = new LoanSmsTemplate();
            borrweInfo.setSmaTagEnum(SmaTagEnum.BORROWING_INFORMATION);
            StringBuilder stringBuilder = new StringBuilder(applicationById.getLoanMoney().toString());
            String stageUnit;
            if(StagingTypeEnum.MONTH.getType().toString().equals(stageType)){
                stageUnit="个月";
            }else {
                stageUnit="天,1期";
            }
            stringBuilder.append("元").append("分期").append(applicationById.getStageLimit()).append(stageUnit);
            borrweInfo.setParam(stringBuilder);
            loanSmsTemplates.add(borrweInfo);
            LoanSmsTemplate bankcardTemp = new LoanSmsTemplate();
            bankcardTemp.setSmaTagEnum(SmaTagEnum.BANK_CARD_NO);
            BankCard bankCard = new BankCard();
            bankCard.setOrderNo(applicationById.getOrderNo());
            BankCard byBankCard = bankCardDao.getByBankCard(bankCard);
            if(byBankCard!=null){
                String bankCardNo = byBankCard.getBankCard();
                int length = bankCardNo.length();
                if(length>=4){
                    bankcardTemp.setParam(bankCardNo.substring(length -4));
                }else {
                    bankcardTemp.setParam(bankCardNo);
                }
            }
            loanSmsTemplates.add(bankcardTemp);
            //下一期还款信息
//            第一期金额为332.43元，最后还款日为9月10日
            Date firstPayTime;
            if(StagingTypeEnum.MONTH.getType().toString().equals(stageType)){
               firstPayTime= DateUtils.addMonth(loanTime,1);
            }else {
                firstPayTime= DateUtils.addDay(loanTime,applicationById.getStageLimit());
            }
            StringBuilder repaymentInfo = new StringBuilder("第一期金额为");
            repaymentInfo.append(firstStage.add(loanMoney.multiply(interestRate))).append("元，最后还款日为").append(DateUtils.format(firstPayTime,DateUtils.yyyyMMdd_hanziformat));
            LoanSmsTemplate nextRepayInfo = new LoanSmsTemplate();
            nextRepayInfo.setSmaTagEnum(SmaTagEnum.NEXT_REPAYMENT);
            nextRepayInfo.setParam(repaymentInfo);
            loanSmsTemplates.add(nextRepayInfo);
            //账单信息
            LoanSmsTemplate billInfo = new LoanSmsTemplate();
            billInfo.setSmaTagEnum(SmaTagEnum.BILLING_INFORMATION);
            //TODO 账单信息待配置
            billInfo.setParam(billing_info_url);
            loanSmsTemplates.add(billInfo);
            SmsTemplate smsTemplate= new SmsTemplate();
            smsTemplate.setMerchId(merchId);
            smsTemplate.setSendStatus(SmsSendStatueEnum.STAGED_LOAN_SUCCESS.getType());
            smsTemplate.setStatus("1");
            SmsTemplate bySmsTemplate = smsTemplateDao.getBySmsTemplate(smsTemplate);
            if (bySmsTemplate!=null){
                try {
                    logger.info("短信标签集合：{}",loanSmsTemplates);
                    smsComponent.sendLoanSms(loanSmsTemplates,bySmsTemplate,applicationById.getBorrowerPhone());
                } catch (Exception e) {
                    logger.error("放款成功短信发送失败：{}",e);
                }
            }
        }
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        return businessVo;
    }

    /**
     * 生成还款计划对象
     *
     *
     * @param borrowerId
     * @param merchId
     * @param proxyUserIdInteger
     * @param returnTime
     * @param applicationId
     * @param principalMoney
     * @param interestRate
     * @param stageNo
     * @return
     */
    private RepaymentPlan createRepaymentPlan(Integer borrowerId, Integer merchId, BigDecimal loanMoney, Integer proxyUserIdInteger, Date returnTime, Integer applicationId, BigDecimal principalMoney, BigDecimal interestRate, Integer stageNo){
        RepaymentPlan repaymentPlan = new RepaymentPlan();
        repaymentPlan.setBorrowerId(borrowerId);
        repaymentPlan.setCreateTime(new Date());
        repaymentPlan.setMerchId(merchId);
        repaymentPlan.setCreateBy(proxyUserIdInteger);
        repaymentPlan.setReturnTime(returnTime);
        repaymentPlan.setApplicationId(applicationId);
        repaymentPlan.setPrincipalMoney(principalMoney);
        BigDecimal interest = loanMoney.multiply(interestRate);
        repaymentPlan.setInterest(interest);
        repaymentPlan.setWholeMoney(principalMoney.add(interest));
        repaymentPlan.setStatus(RepaymentStatusEnum.WAITING_REPAYMENT.getType());
        BigDecimal zero = BigDecimal.ZERO;
        repaymentPlan.setPunishInterest(zero);
        repaymentPlan.setStageNo(stageNo);
        repaymentPlan.setReductiveMoney(zero);
        repaymentPlan.setOverdueDays(0);
        return  repaymentPlan;
    }

    /**
     * 分页查看还款计划
     */
    public BusinessVo<BasePageInfoVo<RepaymentPlanVo>> selectRepayPlan(Integer applicationId, Integer pageNum, Integer pageSize) {
        BusinessVo<BasePageInfoVo<RepaymentPlanVo>> businessVo = new BusinessVo<>();
        PageHelper.startPage(pageNum, pageSize, true);
        Page<RepaymentPlan> page = (Page<RepaymentPlan>) repaymentPlanDao.selectRepaymentPlanApplicationId(applicationId);
        PageInfo<RepaymentPlan> loanMerchantinfoVoPageInfo = page.toPageInfo();
        BasePageInfoVo<RepaymentPlanVo> basePageInfoVo = assemblyBasePageInfo(loanMerchantinfoVoPageInfo);
        List<RepaymentPlanVo> repaymentPlanVoList = new ArrayList<>();
        List<RepaymentPlan> result = page.getResult();
        if(!org.springframework.util.CollectionUtils.isEmpty(result)){
            repaymentPlanVoListConvert(repaymentPlanVoList,result);
        }
        basePageInfoVo.setResultList(repaymentPlanVoList);
        businessVo.setData(basePageInfoVo);
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
        repaymentPlanVo.setApplicationId(repaymentPlan.getApplicationId());
        Integer createBy = repaymentPlan.getCreateBy();
        if(createBy!=null){
            ProxyUser byId = proxyUserDao.getById(createBy);
            if(byId!=null){
                repaymentPlanVo.setCreateBy(createBy);
                repaymentPlanVo.setCreateByName(byId.getUsername());
            }
        }
        Integer updateBy = repaymentPlan.getUpdateBy();
        if(updateBy!=null){
            ProxyUser byIdupdate = proxyUserDao.getById(updateBy);
            if(byIdupdate!=null){
                repaymentPlanVo.setUpdateBy(updateBy);
                repaymentPlanVo.setUpdateByName(byIdupdate.getUsername());
            }
        }
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
     * 查询账户余额
     * @param applicationId
     * @return
     */
    public BusinessVo<BorrowerBalanceVo> selectBorrowerAccount(Integer applicationId) {
        BusinessVo<BorrowerBalanceVo> businessVo = new BusinessVo<>();
        LoanApplication loanApplication = loanApplicationDao.selectLoanApplicationById(applicationId);
        if(loanApplication!=null){
            BorrowerBalance borrowerBalance = borrowerBalanceDao.selectBorrowerBalanceByBorrowerId(loanApplication.getBorrowerId());
            BorrowerBalanceVo borrowerBalanceVo = new BorrowerBalanceVo();
            if(borrowerBalance!=null){
                borrowerBalanceVo.setId(borrowerBalance.getId());
                borrowerBalanceVo.setBalance(borrowerBalance.getBalance());
                borrowerBalanceVo.setBorrowerId(borrowerBalance.getBorrowerId());
            }
            businessVo.setData(borrowerBalanceVo);
        }
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }


    /*@Transactional
    public void repaymentPlanOverdueScheduled(){
        LoanApplication searchLoanApplication = new LoanApplication();
        searchLoanApplication.setStatus(LoanApplicationStatusEnum.LOANED.getType());
        List<LoanApplication> loanApplications = loanApplicationDao.selectLoanApplication(searchLoanApplication);
        if(CollectionUtil.isBlank(loanApplications)){
            logger.warn("计算逾期定时任务获取放款申请失败!");
            return;
        }
        for (LoanApplication loanApplication:loanApplications) {
            try {
                this.repaymentPlanOverdueByLoanApplication(loanApplication);
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
    }*/

    @Transactional(rollbackFor = Exception.class)
    public void repaymentPlanOverdueByLoanApplication(LoanApplication loanApplication) {
        RateTemplate rateTemplate = rateTemplateDao.selectRateTemplateById(loanApplication.getRateId());
        if(rateTemplate==null){
            logger.warn("计算逾期定时任务RateId获取费率模板失败! loanApplication：{}",loanApplication);
            return;
        }
        List<RepaymentPlan> repaymentPlans =
                repaymentPlanDao.listByApplicationIdStatus(
                        loanApplication.getId(),RepaymentPlanStatusEnum.PAY.getType());
        if(CollectionUtil.isBlank(repaymentPlans)){
            logger.warn("计算逾期定时任务获取还款计划表失败! loanApplication：{}",loanApplication);
            return;
        }
        logger.info("计算逾期定时任务，放款申请 loanApplication：{};共有{}条还款计划",
                loanApplication,repaymentPlans.size());
        for (int i = 0; i < repaymentPlans.size(); i++) {
            RepaymentPlan repaymentPlan = repaymentPlans.get(i);
            long nowDay = LocalDate.now().toEpochDay();
            long returnDay = DateUtils.dateToLocalDate(repaymentPlan.getReturnTime()).toEpochDay();
            long overdueDays =  nowDay - returnDay ;
            if(overdueDays<=0){
                continue;
            }
            //overdueDays = Math.abs(overdueDays);
            BigDecimal calculatedAmount  = new BigDecimal(0);//计算金额
            if(RateTemplateOverdueTypeEnum.CURRENT.getType().equals(rateTemplate.getOverdueType())){
                calculatedAmount = repaymentPlan.getPrincipalMoney();
                if(RateTemplateOverdueMoneyTypeEnum.PRINCIPAL_INTEREST.getType().equals(rateTemplate.getOverdueMoneyType())){
                    calculatedAmount = calculatedAmount.add(repaymentPlan.getInterest());
                }
            }else if(RateTemplateOverdueTypeEnum.ALL.getType().equals(rateTemplate.getOverdueType())) {
                calculatedAmount = loanApplication.getLoanMoney();
                if(RateTemplateOverdueMoneyTypeEnum.PRINCIPAL_INTEREST.getType().equals(rateTemplate.getOverdueMoneyType())){
                    for (int j = i; j < repaymentPlans.size(); j++) {
                        calculatedAmount = calculatedAmount.add(repaymentPlans.get(j).getInterest());
                    }
                }
            }
            //罚息 ：逾期常数 + 逾期计算金额 * 逾期费率 * 逾期天数
            MathContext mc = new MathContext(5, RoundingMode.DOWN);
            BigDecimal punishInterest = rateTemplate.getOverdueConstant().add(
                    calculatedAmount.multiply(
                            rateTemplate.getOverdueRate().divide(new BigDecimal(100),mc) ).
                            multiply(new BigDecimal(overdueDays)) );
            repaymentPlan.setPunishInterest(punishInterest);
            repaymentPlan.setStatus(RepaymentPlanStatusEnum.OVERDUE.getType());
            repaymentPlan.setOverdueDays(Integer.valueOf(overdueDays+""));
            BigDecimal wholeMoney = repaymentPlan.getPrincipalMoney().add(
                                    repaymentPlan.getInterest()
                                    ).add(punishInterest);
            repaymentPlan.setWholeMoney(wholeMoney);
            logger.info("逾期罚息计算完成! repaymentPlan:{},{}",
                    repaymentPlan,repaymentPlanDao.update(repaymentPlan));

        }
    }
}
