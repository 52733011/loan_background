package com.xiaochong.loan.background.component;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaochong.loan.background.exception.OKhttpException;
import com.xiaochong.loan.background.utils.enums.BaiduCoordTypeEnum;
import com.xiaochong.loan.background.utils.CollectionUtil;
import com.xiaochong.loan.background.utils.OkHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("baiduApiComponent")
public class BaiduApiComponent {

    Logger logger = LoggerFactory.getLogger(BaiduApiComponent.class);

    @Value("${baidu.api.ak}")
    private String baiduAk;

    @Value("${baidu.api.url.place_suggestion}")
    private String baiduPlaceSuggestionUrl;

    @Value("${baidu.api.url.geocoder}")
    private String baiduGeocoderUrl;

    @Value("${baidu.api.url.geoconv}")
    private String baiduGeoconvUrl;
    
    public JSONObject placeSuggestion(String query,String region) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("query", query);
        paramsMap.put("region", region);
        paramsMap.put("output", "json");
        paramsMap.put("ak", baiduAk);
        String url = baiduPlaceSuggestionUrl;
        if(CollectionUtil.isNotBlank(paramsMap)){
        	url+="?";
        	for (String key : paramsMap.keySet()) {
            	String value = paramsMap.get(key);
            	url+=key+"="+value+"&";
    		}
        	url= url.substring(0,url.length()-1);
        }
        
        try {
			String post = OkHttpUtils.get(url);
			JSONObject json = JSON.parseObject(post);
			return json;
			
        } catch (OKhttpException e) {
        	//result.setMessage("获取报告接口出现异常");
            logger.error(e.getMessage(),e);
		}
        return null;
    }

    public JSONObject geocoder(String latitude,String longitude,BaiduCoordTypeEnum coordTypeEnum) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("location", latitude+","+longitude);
        paramsMap.put("pois", "1");
        paramsMap.put("output", "json");
        paramsMap.put("coordtype", coordTypeEnum.getType());
        paramsMap.put("ak", baiduAk);
        String url = baiduGeocoderUrl;
        if(CollectionUtil.isNotBlank(paramsMap)){
        	url+="?";
        	for (String key : paramsMap.keySet()) {
            	String value = paramsMap.get(key);
            	url+=key+"="+value+"&";
    		}
        	url= url.substring(0,url.length()-1);
        }

        try {
			String post = OkHttpUtils.get(url);
			JSONObject json = JSON.parseObject(post);
			return json;

        } catch (OKhttpException e) {
        	//result.setMessage("获取报告接口出现异常");
            logger.error(e.getMessage(),e);
		}
        return null;
    }

    public JSONObject geoconv(String latitude,String longitude) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("coords", longitude+","+latitude);
        paramsMap.put("from", "1");
        paramsMap.put("to", "5");
        //paramsMap.put("output", "json");
        paramsMap.put("ak", baiduAk);
        String url = baiduGeoconvUrl;
        if(CollectionUtil.isNotBlank(paramsMap)){
        	url+="?";
        	for (String key : paramsMap.keySet()) {
            	String value = paramsMap.get(key);
            	url+=key+"="+value+"&";
    		}
        	url= url.substring(0,url.length()-1);
        }

        try {
			String post = OkHttpUtils.get(url);
			JSONObject json = JSON.parseObject(post);
			return json;

        } catch (OKhttpException e) {
        	//result.setMessage("获取报告接口出现异常");
            logger.error(e.getMessage(),e);
		}
        return null;
    }
}