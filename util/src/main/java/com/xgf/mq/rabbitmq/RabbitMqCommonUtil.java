package com.xgf.mq.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author xgf
 * @create 2022-09-08 17:47
 * @description rabbitmq 通用工具类
 **/

public class RabbitMqCommonUtil {


    public static Connection createChannelDefault() throws IOException, TimeoutException {
        // 创建 ConnectionFactory 连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置 RabbitMQ 服务主机地址（设置ip）
        connectionFactory.setHost(RabbitMqConstant.DEFAULT_HOST_IP);
        // 设置 RabbitMQ 服务端口，端口对于Java代码的AMQP来说是5672，对于http是15672，对于集群是25672
        connectionFactory.setPort(RabbitMqConstant.DEFAULT_PORT);
        // 设置虚拟主机名字
        connectionFactory.setVirtualHost(RabbitMqConstant.DEFAULT_VIRTUAL_HOST_NAME);
        // 设置超时时间
        connectionFactory.setHandshakeTimeout(20000);

        // 设置url(包括ip、端口、用户名、密码)【与用户名+密码二选一使用】
//        connectionFactory.setUri(RabbitMqConstant.DEFAULT_CONNECTION_URI);
        // 设置用户信息 (连接名)
        connectionFactory.setUsername(RabbitMqConstant.DEFAULT_USER_NAME);
        // 设置链接密码，默认guest-factory.setPassword("admin")
        connectionFactory.setPassword(RabbitMqConstant.DEFAULT_USER_PASSWORD);

        // 通过工厂创建连接
        return connectionFactory.newConnection();
    }

}
