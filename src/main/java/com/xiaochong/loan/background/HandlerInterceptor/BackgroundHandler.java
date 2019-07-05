package com.xiaochong.loan.background.HandlerInterceptor;

import com.alibaba.fastjson.JSON;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.ProxyUserDao;
import com.xiaochong.loan.background.dao.ResourcesWebappDao;
import com.xiaochong.loan.background.dao.RoleWebappDao;
import com.xiaochong.loan.background.entity.po.ProxyUser;
import com.xiaochong.loan.background.entity.po.ResourcesWebapp;
import com.xiaochong.loan.background.entity.po.RoleWebapp;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.IsTypeEnum;
import com.xiaochong.loan.background.utils.enums.ResourcesWebappTypeEnum;
import com.xiaochong.loan.background.utils.enums.UserLoginTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by wujiaxing on 2017/8/9.
 * 拦截器
 */
public class BackgroundHandler implements HandlerInterceptor{

    private Logger logger = LoggerFactory.getLogger(BackgroundHandler.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("拦截器响应时间：{}",System.currentTimeMillis());
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setCharacterEncoding("UTF-8");
        SessionComponent sessionComponent = (SessionComponent) SpringContextUtil.getBean("sessionComponent");
        ProxyUserDao proxyUserDao = (ProxyUserDao) SpringContextUtil.getBean("proxyUserDao");
        ResourcesWebappDao resourcesWebappDao = (ResourcesWebappDao) SpringContextUtil.getBean("resourcesWebappDao");
        RoleWebappDao roleWebappDao = (RoleWebappDao) SpringContextUtil.getBean("roleWebappDao");
        String token = httpServletRequest.getParameter("token");
        ProxyUser proxyUser = null;
        RoleWebapp roleWebapp = null;
//        if(true){return true;}
        if(StringUtils.isNotBlank(token)){
            StringBuilder stringBuilder = new StringBuilder(token);
            StringBuilder append = stringBuilder.append("-").append(UserLoginTypeEnum.WEBAPP.getType());
            String id = sessionComponent.getAttribute(append.toString());
            logger.info("登录的用户id：{}，token：{},key:{}",id,token,append);
            if(StringUtils.isNotBlank(id)){
                Integer userId = Integer.parseInt(id);
                proxyUser = proxyUserDao.getById(userId);
                if(proxyUser != null && IsTypeEnum.TRUE.getType().equals(proxyUser.getStatus())){

                    List<ResourcesWebapp> resourcesWebapps = null;
                    roleWebapp = roleWebappDao.getByUserId(userId);
                    if(IsTypeEnum.TRUE.getType().equals(proxyUser.getIsMaster())){
                        resourcesWebapps =
                                resourcesWebappDao.queryResourcesByMerch(
                                        proxyUser.getMerchId(), ResourcesWebappTypeEnum.INTERFACE.getType());
                    }else {
                        if(roleWebapp!=null && IsTypeEnum.TRUE.getType().equals(roleWebapp.getStatus())){
                            resourcesWebapps =
                                    resourcesWebappDao.queryResourcesByUser(
                                            proxyUser.getId(), ResourcesWebappTypeEnum.INTERFACE.getType());
                        }
                    }
                    if(resourcesWebapps!=null && resourcesWebapps.size()>0){
                        for (ResourcesWebapp resourcesWebapp:resourcesWebapps) {
                            StringBuffer requestURL = httpServletRequest.getRequestURL();
                            String[] split = requestURL.toString().split("\\?");
                            if(StringUtils.isNotBlank(resourcesWebapp.getResUrl())){
                                String pattern = resourcesWebapp.getResUrl();
                                pattern = pattern.replaceAll("\\*",".*");
                                pattern = ".*"+pattern;
                                if(Pattern.matches(pattern,split[0])){
                                    return true;
                                }
                            }
                        }

                    }
                }

            }

        }
        logger.info("登录失效");
        BaseResultVo baseResultVo = new BaseResultVo();
        if(proxyUser==null){
            logger.info("用户未找到！");
            baseResultVo.setCode(ResultConstansUtil.LOGIN_INVALID_CODE);
            baseResultVo.setMessage(ResultConstansUtil.LOGIN_INVALID_DESC);
        }else if(!IsTypeEnum.TRUE.getType().equals(proxyUser.getStatus())){
            logger.info("账户禁用！");
            baseResultVo.setCode(ResultConstansUtil.ACCOUNTS_DISABLED_CODE);
            baseResultVo.setMessage(ResultConstansUtil.ACCOUNTS_DISABLED_DESC);
        }else if( (roleWebapp==null || !IsTypeEnum.TRUE.getType().equals(roleWebapp.getStatus())) &&
            !IsTypeEnum.TRUE.getType().equals(proxyUser.getIsMaster())){
            logger.info("未分配角色或角色禁用！");
            baseResultVo.setCode(ResultConstansUtil.ROLE_DISABLED_CODE);
            baseResultVo.setMessage(ResultConstansUtil.ROLE_DISABLED_DESC);
        }else{
            logger.info("账户权限失效！");
            baseResultVo.setCode(ResultConstansUtil.NO_PERMISSIONS_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.NO_PERMISSIONS_ERROR_DESC);
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        String result = JSON.toJSONString(baseResultVo);
        PrintWriter writer = null;
        try{
            httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
            httpServletResponse.setCharacterEncoding("UTF-8");
            writer = httpServletResponse.getWriter();
            writer.write(result);
            writer.flush();
        }catch (Exception e){
            logger.error("拦截器异常",e.getMessage());
        }finally {
            if (null != writer){
                writer.close();
            }
        }
        return false;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
