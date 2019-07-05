package com.xiaochong.loan.background.config;

import com.xiaochong.loan.background.HandlerInterceptor.BackgroundHandler;
import com.xiaochong.loan.background.HandlerInterceptor.ManagerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by wujiaxing on 2017/8/9.
 */
@Configuration
public class BackgroundAdapter extends WebMvcConfigurerAdapter{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BackgroundHandler()).addPathPatterns("/**").excludePathPatterns("/proxyUser/userLogin",
                "/proxyUser/sendSmsCode","/proxyUser/forgetPassword","/proxyUser/sendSmsCodeForForgetPassword",
                "/verification/**","/swagger-resources","/callback/**",
                "/v2/api-docs","/configuration/**","/kaptcha/**","/back/admin/login","/back/**","/user/bank/**",
                "/author/**", "/auth/**","/dataDispose/**","/report/**");
        registry.addInterceptor(new ManagerInterceptor()).addPathPatterns("/back/**")
                .excludePathPatterns("/back/admin/login");
        super.addInterceptors(registry);
    }
}
