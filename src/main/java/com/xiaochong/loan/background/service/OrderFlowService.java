package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.AccountRecord;
import com.xiaochong.loan.background.entity.po.Order;
import com.xiaochong.loan.background.entity.po.OrderFlow;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.OrderFlowBackVo;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.BusinessConstantsUtils;
import com.xiaochong.loan.background.utils.enums.OrderFlowStatusEnum;
import com.xiaochong.loan.background.utils.enums.OrderStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("orderFlowService")
public class OrderFlowService extends BaseService{

	private Logger logger = LoggerFactory.getLogger(OrderFlowService.class);

	@Resource(name = "orderFlowDao")
	private OrderFlowDao orderFlowDao;

	@Resource(name = "orderService")
	private OrderService orderService;

	@Resource(name = "orderDao")
	private OrderDao orderDao;

	@Autowired
	private AccountRecordDao accountRecordDao;

	public BusinessVo<BasePageInfoVo<OrderFlowBackVo>> getOrderFlowList(Integer pageNum, Integer pageSize,
																		Integer status, String searchStatus,
																		String condition,
																		String startTime, String endTime) {
		BusinessVo<BasePageInfoVo<OrderFlowBackVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);

		PageHelper.startPage(pageNum, pageSize,true);
		Page<OrderFlowBackVo> orderFlowList =
				(Page<OrderFlowBackVo>)orderFlowDao.getOrderFlowBackVoList(status, searchStatus, condition, startTime, endTime);
		List<OrderFlowBackVo> result = orderFlowList.getResult();
		result.forEach(r->{
		});
		//拼装成返回类
		PageInfo<OrderFlowBackVo> orderFlowBackVoPageInfo = orderFlowList.toPageInfo();
		//拼装分页类
		BasePageInfoVo<OrderFlowBackVo> basePageInfoVo = assemblyBasePageInfo(orderFlowBackVoPageInfo);
		basePageInfoVo.setResultList(result);
		businessVo.setData(basePageInfoVo);
		return businessVo;
	}

	/**
	 * 订单认证流程定时任务
	 */
	@Transactional
	public void orderFlowAuthScheduled() {
		Order searchOrder = new Order();
		searchOrder.setStatus(OrderStatusEnum.AUTH.getType());
		List<Order> orderList = orderDao.listByOrder(searchOrder);
		for (Order order : orderList) {
			if(!OrderStatusEnum.AUTH.getType().equals(order.getStatus())){
				continue;
			}
			OrderFlow searchOrderFlow = new OrderFlow();
			searchOrderFlow.setOrderNo(order.getOrderNo());
			List<OrderFlow> orderFlows = orderFlowDao.listByOrderFlow(searchOrderFlow);
			if (orderFlows == null || orderFlows.size() == 0) {
				continue;
			}
			boolean authFlag = true;
			for (OrderFlow orderFlow : orderFlows) {
				if (!OrderFlowStatusEnum.SUCCESS.getType().equals(orderFlow.getStatus())) {
					authFlag = false;
					break;
				}
			}
			if (authFlag) {
				logger.info("订单：orderNO:{},realname:{};认证流程完成，进入报告生成", order.getOrderNo(), order.getRealname());
				if (orderService.submitOrderForScheduled(order)) {
					order.setStatus(OrderStatusEnum.REPORTING.getType());
					AccountRecord accountRecord = new AccountRecord();
					accountRecord.setOrderNo(order.getOrderNo());
					accountRecord.setOrderStatus(OrderStatusEnum.REPORTING.getType());
					accountRecordDao.updateAccountRecordByOrderNo(accountRecord);
					orderDao.update(order);
				}
			}
		}

	}
}
