package com.yxhxianyu.tiktok.client;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

public class ThriftClientConnectPoolFactory {

    /**
     * 对象池
     */
    public GenericObjectPool pool;

    /**
     * 实例化池工厂帮助类
     */
    public ThriftClientConnectPoolFactory(GenericObjectPool.Config config, String ip, int port) {
        ConnectionFactory factory = new ConnectionFactory(ip, port);
        //实例化池对象
        this.pool = new GenericObjectPool(factory, config);
        //设置获取对象前校验对象是否可以
        this.pool.setTestOnBorrow(true);
    }

    /**
     * 在池中获取一个空闲的对象
     * 如果没有空闲且池子没满，就会调用makeObject创建一个新的对象
     * 如果满了，就会阻塞等待，直到有空闲对象或者超时
     */
    public TTSocket getConnect() throws Exception {
        return (TTSocket) pool.borrowObject();
    }

    /**
     * 将对象从池中移除
     */
    public void invalidateObject(TTSocket ttSocket) {
        try {
            pool.invalidateObject(ttSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将一个用完的对象返还给对象池
     */
    public void returnConnection(TTSocket ttSocket) {
        try {
            pool.returnObject(ttSocket);
        } catch (Exception e) {
            if (ttSocket != null) {
                try {
                    ttSocket.close();
                } catch (Exception ex) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 池里面保存的对象工厂
     */
    class ConnectionFactory extends BasePoolableObjectFactory {
        //远端地址
        private String host;

        //端口
        private Integer port;

        /**
         * 构造方法初始化地址及端口
         */
        public ConnectionFactory(String ip, int port) {
            this.host = ip;
            this.port = port;
        }

        /**
         * 创建一个对象
         */
        @Override
        public Object makeObject() throws Exception {
            // 实例化一个自定义的一个thrift 对象
            TTSocket ttSocket = new TTSocket(host, port);
            // 打开通道
            ttSocket.open();
            return ttSocket;
        }

        /**
         * 销毁对象
         */
        public void destroyObject(Object obj) {
            try {
                if (obj instanceof TTSocket) {
                    //尝试关闭连接
                    ((TTSocket) obj).close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 校验对象是否可用
         * 通过 pool.setTestOnBorrow(boolean testOnBorrow) 设置
         * 设置为true这会在调用pool.borrowObject()获取对象之前调用这个方法用于校验对象是否可用
         */
        public boolean validateObject(Object obj) {
            if (obj instanceof TTSocket) {
                TTSocket socket = ((TTSocket) obj);
                if (!socket.isOpen()) {
                    return false;
                }
                return true;
            }
            return false;
        }
    }
}
