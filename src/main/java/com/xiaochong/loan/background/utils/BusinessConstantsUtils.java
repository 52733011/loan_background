package com.xiaochong.loan.background.utils;

/**
 * Created by ray.liu on 2017/6/9.
 */
public class BusinessConstantsUtils {

    public static final String SUCCESS_CODE = "0";

    public static final String SUCCESS_DESC = "操作成功";

    public static final String LOGIN_INVALID_CODE = "10005";

    public static final String LOGIN_INVALID_DESC = "登录失效";

    public static final String USERNAME_ERROR_DESC = "用户名错误";

    public static final String USERNAME_ERROR_CODE = "10006";

    public static final String PASSWORD_ERROR_DESC = "密码错误";

    public static final String PASSWORD_ERROR_CODE = "10007";

    public static final String USERNAME_PASSWORD_ERROR_CODE = "10008";

    public static final String USERNAME_PASSWORD_ERROR_DESC = "用户名密码错误";

    public static final String ACCOUNT_FAILURE_CODE = "10009";

    public static final String ACCOUNT_FAILURE_DESC = "账户失效";

    public static final String VERIFICATION_ERROR_CODE = "10011";

    public static final String VERIFICATION_ERROR_DESC = "短信验证码错误";

    public static final String VERIFICATION_SEND_ERROR_CODE = "10012";

    public static final String VERIFICATION_SEND_ERROR_DESC = "短信验证码发送失败";

    public static final String KAPTCHA_ERROR_CODE = "10013";

    public static final String KAPTCHA_ERROR_DESC = "图片验证码错误";

    public static final String MERCHANT_NAME_ERROR_CODE = "10014";

    public static final String MERCHANT_NAME_ERROR_DESC = "图片验证码错误";

    public static final String NOT_MASTER_ERROR_CODE = "10015";

    public static final String NOT_MASTER_ERROR_DESC = "该账户不是主账户";

    public static final String VALIDATE_ERROR_CODE = "10020";

    public static final String VALIDATE_ERROR_DESC = "三要素认证失败";

    public static final String AUTH_DONE_CODE = "10021";

    public static final String AUTH_DONE_DESC = "认证流程已完成";

    public static final String MERCHANT_ACCOUNT_ERROR_DESC = "10022";

    public static final String MERCHANT_ACCOUNT_ERROR_CODE = "账户次数不足";

    public static final String AUTH_REJECT_CODE = "10023";

    public static final String AUTH_REJECT_DESC = "认证流程已拒绝";

    public static final String AUTH_WAIT_CALLBACK_CODE = "10024";

    public static final String AUTH_WAIT_CALLBACK_DESC = "认证流程等待返回";

    public static final String UPDATE_FAILED_CODE = "40004";

    public static final String UPDATE_FAILED_DESC = "修改数据失败";

    public static final String INCORRECT_DATA_INPUT_CODE = "40005";

    public static final String INCORRECT_DATA_INPUT_DESC = "输入数据有误";

    public static final String MERCHANT_RECHARGE_FAILED_CODE = "40006";

    public static final String MERCHANT_RECHARGE_FAILED_DESC = "账户充值失败";

    public static final String MERCHANT_NAME_MISMATCH_CODE = "40007";

    public static final String MERCHANT_NAME_MISMATCH_DESC = "商户名与手机号不匹配";

    public static final String PHONE_ERROR_CODE = "40008";

    public static final String PHONE_ERROR_DESC = "该用户不存在";

    public static final String ACCOUNT_EXIST_ERROR_CODE = "40009";

    public static final String ACCOUNT_EXIST_ERROR_DESC = "该商户联系人手机号已经存在";

    public static final String MERCHANT_NAME_EXIST_ERROR_CODE = "40010";

    public static final String MERCHANT_NAME_EXIST_ERROR_DESC = "该商户名已存在";

    public static final String ORDER_TOKEN_ERROR_CODE = "40011";

    public static final String ORDER_TOKEN_ERROR_DESC = "此订单不存在";

    public static final String FLOW_ALREADY_PASS_ERROR_CODE = "40012";

    public static final String FLOW_ALREADY_PASS_ERROR_DESC = "此认证流程已通过";

    public static final String EDUCATION_FLOW_REPETITION_ERROR_CODE = "40013";

    public static final String EDUCATION_FLOW_REPETITION_ERROR_DESC = "学信认证重复";

    public static final String EDUCATION_ACCOUNT_ERROR_CODE = "40014";

    public static final String EDUCATION_ACCOUNT_ERROR_DESC = "学信账号与本人不符";

    public static final String PROJECT_NAME_REPETITION_CODE = "40015";

    public static final String PROJECT_NAME_REPETITION_DESC = "项目名重复";

