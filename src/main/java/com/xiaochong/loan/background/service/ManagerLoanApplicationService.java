package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.OssComponent;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.LoanApplication;
import com.xiaochong.loan.background.entity.po.Merchantinfo;
import com.xiaochong.loan.background.entity.po.ProxyUser;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.ManageLoanApplicationVo;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.BusinessConstantsUtils;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.enums.StagingTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinxin on 2017/9/14.
 */
@Service
public class ManagerLoanApplicationService extends BaseService {

    private Logger logger = LoggerFactory.getLogger(LoanApplicationService.class);


    @Autowired
    private OssComponent ossComponent;

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private ProxyUserDao proxyUserDao;

    @Autowired
    private LoanApplicationDao loanApplicationDao;

    @Autowired
    private BorrowerDao borrowerDao;
    /**
     * 根据状态查询
     * @param loanApplication
     * @param pageNum
     * @param pageSize
     * @return
     */
    public BusinessVo<BasePageInfoVo<ManageLoanApplicationVo>> selectLoanApplicationList(LoanApplication loanApplication, Integer pageNum, Integer pageSize) {
        BusinessVo<BasePageInfoVo<ManageLoanApplicationVo>> businessVo = new BusinessVo<>();
        PageHelper.startPage(pageNum, pageSize, true);
        Page<LoanApplication> page = (Page<LoanApplication>) loanApplicationDao.selectLoanAuditApplicationListByStutus(loanApplication);
        PageInfo<LoanApplication> loanMerchantinfoVoPageInfo = page.toPageInfo();
        BasePageInfoVo<ManageLoanApplicationVo> basePageInfoVo = assemblyBasePageInfo(loanMerchantinfoVoPageInfo);
        List<ManageLoanApplicationVo> loanApplicationVoList = new ArrayList<>();
        List<LoanApplication> result = page.getResult();
        if(!CollectionUtils.isEmpty(result)){
            loanApplicationListConvert(loanApplicationVoList,result);
        }
        basePageInfoVo.setResultList(loanApplicationVoList);
        businessVo.setData(basePageInfoVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 放贷集合类转换
     * @param loanApplicationVoList
     * @param result
     */
    private void loanApplicationListConvert(List<ManageLoanApplicationVo> loanApplicationVoList, List<LoanApplication> result) {
        result.forEach(loanApplication -> {
            ManageLoanApplicationVo loanApplicationVo = new ManageLoanApplicationVo();
            loanApplicationConvert(loanApplicationVo,loanApplication);
            loanApplicationVoList.add(loanApplicationVo);
        });
    }

    /**
     * 放贷类转换
     * @param loanApplicationVo
     * @param loanApplication
     */
    private void loanApplicationConvert(ManageLoanApplicationVo loanApplicationVo, LoanApplication loanApplication) {
        //创建人信息
        Integer createBy = loanApplication.getCreateBy();
        if(createBy!=null){
            ProxyUser byId = proxyUserDao.getById(createBy);
            if(byId!=null){
                loanApplicationVo.setCreateByName(byId.getUsername());
                loanApplicationVo.setCreateByPhone(byId.getPhone());
            }
        }
        loanApplicationVo.setId(loanApplication.getId());
        loanApplicationVo.setUserName(loanApplication.getBorrowerName());
        loanApplicationVo.setPhone(loanApplication.getBorrowerPhone());
        loanApplicationVo.setIdCard(loanApplication.getBorrowerIdCard());
        loanApplicationVo.setLoanMoney(loanApplication.getLoanMoney().toString());
        loanApplicationVo.setRemitMoney(loanApplication.getRemitMoney().toString());
        loanApplicationVo.setOrderNo(loanApplication.getOrderNo());
        loanApplicationVo.setSubmitType(loanApplication.getSubmitType());
        String stageType = loanApplication.getStageType();
        String suffix;
        if(StagingTypeEnum.DAY.getType().toString().equals(stageType)){
            suffix="天";
        }else {
            suffix="月";
        }
        loanApplicationVo.setStageType(stageType);
        loanApplicationVo.setStageLimit(loanApplication.getStageLimit()+suffix);
        loanApplicationVo.setCreateTime(DateUtils.format(loanApplication.getCreateTime(),DateUtils.yyyyMMdd_format));
        loanApplicationVo.setStatus(loanApplication.getStatus());
        Integer merchId = loanApplication.getMerchId();
        loanApplicationVo.setMerchId(merchId);
        if(merchId!=null){
            Merchantinfo merchantinfoByToid = merchantDao.findMerchantinfoByToid(merchId);
            if(merchantinfoByToid!=null){
                loanApplicationVo.setMerchName(merchantinfoByToid.getMerchantName());
            }
        }
    }



}
