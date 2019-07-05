package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.AccountRecordDao;
import com.xiaochong.loan.background.dao.MerchantAccountDao;
import com.xiaochong.loan.background.dao.ProxyUserDao;
import com.xiaochong.loan.background.entity.po.AccountRecord;
import com.xiaochong.loan.background.entity.po.MerchantAccount;
import com.xiaochong.loan.background.entity.po.ProxyUser;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.AccountRecordResultEnum;
import com.xiaochong.loan.background.utils.enums.ProxyUserTypeEnum;
import com.xiaochong.loan.background.utils.enums.TransactTypeEnum;
import com.xiaochong.loan.background.utils.enums.UserLoginTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinxin on 2017/8/15.
 */
@Service
public class AccountRecordService extends BaseService{

    private Logger logger = LoggerFactory.getLogger(AccountRecordService.class);

    @Autowired
    private AccountRecordDao accountRecordDao;

    @Autowired
    private ProxyUserDao proxyUserDao;

    @Autowired
    private SessionComponent sessionComponent;

    @Autowired
    private MerchantAccountDao merchantAccountDao;

    /**
     * 分页查询
     * @param accountRecord
     * @param token
     * @param pageNum
     * @param pageSize
     * @return
     */
    public BusinessVo<BasePageInfoVo<AccountRecordVo>> selectAccountRecord(AccountRecord accountRecord, String token, Integer pageNum, Integer pageSize) {
        BusinessVo<BasePageInfoVo<AccountRecordVo>> businessVo = new BusinessVo<>();
        Integer userId = getUserId(token);
        if(userId!=null){
            ProxyUser byId = proxyUserDao.getById(userId);
            if(byId!=null){
                if(! ProxyUserTypeEnum.MASTER.getType().equals(byId.getIsMaster())){
                    accountRecord.setCreateBy(byId.getId());
                }
                accountRecord.setMerchId(byId.getMerchId());
            }
        }else{
            businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
            businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
            return businessVo;
        }
        PageHelper.startPage(pageNum, pageSize, true);
        Page<AccountRecord> page = (Page<AccountRecord>) accountRecordDao.selectAccountRecord(accountRecord);
        PageInfo<AccountRecord> fieldPageInfo = page.toPageInfo();
        BasePageInfoVo<AccountRecordVo> basePageInfoVo = assemblyBasePageInfo(fieldPageInfo);
        List<AccountRecordVo> accountRecordVoArrayList = new ArrayList<>();
        List<AccountRecord> result = page.getResult();
        if(result!=null&&result.size()!=0){
            accountRecordListConvert(accountRecordVoArrayList,result);
        }
        basePageInfoVo.setResultList(accountRecordVoArrayList);
        businessVo.setData(basePageInfoVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }


    /**
     * 集合类转换
     * @param accountRecordVoArrayList
     * @param result
     */
    private void accountRecordListConvert(List<AccountRecordVo> accountRecordVoArrayList, List<AccountRecord> result) {
        result.forEach(accountRecord -> {
            AccountRecordVo accountRecordVo = new AccountRecordVo();
            accoundRecordConvert(accountRecordVo,accountRecord);
            accountRecordVoArrayList.add(accountRecordVo);
        });
    }

    /**
     * 类转换
     * @param accountRecordVo
     * @param accountRecord
     */
    private void accoundRecordConvert(AccountRecordVo accountRecordVo,AccountRecord accountRecord){
        accountRecordVo.setId(accountRecord.getId());
        accountRecordVo.setMerchId(accountRecord.getMerchId());
        accountRecordVo.setOrderNo(accountRecord.getOrderNo());
        accountRecordVo.setOrderStatus(accountRecord.getOrderStatus());
        Integer createBy = accountRecord.getCreateBy();
        accountRecordVo.setTransactType(accountRecord.getTransactType());
        if(!TransactTypeEnum.RECHARGE.getType().equals(accountRecord.getTransactType())){
            accountRecordVo.setCreateBy(createBy);
            ProxyUser byId = proxyUserDao.getById(createBy);
            if(byId!=null){
                accountRecordVo.setCreateByName(byId.getUsername());
            }
        }
        accountRecordVo.setCreateTime(DateUtils.format(accountRecord.getCreateTime(),DateUtils.ymdhms_format));
        accountRecordVo.setTransactCount(accountRecord.getTransactCount());
    }

    /**
     * 添加账户信息
     * @param accountRecord
     * @return
     */
    public BusinessVo<String> insertAccountRecord(AccountRecord accountRecord) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        accountRecordDao.insertAccountRescord(accountRecord);
        businessVo.setData("账户记录添加成功");
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 根据token获取账户信息
     * @param token
     * @return
     */
    public BusinessVo<MerchantAccountVo> getAccountInfo(String token) {
        BusinessVo<MerchantAccountVo> businessVo = new BusinessVo<>();
        MerchantAccountVo merchantAccountVo = new MerchantAccountVo();
        Integer userId = getUserId(token);
        ProxyUser byId = proxyUserDao.getById(userId);
        Integer merchId = byId.getMerchId();
        MerchantAccount merchantAccount = merchantAccountDao.selectMerchantAccountByMerchId(merchId);
        if(merchantAccount!=null){
            merchantAccountVo.setMerchId(merchId);
            merchantAccountVo.setSurplusCount(merchantAccount.getSurplusCount());
            merchantAccountVo.setAccountStatus(byId.getStatus());
        }
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setMerchId(merchId);
        accountRecord.setResult(AccountRecordResultEnum.LOCK.getType());
        int lockCount=accountRecordDao.getCountForAccountRecord(accountRecord);
        merchantAccountVo.setLockCount(lockCount);
        businessVo.setData(merchantAccountVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 根据token获取userId
     * @param token
     * @return
     */
    private Integer getUserId(String token){
        StringBuilder stringBuilder = new StringBuilder(token);
        stringBuilder.append("-").append(UserLoginTypeEnum.WEBAPP.getType());
        String attribute = sessionComponent.getAttribute(stringBuilder.toString());
        return attribute==null?null:Integer.valueOf(attribute);
    }
}