    public static final String DATA_TEMPLATE_NAME_REPETITION_CODE = "40016";

    public static final String DATA_TEMPLATE_NAME_REPETITION_DESC = "商户数据模板名重复";

    public static final String DATA_UPLOAD_FAILED_CODE = "40017";

    public static final String DATA_UPLOAD_FAILED_DESC = "商户数据上传失败";

    public static final String LOAN_APPLICATION_AUDITED_CODE = "40018";

    public static final String LOAN_APPLICATION_AUDITED_DESC = "此放款订单已被审核";

    public static final String ORDER_NO_NOT_EXISTS_CODE = "40019";

    public static final String ORDER_NO_NOT_EXISTS_DESC = "订单号不存在";

    public static final String LIMITED_AUTHORITY_ERROR_CODE  = "40020";

    public static final String LIMITED_AUTHORITY_ERROR_DESC = "审核权限未配置，无法放款";

    public static final String ORDER_BEEN_USED_CODE  = "40021";

    public static final String ORDER_BEEN_USED_DESC = "该订单已被放款";

    public static final String MERCHANT_DATA_TEMPLATE_IS_NULL_CODE  = "40022";

    public static final String MERCHANT_DATA_TEMPLATE_IS_NULL_DESC = "该商户未配置数据模板";

    public static final String MERCHANT_RATE_TEMPLATE_IS_NULL_CODE  = "40023";

    public static final String MERCHANT_RATE_TEMPLATE_IS_NULL_DESC = "该商户未配置费率模板";

    public static final String RATE_NAME_REPEAT_CODE  = "40024";

    public static final String RATE_NAME_REPEAT_DESC = "费率模板名称重复";

    public static final String REQUEST_REPORT_DEFEAT_CODE = "40025";

    public static final String REQUEST_REPORT_DEFEAT_DESC = "组装报告url失败";

    public static final String READ_FILE_DEFEAT_CODE = "50002";

    public static final String READ_FILE_DEFEAT_DESC = "读取文件失败";

    public static final String REPORT_UPLOAD_OSS_DEFEAT_CODE = "50003";

    public static final String REPORT_UPLOAD_OSS_DEFEAT_DESC = "上传oss服务失败";

    public static final String STAGE_OUT_OF_MONTHS__CODE = "50004";

    public static final String STAGE_OUT_OF_MONTHS__DESC = "放贷月数超限，上限为240个月";

    public static final String STAGE_OUT_OF_DAYS__CODE = "50005";

    public static final String STAGE_OUT_OF_DAYS__DESC = "放贷天数数超限，上限为90天";

    public static final String REPORT_NOT_FINISH__CODE = "50006";

    public static final String REPORT_NOT_FINISH__DESC = "报告未完成，不可放款";

    public static final String SMS_NOT_ALLOCATION_CODE = "50007";

    public static final String SMS_NOT_ALLOCATION_DESC = "商戶短信模板未配置";

    public static final String PROJECT_DATA_IS_NOT_EXISTS_CODE = "50008";

    public static final String PROJECT_DATA_IS_NOT_EXISTS_DESC = "数据项目不存在";

    public static final String LACK_OF_SUBMIT_DATA_CODE = "50009";

    public static final String LACK_OF_SUBMIT_DATA_DESC = "数据项目不存在";

    public static final String REDUCTIVE_MONEY_ERROR_CODE = "50010";

    public static final String REDUCTIVE_MONEY_ERROR_DESC = "减免金额大于总金额";

    public static final String REPAYMENT_DEFEATE_CODE = "50011";

    public static final String REPAYMENT_DEFEATE_DESC = "还款失败";

    public static final String LESS_BANLANCE_CODE = "50012";

    public static final String LESS_BANLANCE_DESC = "余额不足，还款失败";

    public static final String RECHARGE_ERROR_CODE = "50013";

    public static final String RECHARGE_ERROR_DESC = "充值失败";

    public static final String FILE_FORMATE_ERROR_CODE = "50014";

    public static final String FILE_FORMATE_ERROR_DESC = "文件格式错误";

    public static final String DATA_FORMATE_ERROR_CODE = "50015";

    public static final String DATA_FORMATE_ERROR_DESC = "数据格式错误";

    public static final String REPAYMENT_ID_ERROR_CODE = "50016";

    public static final String REPAYMENT_ID_ERROR_DESC = "无此还款计划";

    public static final String DATA_TOO_LARGE_ERROR_CODE = "50017";

    public static final String DATA_TOO_LARGE_ERROR_DESC = "数据过大";

    public static final String REPAYMENTED_ERROR_CODE = "50018";

    public static final String REPAYMENTED_ERROR_DESC = "此笔已还";

}
