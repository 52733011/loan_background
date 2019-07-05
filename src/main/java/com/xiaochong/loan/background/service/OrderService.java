package com.xiaochong.loan.background.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.IncrementComponent;
import com.xiaochong.loan.background.component.RiskDataComponent;
import com.xiaochong.loan.background.component.SMSComponent;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.exception.OKhttpException;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;


@Service("orderService")
public class OrderService extends BaseService {

    private Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Resource(name = "sessionComponent")
    private SessionComponent sessionComponent;

    @Resource(name = "smsComponent")
    private SMSComponent smsComponent;

    @Resource(name = "incrementComponent")
    private IncrementComponent incrementComponent;

    @Resource(name = "userDao")
    private UserDao userDao;

    @Resource(name = "proxyUserDao")
    private ProxyUserDao proxyUserDao;

    @Resource(name = "checkflowDao")
    private CheckflowDao checkflowDao;

    @Autowired
    private AccountRecordDao accountRecordDao;

    @Resource(name = "orderDao")
    private OrderDao orderDao;

    @Resource(name = "orderFlowDao")
    private OrderFlowDao orderFlowDao;

    @Resource(name = "authLogDao")
    private AuthLogDao authLogDao;

    @Resource(name = "merchantinfoFlowDao")
    private MerchantinfoFlowDao merchantinfoFlowDao;

    @Resource(name = "orderUserInfoDao")
    private OrderUserInfoDao orderUserInfoDao;

    @Resource(name = "riskDataComponent")
    private RiskDataComponent riskDataComponent;

    @Autowired
    private MerchantAccountService merchantAccountService;

    @Value("${bd.callbackhost}")
    private String bdCallbackhost;

    @Value("${risk.order.callback.url}")
    private String riskOrderCallbackUrl;


    /**
     * 发起背调
     *
     * @param token token
     * @param name  name
     * @param card  card
     * @param phone phone
     * @return BusinessVo<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessVo<String> startBd(String token, String name, String card, String phone) throws DataDisposeException, OKhttpException {
        BusinessVo<String> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);

		//取消三要素认证 TODO
		/*if(!riskDataComponent.validateMobile(name, card, phone)){
			businessVo.setCode(BusinessConstantsUtils.VALIDATE_ERROR_CODE);
			businessVo.setMessage(BusinessConstantsUtils.VALIDATE_ERROR_DESC);
			return businessVo;
		}*/
        String idString = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        ProxyUser proxyUser = proxyUserDao.getById(Integer.parseInt(idString));
        if (proxyUser == null) {
            businessVo.setCode(BusinessConstantsUtils.USERNAME_ERROR_DESC);
            businessVo.setMessage(BusinessConstantsUtils.USERNAME_ERROR_CODE);
            return businessVo;
        }
        Integer merchId = proxyUser.getMerchId();
        Integer proxyUserId = proxyUser.getId();
        //扣除次数
        Map<String, Integer> stringIntegerMap = merchantAccountService.updateMerchantAccount(-1, merchId, proxyUserId);
        if (stringIntegerMap != null && new Integer(1).equals(stringIntegerMap.get("state"))) {
            logger.info("账户次数扣除成功,merchId{}", merchId);
        } else {
            logger.info("账户次数扣除失败，merchId{}", merchId);
            businessVo.setCode(BusinessConstantsUtils.MERCHANT_ACCOUNT_ERROR_DESC);
            businessVo.setMessage(BusinessConstantsUtils.MERCHANT_ACCOUNT_ERROR_CODE);
            return businessVo;
        }

        card = card.replace('x','X');
        User user = new User();
        user.setIdCard(card);
        user.setRealname(name);
        user.setPhone(phone);
        //根据身份证号判断性别
        String sexByIdCard = IdCardUtil.getSexByIdCard(card);
        if ("man".equals(sexByIdCard)) {
            user.setSex(SexTypeEnum.MAN.getType());
        } else if ("woman".equals(sexByIdCard)) {
            user.setSex(SexTypeEnum.WOMAN.getType());
        }

        user = userDao.insertOrUpdateUser(user);
        if (user == null) {
            logger.error("更新被调人信息失败！");
            throw new DataDisposeException("更新被调人信息失败！");
        }

        // 上传借贷人信息到risk
        logger.info(riskDataComponent.subUserInfo(card, name, phone).toString());


        Order loanOrder = new Order();
        String orderToken = UUID.randomUUID().toString();
        loanOrder.setToken(orderToken);
        loanOrder.setRealname(name);
        loanOrder.setIdCard(card);
        loanOrder.setPhone(phone);
        String randomString = "" + new Random().nextInt(9) +
                new Random().nextInt(9) +
                new Random().nextInt(9);
        String orderNo = DateUtils.getCurrentDateyymd() + randomString + incrementComponent.getCountNo(CountTypeEnum.ORDER);
        loanOrder.setOrderNo(orderNo);
        loanOrder.setUserId(user.getId());

