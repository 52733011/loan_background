package com.xiaochong.loan.background.service;

import com.xiaochong.loan.background.controller.manager.FlowOptionController;
import com.xiaochong.loan.background.dao.FlowOptionDao;
import com.xiaochong.loan.background.dao.MerchantFlowOptionDao;
import com.xiaochong.loan.background.entity.po.FlowOption;
import com.xiaochong.loan.background.entity.po.MerchantFlowOption;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.FlowOptionVo;
import com.xiaochong.loan.background.entity.vo.MerchantFlowOptionVo;
import com.xiaochong.loan.background.entity.vo.MerchantinfoFlowVo;
import com.xiaochong.loan.background.utils.BusinessConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinxin on 2017/8/12.
 */
@Service
public class FlowOptionService {

    private Logger logger = LoggerFactory.getLogger(FlowOptionService.class);

    @Autowired
    private FlowOptionDao flowOptionDao;

    @Autowired
    private  MerchantFlowOptionDao merchantFlowOptionDao;

    /**
     * 根据认证流程号查询认证项
     * @param flowNo
     * @return
     */
    public BusinessVo<List<FlowOptionVo>> selectByFlowNo(String flowNo) {
        BusinessVo<List<FlowOptionVo>> businessVo= new BusinessVo<>();
        FlowOption flowOption = new FlowOption();
        flowOption.setFlowNo(flowNo);
        List<FlowOption> flowOptions = flowOptionDao.selectFlowOption(flowOption);
        List<FlowOptionVo> flowOptionVoList = new ArrayList<>();
        flowOptionListConvert(flowOptionVoList,flowOptions);
        businessVo.setData(flowOptionVoList);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 认证流程项类转换
     * @param flowOptionVo
     * @param flowOption
     */
    private void  flowOptionConvert(FlowOptionVo flowOptionVo,FlowOption flowOption){
        flowOptionVo.setId(flowOption.getId());
        flowOptionVo.setFlowNo(flowOption.getFlowNo());
        flowOptionVo.setFlowOptionNo(flowOption.getFlowOptionNo());
        flowOptionVo.setOptionDesc(flowOption.getOptionDesc());
        flowOptionVo.setOptionName(flowOption.getOptionName());
    }


    /**
     *  认证流程项集合转换
     * @param flowOptionVoList
     * @param flowOptionList
     */
    private void flowOptionListConvert(List<FlowOptionVo> flowOptionVoList ,List<FlowOption> flowOptionList){
        flowOptionList.forEach(flowOption -> {
            FlowOptionVo flowOptionVo = new FlowOptionVo();
            flowOptionConvert(flowOptionVo,flowOption);
            flowOptionVoList.add(flowOptionVo);
        });

    }

    /**
     *
     * @param merchantFlowOption
     * @return
     */
    public BusinessVo<MerchantFlowOptionVo> selectByFlowNoAndMerchid(MerchantFlowOption merchantFlowOption) {
        BusinessVo<MerchantFlowOptionVo> businessVo= new BusinessVo<>();
        MerchantFlowOptionVo merchantFlowOptionVo = new MerchantFlowOptionVo();
        List<MerchantFlowOption> merchantFlowOptionList = merchantFlowOptionDao.selectMerchantFlowOption(merchantFlowOption);
        if(merchantFlowOptionList!=null &&merchantFlowOptionList.size()!=0){
            StringBuilder stringBuilder = new StringBuilder();
            merchantFlowOptionList.forEach(m->{
                stringBuilder.append(",").append(m.getFlowOptionNo());
            });
            merchantFlowOptionVo.setFlowOptionNo(stringBuilder.substring(1));
            merchantFlowOptionVo.setMerchId(merchantFlowOption.getMerchId());
            merchantFlowOptionVo.setFlowNo(merchantFlowOption.getFlowNo());
        }
        businessVo.setData(merchantFlowOptionVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    public BusinessVo<String> insertOrUpdateFlowOption(MerchantFlowOption merchantFlowOption, String flowOption) {
        BusinessVo<String> businessVo= new BusinessVo<>();
        int info= merchantFlowOptionDao.deleteByMerchIdAndFlowNo(merchantFlowOption);
        for (String s : flowOption.split(",")) {
             merchantFlowOption.setFlowOptionNo(s);
             merchantFlowOptionDao.insertMerchantFlowOption(merchantFlowOption);
        }
        if(info==0){
            businessVo.setData("认证流程项添加成功");
        }else {
            businessVo.setData("认证流程项更新成功");
        }
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }
}
