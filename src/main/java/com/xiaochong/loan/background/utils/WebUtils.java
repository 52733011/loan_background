package com.xiaochong.loan.background.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtils {

	/**
	 * 代理登录session key
	 */
	public final static String PROXY_LOGIN_KEY="proxy_login_key";
	
	/**
	 * 登录成功code
	 */
	public final static String LOGIN_SUCESS_CODE="login_success_code";
	
	/**
	 * 登录失败code
	 */
	public final static String LOGIN_ERROR_CODE="login_error_code";
	
	/**
	 * 用户信息不存在
	 */
	public final static String LOGIN_INEXISTENCE_CODE="login_inexistence_code";
	
	/**
	 * 验证码错误
	 */
	public final static String CHECKCODE_ERROR_CODE="checkcode_error_code";
	
	/**
	 * 未登录
	 */
	public final static String NOT_LOGIN="not_login";
	
	
	/**
	 * 没有ordertoken或token失效
	 */
	public final static String NOT_ORDER_TOKEN="not_order_token";
				
	
	/**
	 * 生成手机验证码(4N)
	 * 
	 * @return
	 */
	public static String getSecurityCode() {
		int[] securityCode = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		String code = "";
		for (int i = 0; i < 4; i++) {
			int number = (int) (Math.random() * securityCode.length);
			code = number + "" + code;
		}
		return code;
	}
	
	public static void doAjaxJson(Object obj,HttpServletResponse response){
		String data = JSON.toJSONString(obj);
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			response.getWriter().write(data);
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 得到真实ip
	 * @param request
	 * @return
	 */
	public static String getClientIP(HttpServletRequest request) {   
	    String ip = request.getHeader("x-forwarded-for");   
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
	        ip = request.getHeader("Proxy-Client-IP");   
	    }   
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
	        ip = request.getHeader("WL-Proxy-Client-IP");   
	  
	    }   
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
	        ip = request.getRemoteAddr();   
	    }   
	    return ip;   
	}
	
}
