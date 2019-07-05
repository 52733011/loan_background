package com.xiaochong.loan.background.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by lijiawei on 2017/6/9.
 */
public class JsonUtils {

    public static String getNodeValue(String jsonStr,String node){
        String value = "";
        if (StringUtils.isNotBlank(jsonStr)){
            JSONObject object = JSONObject.parseObject(jsonStr);
            value = null != object.get(node) ? object.get(node).toString():"";
        }
        return value;
    }
}
