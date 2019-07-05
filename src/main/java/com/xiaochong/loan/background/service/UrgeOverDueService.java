package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.UrgeOverDueStatusEnum;
import com.xiaochong.loan.background.utils.enums.UserLoginTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by jinxin on 2017/10/18.
 */
@Service
public class UrgeOverDueService extends BaseService {

    private Logger logger = LoggerFactory.getLogger(UrgeOverDueService.class);

    @Autowired
    private UrgeOverdueDao urgeOverdueDao;

    @Autowired
    private ProxyUserDao proxyUserDao;

    @Autowired
    private  SessionComponent sessionComponent;

    @Autowired
    private UrgeRecordDao urgeRecordDao;

    /**
     * 催收逾期人员查询
     * @param urgeOverdue
     * @param status
     *@param pageNum
     * @param pageSize
     * @param token    @return
     */
    public BusinessVo<UrgeOverDueSearchVo> selectByUrgeOverdue(UrgeOverdue urgeOverdue, String status, Integer pageNum, Integer pageSize, String token) {
        BusinessVo<UrgeOverDueSearchVo> businessVo = new BusinessVo<>();
        UrgeOverDueSearchVo urgeOverDueSearchVo = new UrgeOverDueSearchVo();
        ProxyUser proxyUserByToken = getProxyUserByToken(token);
        urgeOverdue.setMerchId(proxyUserByToken.getMerchId());
        List<Map<String, Object>> statusCountList = urgeOverdueDao.selectCountByUrgeOverdue(urgeOverdue);
        Integer allCount =0;
        urgeOverDueSearchVo.setOffTheStocks(0);
        urgeOverDueSearchVo.setUrgeedToday(0);
        urgeOverDueSearchVo.setWaitingUrge(0);
        if(CollectionUtils.isNotEmpty(statusCountList)){
            for (Map<String, Object> map : statusCountList) {
                String statusGet = (String) map.get("status");
                Integer count = Integer.valueOf(map.get("count").toString()) ;
                if(UrgeOverDueStatusEnum.URGEED_TODAY.getType().equals(statusGet)){
                    urgeOverDueSearchVo.setUrgeedToday(count);
                }else   if(UrgeOverDueStatusEnum.WAITING_URGE.getType().equals(statusGet)){
                    urgeOverDueSearchVo.setWaitingUrge(count);
                } else if(UrgeOverDueStatusEnum.OFF_THE_STOCKS.getType().equals(statusGet)){
                    urgeOverDueSearchVo.setOffTheStocks(count);
                }
                allCount=allCount+count;
            }
        }
        urgeOverDueSearchVo.setAllCount(allCount);
        urgeOverdue.setStatus(status);
        PageHelper.startPage(pageNum, pageSize, true);
        Page<UrgeOverdue> page = (Page<UrgeOverdue>) urgeOverdueDao.selectByUrgeOverDue(urgeOverdue);
        PageInfo<UrgeOverdue> loanMerchantinfoVoPageInfo = page.toPageInfo();
        BasePageInfoVo<UrgeOverdueVo> basePageInfoVo = assemblyBasePageInfo(loanMerchantinfoVoPageInfo);
        List<UrgeOverdueVo> urgeOverdueVoList = new ArrayList<>();
        List<UrgeOverdue> result = page.getResult();
        if(CollectionUtils.isNotEmpty(result)){
            urgeOverdueRecordListConvert(urgeOverdueVoList,result);
        }
        basePageInfoVo.setResultList(urgeOverdueVoList);
        urgeOverDueSearchVo.setBasePageInfoVo(basePageInfoVo);
        businessVo.setData(urgeOverDueSearchVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 催收逾期集合类转换
     * @param urgeOverdueVoList
     * @param result
     */
    private void urgeOverdueRecordListConvert(List<UrgeOverdueVo> urgeOverdueVoList, List<UrgeOverdue> result) {
        result.forEach(urgeOverdue -> {
            UrgeOverdueVo urgeOverdueVo = new UrgeOverdueVo();
            urgeOverdueConvert(urgeOverdueVo,urgeOverdue);
            urgeOverdueVoList.add(urgeOverdueVo);
        });
    }


    /**
     * 催收逾期类转换
     * @param urgeOverdueVo
     * @param urgeOverdue
     */
    private void urgeOverdueConvert(UrgeOverdueVo urgeOverdueVo, UrgeOverdue urgeOverdue) {
        urgeOverdueVo.setId(urgeOverdue.getId());
        urgeOverdueVo.setBorrowerId(urgeOverdue.getBorrowerId());
        urgeOverdueVo.setBorrowerName(urgeOverdue.getBorrowerName());
        urgeOverdueVo.setBorrowerPhone(urgeOverdue.getBorrowerPhone());
        Integer lastFollowId = urgeOverdue.getLastFollowId();
        if(lastFollowId!=null){
            ProxyUser byId = proxyUserDao.getById(lastFollowId);
            if(byId!=null){
                urgeOverdueVo.setLastFollowId(lastFollowId);
                urgeOverdueVo.setLastFollowName(byId.getUsername());
            }
        }
        urgeOverdueVo.setLastFollowTime(DateUtils.format(urgeOverdue.getLastFollowTime(),DateUtils.yyyyMMddHHmmss_hanziformat2));
        urgeOverdueVo.setMaxOverdueDays(urgeOverdue.getMaxOverdueDays());
        urgeOverdueVo.setOverdueAllMoney(urgeOverdue.getOverdueAllMoney());
        urgeOverdueVo.setOverdueNum(urgeOverdue.getOverdueNum());
        urgeOverdueVo.setStatus(urgeOverdue.getStatus());
        urgeOverdueVo.setApplicationId(urgeOverdue.getApplicationId());
    }

    /**
     * 根据token获取用户对象
     * @param token
     * @return
     */
    private ProxyUser getProxyUserByToken(String token){
        String idString = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        return proxyUserDao.getById(Integer.parseInt(idString));
    }

    /**
     * 分页查询逾期催记记录
     * @param urgeOverDueId
     * @param pageNum
     *@param pageSize @return
     */
    public BusinessVo<BasePageInfoVo<UrgeRecordVo>> selectUrgeRecord(Integer urgeOverDueId, Integer pageNum, Integer pageSize) {
        BusinessVo<BasePageInfoVo<UrgeRecordVo>> businessVo = new BusinessVo<>();
        PageHelper.startPage(pageNum, pageSize, true);
        Page<UrgeRecord> page = (Page<UrgeRecord>) urgeRecordDao.selectByUrgeOverDueId(urgeOverDueId);
        PageInfo<UrgeRecord> urgeRecordPageInfo = page.toPageInfo();
        BasePageInfoVo<UrgeRecordVo> basePageInfoVo = assemblyBasePageInfo(urgeRecordPageInfo);
        List<UrgeRecordVo> urgeRecordVoList = new ArrayList<>();
        List<UrgeRecord> result = page.getResult();
        if(!org.springframework.util.CollectionUtils.isEmpty(result)){
            urgeRecordListConvert(urgeRecordVoList,result);
        }
        basePageInfoVo.setResultList(urgeRecordVoList);
        businessVo.setData(basePageInfoVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 催记类集合转化
     * @param urgeRecordVoList
     * @param result
     */
    private void urgeRecordListConvert(List<UrgeRecordVo> urgeRecordVoList, List<UrgeRecord> result) {
        result.forEach(urgeRecord -> {
            UrgeRecordVo urgeRecordVo = new UrgeRecordVo();
            urgeRecordConvert(urgeRecordVo,urgeRecord);
            urgeRecordVoList.add(urgeRecordVo);
        });
    }
    /**
     * 催记类转化
     * @param urgeRecordVo
     * @param urgeRecord
     */
    private void urgeRecordConvert(UrgeRecordVo urgeRecordVo, UrgeRecord urgeRecord) {
        urgeRecordVo.setId(urgeRecord.getId());
        urgeRecordVo.setStageNo(urgeRecord.getStageNo());
        urgeRecordVo.setUrgeContent(urgeRecord.getUrgeContent());
        urgeRecordVo.setUrgeOverDueId(urgeRecord.getUrgeOverDueId());
        urgeRecordVo.setCreateTime(DateUtils.format(urgeRecord.getCreateTime(),DateUtils.yyyyMMddHHmmss_hanziformat2));
        Integer createUser = urgeRecord.getCreateUser();
        urgeRecordVo.setCreateUser(createUser);
        if(createUser!=null){
            ProxyUser byId = proxyUserDao.getById(createUser);
            urgeRecordVo.setCreateUserName(byId.getUsername());
            urgeRecordVo.setCreateUserPhone(byId.getPhone());
        }
    }

    /**
     * 添加催记
     * @param token
     * @param urgeOverDueId
     * @param urgeRecordContent
     * @return
     */
    public BusinessVo<String> addUrgeRecord(String token, Integer urgeOverDueId, String urgeRecordContent) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        if(urgeRecordContent.length()>3000){
            businessVo.setCode(BusinessConstantsUtils.DATA_TOO_LARGE_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.DATA_TOO_LARGE_ERROR_DESC);
            return businessVo;
        }
        ProxyUser proxyUserByToken = getProxyUserByToken(token);
        Integer id = proxyUserByToken.getId();
        Date now = new Date();
        UrgeRecord urgeRecord = new UrgeRecord();
        urgeRecord.setUrgeOverDueId(urgeOverDueId);
        urgeRecord.setUrgeContent(urgeRecordContent);
        urgeRecord.setCreateUser(id);
        urgeRecord.setCreateTime(now);
        int count= urgeRecordDao.selectCountByUrgeOverDueId(urgeOverDueId);
        urgeRecord.setStageNo(++count);
        urgeRecordDao.insertUrgeRecord(urgeRecord);
        UrgeOverdue urgeOverdue = new UrgeOverdue();
        urgeOverdue.setId(urgeOverDueId);
        urgeOverdue.setStatus(UrgeOverDueStatusEnum.URGEED_TODAY.getType());
        urgeOverdue.setLastFollowId(id);
        urgeOverdue.setLastFollowTime(now);
        urgeOverdue.setUpdateTime(now);
        urgeOverdue.setUpdateUser(id);
        urgeOverdueDao.update(urgeOverdue);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }
}
