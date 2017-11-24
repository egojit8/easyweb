package com.egojit.easyweb.common.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * Created by egojit on 2017/11/24.
 */
@Configuration
public class WebConfig {
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
                container.addErrorPages(error404Page);
                container.addErrorPages(error500Page);
            }
        };
    }
}
