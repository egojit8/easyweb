package com.egojit.easyweb;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;


@EnableEncryptableProperties
@SpringBootApplication
@ComponentScan("com.egojit.easyweb")
@ServletComponentScan
public class EgojitLogMicroApplication {

	public static void main(String[] args) {
		SpringApplication.run(EgojitLogMicroApplication.class, args);
	}

}
