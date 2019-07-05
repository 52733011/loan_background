package com.xiaochong.loan.background.config;

import com.github.pagehelper.PageHelper;
import com.xc.logclient.logapi.SqlMonitorInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by ray.liu on 2017/4/25.
 */
@Configuration
@MapperScan(basePackages = "com.xiaochong.loan.background.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class MyBatisConf {
    @Bean("pageHelper")
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

    @Value("${mybatis.type-aliases-package}")
    private String alpackage;

    @Value("${mybatis.mapper-locations}")
    private String locations;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource,@Qualifier("pageHelper") PageHelper pageHelper) throws Exception{
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
//		fb.setDataSource(shardingDataSourceConfig.getDataSource());
        fb.setDataSource(dataSource);
        fb.setTypeAliasesPackage(alpackage);// 指定基包
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(locations));
        fb.setPlugins(new Interceptor[]{new SqlMonitorInterceptor(),pageHelper});
        return fb.getObject();
    }
}
