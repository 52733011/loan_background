package com.xiaochong.loan.background.utils;

import java.util.UUID;

/**
 * Created by wujiaxing on 2017/8/9.
 */
public class CreateTokenUtil {

    public static String createToken(){
        String token = "";
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        token = MD5Utils.MD5Encode(uuid);
        return token;
    }
}
