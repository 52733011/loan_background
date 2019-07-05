package com.xiaochong.loan.background.utils;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wujiaxing on 2017/11/08.
 *
 */
public class LogTraceUtils {

    public static void info(String title, Object data) {
        if(data instanceof Map){
            LogTrace.info(title,JSON.toJSONString(data));
        }else{
            Map<String, Object> map = new HashMap<>();
            map.put("log",data);
            LogTrace.info(title, JSON.toJSONString(map));
        }
    }

    public static void info(String title, String key, String value) {
        LogTrace.info(title,key,value);
    }


    public static String logReplace(String log,Object... var) {
        if(StringUtils.isNotBlank(log)){
            if(var!=null && var.length>0){
                for (int i = 0; i < var.length; i++) {
                    log = log.replaceFirst("\\{\\}", var[i]!=null?var[i].toString():"");
                }
            }
        }
        return log;
    }

    public static void error(String title, Object data, Exception ex) {
        if(data instanceof Map){
            LogTrace.error(title,JSON.toJSONString(data), ex);
        }else{
            Map<String, Object> map = new HashMap<>();
            map.put("log",data);
            LogTrace.error(title, JSON.toJSONString(map), ex);
        }

    }


    public static void error(String title, Exception ex) {
        LogTrace.error(title, ex);
    }
}
