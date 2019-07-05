package com.xiaochong.loan.background.utils;

import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.ProxyUserDao;
import com.xiaochong.loan.background.entity.po.ManageAdmin;
import com.xiaochong.loan.background.entity.po.ProxyUser;
import com.xiaochong.loan.background.utils.enums.UserLoginTypeEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by jinxin on 2017/10/27.
 */
public class UserTokenUtils {

    private static SessionComponent sessionComponent=(SessionComponent)SpringContextUtil.getBean("sessionComponent");

    private static ProxyUserDao proxyUserDao=(ProxyUserDao)SpringContextUtil.getBean("proxyUserDao");

    /**
     * 管理后台用户
     * @param token
     * @return
     */
    public static ManageAdmin getManagerAdminByToken(String token){
        if(StringUtils.isBlank(token)){
            return null;
        }
        StringBuilder key = new StringBuilder(UserLoginTypeEnum.MANAGE.getType()).append("-").append(token);
        return sessionComponent.getAttributeManageAdmin(key.toString());
    }

    /**
     * 管理后台 ID Integer
     * @param token
     * @return
     */
    public static Integer getManagerAdminIdByToken(String token){
        ManageAdmin managerAdminByToken = getManagerAdminByToken(token);
        return managerAdminByToken==null?null:managerAdminByToken.getId();
    }

    /**
     * 前台用户
     * @param token
     * @return
     */
    public static ProxyUser getProxyUserByToken(String token){
        Integer proxyUserIdByToken = getProxyUserIdByToken(token);
        return proxyUserIdByToken==null?null:proxyUserDao.getById(proxyUserIdByToken);
    }

    /**
     * 前台用户ID Integer
     * @param token
     * @return
     */
    public  static Integer getProxyUserIdByToken(String token){
        String idString = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        Integer id;
        try{
             id = StringUtils.isBlank(idString) == true ? null : Integer.valueOf(idString);
        }catch (Exception e){
            return null;
        }
        return  id;
    }

}
