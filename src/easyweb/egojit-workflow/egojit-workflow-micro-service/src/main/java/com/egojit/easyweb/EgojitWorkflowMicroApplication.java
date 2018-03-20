package com.egojit.easyweb;

//import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;


//@EnableEncryptableProperties
@EnableAutoConfiguration(exclude = {
		org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
		org.activiti.spring.boot.SecurityAutoConfiguration.class
})
@SpringBootApplication
@ComponentScan("com.egojit.easyweb")
@ServletComponentScan
public class EgojitWorkflowMicroApplication {
	@Autowired
	RuntimeService runtimeService;
	public static void main(String[] args) {
		SpringApplication.run(EgojitWorkflowMicroApplication.class, args);
	}

}
