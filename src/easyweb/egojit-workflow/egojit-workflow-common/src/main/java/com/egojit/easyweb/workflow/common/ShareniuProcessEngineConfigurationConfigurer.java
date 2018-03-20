package com.egojit.easyweb.workflow.common;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShareniuProcessEngineConfigurationConfigurer implements ProcessEngineConfigurationConfigurer {

    @Autowired
    ActivitiDatabaseConfig databaseConfig;
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        processEngineConfiguration.setJdbcDriver(databaseConfig.getDriverClassName());
        processEngineConfiguration.setJdbcUrl(databaseConfig.getUrl());
//        processEngineConfiguration.setSqlSessionFactory(sqlSessionFactory);
//        processEngineConfiguration.setDatabaseSchemaUpdate("SP2");
//        processEngineConfiguration.setDatabaseSchema("TEST");
//        processEngineConfiguration.setDatabaseSchemaUpdate("true");
        System.out.println("ShareniuProcessEngineConfigurationConfigurer#############");
        System.out.println(processEngineConfiguration.getActivityFontName());
    }
}