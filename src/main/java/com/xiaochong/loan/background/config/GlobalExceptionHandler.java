package com.xiaochong.loan.background.config;


import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理痛一返回系统错误
 * Created by ray.liu on 2017/6/22.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public BaseResultVo exceptionHandler(RuntimeException e){
        logger.info("系统错误：--------->全局异常处理:{}",e.getMessage());
        logger.error(e.getMessage(),e);
        BaseResultVo baseResultVo = new BaseResultVo();
        baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return baseResultVo;
    }
}
