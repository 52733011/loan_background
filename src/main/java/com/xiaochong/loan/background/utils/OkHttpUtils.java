package com.xiaochong.loan.background.utils;


import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.exception.OKhttpException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ray.liu on 2017/4/13.
 */
public class OkHttpUtils {


    private static Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);


    private static final MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private static final MediaType xmlMediaType = MediaType.parse("text/xml;charset=UTF-8");

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS).build();

    private static OkHttpClient oneMinusClient = new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60,TimeUnit.SECONDS).build();

    private static OkHttpClient halfMinusClient = new OkHttpClient().newBuilder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30,TimeUnit.SECONDS).build();

    private static OkHttpClient client_post_seventy_seconds = new OkHttpClient().newBuilder()
            .connectTimeout(70, TimeUnit.SECONDS)
            .readTimeout(70, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(50, 5L, TimeUnit.MINUTES))
            .build();

    private static OkHttpClient client_post_two_minutes = new OkHttpClient().newBuilder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(50, 5L, TimeUnit.MINUTES))
            .build();

    /**
     * okhttp get请求
     * @param url 请求地址
     * @return 请求返回结果
     * @throws OKhttpException 请求异常
     */
    public static String get(String url) throws OKhttpException {

        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()){
                return response.body().string();
            }else {
                throw new OKhttpException(ExceptionConstansUtil.OKHTTP_ERROR + response);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            throw new OKhttpException(ExceptionConstansUtil.OKHTTP_ERROR,e);
        }finally {
            if (null != response){
                response.body().close();
            }
        }

    }


    /**
     * post 请求
     * @param url url
     * @param paramsMap 请求参数map
     * @return 请求结果
     * @throws OKhttpException 异常
     */
    public static String post(String url, Map<String,String> paramsMap) throws OKhttpException {
        logger.info("请求risk方法post，url【{}】，paramsMap【{}】",url,paramsMap);
        LogTraceUtils.info("httpUtils","请求risk方法post，url【"+url
                +"】，paramsMap【"+paramsMap+"】");
        Request request;
        if (!paramsMap.isEmpty()){
            FormBody.Builder builder = new FormBody.Builder();
            for (String key:paramsMap.keySet()){
                builder.add(key, paramsMap.get(key));
            }
            RequestBody requestBody = builder.build();
            request = new Request.Builder().url(url).post(requestBody).build();
        }else {
            request = new Request.Builder().url(url).build();
        }

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()){
                logger.info(String.valueOf(response.body().contentLength()));
                return response.body().string();
            }else {
                throw new OKhttpException(ExceptionConstansUtil.OKHTTP_ERROR);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            LogTrace.error("httpUtils",e.getMessage(),e);
            throw new OKhttpException(ExceptionConstansUtil.OKHTTP_ERROR,e);
        }finally {
            if (null != response){
                response.body().close();
            }
        }
    }

    /**
     * json 的POST请求
     * @return
     */
    public static String jsonPost(String url,String json){
        String result ="";
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        //json为String类型的json数据
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response=client.newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                result= response.body().string();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            LogTrace.error("httpUtils",e.getMessage(),e);
        }
        return result;
    }



    /**
     * 70秒超时时间
     * @param url
     * @param paramsMap
     * @return
     * @throws OKhttpException
     */
    public static String postSeventySeconds(String url, Map<String,String> paramsMap)throws OKhttpException {
        Request request;
        if (!paramsMap.isEmpty()){
            FormBody.Builder builder = new FormBody.Builder();
            for (String key:paramsMap.keySet()){
                builder.add(key, paramsMap.get(key));
            }
            RequestBody requestBody = builder.build();
            request = new Request.Builder().url(url).post(requestBody).build();
        }else {
            request = new Request.Builder().url(url).build();
        }

        Response response = null;
        try {
            response = client_post_seventy_seconds.newCall(request).execute();
            if (response.isSuccessful()){
                return response.body().string();
            }else {
                throw new OKhttpException(ExceptionConstansUtil.OKHTTP_ERROR);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            LogTrace.error("httpUtils",e.getMessage(),e);
            throw new OKhttpException(ExceptionConstansUtil.OKHTTP_ERROR,e);
        }finally {
            if (null != response){
                response.body().close();
            }
        }
    }



    /**
     * 120秒超时时间
     * @param url
     * @param paramsMap
     * @return
     * @throws OKhttpException
     */
    public static String postTwoMinutes(String url, Map<String,String> paramsMap)throws OKhttpException {
        logger.info("请求risk方法postTwoMinutes，url【{}】，paramsMap【{}】",url,paramsMap);
         LogTraceUtils.info("httpUtils","请求risk方法postTwoMinutes，url【"+url
                +"】，paramsMap【"+paramsMap+"】");
        Request request;
        if (!paramsMap.isEmpty()){
            FormBody.Builder builder = new FormBody.Builder();
            for (String key:paramsMap.keySet()){
                builder.add(key, paramsMap.get(key));
            }
            RequestBody requestBody = builder.build();
            request = new Request.Builder().url(url).post(requestBody).build();
        }else {
            request = new Request.Builder().url(url).build();
        }

        Response response = null;
        try {
            response = client_post_two_minutes.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new OKhttpException(ExceptionConstansUtil.OKHTTP_ERROR + response);
            }
        } catch (IOException e) {
            throw new OKhttpException(ExceptionConstansUtil.OKHTTP_ERROR,e);
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
    }




    /**
     * 30秒超时时间
     * @param url
     * @param paramsMap
     * @return
     * @throws OKhttpException
     */
    public static String halfMinutes(String url, Map<String,String> paramsMap)throws OKhttpException {
        logger.info("请求risk方法halfMinutes，url【{}】，paramsMap【{}】",url,paramsMap);
        LogTraceUtils.info("httpUtils","请求risk方法halfMinutes，url【"+url
                +"】，paramsMap【"+paramsMap+"】");
        Request request;
        if (!paramsMap.isEmpty()){
            FormBody.Builder builder = new FormBody.Builder();
            for (String key:paramsMap.keySet()){
                builder.add(key, paramsMap.get(key));
            }
            RequestBody requestBody = builder.build();
            request = new Request.Builder().url(url).post(requestBody).build();
        }else {
            request = new Request.Builder().url(url).build();
        }

        Response response = null;
        try {
            response = halfMinusClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new OKhttpException(ExceptionConstansUtil.OKHTTP_ERROR + response);
            }
        } catch (IOException e) {
            throw new OKhttpException(ExceptionConstansUtil.OKHTTP_ERROR,e);
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
    }



}
