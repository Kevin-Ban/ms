package com.stock.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan("con.stock")
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory buildSqlSessionFactory(DataSource datasource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(datasource);

        PageInterceptor interceptor = new com.github.pagehelper.PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        interceptor.setProperties(properties);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{interceptor});

        // 开启驼峰支持
        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);

        sqlSessionFactoryBean.getObject().getConfiguration().setCallSettersOnNulls(true);
        return sqlSessionFactoryBean.getObject();
    }
}
