package com.xiaochong.loan.background.component;

import com.alibaba.fastjson.JSON;
import com.xiaochong.loan.background.dao.RedisDao;
import com.xiaochong.loan.background.entity.po.CountNum;
import com.xiaochong.loan.background.entity.po.ManageAdmin;
import com.xiaochong.loan.background.entity.po.ProxyUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by ray.liu on 2017/4/28.
 */
@Component("sessionComponent")
public class SessionComponent {

    @Resource(name = "redisDao")
    private RedisDao redisDao;


    /**
     * 将信息放入session中
     * @param key key
     * @param code code
     * @return 是否存入成功
     */
    public boolean setAttribute(String key,String code){
        if(StringUtils.isBlank(key) || StringUtils.isBlank(code)){
            return false;
        }
        return redisDao.set(key, code);
    }

    public boolean setAttribute(String key,String code,long second){
        if(StringUtils.isBlank(key) || StringUtils.isBlank(code)){
            return false;
        }
        return redisDao.set(key, code ,second);
    }

    /**
     * 将计数信息放入session中
     * @param token 用户token
     * @param countNum 计数信息
     * @return 是否存入成功
     */
    public boolean setAttribute(String token,CountNum countNum){
        String countJson = JSON.toJSONString(countNum);
        return redisDao.set(token, countJson);
    }

    /**
     * 取出计数对象
     * @param token token
     * @return 计数信息
     */
    public CountNum getAttributeCount(String token) {
        String countNum = redisDao.get(token);
        return StringUtils.isNotBlank(countNum)?JSON.parseObject(countNum, CountNum.class):null;
    }

    /**
     * 取出code
     * @param key key
     * @return
     */
    public String getAttribute(String key) {
        return StringUtils.isNotBlank(key)?redisDao.get(key):null;
    }


    public void delAttribute(String token){
        redisDao.delete(token);
    }

    /*public boolean setProxyUserAttribute(String token, ProxyUser proxyUser) {
        String companySonJson = JSON.toJSONString(proxyUser);
        return redisDao.set(token, companySonJson);
    }

    public ProxyUser getProxyUserAttribute(String token) {
        String proxyUser = redisDao.get(token);
        return StringUtils.isNotBlank(proxyUser)?JSON.parseObject(proxyUser, ProxyUser.class):null;
    }*/

    /**
     * 将用户信息放入session中
     * @param token 用户token
     * @param manageAdmin 用户信息
     * @return 是否存入成功
     */
    public boolean setAttribute(String token,ManageAdmin manageAdmin){
        String companyUserJson = JSON.toJSONString(manageAdmin);
        return redisDao.set(token, companyUserJson);
    }

    /**
     * 取出用户对象
     * @param token 用户token
     * @return 用户信息
     */
    public ManageAdmin getAttributeManageAdmin(String token){
        String manageAdmin = redisDao.get(token);
        return StringUtils.isNotBlank(manageAdmin)?JSON.parseObject(manageAdmin, ManageAdmin.class):null;
    }


}
