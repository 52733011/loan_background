package com.xiaochong.loan.background.entity.vo;

import com.xiaochong.loan.background.entity.po.ContactInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ContactInfoWebappVo {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "订单Token")
    private String orderToken;
    @ApiModelProperty(value = "商户id")
    private Integer merchId;
    @ApiModelProperty(value = "身份证号")
    private String idCard;
    @ApiModelProperty(value = "关系类型1:父亲; 2:母亲; 3:配偶;6:舍友(同学);7:辅导员(老师);9:朋友;10:恋人;11:亲戚;12:其他")
    private String relation;
    @ApiModelProperty(value = "关系名（选择其他时填写）")
    private String relationName;
    @ApiModelProperty(value = "真实姓名")
    private String realName;
    @ApiModelProperty(value = "手机号")
    private String phone;


    public void setContactInfo(ContactInfo contactInfo) {
        this.id = contactInfo.getId();
        this.orderNo = contactInfo.getOrderNo();
        this.orderToken = contactInfo.getOrderToken();
        this.merchId = contactInfo.getMerchId();
        this.idCard = contactInfo.getIdCard();
        this.relation = contactInfo.getRelation();
        this.relationName = contactInfo.getRelationName();
        this.realName = contactInfo.getRealName();
        this.phone = contactInfo.getPhone();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderToken() {
        return orderToken;
    }

    public void setOrderToken(String orderToken) {
        this.orderToken = orderToken;
    }

    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
