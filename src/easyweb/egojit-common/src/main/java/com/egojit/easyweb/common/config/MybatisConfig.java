package com.egojit.easyweb.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * Created by egojit on 2017/11/23.
 */
@Configuration
public class MybatisConfig {
    @Bean
    public MapperScannerConfigurer getMapperScannerConfigurer(){
        MapperScannerConfigurer configurer=new MapperScannerConfigurer();
        configurer.setBasePackage("com.egojit.easyweb.**.mapper");
        Properties properties=new Properties();
        properties.put("mappers","tk.mybatis.mapper.common.Mapper");
        configurer.setProperties(properties);
        return  configurer;
    }
}
