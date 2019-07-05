package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.ProxyUserDao;
import com.xiaochong.loan.background.dao.RateTemplateDao;
import com.xiaochong.loan.background.entity.po.ProxyUser;
import com.xiaochong.loan.background.entity.po.RateTemplate;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.RateTemplateVo;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("rateTemplateService")
public class RateTemplateService extends BaseService{

    private Logger logger = LoggerFactory.getLogger(RateTemplateService.class);

    @Resource(name = "rateTemplateDao")
    private RateTemplateDao rateTemplateDao;

    @Resource(name = "sessionComponent")
    private SessionComponent sessionComponent;

    @Resource(name = "proxyUserDao")
    private ProxyUserDao proxyUserDao;


    /**
     * 新增费率
     * @param rateTemplate 费率对象
     * @param token token
     * @return 操作结果
     */
    @Transactional
    public BusinessVo<Boolean> insertTemplate(String token,
                                              String rateName,
                                              BigDecimal depositMoney,
                                              Integer depositType,
                                              BigDecimal interestRate,
                                              Integer overdueMoneyType,
                                              BigDecimal overdueRate,
                                              BigDecimal overdueConstant,
                                              Integer overdueType,
                                              BigDecimal serviceCharge,
                                              Integer stagingType){
        BusinessVo<Boolean> businessVo = new BusinessVo<>();
        String code = BusinessConstantsUtils.SUCCESS_CODE;
        String message = BusinessConstantsUtils.SUCCESS_DESC;

        String key = (token+"-"+ UserLoginTypeEnum.WEBAPP.getType());
        String userId = sessionComponent.getAttribute(key);
        ProxyUser proxyUser = StringUtils.isNotBlank(userId)?
                proxyUserDao.getById(Integer.parseInt(userId)):null;
        if(proxyUser==null){
            businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
            businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
            logger.warn("rateTemplateService.insertTemplate登录失效！token:{}",token);
            return businessVo;
        }
        RateTemplate rateTemplate = new RateTemplate();
        rateTemplate.setRateName(rateName);
        rateTemplate.setMerId(proxyUser.getMerchId());
        List<RateTemplate> rateTemplates = rateTemplateDao.selectRateTemplate(rateTemplate);
        if(rateTemplates!=null && rateTemplates.size()>0){
            businessVo.setCode(BusinessConstantsUtils.RATE_NAME_REPEAT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.RATE_NAME_REPEAT_DESC);
            logger.warn("rateTemplateService.insertTemplate名称重复rateTemplate:",rateTemplate);
            return businessVo;
        }
        rateTemplate = new RateTemplate();
        rateTemplate.setRateName(rateName);
        rateTemplate.setStagingType(stagingType);
        rateTemplate.setInterestRate(interestRate);
        rateTemplate.setServiceCharge(serviceCharge);
        rateTemplate.setDepositType(depositType);
        rateTemplate.setDepositMoney(depositMoney);
        rateTemplate.setOverdueType(overdueType);
        rateTemplate.setOverdueMoneyType(overdueMoneyType);
        rateTemplate.setOverdueRate(overdueRate);
        rateTemplate.setOverdueConstant(overdueConstant);
        rateTemplate.setMerId(proxyUser.getMerchId());
        rateTemplate.setCreateUser(proxyUser.getId());
        rateTemplateDao.insert(rateTemplate);
        businessVo.setMessage(message);
        businessVo.setCode(code);
        return businessVo;
    }


    /**
     * 费率列表
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @return 分页结果
     */
    public BusinessVo<BasePageInfoVo<RateTemplateVo>> RateTemplateList(Integer pageNum,Integer pageSize,String token){
        BusinessVo<BasePageInfoVo<RateTemplateVo>> businessVo = new BusinessVo<>();
        String code = BusinessConstantsUtils.SUCCESS_CODE;
        String message = BusinessConstantsUtils.SUCCESS_DESC;

        String key = (token+"-"+ UserLoginTypeEnum.WEBAPP.getType());
        String userId = sessionComponent.getAttribute(key);
        if (StringUtils.isNotBlank(userId)){
            ProxyUser proxyUser = proxyUserDao.getById(Integer.parseInt(userId));
            if (Objects.nonNull(proxyUser)){
                Page<RateTemplate> rateTemplates = rateTemplateDao.selectAllPage(pageNum, pageSize,proxyUser.getMerchId());
                List<RateTemplate> result = rateTemplates.getResult();

                List<RateTemplateVo> rateTemplateVoList = new ArrayList<>();
                result.forEach(rateTemplate -> {
                    RateTemplateVo rateTemplateVo = new RateTemplateVo();
                    rateTemplateVo.setRateName(rateTemplate.getRateName());
                    rateTemplateVo.setId(rateTemplate.getId().toString());
                    rateTemplateVo.setStagingType(StagingTypeEnum.getNameByType(rateTemplate.getStagingType()));
                    BigDecimal interestRate = rateTemplate.getInterestRate();
                    String interestType;
                    if (Objects.equals(StagingTypeEnum.MONTH.getType(), rateTemplate.getStagingType())){
                        interestType = "%/月";
                    }else {
                        interestType = "/次";
                    }
                    rateTemplateVo.setInterestRate(interestRate + interestType);
                    rateTemplateVo.setServiceCharge(rateTemplate.getServiceCharge() + "%");
                    BigDecimal depositMoney = rateTemplate.getDepositMoney();
                    Integer depositType = rateTemplate.getDepositType();
                    if (Objects.equals(DepositTypeEnum.ACTUAL.getType(), depositType)){
                        rateTemplateVo.setDepositMoney(depositMoney+"元");
                    }else {
                        rateTemplateVo.setDepositMoney(depositMoney+"%");
                    }
                    rateTemplateVo.setOverdueRate(rateTemplate.getOverdueRate()+"%/日");
                    Integer overdueType = rateTemplate.getOverdueType();
                    String overdueTypeName = "全部待还";
                    if (Objects.equals(OverdueTypeEnum.CURRENT.getType(), overdueType)){
                        overdueTypeName = "当期待还";
                    }
                    rateTemplateVo.setOverdueType(overdueTypeName + OverdueMoneyTypeEnum.getNameByType(rateTemplate.getOverdueMoneyType()));
                    rateTemplateVo.setCreateTime(DateUtils.format(rateTemplate.getCreateTime(),DateUtils.yyyyMMddHHmmss_hanziformat));
                    rateTemplateVo.setOverdueConstant(rateTemplate.getOverdueConstant()+"/元");
                    ProxyUser user = proxyUserDao.getById(rateTemplate.getCreateUser());
                    rateTemplateVo.setCreateUser(user.getUsername());
                    rateTemplateVoList.add(rateTemplateVo);
                });
                BasePageInfoVo<RateTemplateVo> pageInfoVo = assemblyBasePageInfo(rateTemplates.toPageInfo());
                pageInfoVo.setResultList(rateTemplateVoList);
                businessVo.setData(pageInfoVo);
            }else {
                code = BusinessConstantsUtils.PHONE_ERROR_CODE;
                message = BusinessConstantsUtils.PHONE_ERROR_DESC;
            }
        }

        businessVo.setCode(code);
        businessVo.setMessage(message);
        return businessVo;
    }

}
