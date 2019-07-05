package com.xiaochong.loan.background.controller.base;

import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by wujiaxing on 2017/9/1.
 */
public class BaseController {

    protected <T> BaseResultVo<T> paramsNotNullReturn(BaseResultVo<T> baseResultVo,Object... params){

        if(!this.paramsNotNullReturn(params)){
            baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
            return baseResultVo;
        }
        return baseResultVo;
    }

    protected boolean paramsNotNullReturn(Object... params){
        for (Object param: params) {
            if (param instanceof String) {
                if(StringUtils.isBlank((String)param)){
                    return false;
                }
            }else{
                if(param==null){
                    return false;
                }
            }
        }
        return true;
    }


}
