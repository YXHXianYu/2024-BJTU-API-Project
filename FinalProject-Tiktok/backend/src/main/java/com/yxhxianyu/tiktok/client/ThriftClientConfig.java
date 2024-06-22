package com.yxhxianyu.tiktok.client;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThriftClientConfig {

    //服务端的地址
    @Value("${server.thrift.host}")
    private String host;

    //服务端的端口
    @Value("${server.thrift.port}")
    private Integer port;

    @Bean
    public ThriftClientConnectPoolFactory thriftPool() {
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        //创建一个池工厂对象, 交由Spring管理
        return new ThriftClientConnectPoolFactory(config, host, port);
    }
}
