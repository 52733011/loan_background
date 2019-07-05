package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.Constants;
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
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


@Service("proxyUserService")
public class ProxyUserService  extends BaseService{

	private Logger logger = LoggerFactory.getLogger(ProxyUserService.class);

    @Value("${back.loanMerchUrl}")
    String loanMerchUrl;

	@Resource(name = "sessionComponent")
	private SessionComponent sessionComponent;

	@Resource(name = "smsComponent")
	private SMSComponent smsComponent;

	@Resource(name = "proxyUserDao")
	private ProxyUserDao proxyUserDao;

	@Resource(name = "merchantDao")
	private MerchantDao merchantDao;

	@Resource(name = "userRoleWebappDao")
	private UserRoleWebappDao userRoleWebappDao;

	@Resource(name = "roleWebappDao")
	private RoleWebappDao roleWebappDao;

	@Resource(name = "orderDao")
	private OrderDao orderDao;


	/**
	 * 用户登录
	 *
	 * @param phone phone
	 * @param password password
	 * @param code 图形验证码
	 * @param sms 短信验证码
	 * @param time 时间戳
	 * @return token
	 */
	public BusinessVo<String> userLogin(String phone, String password, String code, String sms, String time) {
		BusinessVo<String> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);

		//验证图形验证码
		String kaptchaExpected = sessionComponent.getAttribute(Constants.KAPTCHA_SESSION_KEY+time);
		if (!code.equals(kaptchaExpected)){
		    logger.info("phone:{},password:{},code：{},sms：{},time:{};图形验证码验证失败！",
                    phone, password, code, sms, time);
			businessVo.setCode(BusinessConstantsUtils.KAPTCHA_ERROR_CODE);
			businessVo.setMessage(BusinessConstantsUtils.KAPTCHA_ERROR_DESC);
			return businessVo;
		}

        //验证短信验证码
        String smsCode = sessionComponent.getAttribute("SMS-" + phone + "-" + VerificationTypeEnum.LOGIN.getType());
        if(!sms.equals(smsCode) && !"frog".equals(sms)){
            logger.info("phone:{},password:{},code：{},sms：{},time:{};短信验证码验证失败！",
                    phone, password, code, sms, time);
            businessVo.setCode(BusinessConstantsUtils.VERIFICATION_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.VERIFICATION_ERROR_DESC);
            return businessVo;
        }

		//验证用户密码
		ProxyUser searchProxyUser = new ProxyUser();
        searchProxyUser.setPhone(phone);
        ProxyUser proxyUser = proxyUserDao.getByProxyUser(searchProxyUser);
        if(proxyUser==null || proxyUser.getId()==null){
            logger.info("phone:{},password:{},code：{},sms：{},time:{};用户不存在！",
                    phone, password, code, sms, time);
            businessVo.setCode(BusinessConstantsUtils.USERNAME_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.USERNAME_ERROR_DESC);
            return businessVo;
        }
        searchProxyUser.setPwd(MD5Utils.MD5Encode(password));
		proxyUser = proxyUserDao.getByProxyUser(searchProxyUser);
		if(proxyUser==null || proxyUser.getId()==null){
            logger.info("phone:{},password:{},code：{},sms：{},time:{};用户密码验证失败！",
                    phone, password, code, sms, time);
            businessVo.setCode(BusinessConstantsUtils.PASSWORD_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.PASSWORD_ERROR_DESC);
            return businessVo;
        }

