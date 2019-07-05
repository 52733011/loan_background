package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.MerchantRechargeVo;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.AccountRecordResultEnum;
import com.xiaochong.loan.background.utils.enums.MerchantRechargeRechargeStatusEnum;
import com.xiaochong.loan.background.utils.enums.TransactTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jinxin on 2017/8/15.
 */
@Service
public class MerchantAccountService extends BaseService{

    private Logger logger = LoggerFactory.getLogger(MerchantAccountService.class);

    @Autowired
    private MerchantAccountDao merchantAccountDao;

    @Autowired
    private MerchantRechargeDao merchantRechargeDao;

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private ProxyUserDao proxyUserDao;

    @Autowired
    private AccountRecordDao accountRecordDao;

    @Autowired
    private ManageAdminDao manageAdminDao;

    private Lock lock = new ReentrantLock();

    public Map<String,Integer> updateMerchantAccount(Integer count, Integer merchId, Integer userId) {
        Map<String,Integer> resultMap = new HashMap<>();
        try{
            lock.lock();
            //result-- 1:更新成功，0:余额不足
            Integer result = 1;
            MerchantAccount merchantAccount = merchantAccountDao.selectMerchantAccountByMerchId(merchId);
            //原账户余额
            Integer surplusCount =merchantAccount.getSurplusCount();
            resultMap.put("beforeCount",count);
                //充值或花费
            surplusCount += count;
            if(surplusCount<0){
                result = 0;
            }
            if (result == 1){
                MerchantAccount merchantAccountUpdate= new MerchantAccount();
                merchantAccountUpdate.setId(merchantAccount.getId());
                merchantAccountUpdate.setUpdateBy(userId);
                merchantAccountUpdate.setUpdateTime(new Date());
                merchantAccountUpdate.setSurplusCount(surplusCount);
                merchantAccountDao.updateMerchantAccount(merchantAccountUpdate);
            }
            resultMap.put("state",result);
            resultMap.put("afterCount",surplusCount);
        }catch (Exception e){
            resultMap.put("state",2);
            logger.error(e.getMessage(),e);
        }finally {
            lock.unlock();
        }
        return resultMap;
    }

