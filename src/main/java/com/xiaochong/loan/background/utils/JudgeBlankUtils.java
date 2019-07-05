package com.xiaochong.loan.background.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by jinxin on 2017/10/26.
 * 判断是否为空
 */
public class JudgeBlankUtils {

    public  static  boolean JudgeBlank(Object ...objects){
        for (Object o:objects){
            if(o==null){
                return false;
            }
            if(o instanceof String){
                if(StringUtils.isBlank(o.toString())){
                    return false;
                }
            }
        }
        return true;
    }
}
