package com.xgf.mq.rabbitmq;

/**
 * @author xgf
 * @create 2022-09-08 10:56
 * @description rabbit mq 常量
 **/

public class RabbitMqConstant {

    /**
     * RabbitMQ 服务主机默认 IP 地址
     */
    public static final String DEFAULT_HOST_IP = "localhost";

    /**
     * 设置 RabbitMQ 服务默认端口，端口对于 Java 代码的 AMQP 来说是5672，对于 http 是15672，对于集群是 25672
     */
    public static final Integer DEFAULT_PORT = 5672;

    /**
     * 设置默认虚拟主机名
     */
    public static final String DEFAULT_VIRTUAL_HOST_NAME = "/";

    /**
     * 设置默认虚拟主机名 (包括ip、端口、用户名、密码)
     */
    public static final String DEFAULT_CONNECTION_URI = "amqp://guest:guest@localhost:5672";

    /**
     * 设置默认登录用户名
     */
    public static final String DEFAULT_USER_NAME = "guest";

    /**
     * 设置默认登录用密码
     */
    public static final String DEFAULT_USER_PASSWORD = "guest";

    /**
     * Rabbitmq 队列信息
     */
    public static class MqQueue {

        /**
         * RabbitMQ 默认队列名
         */
        public static final String QUEUE_DEFAULT_NAME = "defaultQueue";

    }

    /**
     * Rabbitmq tag号
     */
    public static class MqTag {

    }

    /**
     * Rabbitmq exchange 交换机
     */
    public static class MqExchange {

    }



}
