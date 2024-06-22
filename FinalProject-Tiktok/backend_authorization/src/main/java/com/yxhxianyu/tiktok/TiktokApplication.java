package com.yxhxianyu.tiktok;

import com.yxhxianyu.tiktok.server.ThriftServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

@ServletComponentScan
@SpringBootApplication
public class TiktokApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiktokApplication.class, args);
	}

	/**
	 * 启动Thrift服务
	 */
	@Bean(initMethod = "start")
	public ThriftServer init() {
		return new ThriftServer();
	}

}
