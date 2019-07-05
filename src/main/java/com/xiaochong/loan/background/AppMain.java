package com.xiaochong.loan.background;

import com.xiaochong.loan.background.utils.SpringContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
@SpringBootApplication
@ServletComponentScan
//@MapperScan(basePackages = "com.xiaochong.loan.background.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class AppMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AppMain.class, args);
        SpringContextUtil.setApplicationContext(applicationContext);
    }
}
