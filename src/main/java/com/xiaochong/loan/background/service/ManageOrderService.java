package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.dao.MerchantDao;
import com.xiaochong.loan.background.dao.OrderDao;
import com.xiaochong.loan.background.entity.po.Merchantinfo;
import com.xiaochong.loan.background.entity.po.Order;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.OrderManageVo;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.BusinessConstantsUtils;
import com.xiaochong.loan.background.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinxin on 2017/8/24.
 */
@Service
public class ManageOrderService extends BaseService {

    @Autowired
    private OrderDao orderDao;


    @Autowired
    private MerchantDao merchantDao;

    /**
     * 后台订单列表查询
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    public BusinessVo<BasePageInfoVo<OrderManageVo>> orderListSearch(String orderNo, Integer pageNum, Integer pageSize) {
        BusinessVo<BasePageInfoVo<OrderManageVo>> businessVo = new BusinessVo<>();
        PageHelper.startPage(pageNum, pageSize, true);
        Order order = new Order();
        order.setOrderNo(orderNo);
        Page<Order> page = (Page<Order>) orderDao.listByOrder(order);
        PageInfo<Order> loanMerchantinfoVoPageInfo = page.toPageInfo();
        BasePageInfoVo<OrderManageVo> basePageInfoVo = assemblyBasePageInfo(loanMerchantinfoVoPageInfo);
        List<OrderManageVo> orderManageVoList = new ArrayList<>();
        List<Order> result = page.getResult();
        if(result!=null&&result.size()!=0){
            orderListConvert(orderManageVoList,result);
        }
        basePageInfoVo.setResultList(orderManageVoList);
        businessVo.setData(basePageInfoVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * 订单集合类转换
     * @param orderManageVoList
     * @param result
     */
    private void orderListConvert(List<OrderManageVo> orderManageVoList, List<Order> result) {
        result.forEach(order -> {
            OrderManageVo orderManageVo = new OrderManageVo();
            orderConvert(orderManageVo,order);
            orderManageVoList.add(orderManageVo);
        });
    }

    /**
     * 订单类转换
     * @param orderManageVo
     * @param order
     */
    private void orderConvert(OrderManageVo orderManageVo, Order order) {
        Integer merId = order.getMerId();
        Merchantinfo merchantinfoByToid = merchantDao.findMerchantinfoByToid(merId);
        orderManageVo.setMerchId(merId);
        if(merchantinfoByToid!=null){
            orderManageVo.setMerchName(merchantinfoByToid.getMerchantName());
        }
        orderManageVo.setOrderNo(order.getOrderNo());
        orderManageVo.setCreatetime(DateUtils.format(order.getCreatetime(),DateUtils.yyyyMMdd_format));
        orderManageVo.setRealname(order.getRealname());
        orderManageVo.setPhone(order.getPhone());
        orderManageVo.setStatus(order.getStatus());
        orderManageVo.setIsOld(order.getIsOld());
    }
}
