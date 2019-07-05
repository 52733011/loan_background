package com.xiaochong.loan.background.utils;

import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YYSUserInfoTypeUtil {


    private static Map<String,String> searchedOrgTypeMap = new HashMap<String, String>(){{
        put("CASH_LOAN","现金贷");
        put("P2P","P2P理财");
        put("ZHENGXIN","征信机构");
        put("CREDITPAY","信用支付");
        put("CONSUMSTAGE","消费分期");
        put("COMPENSATION","信用卡代偿");
        put("DIVERSION","导流平台");
        put("DATACOVERGE","数据聚合平台");
    }};


    public static String getSearchOrgType(List<String> typeList){
        if (CollectionUtils.isNotEmpty(typeList)){
            StringBuffer stringBuffer = new StringBuffer();
            typeList.forEach(item -> {
                String type = searchedOrgTypeMap.get(item);
                stringBuffer.append(type).append(",");
            });
            String result = stringBuffer.toString();
            return result.substring(0,result.length()-1);
        }else{
            return "-";
        }
    }


}
