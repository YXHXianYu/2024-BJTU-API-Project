package com.yxhxianyu.tiktok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class TiktokApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiktokApplication.class, args);
	}

}