        loanOrder.setProxyUser(proxyUserId);

        loanOrder.setMerId(merchId);
        Date nowtime = new Date();
        loanOrder.setCreatetime(nowtime);
        loanOrder.setStatus(OrderStatusEnum.UNAUTH.getType());
        orderDao.insert(loanOrder);
        //插入账户记录
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setMerchId(merchId);
        accountRecord.setResult(AccountRecordResultEnum.LOCK.getType());
        accountRecord.setCreateTime(nowtime);
        accountRecord.setUpdateTime(nowtime);
        accountRecord.setCreateBy(proxyUserId);
        accountRecord.setUpdateBy(proxyUserId);
        accountRecord.setOrderNo(orderNo);
        accountRecord.setOrderStatus(loanOrder.getStatus());
        accountRecord.setTransactType(TransactTypeEnum.LOCK.getType());
        accountRecord.setTransactCount(1);
        accountRecordDao.insertAccountRescord(accountRecord);

        // 商户id
        Integer merid = proxyUser.getMerchId();
        List<MerchantinfoFlow> merchantinfoFlows = merchantinfoFlowDao.getByMerchId(merid);
        if (null == merchantinfoFlows || merchantinfoFlows.size() == 0) {
            businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
            businessVo.setMessage("请联系管理员配置认证流程！");
            return businessVo;
        }
        for (MerchantinfoFlow merchantinfoFlow : merchantinfoFlows) {
            if (new Integer(5).equals(merchantinfoFlow.getFlowStep())) {
                continue;
            }
            Checkflow checkflow = new Checkflow();
            checkflow.setFlowNo(merchantinfoFlow.getFlowNo());
            checkflow = checkflowDao.getByCheckflow(checkflow);
            if(checkflow==null || CheckflowTypeEnum.USERINFO.getType().equals(checkflow.getType())){
                continue;
            }
            OrderFlow orderFlow = new OrderFlow();
            orderFlow.setOrderNo(loanOrder.getOrderNo());
            orderFlow.setOrderToken(orderToken);
            orderFlow.setMerchId(proxyUser.getMerchId());
            orderFlow.setFlowNo(merchantinfoFlow.getFlowNo());
            orderFlow.setFlowStep(merchantinfoFlow.getFlowStep());
            if(CheckflowTypeEnum.OPERATOR_REPORT.getType().equals(checkflow.getType())){

                orderFlow.setStatus(OrderFlowStatusEnum.AUTH.getType());
            }else{

                orderFlow.setStatus(OrderFlowStatusEnum.UNAUTH.getType());
            }
            orderFlow.setCreatetime(nowtime);
            if (orderFlowDao.insert(orderFlow) != 1) {
                logger.error("插入订单认证数据失败！");
                throw new DataDisposeException("插入订单认证数据失败！");
            }
        }

        OrderUserInfo orderUserInfo = new OrderUserInfo();
        orderUserInfo.setSex(user.getSex());
        orderUserInfo.setOrderNo(loanOrder.getOrderNo());
        orderUserInfo.setOrderToken(loanOrder.getToken());
        orderUserInfo.setMerchId(loanOrder.getMerId());
        if (orderUserInfoDao.insert(orderUserInfo) != 1) {
            logger.error("插入订单用户信息失败！");
            throw new DataDisposeException("插入订单用户信息失败！");
        }

        // 生成url
        String auth_url = bdCallbackhost + "h5/authorization.html"
                + "?ordertoken=" + loanOrder.getToken() + "&mid=" + merid
                + "&stp=0";

