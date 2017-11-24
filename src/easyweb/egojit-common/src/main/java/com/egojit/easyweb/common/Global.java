/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.egojit.easyweb.common;


import com.egojit.easyweb.common.utils.SpringContextHolder;
import com.egojit.easyweb.common.utils.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.IOException;

/**
 * 全局配置类
 * @author ThinkGem
 * @version 2014-06-25
 */
public class Global {

	private static Environment env;
	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();


	/**
	 * 显示/隐藏
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";
	
	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	/**
	 * 上传文件基础虚拟路径
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";
	
	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		env=SpringContextHolder.getApplicationContext().getEnvironment();
		return global;
	}
	
	/**
	 * 获取配置
	 */
	public String getConfig(String key) {

		if (env == null){
			env= SpringContextHolder.getApplicationContext().getEnvironment();
		}
		String value = env.getProperty(key);
		return value;
	}
	

	
	/**
	 * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
	 */
	public  Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}
	
	/**
	 * 在修改系统用户和角色时是否同步到Activiti
	 */
	public  Boolean isSynActivitiIndetity() {
		String dm = getConfig("activiti.isSynActivitiIndetity");
		return "true".equals(dm) || "1".equals(dm);
	}
    
	/**
	 * 页面获取常量
	 */
	public  Object getConst(String field) {
		try {
			return Global.class.getField(field).get(null);
		} catch (Exception e) {
			// 异常代表无配置，这里什么也不做
		}
		return null;
	}

	/**
	 * 获取上传文件的根目录
	 * @return
	 */
	public  String getUserfilesBaseDir() {
		String dir = getConfig("userfiles.basedir");
		if (StringUtils.isBlank(dir)){
			try {
//				dir = ServletContextFactory.getServletContext().getRealPath("/");
			} catch (Exception e) {
				return "";
			}
		}
		if(!dir.endsWith("/")) {
			dir += "/";
		}
//		System.out.println("userfiles.basedir: " + dir);
		return dir;
	}
	/**
	 * 获取URL后缀
	 */
	public  String getUrlSuffix() {
		return getConfig("app.urlSuffix");
	}
    /**
     * 获取工程路径
     * @return
     */
    public  String getProjectPath(){
    	// 如果配置了工程路径，则直接返回，否则自动获取。
		String projectPath = getConfig("projectPath");
		if (StringUtils.isNotBlank(projectPath)){
			return projectPath;
		}
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null){
				while(true){
					File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
					if (f == null || f.exists()){
						break;
					}
					if (file.getParentFile() != null){
						file = file.getParentFile();
					}else{
						break;
					}
				}
				projectPath = file.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectPath;
    }
	
}
