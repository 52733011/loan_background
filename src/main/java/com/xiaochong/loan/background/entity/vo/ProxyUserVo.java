package com.xiaochong.loan.background.entity.vo;

import com.xiaochong.loan.background.entity.po.Merchantinfo;
import com.xiaochong.loan.background.entity.po.ProxyUser;
import com.xiaochong.loan.background.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ProxyUserVo {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String pwd;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "电话")
    private String phone;
    @ApiModelProperty(value = "账号状态：0无效，1有效",example = "1")
    private String status;
    @ApiModelProperty(value = "是否初次登录：1 是，0 否")
    private String firstLogin;
    @ApiModelProperty(value = "创建时间")
    private String createtime;
    @ApiModelProperty(value = "是否为主账户：1主，0副",example = "0")
    private String isMaster;
    @ApiModelProperty(value = "商户名称")
    private String merchantName;
    @ApiModelProperty(value = "商户id")
    private Integer merchId;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "商户简称")
    private String merchantShortName;
    @ApiModelProperty(value = "英文简称")
    private String merchantEnName;


    public void setProxyUser(ProxyUser proxyUser) {
        this.id = proxyUser.getId();
        this.username = proxyUser.getUsername();
        this.pwd = proxyUser.getPwd();
        this.email = proxyUser.getEmail();
        this.phone = proxyUser.getPhone();
        this.status = proxyUser.getStatus();
        this.firstLogin = proxyUser.getFirstLogin();
        this.createtime = DateUtils.format(proxyUser.getCreatetime(),DateUtils.ymdhms_format);
        this.isMaster = proxyUser.getIsMaster();
        this.merchId = proxyUser.getMerchId();
    }


    public void setMerchantinfo(Merchantinfo merchantinfo) {
        this.merchId = merchantinfo.getId();
        this.merchantName = merchantinfo.getMerchantName();
        this.merchantShortName = merchantinfo.getShortName();
        this.merchantEnName = merchantinfo.getEnName();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(String firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(String isMaster) {
        this.isMaster = isMaster;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public String getMerchantEnName() {
        return merchantEnName;
    }

    public void setMerchantEnName(String merchantEnName) {
        this.merchantEnName = merchantEnName;
    }
}