    /**
     * 充值账户分页查询
     * @param merchantRecharge
     * @param pageNum
     * @param pageSize
     * @return
     */
    public BusinessVo<BasePageInfoVo<MerchantRechargeVo>> selectMerchantRecharge(MerchantRecharge merchantRecharge, Integer pageNum, Integer pageSize) {
        BusinessVo<BasePageInfoVo<MerchantRechargeVo>> businessVo = new BusinessVo<>();
        PageHelper.startPage(pageNum, pageSize, true);
        Page<MerchantRecharge> page = (Page<MerchantRecharge>) merchantRechargeDao.selectMerchantRecharge(merchantRecharge);
        PageInfo<MerchantRecharge> loanMerchantinfoVoPageInfo = page.toPageInfo();
        BasePageInfoVo<MerchantRechargeVo> basePageInfoVo = assemblyBasePageInfo(loanMerchantinfoVoPageInfo);
        List<MerchantRechargeVo> merchantinfoVoList = new ArrayList<>();
        List<MerchantRecharge> result = page.getResult();
        if(result!=null&&result.size()!=0){
            MerchantRechargeListConvert(merchantinfoVoList,result);
        }
        basePageInfoVo.setResultList(merchantinfoVoList);
        businessVo.setData(basePageInfoVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 返回集合转化
     * @param merchantinfoVoList
     * @param result
     */
    private void MerchantRechargeListConvert(List<MerchantRechargeVo> merchantinfoVoList, List<MerchantRecharge> result) {
        result.forEach(merchantRecharge->{
            MerchantRechargeVo merchantRechargeVo= new MerchantRechargeVo();
            MerchantRechargeConvert(merchantRechargeVo,merchantRecharge);
            merchantinfoVoList.add(merchantRechargeVo);
        });
    }

    /**
     * 返回类转换
     * @param merchantRechargeVo
     * @param merchantRecharge
     */
    private void MerchantRechargeConvert(MerchantRechargeVo merchantRechargeVo, MerchantRecharge merchantRecharge) {
        merchantRechargeVo.setId(merchantRecharge.getId());
        merchantRechargeVo.setRechargeNo(merchantRecharge.getRechargeNo());
        merchantRechargeVo.setCount(merchantRecharge.getCount());
        Integer rechargeBy = merchantRecharge.getRechargeBy();
        merchantRechargeVo.setRechargeBy(rechargeBy);
        Integer merchId = merchantRecharge.getMerchId();
        merchantRechargeVo.setMerchId(merchId);
        merchantRechargeVo.setRechargeMark(merchantRecharge.getRechargeMark());
        merchantRechargeVo.setRechargeType(merchantRecharge.getRechargeType());
        merchantRechargeVo.setRechargeTime(DateUtils.format(merchantRecharge.getRechargeTime(),DateUtils.yyyyMMdd_format));
        merchantRechargeVo.setVoucherName(merchantRecharge.getVoucherName());
        merchantRechargeVo.setVoucherUrl(merchantRecharge.getVoucherUrl());
        merchantRechargeVo.setStatus(merchantRecharge.getStatus());
        if(rechargeBy!=null){
            ManageAdmin byId = manageAdminDao.getById(rechargeBy);
            if(byId!=null){
                merchantRechargeVo.setRechargeByName(byId.getUserName());
            }
        }
        Merchantinfo merchantinfoByToid = merchantDao.findMerchantinfoByToid(merchId);
        if(merchantinfoByToid!=null){
            merchantRechargeVo.setMerchName(merchantinfoByToid.getMerchantName());
        }
    }

    /**
     * 充值记录插入
     * @param merchantRecharge
     * @return
     */
    @Transactional
    public BusinessVo<String> merchantRecharge(MerchantRecharge merchantRecharge) {
        BusinessVo<String> businessVo= new BusinessVo<>();
        Integer count = merchantRecharge.getCount();
        Integer merchId = merchantRecharge.getMerchId();
        Integer rechargeBy = merchantRecharge.getRechargeBy();
        Map<String, Integer> result = updateMerchantAccount(count, merchId, rechargeBy);
        if(result!=null&& new Integer(1).equals(result.get("state"))){
            logger.info("充值成功merchId:{}，count:{}", merchId, count);
            merchantRecharge.setStatus(MerchantRechargeRechargeStatusEnum.SUCCESS.getType());
            merchantRechargeDao.insertMerchantRecharge(merchantRecharge);
            //插入账户记录
            AccountRecord accountRecord = new AccountRecord();
            accountRecord.setOrderNo(merchantRecharge.getRechargeNo());
            accountRecord.setMerchId(merchId);
            accountRecord.setTransactCount(count);
            accountRecord.setTransactType(TransactTypeEnum.RECHARGE.getType());
            accountRecord.setResult(AccountRecordResultEnum.SUCCESS.getType());
            accountRecord.setUpdateBy(rechargeBy);
            accountRecord.setCreateBy(rechargeBy);
            Date rechargeTime = merchantRecharge.getRechargeTime();
            accountRecord.setUpdateTime(rechargeTime);
            accountRecord.setCreateTime(rechargeTime);
            accountRecordDao.insertAccountRescord(accountRecord);
            businessVo.setData("账户充值记录插入成功！");
            businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
            businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        }else {
            businessVo.setData("账户充值失败！");
            businessVo.setCode(BusinessConstantsUtils.MERCHANT_RECHARGE_FAILED_CODE);
            businessVo.setMessage(BusinessConstantsUtils.MERCHANT_RECHARGE_FAILED_DESC);
        }

        return businessVo;
    }
}
