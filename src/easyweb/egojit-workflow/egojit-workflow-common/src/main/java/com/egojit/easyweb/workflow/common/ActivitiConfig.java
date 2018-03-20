package com.egojit.easyweb.workflow.common;

import com.alibaba.druid.pool.DruidDataSource;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Configuration
public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {



    //注入数据源和事务管理器
    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            @Qualifier("activitidatabaseSource") DataSource dataSource,
            @Qualifier("activitiTransactionManager") PlatformTransactionManager transactionManager,
            SpringAsyncExecutor springAsyncExecutor) throws IOException {
        return this.baseSpringProcessEngineConfiguration(dataSource, transactionManager, springAsyncExecutor);
    }


    @Autowired
    ActivitiDatabaseConfig databaseConfig;

    public void setEnvironment(Environment env) {
    }


    @Bean(name = "activitiTransactionManager")
    public DataSourceTransactionManager masterTransactionManager(@Qualifier("activitidatabaseSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean(name = "activitiSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("activitidatabaseSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/*.xml"));
        return sessionFactoryBean.getObject();
    }

    @Bean(name = "activitidatabaseSource")
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(databaseConfig.getUrl());
        datasource.setDriverClassName(databaseConfig.getDriverClassName());
        datasource.setUsername(databaseConfig.getUsername());
        datasource.setPassword(databaseConfig.getPassword());
        datasource.setInitialSize(databaseConfig.getInitialSize());
        datasource.setMinIdle(databaseConfig.getMinIdle());
        datasource.setMaxWait(databaseConfig.getMaxWait());
        datasource.setMaxActive(databaseConfig.getMaxActive());
        datasource.setMinEvictableIdleTimeMillis(databaseConfig.getMinEvictableIdleTimeMillis());
        try {
            datasource.setFilters("stat,wall,log4j2");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource;
    }




}

