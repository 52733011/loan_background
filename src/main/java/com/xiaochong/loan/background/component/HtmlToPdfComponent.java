package com.xiaochong.loan.background.component;

import com.xiaochong.loan.background.HandlerInterceptor.HtmlToPdfInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by jinxin on 2017/7/14.
 */
@Component
public class HtmlToPdfComponent {

    private static Logger logger = LoggerFactory.getLogger(HtmlToPdfComponent.class);

    //wkhtmltopdf在系统中的路径
    @Value("${webapp.toPdfTool}")
    private  String toPdfTool ;

    @Value("${webapp.pdfPath}")
    private String  pdfPath;

    /**
     * html转pdf
     * @param srcPath html路径，可以是硬盘上的路径，也可以是网络路径
     * @param destPath pdf保存路径
     * @return 转换成功
     */
    public  File convert(String srcPath, String destPath){
        String path = pdfPath + destPath;
        File file = new File(path);
        File parent = file.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if(!parent.exists()){
            parent.mkdirs();
        }
        StringBuilder cmd = new StringBuilder();
        cmd.append(toPdfTool);
        cmd.append(" ");
        cmd.append("--page-size A4");
        cmd.append(" ");
        cmd.append(srcPath);
        cmd.append(" ");
        cmd.append(path);
        logger.info("执行HTML转PDF的命令：{}",cmd);
        Process proc=null;
        try{
            proc = Runtime.getRuntime().exec(cmd.toString());
            logger.info("pdf下载成功");
            HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
            HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
            error.start();
            output.start();
            logger.info("pdf调用结束");
            proc.waitFor();
            proc.destroy();
            logger.info("调用结束");
        }catch(Exception e){
            file=null;
            try{
                if(proc!=null){
                    proc.getErrorStream().close();
                    proc.getInputStream().close();
                    proc.getOutputStream().close();
                }
            }
            catch(Exception ee){}
            logger.info("HTML转PDF失败：{}",e);
        }
        return file;
    }
}
