package com.egojit.easyweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.egojit.easyweb")
@ServletComponentScan
public class EgojitUpmsWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(EgojitUpmsWebApplication.class, args);
	}
}
