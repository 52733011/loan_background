package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.OrderFlow;
import com.xiaochong.loan.background.entity.vo.OrderFlowBackVo;
import com.xiaochong.loan.background.mapper.OrderFlowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository("orderFlowDao")
public class OrderFlowDao {

    Logger logger = LoggerFactory.getLogger(OrderFlowDao.class);

    @Resource
    private OrderFlowMapper orderFlowMapper;

    public OrderFlow getByOrderFlow(OrderFlow orderFlow) {
        return orderFlowMapper.getByOrderFlow(orderFlow);
    }

    public List<OrderFlow> listByOrderFlow(OrderFlow orderFlow) {
        return orderFlowMapper.listByOrderFlow(orderFlow);
    }


    /**
     * 根据订单编号查询认证流项
     * @param orderNum 订单编号
     * @return 认证流程集合
     */
    public List<OrderFlow> getByOrderNo(String orderNum){
        return orderFlowMapper.selectFlowByOrderNum(orderNum);
    }

    public int insert(OrderFlow orderFlow) {
        if(orderFlow.getCreatetime()==null){
            orderFlow.setCreatetime(new Date());
        }
        return orderFlowMapper.insertSelective(orderFlow);
    }

    public int update(OrderFlow orderFlow) {
        if(orderFlow.getUpdatetime()==null){
            orderFlow.setUpdatetime(new Date());
        }
        return orderFlowMapper.updateByPrimaryKeySelective(orderFlow);
    }

    public List<OrderFlowBackVo> getOrderFlowBackVoList(Integer status, String searchStatus,
                                                  String condition, String startTime, String endTime) {
        return orderFlowMapper.getOrderFlowBackVoList(status, searchStatus, condition, startTime, endTime);
    }
}