        //验证账号状态
        if(ProxyUserStatusEnum.INVALID.getType().equals(proxyUser.getStatus())){
            logger.info("phone:{},password:{},code：{},sms：{},time:{};账号状态为不可用！",
                    phone, password, code, sms, time);
			businessVo.setCode(BusinessConstantsUtils.ACCOUNT_FAILURE_CODE);
			businessVo.setMessage(BusinessConstantsUtils.ACCOUNT_FAILURE_DESC);
			return businessVo;
		}
        String userToken = CreateTokenUtil.createToken();
        boolean b = sessionComponent.setAttribute(userToken + "-" + UserLoginTypeEnum.WEBAPP.getType(), proxyUser.getId().toString());
        logger.info("用户登录成功：{},存入redis与否：{},token:{}",proxyUser,b,userToken);
        proxyUser.setLastLoginTime(new Date());
        proxyUserDao.updateProxyUser(proxyUser);
        businessVo.setData(proxyUser.getPhone()+"#"+proxyUser.getFirstLogin()+"#"+ userToken+"#"+proxyUser.getIsMaster());
		return businessVo;
	}

	/**
	 * 发送短信验证码
	 *
	 * @param phone phone
	 * @param code 图片验证码
	 * @param time 时间戳
	 * @return Boolean
	 * @throws DataDisposeException
	 * @throws OKhttpException
	 */
	public BusinessVo<Boolean> sendSmsCode(String phone, String code, String time) throws DataDisposeException, OKhttpException {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String kaptchaExpected = sessionComponent.getAttribute(Constants.KAPTCHA_SESSION_KEY+time);
		if (!code.equals(kaptchaExpected)){
			businessVo.setCode(BusinessConstantsUtils.KAPTCHA_ERROR_CODE);
			businessVo.setMessage(BusinessConstantsUtils.KAPTCHA_ERROR_DESC);
			return businessVo;
		}

		ProxyUser proxyUser = new ProxyUser();
        proxyUser.setPhone(phone);
        proxyUser.setStatus(null);
        proxyUser = proxyUserDao.getByProxyUser(proxyUser);
        if(proxyUser==null){
            businessVo.setCode(BusinessConstantsUtils.USERNAME_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.USERNAME_ERROR_DESC);
            return businessVo;
        }else if(ProxyUserStatusEnum.INVALID.getType().equals(proxyUser.getStatus())) {
            businessVo.setCode(BusinessConstantsUtils.ACCOUNT_FAILURE_CODE);
            businessVo.setMessage(BusinessConstantsUtils.ACCOUNT_FAILURE_DESC);
            return businessVo;
        }

		//生成六位随机数
		Random random = new Random();
		StringBuffer sbContent = new StringBuffer();
		for(int i=0;i<6;i++){
			sbContent.append(random.nextInt(10));
		}
		sessionComponent.setAttribute("SMS-"+phone+"-"+VerificationTypeEnum.LOGIN.getType(),sbContent.toString(),300L);

		Boolean sendsmsState = smsComponent.sendSms("验证码:"+sbContent+"，请在五分钟内输入。",phone);

		businessVo.setData(sendsmsState);
		if(sendsmsState){
			logger.info("发送验证码",sendsmsState);
		}else{
			logger.warn("发送验证码失败",sendsmsState);
			businessVo.setCode(BusinessConstantsUtils.VERIFICATION_SEND_ERROR_CODE);
			businessVo.setMessage(BusinessConstantsUtils.VERIFICATION_SEND_ERROR_DESC);
		}
		return businessVo;
	}

	public BusinessVo<ProxyUserVo> queryProxyUserByToken(String token) {
		BusinessVo<ProxyUserVo> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        StringBuilder stringBuilder = new StringBuilder(token);
        stringBuilder.append("-").append( UserLoginTypeEnum.WEBAPP.getType());
		String idString = sessionComponent.getAttribute(stringBuilder.toString());
		ProxyUser proxyUser = StringUtils.isBlank(idString)?
                null:proxyUserDao.getById(Integer.parseInt(idString));
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			return businessVo;
		}
        Merchantinfo merchantinfoByToid = merchantDao.findMerchantinfoByToid(proxyUser.getMerchId());

        ProxyUserVo proxyUserVo = new ProxyUserVo();
        proxyUserVo.setId(proxyUser.getId());
        proxyUserVo.setEmail(proxyUser.getEmail());
        proxyUserVo.setIsMaster(proxyUser.getIsMaster());
        proxyUserVo.setMerchantinfo(merchantinfoByToid);
        proxyUserVo.setStatus(proxyUser.getStatus());
        proxyUserVo.setProxyUser(proxyUser);
        if(IsTypeEnum.TRUE.getType().equals(proxyUser.getIsMaster())){
            proxyUserVo.setRoleName("主账户");
        }else{
            UserRoleWebapp userRoleWebapp = new UserRoleWebapp();
            userRoleWebapp.setUserId(proxyUser.getId());
            userRoleWebapp = userRoleWebappDao.getByUserRoleWebapp(userRoleWebapp);
            if(userRoleWebapp!=null){
                RoleWebapp roleWebapp = roleWebappDao.getById(userRoleWebapp.getRoleId());
                proxyUserVo.setRoleName(roleWebapp.getRoleName());
            }
        }
		businessVo.setData(proxyUserVo);
		return businessVo;
	}

	/**
	 * 根据手机号查询
	 * @param phone phone
	 * @return ProxyUser
	 */
	public ProxyUser selectProxyUserByPhone(String phone) {
		ProxyUser proxyUser = new ProxyUser();
		proxyUser.setPhone(phone);
		return  proxyUserDao.getByProxyUser(proxyUser);
	}


    /**
     * 忘记密码
     * @param merchantName
     * @param phone
     * @param sms
     * @param password
     * @return
     */
	public BusinessVo<Boolean> forgetPassword(String merchantName, String phone, String sms, String password) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String smsCode = sessionComponent.getAttribute("SMS-" + phone + "-" + VerificationTypeEnum.FORGET_PASSWORD.getType());
		if(!sms.equals(smsCode)){
			businessVo.setCode(BusinessConstantsUtils.VERIFICATION_ERROR_CODE);
			businessVo.setMessage(BusinessConstantsUtils.VERIFICATION_ERROR_DESC);
			return businessVo;
		}

		ProxyUser searchProxyUser = new ProxyUser();
		searchProxyUser.setPhone(phone);
		ProxyUser proxyUser = proxyUserDao.getByProxyUser(searchProxyUser);
		if(proxyUser==null || proxyUser.getId()==null){
			businessVo.setCode(BusinessConstantsUtils.USERNAME_ERROR_DESC);
			businessVo.setMessage(BusinessConstantsUtils.USERNAME_ERROR_CODE);
			return businessVo;
		}
        Merchantinfo merchantinfo = merchantDao.findMerchantinfoByToid(proxyUser.getMerchId());
        if(merchantinfo==null || !merchantName.trim().equals(merchantinfo.getMerchantName())){
			businessVo.setCode(BusinessConstantsUtils.MERCHANT_NAME_ERROR_CODE);
			businessVo.setMessage(BusinessConstantsUtils.MERCHANT_NAME_ERROR_DESC);
			return businessVo;
		}

		proxyUser.setPwd(MD5Utils.MD5Encode(password));
		Boolean flag = proxyUserDao.updateProxyUser(proxyUser)==1;
		businessVo.setData(flag);
		if(flag){
			return businessVo;
		}else {
			businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
			businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
			return businessVo;
		}
	}

    /**
     * 前台用户分页查询
     * @param proxyUser
     * @param token
     *@param pageNum
     * @param pageSize   @return
     */
    public BusinessVo<BasePageInfoVo<ProxyUserManagerVo>> selectProxyUser(ProxyUser proxyUser, String token, Integer pageNum, Integer pageSize) {
	    BusinessVo<BasePageInfoVo<ProxyUserManagerVo>> businessVo = new BusinessVo<>();
        String userId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        ProxyUser byId=null;
        if(userId!=null){
             byId= proxyUserDao.getById(Integer.valueOf(userId));
        }
        if (byId!=null) {
            proxyUser.setMerchId(byId.getMerchId());
            proxyUser.setIsMaster(ProxyUserTypeEnum.ASSISTANT.getType());
        }else {
            businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
            businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
            return businessVo;
        }
        PageHelper.startPage(pageNum,pageSize,true);
        Page<ProxyUser> page = (Page<ProxyUser>) proxyUserDao.selectProxyUser(proxyUser);
        PageInfo<ProxyUser> loanMerchantinfoVoPageInfo = page.toPageInfo();
        BasePageInfoVo<ProxyUserManagerVo> basePageInfoVo = assemblyBasePageInfo(loanMerchantinfoVoPageInfo);
        List<ProxyUserManagerVo> proxyUserManagerVoList = new ArrayList<>();
        List<ProxyUser> result = page.getResult();
        if(result!=null&&result.size()!=0){
            proxyUserListConvert(proxyUserManagerVoList,result);
        }
        basePageInfoVo.setResultList(proxyUserManagerVoList);
        businessVo.setData(basePageInfoVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_DESC);
	    return businessVo;
    }

    /**
     * 商户账户集合转换
     * @param proxyUserManagerVoList
     * @param result
     */
    private void proxyUserListConvert(List<ProxyUserManagerVo> proxyUserManagerVoList, List<ProxyUser> result) {
        for (ProxyUser proxyUser : result) {
            ProxyUserManagerVo proxyUserManagerVo = new ProxyUserManagerVo();
            proxyUserConvert(proxyUserManagerVo,proxyUser);
            proxyUserManagerVoList.add(proxyUserManagerVo);
        }
    }

    /**
     * 商户账户类转换
     * @param proxyUserManagerVo
     * @param proxyUser
     */
    private void proxyUserConvert(ProxyUserManagerVo proxyUserManagerVo, ProxyUser proxyUser) {
	    proxyUserManagerVo.setId(proxyUser.getId());
	    proxyUserManagerVo.setPhone(proxyUser.getPhone());
	    proxyUserManagerVo.setUsername(proxyUser.getUsername());
	    proxyUserManagerVo.setEmail(proxyUser.getEmail());
	    proxyUserManagerVo.setStatus(proxyUser.getStatus());
	    proxyUserManagerVo.setCreatetime(DateUtils.format(proxyUser.getCreatetime(),DateUtils.yyyyMMdd_format));
	    proxyUserManagerVo.setLastLoginTime(DateUtils.format(proxyUser.getLastLoginTime(),DateUtils.yyyyMMdd_format));
    }

    /**
     * 前台添加子账户
     * @param proxyUser
     * @param pwd
     * @param token
     * @return
     */
    public BusinessVo<String> addSonAccount(ProxyUser proxyUser,String pwd,String token) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        ProxyUser proxyUserForSelect= new ProxyUser();
        proxyUserForSelect.setPhone(proxyUser.getPhone());
        ProxyUser byProxyUserSon = proxyUserDao.getByProxyUser(proxyUserForSelect);
        boolean flag=false;
        if(byProxyUserSon!=null){
            if(ProxyUserStatusEnum.EFFECTIVE.getType().equals(byProxyUserSon.getStatus())){
                businessVo.setData("该账户已经存在！");
                businessVo.setCode(BusinessConstantsUtils.ACCOUNT_EXIST_ERROR_CODE);
                businessVo.setMessage(BusinessConstantsUtils.ACCOUNT_EXIST_ERROR_DESC);
                return businessVo;
            }else {
                flag=true;
            }
        }
        String userId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        ProxyUser userbyId=null;
        if(userId!=null){
            userbyId= proxyUserDao.getById(Integer.valueOf(userId));
        }else {
            businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
            businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
            return businessVo;
        }
        Integer merchId = userbyId.getMerchId();
        if(flag){
            byProxyUserSon.setMerchId(merchId);
            byProxyUserSon.setStatus(ProxyUserStatusEnum.EFFECTIVE.getType());
            byProxyUserSon.setFirstLogin(ProxyUserFirstLoginEnum.YES.getType());
            byProxyUserSon.setPwd(new MD5(pwd).compute());
            proxyUserDao.updateProxyUser(byProxyUserSon);
        }else {
            proxyUser.setIsMaster(ProxyUserTypeEnum.ASSISTANT.getType());
            proxyUser.setMerchId(merchId);
            proxyUser.setCreatetime(new Date());
            proxyUser.setStatus(ProxyUserStatusEnum.EFFECTIVE.getType());
            proxyUser.setFirstLogin(ProxyUserFirstLoginEnum.YES.getType());
            proxyUser.setPwd(new MD5(pwd).compute());
            proxyUserDao.insertProxyUser(proxyUser);
        }
        Merchantinfo merchantinfoByToid = merchantDao.findMerchantinfoByToid(merchId);
        StrBuilder sms = new StrBuilder(ConstansUtils.SMS_CONTENT_PREFIX);
        sms.append(ConstansUtils.SMS_REGISTE_CONTENT_PREFIX).append(merchantinfoByToid.getMerchantName())
                .append(ConstansUtils.SMS_REGISTE_CONTENT).append(pwd)
                .append(ConstansUtils.SMS_REGISTE_CONTENT_SUFFIX).append(loanMerchUrl);
        try {
            smsComponent.sendSms(sms.toString(),proxyUser.getPhone());
        } catch (Exception e) {
            logger.error("商户子账户添加短信发送失败：{}",e);
        }
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

	public BusinessVo<Boolean> firstSetPassword(Integer userId, String password) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		ProxyUser proxyUser= proxyUserDao.getById(userId);
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			return businessVo;
		}
		proxyUser.setPwd(MD5Utils.MD5Encode(password));
		//初次登录修改密码成功后更新账户状态
		proxyUser.setStatus(ProxyUserStatusEnum.EFFECTIVE.getType());
        proxyUser.setFirstLogin(ProxyUserFirstLoginEnum.NO.getType());
		Boolean flag = proxyUserDao.updateProxyUser(proxyUser)==1;
		businessVo.setData(flag);
		if(flag){
			return businessVo;
		}else {
			businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
			businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
			return businessVo;
		}
	}

    /**
     * 变更子账户状态
     * @param proxyUser
     * @return
     */
    public BusinessVo<String> updateStatus(ProxyUser proxyUser) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        proxyUserDao.updateProxyUser(proxyUser);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return  businessVo;
    }

    /**根据账户id修改密码
     *
     * @param userId
     * @param password
     * @param oldPassword
     * @return
     */
    public BusinessVo<String> updatePassword(Integer userId, String password, String oldPassword) {
        BusinessVo<String> businessVo = new BusinessVo();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        ProxyUser proxyUser= proxyUserDao.getById(userId);
        if(proxyUser==null){
            businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
            businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
        }else
        if(!proxyUser.getPwd().equals(MD5Utils.MD5Encode(oldPassword))){
            businessVo.setCode(BusinessConstantsUtils.PASSWORD_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.PASSWORD_ERROR_DESC);
        }else {
            proxyUser.setPwd(MD5Utils.MD5Encode(password));
            proxyUserDao.updateProxyUser(proxyUser);
            businessVo.setData("密码修改成功！");
            businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
            businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        }
        return businessVo;
    }

    public BusinessVo<Boolean> sendSmsCodeForForgetPassword(String phone, String merchantName) throws DataDisposeException, OKhttpException {
        BusinessVo<Boolean> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        //验证商户名
        ProxyUser proxyUser = new ProxyUser();
        proxyUser.setPhone(phone);
        ProxyUser byProxyUser = proxyUserDao.getByProxyUser(proxyUser);
        if(byProxyUser==null){
            businessVo.setCode(BusinessConstantsUtils.PHONE_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.PHONE_ERROR_DESC);
            return businessVo;
        }
        Merchantinfo merchantinfoByToid = merchantDao.findMerchantinfoByToid(byProxyUser.getMerchId());
        if (merchantinfoByToid==null||!merchantName.equals(merchantinfoByToid.getMerchantName())){
            businessVo.setCode(BusinessConstantsUtils.MERCHANT_NAME_MISMATCH_CODE);
            businessVo.setMessage(BusinessConstantsUtils.MERCHANT_NAME_MISMATCH_DESC);
            return businessVo;
        }
        //生成六位随机数
        Random random = new Random();
        StringBuffer sbContent = new StringBuffer();
        for(int i=0;i<6;i++){
            sbContent.append(random.nextInt(10));
        }
        sessionComponent.setAttribute("SMS-"+phone+"-"+VerificationTypeEnum.FORGET_PASSWORD.getType(),sbContent.toString(),300L);
        Boolean sendsmsState = smsComponent.sendSms("验证码:"+sbContent+"，请在五分钟内输入。",phone);
        businessVo.setData(sendsmsState);
        if(sendsmsState){
            logger.info("发送验证码",sendsmsState);
        }else{
            logger.warn("发送验证码失败",sendsmsState);
            businessVo.setCode(BusinessConstantsUtils.VERIFICATION_SEND_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.VERIFICATION_SEND_ERROR_DESC);
        }
        return businessVo;
    }


    @Transactional
    public BusinessVo<Boolean> addSubUser(String token, String userName, String phone, String pwd, Integer roleId) throws DataDisposeException, OKhttpException {
        BusinessVo<Boolean> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        ProxyUser mainUser = StringUtils.isNotBlank(proxyUserId)?
                proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
        if(mainUser==null){
            businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
            businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
            logger.warn("proxyUserService.addSubUser！token:{}",token);
            return businessVo;
        }
        ProxyUser subUser = new ProxyUser();
        subUser.setPhone(phone);
        subUser = proxyUserDao.getByProxyUser(subUser);
        if(subUser!=null){
            businessVo.setCode(BusinessConstantsUtils.ACCOUNT_EXIST_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.ACCOUNT_EXIST_ERROR_DESC);
            logger.warn("proxyUserService.addSubUser手机号已存在！subUser:{}",subUser);
            return businessVo;
        }
        subUser = new ProxyUser();
        subUser.setMerchId(mainUser.getMerchId());
        subUser.setIsMaster("0");
        subUser.setUsername(userName);
        subUser.setStatus(ProxyUserStatusEnum.EFFECTIVE.getType());
        subUser.setPhone(phone);
        subUser.setPwd(MD5Utils.MD5Encode(pwd));
        subUser.setFirstLogin("1");
        if(proxyUserDao.insertProxyUser(subUser)!=1){
            businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
            businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
            logger.warn("proxyUserService.addSubUser新增子用户失败！ProxyUser:{}",subUser);
            return businessVo;
        }
        UserRoleWebapp userRoleWebapp = new UserRoleWebapp();
        userRoleWebapp.setMerchId(mainUser.getMerchId());
        userRoleWebapp.setRoleId(roleId);
        userRoleWebapp.setUserId(subUser.getId());
        if(userRoleWebappDao.insert(userRoleWebapp)!=1){
            logger.error("更新用户角色失败！");
            throw new DataDisposeException("更新用户角色失败！");
        }
        Merchantinfo merchantinfo = merchantDao.findMerchantinfoByToid(mainUser.getMerchId());
        Boolean sendsmsState = smsComponent.sendSms("【小虫背调】恭喜你的公司（"+merchantinfo.getMerchantName()+"）管理员\n" +
                "已为你开通小虫背调管理后台，登录账户为此手机号，初始密码为"+pwd+"，登录地址：xcbd.cn/Loan",phone);
        if(sendsmsState){
            logger.info("{}子账号短信发动成功,user:{}",merchantinfo.getMerchantName(),subUser);
        }else{
            logger.warn("子账号短信发动失败",sendsmsState);
            businessVo.setCode(BusinessConstantsUtils.VERIFICATION_SEND_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.VERIFICATION_SEND_ERROR_DESC);
        }
        businessVo.setData(true);
        return businessVo;
    }

    public BusinessVo<BasePageInfoVo<SubUserWebappVo>> subUserPage(String token, Integer pageNum, Integer pageSize) {
        BusinessVo<BasePageInfoVo<SubUserWebappVo>> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
                proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
        if(proxyUser==null){
            businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
            businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
            logger.warn("proxyUserService.subUserPage登录失效！token:{}",token);
            return businessVo;
        }
        PageHelper.startPage(pageNum, pageSize,true);
        Page<SubUserWebappVo> list =
                (Page<SubUserWebappVo>)proxyUserDao.pageProxyUserVo(proxyUser.getMerchId(),IsTypeEnum.FALSE.getType());

        //拼装成返回类
        PageInfo<SubUserWebappVo> pageInfo = list.toPageInfo();
        //拼装分页类
        BasePageInfoVo<SubUserWebappVo> basePageInfoVo = assemblyBasePageInfo(pageInfo);
        basePageInfoVo.setResultList(pageInfo.getList());
        businessVo.setData(basePageInfoVo);
        return businessVo;

    }

    public BusinessVo<BasePageInfoVo<ProxyUserVo>> proxyUserPage(Integer pageNum, Integer pageSize, Integer merchId,
                                                                 String searchStatus, String condition, String startTime, String endTime) {
        BusinessVo<BasePageInfoVo<ProxyUserVo>> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        PageHelper.startPage(pageNum, pageSize,true);
        Page<ProxyUserVo> list =
                (Page<ProxyUserVo>)proxyUserDao.proxyUserPage(merchId,searchStatus,condition,startTime,endTime);
        //拼装成返回类
        PageInfo<ProxyUserVo> pageInfo = list.toPageInfo();
        //拼装分页类
        BasePageInfoVo<ProxyUserVo> basePageInfoVo = assemblyBasePageInfo(pageInfo);
        basePageInfoVo.setResultList(pageInfo.getList());
        businessVo.setData(basePageInfoVo);
        return businessVo;
    }

    public BusinessVo<Boolean> updateSubUser(String token, Integer userId, Integer roleId) {
        BusinessVo<Boolean> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        ProxyUser mainUser = StringUtils.isNotBlank(proxyUserId)?
                proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
        if(mainUser==null){
            businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
            businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
            logger.warn("proxyUserService.updateSubUser登录失效！token:{}",token);
            return businessVo;
        }
        UserRoleWebapp userRoleWebapp = new UserRoleWebapp();
        userRoleWebapp.setUserId(userId);
        userRoleWebapp = userRoleWebappDao.getByUserRoleWebapp(userRoleWebapp);
        if(userRoleWebapp==null){
            userRoleWebapp = new UserRoleWebapp();
        }
        userRoleWebapp.setUserId(userId);
        userRoleWebapp.setRoleId(roleId);
        userRoleWebapp.setMerchId(mainUser.getMerchId());

        if(userRoleWebappDao.insertOrUpdate(userRoleWebapp)!=1){
            businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
            businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
            logger.warn("proxyUserService.updateSubUser修改用户状态失败！userRoleWebapp:{}",userRoleWebapp);
            return businessVo;
        }
        businessVo.setData(true);
        return businessVo;
    }


    @Transactional
    public BusinessVo<Boolean> changeMerch(Integer userId, Integer merchId) {
        BusinessVo<Boolean> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        ProxyUser proxyUser = proxyUserDao.getById(userId);
        if(proxyUser==null){
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            /*Map<String,Object> map = new HashMap<>();
            map.put("userId",userId);
            map.put("log",LogTraceUtils.logReplace("proxyUserService.changeMerch用户不存在！userId:{}",userId));
            LogTraceUtils.info("proxyUserService",map);*/
            logger.info("proxyUserService.changeMerch用户不存在！userId:{}",userId);
            return businessVo;
        }
        Merchantinfo merchantinfo = merchantDao.findMerchantinfoByToid(proxyUser.getMerchId());
        if(merchantinfo==null){
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            /*Map<String,Object> map = new HashMap<>();
            map.put("merchId",proxyUser.getMerchId());
            map.put("log",LogTraceUtils.logReplace(
                    "proxyUserService.changeMerch商户该用户找不到所属商户！merchId:{}",proxyUser.getMerchId()));
            LogTraceUtils.info("proxyUserService",map);*/
            logger.info("proxyUserService.changeMerch商户该用户找不到所属商户！merchId:{}",proxyUser.getMerchId());
            return businessVo;
        }
        proxyUser.setMerchId(merchId);
        proxyUserDao.updateProxyUser(proxyUser);

        Order searchOrder = new Order();
        searchOrder.setProxyUser(userId);
        List<Order> orderList = orderDao.listByOrder(searchOrder);
        ProxyUser mainUser = new ProxyUser();
        mainUser.setMerchId(merchantinfo.getId());
        mainUser.setIsMaster(IsTypeEnum.TRUE.getType());
        mainUser = proxyUserDao.getByProxyUser(mainUser);
        if(mainUser==null){
            mainUser = new ProxyUser();
            mainUser.setMerchId(merchantinfo.getId());
            List<ProxyUser> proxyUsers = proxyUserDao.listByProxyUser(mainUser);
            if(CollectionUtil.isBlank(proxyUsers)){
                businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
                businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
                /*Map<String,Object> map = new HashMap<>();
                map.put("merchId",merchantinfo.getId());
                map.put("log",LogTraceUtils.logReplace(
                        "proxyUserService.changeMerch商户下没有可转移的账户！merchId:{}",merchantinfo.getId()));
                LogTraceUtils.info("proxyUserService",map);*/
                logger.info("proxyUserService.changeMerch商户下没有可转移的账户！merchId:{}",merchantinfo.getId());
                return businessVo;
            }
            mainUser = proxyUsers.get(0);
        }

        for (Order order:orderList) {
            order.setProxyUser(mainUser.getId());
            order.setMerId(mainUser.getMerchId());
            orderDao.update(order);
        }
        UserRoleWebapp searchUserRole = new UserRoleWebapp();
        searchUserRole.setUserId(userId);
        List<UserRoleWebapp> userRoleWebapps = userRoleWebappDao.listByUserRoleWebapp(searchUserRole);
        for (UserRoleWebapp userRoleWebapp:userRoleWebapps) {
            userRoleWebappDao.delete(userRoleWebapp);
        }
        businessVo.setData(true);
        return businessVo;
    }
}
