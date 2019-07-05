package com.xiaochong.loan.background.fileter;

import com.xc.logclient.filter.InternalApiTraceFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

@WebFilter(filterName="apitrace",urlPatterns="/*",
initParams={
        @WebInitParam(name="exclusions",value="*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico")//忽略资源
}
)
public class APITraceFilter extends InternalApiTraceFilter {


}
