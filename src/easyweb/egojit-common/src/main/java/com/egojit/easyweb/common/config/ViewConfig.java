package com.egojit.easyweb.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by egojit on 2017/10/25.
 */
@Component("viewConfig")
public class ViewConfig {


    @Value("${spring.thymeleaf.prefix}")
    private String remoteViewUrl;

    public String getRemoteViewUrl() {
        return remoteViewUrl;
    }

    public void setRemoteViewUrl(String remoteViewUrl) {
        this.remoteViewUrl = remoteViewUrl;
    }


//    @Value("${app.user.multiAccountLogin}")
//    private Boolean multiAccountLogin;
//
//    @Value("${app.staticFile}")
//    private Boolean staticFile;
//
//    public String getDbtype() {
//        return dbtype;
//    }
//
//    public void setDbtype(String dbtype) {
//        this.dbtype = dbtype;
//    }
//
//    public Boolean getMultiAccountLogin() {
//        return multiAccountLogin;
//    }
//
//    public void setMultiAccountLogin(Boolean multiAccountLogin) {
//        this.multiAccountLogin = multiAccountLogin;
//    }
//
//    public Boolean getStaticFile() {
//        return staticFile;
//    }
//
//    public void setStaticFile(Boolean staticFile) {
//        this.staticFile = staticFile;
//    }
}
