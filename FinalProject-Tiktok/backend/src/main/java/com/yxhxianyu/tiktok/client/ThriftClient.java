package com.yxhxianyu.tiktok.client;

import com.yxhxianyu.tiktok.dto.AuthorizationServiceDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;


public class ThriftClient {

    @Setter
    private String host;

    @Setter
    private Integer port;

    private TTransport tTransport;

    private TProtocol tProtocol;

    private AuthorizationServiceDTO.Client client;

    private void init(){
        tTransport = new TFramedTransport(new TSocket(host, port), 600);
        //协议对象，需要和服务端保持一致
        tProtocol = new TCompactProtocol(tTransport);
        client = new AuthorizationServiceDTO.Client(tProtocol);
    }

    public AuthorizationServiceDTO.Client getClient() {
        return client;
    }

}
