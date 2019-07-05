package com.xiaochong.loan.background.utils;

public class RiskConstantsUtils {
    /**
     * 天行学历查询接口---成功
     */
    public static final String TXEDUCATION_SUCCESS = "1000";

    /**
     * 华征个人工商信息接口---成功
     */
    public static final String HZHBUSINESS_SUCCESS = "1000";

    /**
     * 运营商认证
     */
    public static final String YYS_TYPE = "002";

    /**
     * 学信网认证
     */
    public static final String EDUCATION_TYPE = "005";

    /**
     * 学信网认证---成功
     */
    public static final String EDUCATION_SUCCESS="105";

    /**
     * 新学信网认证---成功
     */
    public static final String EDUCATION_NEW_SUCCESS = "1000";

    /**
     * 新学信网认证 查询成功，需通过验证码继续查询
     */
    public static final String EDUCATION_NEED_QUERY_AGAIN_SUCCESS="2005";

    /**
     * 新学信网认证 查询失败
     */
    public static final String EDUCATION_QUERY_FAILED_SUCCESS="4000";

    /**
     * 新学信网认证  系统错误
     */
    public static final String EDUCATION_SYSTEM_ERROR_SUCCESS="5000";




    /**
     * 芝麻认证
     */
    public static final String ZM_TYPE = "007";

    /**
     * 银联接口---成功
     */
    public static final String UNIONPAY_SUCCESS="100";

    /**
     * 提交联系人接口---成功
     */
    public static final String SUBMIT_CONTACT_CODE_SUCCESS="100";

    /**
     * 银联接口---失败
     */
    public static final String UNIONPAY_ERROR="101";

    public static final String YYS_NODE_CODE = "code";

    public static final String YYS_NODE_MESSAGE = "message";

    public static final String YYS_NODE_DATA = "data";

    public static final String YYS_NODE_GUIDE_CODE = "guide_code";

    public static final String YYS_NODE_GUIDE_MESSAGE = "guide_message";

    public static final String YYS_NODE_TASKID = "task_id";

    public static final String YYS_NODE_IMGCODE = "auth_code";

    public static final String YYS_NODE_TRACEID = "trace_id";

    public static final String YYS_CODE_103 = "103";

    public static final String YYS_CODE_103_DESC = "系统维护中";

    public static final String YYS_CODE_104 = "104";

    public static final String YYS_CODE_105 = "105";

    //未授权订单
    public static final String ORDER_STATUS_UNAUTH="0";

    //已授权完全订单
    public static final String ORDER_STATUS_AUTH="1";

    //已支付订单
    public static final String ORDER_STATUS_HASPAY="2";

    //自动风控通过
    public static final String AUTO_RISK_REVIEW="1";

    //自动风控不通过
    public static final String AUTO_RISK_REJECT="0";

    //众安三要素验证返回code：成功
    public static final String ZHONGAN_RESULT_CODE_SUCCESS="1000";
    //众安三要素验证返回errorCode：成功
    public static final String ZHONGAN_DATA_ERRORCODE_SUCCESS="0000";
    //众安三要素验证返回errorCode：未查得
    public static final String ZHONGAN_DATA_ERRORCODE_NOT_FOUND="0001";
}
