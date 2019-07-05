package com.xiaochong.loan.background.HandlerInterceptor;

import com.alibaba.fastjson.JSON;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.ManageAdminDao;
import com.xiaochong.loan.background.dao.ResourcesManageDao;
import com.xiaochong.loan.background.dao.RoleManageDao;
import com.xiaochong.loan.background.entity.po.ManageAdmin;
import com.xiaochong.loan.background.entity.po.ResourcesManage;
import com.xiaochong.loan.background.entity.po.RoleManage;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.IsTypeEnum;
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
 * Created by ray.liu on 2017/5/13.
 */
public class ManagerInterceptor implements HandlerInterceptor {

    private SessionComponent sessionComponent;

    private Logger logger = LoggerFactory.getLogger(ManagerInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setCharacterEncoding("UTF-8");
        sessionComponent =(SessionComponent) SpringContextUtil.getBean("sessionComponent");
        ResourcesManageDao resourcesManageDao =(ResourcesManageDao) SpringContextUtil.getBean("resourcesManageDao");
        RoleManageDao roleManageDao = (RoleManageDao) SpringContextUtil.getBean("roleManageDao");
        ManageAdminDao manageAdminDao = (ManageAdminDao) SpringContextUtil.getBean("manageAdminDao");

        ManageAdmin manageAdmin = new ManageAdmin();
        RoleManage roleManage = null;
        String token = httpServletRequest.getParameter("token");
//        if(true){return true;}
        if(StringUtils.isNotBlank(token)){
            StringBuilder key = new StringBuilder(UserLoginTypeEnum.MANAGE.getType()).append("-").append(token);
            manageAdmin = sessionComponent.getAttributeManageAdmin(key.toString());
            logger.info("ManagerInterceptor,manageAdmin = {}",manageAdmin);
            if(manageAdmin!=null){
                manageAdmin = manageAdminDao.getById(manageAdmin.getId());
            }
        }
        if(manageAdmin != null  && IsTypeEnum.TRUE.getType().equals(manageAdmin.getStatus())){
            roleManage = roleManageDao.getByUserId(manageAdmin.getId());
            logger.info("ManagerInterceptor,roleManage = {}",roleManage);
            if(roleManage!=null && IsTypeEnum.TRUE.getType().equals(roleManage.getStatus())){
                if(roleManage.getId().intValue()==1){
                    return true;
                }

                List<ResourcesManage> resourcesManages = resourcesManageDao.queryResources(manageAdmin.getId());
                for (ResourcesManage resourcesManage:resourcesManages) {
                    StringBuffer requestURL = httpServletRequest.getRequestURL();
                    String[] split = requestURL.toString().split("\\?");
                    if(StringUtils.isNotBlank(resourcesManage.getResUrl())){
                        String pattern = resourcesManage.getResUrl();
                        pattern = pattern.replaceAll("\\*",".*");
                        pattern = ".*"+pattern;
                        if(Pattern.matches(pattern,split[0])){
                            return true;
                        }
                    }
                }
            }
        }
        logger.info("管理平台登录失效");
        PrintWriter writer = null;
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        if(manageAdmin==null){
            logger.info("用户未找到！");
            baseResultVo.setCode(ResultConstansUtil.LOGIN_INVALID_CODE);
            baseResultVo.setMessage(ResultConstansUtil.LOGIN_INVALID_DESC);
        }else if(!IsTypeEnum.TRUE.getType().equals(manageAdmin.getStatus()) ){
            logger.info("账户禁用！");
            baseResultVo.setCode(ResultConstansUtil.ACCOUNTS_DISABLED_CODE);
            baseResultVo.setMessage(ResultConstansUtil.ACCOUNTS_DISABLED_DESC);
        }else if(roleManage==null || !IsTypeEnum.TRUE.getType().equals(roleManage.getStatus())){
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
        try{
            httpServletResponse.setCharacterEncoding("utf-8");
            writer = httpServletResponse.getWriter();
            writer.write(result);
            writer.flush();
        }catch (Exception e){
            logger.error("管理平台拦截器异常",e.getMessage());
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
