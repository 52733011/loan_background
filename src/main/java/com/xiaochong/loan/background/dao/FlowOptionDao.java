package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.FlowOption;
import com.xiaochong.loan.background.mapper.FlowOptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jinxin on 2017/8/11.
 */
@Repository("flowOptionDao")
public class FlowOptionDao {

    @Autowired
    private FlowOptionMapper flowOptionMapper;

    public List<FlowOption> selectFlowOption(FlowOption flowOption){
        return flowOptionMapper.selectFlowOption(flowOption);
    }


    public FlowOption selectByFlowOptionNo(String flowOptionNo){
        return flowOptionMapper.selectOneByFlowOptionNo(flowOptionNo);
    }

    public List<FlowOption> selectByFlowOptionNo(FlowOption flowOption){
        return flowOptionMapper.selectFlowOption(flowOption);
    }


    public List<FlowOption> selectByFlowOptionNoList(List<String> flowOptionNoList){
        return flowOptionMapper.selectByFlowOptionNoList(flowOptionNoList);
    }

    public FlowOption selectOneFlowOption(FlowOption flowOption){
        return flowOptionMapper.selectOneFlowOption(flowOption);
    }

}
