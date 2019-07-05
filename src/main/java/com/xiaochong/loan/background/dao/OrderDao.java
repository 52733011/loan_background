package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.Order;
import com.xiaochong.loan.background.entity.vo.OrderWebappVo;
import com.xiaochong.loan.background.mapper.OrderMapper;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository("orderDao")
public class OrderDao {

    @Resource
    private OrderMapper orderMapper;

    public int insert(Order order) {
        if(order.getCreatetime()==null){
            order.setCreatetime(new Date());
        }
        return orderMapper.insertSelective(order);
    }

    public int update(Order order) {
        if(order.getUpdatetime()==null){
            order.setUpdatetime(new Date());
        }
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    public Order selectOrderByToken(String orderToken){
       return orderMapper.selectOrderByToken(orderToken);
    }

    public List<OrderWebappVo> getOrderWebappVoList(String proxyUserId, Integer merchId, String[] status, String searchStatus,
                                                    String condition, Date startTime, Date endTime) {
        return orderMapper.getOrderWebappVoList(proxyUserId, merchId, status, searchStatus, condition, startTime, endTime);
    }

    public Order getByOrder(Order order) {
        return orderMapper.getByOrder(order);
    }

    public List<Order> listByOrder(Order order) {
        return orderMapper.listByOrder(order);
    }

    /**
     * 根据订单编号查询订单
     * @param orderNum 订单编号
     * @return 订单主体
     */
    public Order selectOrderByOrdeNum(String orderNum){
        return orderMapper.selectOrderByOrderNum(orderNum);
    }

    public Order selectOrderById(Integer orderId) {
        return orderMapper.selectByPrimaryKey(orderId);
    }
}