        // 发送短信
        try {
            logger.info("发送短信【" + auth_url + "】" + phone);
            smsComponent.sendSms("请点击背调地址(有效期2小时)：" + auth_url + "", phone);
        } catch (DataDisposeException | OKhttpException e) {
            logger.error("发送背调地址报错", e);
        }
        return businessVo;
    }

    /**
     * 背调订单列表
     *
     * @param proxyUserId  proxyUserId
     * @param condition    查询参数
     * @param searchStatus 查询项:realname、phone、idCard
     * @param startTime    startTime
     * @param endTime      endTime
     * @param pageNum      pageNum
     * @param pageSize     pageSize
     * @return OrderWebappVo
     */
    public BusinessVo<BasePageInfoVo<OrderWebappVo>> getOrderPage(String proxyUserId,String status, String condition, String searchStatus, String startTime, String endTime, Integer pageNum, Integer pageSize) {
        BusinessVo<BasePageInfoVo<OrderWebappVo>> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        String[] statusArray = null;
        if(StringUtils.isNotBlank(status)){
            statusArray = status.split(",");
        }
        if(StringUtils.isNotBlank(endTime)){
            endTime+=" 23:59:59";
        }
        ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
                proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
        if(proxyUser==null){
            businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
            businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
            logger.warn("getOrderPage登录失效！");
            return businessVo;
        }
        Integer merchId = null;
        if(ProxyUserTypeEnum.MASTER.getType().equals(proxyUser.getIsMaster())){
            proxyUserId=null;
            merchId = proxyUser.getMerchId();
        }
        PageHelper.startPage(pageNum, pageSize,true);
        Page<OrderWebappVo> publicTransferList =
                (Page<OrderWebappVo>)orderDao.getOrderWebappVoList(proxyUserId, merchId, statusArray,searchStatus, condition,
                        TypeUtils.castToDate(startTime), TypeUtils.castToDate(endTime));

        List<OrderWebappVo> result = publicTransferList.getResult();
        for (OrderWebappVo orderWebappVo:result) {
            if(StringUtils.isNotBlank(orderWebappVo.getSex())){
                orderWebappVo.setSex(SexTypeEnum.getName(orderWebappVo.getSex()));
            }
        }
        //拼装成返回类
        PageInfo<OrderWebappVo> orderListBoPageInfo = publicTransferList.toPageInfo();
        //拼装分页类
        BasePageInfoVo<OrderWebappVo> basePageInfoVo = assemblyBasePageInfo(orderListBoPageInfo);
        basePageInfoVo.setResultList(orderListBoPageInfo.getList());
        businessVo.setData(basePageInfoVo);
        return businessVo;
    }


    public BusinessVo<List<Map<String, Object>>> getOrderStatusNum(String proxyUserId) {
        BusinessVo<List<Map<String, Object>>> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);

        Order order = new Order();
        order.setProxyUser(Integer.valueOf(proxyUserId));
        OrderStatusEnum[] orderStatusEnums = OrderStatusEnum.values();
        List<Map<String, Object>> returnList = new ArrayList<>();
        int allNum = 0;
        for (OrderStatusEnum orderStatusEnum : orderStatusEnums) {
            Map<String, Object> returnMap = new HashMap<>();
            order.setStatus(orderStatusEnum.getType());
            List<Order> orderList = orderDao.listByOrder(order);
            allNum += orderList.size();
            returnMap.put("num", CollectionUtils.isEmpty(orderList) ? 0 : orderList.size());
            returnMap.put("name", orderStatusEnum.getName());
            returnMap.put("status", orderStatusEnum.getType());
            returnList.add(returnMap);
        }
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("num", allNum);
        returnMap.put("name", "全部");
        returnMap.put("status", "");
        returnList.add(returnMap);
        businessVo.setData(returnList);
        return businessVo;
    }



    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public boolean submitOrderForScheduled(Order order) {
        String id_number = order.getIdCard();
        String account_name = order.getRealname();
        String account_mobile = order.getPhone();
        AuthLog searchAuthLog = new AuthLog();
        searchAuthLog.setIdCard(id_number);
        searchAuthLog.setOrderNum(order.getOrderNo());
        List<AuthLog> authLogs = authLogDao
                .listByAuthLog(searchAuthLog);
        boolean checkflag = true;
        for (AuthLog authLog : authLogs) {
            String authresult = authLog.getResult();
            if (!ConstansUtils.SUCCESS.equals(authresult)) {
                checkflag = false;
            }
        }

        if (checkflag) {
            OrderUserInfo orderUserInfo = new OrderUserInfo();
            orderUserInfo.setOrderNo(order.getOrderNo());
            orderUserInfo = orderUserInfoDao.getByOrderUserInfo(orderUserInfo);
            // 所有认证都通过
            String client_ip = orderUserInfo.getClientIp();

            riskDataComponent.tondunLoanEvent(id_number, account_name,
                    account_mobile, client_ip);

            String callback_url = riskOrderCallbackUrl;

            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderNo(order.getOrderNo());
            orderInfo.setCreatedAt(DateUtils.getCurrentDateStr());
            orderInfo.setGisLatitude(orderUserInfo.getLatitude());
            orderInfo.setGisLongitude(orderUserInfo.getLongitude());
            RiskResultVo<String> orderResult = riskDataComponent.submitOrder(
                    id_number, StringUtils.isNotBlank(orderUserInfo.getClientIp())?
                            orderUserInfo.getClientIp():"127.0.0.1", callback_url, JSON.toJSONString(orderInfo));
            logger.info(orderResult.toString());
            if (orderResult.getCode().equals("100")) {
                return true;
            }
        } else {
            logger.info("验证未完全通过，请稍后再试");
        }
        return false;
    }
}
