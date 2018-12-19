package com.sec.ilearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

@EnableCaching
@SpringBootApplication
@EnableSwagger2
public class IlearnApplication {

	public static void main(String[] args) {
		SpringApplication.run(IlearnApplication.class, args);
	}

	@PostConstruct
	public void init(){
		System.out.println("欢迎登陆系统。。。");
	}

}

