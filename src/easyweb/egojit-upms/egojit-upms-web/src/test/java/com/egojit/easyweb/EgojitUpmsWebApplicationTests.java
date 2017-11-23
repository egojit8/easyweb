package com.egojit.easyweb;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EgojitUpmsWebApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	StringEncryptor stringEncryptor;//密码解码器自动注入

	@Test
	public void test() {
		System.out.println(stringEncryptor.encrypt("egojit123"));
	}

}
