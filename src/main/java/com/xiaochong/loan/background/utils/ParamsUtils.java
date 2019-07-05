package com.xiaochong.loan.background.utils;

import com.xiaochong.loan.background.exception.DataDisposeException;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by ray.liu on 2017/4/13.
 */
public class ParamsUtils {

    /**
     * 把map参数转换成url可用的字符串参数
     * @param paramsMap
     * @return
     * @throws DataDisposeException
     */
    public  static String mapToString(Map<String, String> paramsMap) throws DataDisposeException {
        StringBuilder urlBody = new StringBuilder();
        String result = null;
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            if (entry.getValue() != null && !entry.getKey().equals("class")){
                try {
                    urlBody.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    throw new DataDisposeException("参数转换异常",e);
                }
            }
        }
        if (StringUtils.isNotBlank(urlBody)){
            result = String.valueOf(urlBody.deleteCharAt(urlBody.length()-1));
        }
        return result;
    }
}
