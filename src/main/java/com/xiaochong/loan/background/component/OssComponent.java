package com.xiaochong.loan.background.component;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.xiaochong.loan.background.exception.FileException;
import com.xiaochong.loan.background.utils.ExceptionConstansUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * Created by ray.liu on 2017/7/18.
 */
@Component("component.OssComponent")
public class OssComponent {


    private Logger logger = LoggerFactory.getLogger(OssComponent.class);

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.geturl.endpoint}")
    private String getUrlEndpoint;


    /**
     * 上传到oss服务器
     * @param fileName 文件名
     * @param file 文件对象
     * @return 出错返回"",唯一MD5数字签名
     */
    public String uploadFile(String fileName, MultipartFile file) throws FileException {
        try {
            return this.uploadFile(fileName,file.getInputStream());
        } catch (IOException e) {
            logger.info(e.getMessage(),e);
            throw new FileException(ExceptionConstansUtil.FILE_TO_OSS_ERROR);
        }
    }


    /**
     * 上传到oss服务器
     * @param fileName 文件名
     * @param inputStream 文件对象
     * @return 出错返回"",唯一MD5数字签名
     */
    public String uploadFile(String fileName, InputStream inputStream) throws FileException {
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        //创建client实例
        try {
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, fileName, inputStream);
            if (putObjectResult.getETag() == null){
                logger.info("文件上传oss失败");
                throw new FileException(ExceptionConstansUtil.FILE_TO_OSS_ERROR);
            }
            return this.getUrl(fileName);
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            throw new FileException(ExceptionConstansUtil.FILE_TO_OSS_ERROR);
        }finally {
            ossClient.shutdown();
        }
    }

    public String getUrl(String fileName) throws FileException {
        OSSClient ossClient = new OSSClient(getUrlEndpoint,accessKeyId,accessKeySecret);
        String resultUrl = null;
        try {
            //设置url过期时间为10年
            Date expiration = new Date(new Date().getTime() + (3600L * 1000 * 24 * 365 * 10));
            URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
            resultUrl = url.toString();
            logger.info("url is:{},bucketName:{}",url,bucketName);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new FileException(ExceptionConstansUtil.GET_URL_DEFEAT);
        }finally {
            ossClient.shutdown();
        }
        return resultUrl;
    }
}
