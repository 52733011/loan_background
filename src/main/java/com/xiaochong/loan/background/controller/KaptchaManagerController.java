package com.xiaochong.loan.background.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * Created by wujiaxing on 2017/5/9.
 */
@Api(value = "图片验证码")
@RestController
@RequestMapping("/kaptcha")
public class KaptchaManagerController {

    private Logger logger = LoggerFactory.getLogger(KaptchaManagerController.class);

    @Resource(name = "sessionComponent")
    private SessionComponent sessionComponent;

    @Autowired
    private Producer captchaProducer;

    @ApiOperation(value = "验证码获取",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time", value = "时间戳", required = true, dataType = "Integer", paramType = "form"),
    })
    @RequestMapping("/image/kaptcha.jpg")
    public ModelAndView handleRequest(HttpServletResponse response,
                                      @RequestParam("time")String time) throws Exception{
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaProducer.createText();

        // store the text in the session
        //request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        sessionComponent.setAttribute(Constants.KAPTCHA_SESSION_KEY+time,capText,300L);
        logger.info(Constants.KAPTCHA_SESSION_KEY+time+";code:"+capText);

        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);

        ServletOutputStream out = response.getOutputStream();

        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

    @ApiOperation(value = "验证码验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "code", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/check")
    public BaseResultVo loginCheck(HttpServletRequest request,
                                   @RequestParam(value = "code") String code){
        logger.info("验证码验证,参数为：{}", "{code="+code+";}");
        BaseResultVo resultVo = new BaseResultVo();
        String resultCode = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        //用户输入的验证码的值
        String kaptchaExpected = (String) request.getSession().getAttribute(
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //校验验证码是否正确
        if (code == null || !code.equals(kaptchaExpected) ) {
            resultCode = ResultConstansUtil.PARAMS_ERROR_CODE;
            message = ResultConstansUtil.PARAMS_ERROR_DESC;//返回验证码错误
        }
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        resultVo.setMessage(message);
        resultVo.setCode(resultCode);
        logger.info("验证码验证结束：{}", resultVo);
        return resultVo;
    }



}
