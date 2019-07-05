package com.xiaochong.loan.background.utils;

/**
 * Created by jinxin on 2017/8/9.
 * 商户状态
 */
public enum FlowOptionTypeEnum {

    USER_INFO("用户信息","01"),EDUCATION_INFO("学历信息","02"),PHONE_INFO("手机信息","03")
    ,USER_QUERY_INFO("用户查询信息","04"),LINKMAN_INFO("联系人信息","05"),COMMUNICATION_BLACKLIST_INFO("通讯黑名单信息","06")
    ,LINKMAN_LIST_OF_LAST_SIX_MONTH("近6个月联系人","07"),DEBIT_AND_CREDIT_RECORD("借贷过往记录","08")
    ,BADNESS_SCAN_RECORD("不良扫描记录","09"),WATCH_DETAILS("行业关注名单","10"),LBS_INFO("地理位置信息","11");

    private String name;

    private String type;


    FlowOptionTypeEnum(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
