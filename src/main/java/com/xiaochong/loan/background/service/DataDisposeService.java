package com.xiaochong.loan.background.service;

import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.mapper.*;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.CollectionUtil;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.enums.OrderStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by wujiaxing on 2017/9/1.
 * 数据修复
 */
@Service("dataDisposeService")
public class DataDisposeService extends BaseService{

	private Logger logger = LoggerFactory.getLogger(DataDisposeService.class);

	@Resource
	private MerchantinfoCopyMapper merchantinfoCopyMapper;

	@Resource
	private ProxyuserCopyMapper proxyuserCopyMapper;

	@Resource
	private OrderCopyMapper orderCopyMapper;

	@Resource
	private UserMapper userMapper;

	@Resource
	private UserCopyMapper userCopyMapper;

	@Resource
	private ProxyUserMapper proxyUserMapper;

	@Resource
	private OrderMapper orderMapper;

	@Resource
	private CheckflowMapper checkflowMapper;

	@Autowired
	private MerchantMapper merchantMapper;

	@Autowired
	private MerchantinfoFlowMapper merchantinfoFlowMapper;

	@Transactional
	public void oldOrderDataDispose() {
		List<MerchantinfoCopy> merchantinfoCopies = merchantinfoCopyMapper.listByMerchantinfoCopy(new MerchantinfoCopy());
		for (MerchantinfoCopy merchantinfoCopy:merchantinfoCopies) {
			ProxyuserCopy searchProxyuserCopy = new ProxyuserCopy();
			searchProxyuserCopy.setMerid(merchantinfoCopy.getToid());
			//searchProxyuserCopy.setIsmaster("1");
			List<ProxyuserCopy> proxyuserCopys = proxyuserCopyMapper.listByProxyuserCopy(searchProxyuserCopy);
			for (ProxyuserCopy proxyuserCopy:proxyuserCopys) {
				OrderCopy searchOrderCopy = new OrderCopy();
				searchOrderCopy.setProxyUser(proxyuserCopy.getToid());
				List<OrderCopy> orderCopies = orderCopyMapper.listByOrderCopy(searchOrderCopy);
				for (OrderCopy orderCopy:orderCopies) {
					Order order = new Order();
					Merchantinfo merchantinfo = new Merchantinfo();
					if("方浩".equals(merchantinfoCopy.getLinkMan())){
						merchantinfo.setLinkMan(merchantinfoCopy.getLinkMan());
					}else{
						merchantinfo.setMerchantName(merchantinfoCopy.getMerchantName());
					}
					List<Merchantinfo> merchantinfos = merchantMapper.selectMerchantinfo(merchantinfo);
					if(CollectionUtil.isBlank(merchantinfos)){
						continue;
					}
					order.setMerId(merchantinfos.get(0).getId());
					order.setRealname(orderCopy.getRealname());
					order.setIdCard(orderCopy.getIdCard());
					order.setPhone(orderCopy.getPhone());
					if("0".equals(orderCopy.getStatus())){
						order.setStatus(orderCopy.getStatus());
					}else if("1".equals(orderCopy.getStatus())){
						order.setStatus(OrderStatusEnum.FINISH.getType());
					}else if("2".equals(orderCopy.getStatus())){
						order.setStatus(OrderStatusEnum.PASTDUE.getType());
					}
					order.setOrderNo(orderCopy.getOrderNo());
					order.setToken(orderCopy.getToken());
					order.setCreatetime(DateUtils.stringToDate(
							orderCopy.getCreatetime(),DateUtils.ymdhms_format));
					order.setUpdatetime(DateUtils.stringToDate(
							orderCopy.getUpdatetime(),DateUtils.ymdhms_format));
					UserCopy userCopy = userCopyMapper.selectByPrimaryKey(orderCopy.getUserid());
					User user = new User();
					user.setRealname(userCopy.getRealname());
					List<User> users = userMapper.listByUser(user);
					if(CollectionUtil.isNotBlank(users)){
						order.setUserId(users.get(0).getId());
					}
					ProxyUser proxyUser = new ProxyUser();
					proxyUser.setUsername(proxyuserCopy.getUsername());
					List<ProxyUser> proxyUsers = proxyUserMapper.listByProxyUser(proxyUser);
					if(CollectionUtil.isNotBlank(proxyUsers)){
						order.setProxyUser(proxyUsers.get(0).getId());
					}else{
						proxyUser = new ProxyUser();
						proxyUser.setMerchId(merchantinfo.getId());
						proxyUser.setIsMaster("1");
						proxyUsers = proxyUserMapper.listByProxyUser(proxyUser);
						if(CollectionUtil.isNotBlank(proxyUsers)){
							order.setProxyUser(proxyUsers.get(0).getId());
						}
					}
					order.setRiskStatus(orderCopy.getRiskstatus());
					order.setCheckStatus(orderCopy.getCheckstatus());
					order.setChecktime(DateUtils.stringToDate(
							orderCopy.getChecktime(),DateUtils.ymdhms_format));
					order.setRiskCheckResult(orderCopy.getRiskCheckResult());
					order.setIsOld("1");
					orderMapper.insertSelective(order);
				}
			}
		}
	}

	@Transactional
    public void merchantFlowFlash() {
		List<MerchantinfoFlow> merchantinfoFlows =
				merchantinfoFlowMapper.listByMerchantinfoFlow(new MerchantinfoFlow());
		List<Checkflow> checkflows = checkflowMapper.listByCheckflow(new Checkflow());
		for (MerchantinfoFlow merchantinfoFlow:merchantinfoFlows) {
			for (Checkflow checkflow:checkflows) {
				if( StringUtils.isNotBlank(merchantinfoFlow.getFlowNo()) &&
						merchantinfoFlow.getFlowNo().equals(checkflow.getFlowNo()) ){
					merchantinfoFlow.setFlowStep(checkflow.getStep());
					logger.info("update success merchantinfoFlow:{}",merchantinfoFlow);
					break;
				}
			}
		}
	}
}
