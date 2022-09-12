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
     * Rabbitmq tag号（routingKey） 路由key
     */
    public static class MqTag {

        /**
         * RabbitMQ 默认路由标签
         */
        public static final String TAG_DEFAULT_NAME = "defaultTag";

        /**
         * RabbitMQ 默认 direct 路由标签
         */
        public static final String TAG_DIRECT_DEFAULT_NAME = "direct.defaultTag";

    }

    /**
     * Rabbitmq exchange 交换机
     */
    public static class MqExchange {

        /**
         * RabbitMQ 默认 direct 交换机名
         */
        public static final String EXCHANGE_DIRECT_NAME = "directExchange";

        /**
         * RabbitMQ 默认 fanout 交换机名
         */
        public static final String EXCHANGE_FANOUT_NAME = "fanoutExchange";

        /**
         * RabbitMQ 默认 topic 交换机名
         */
        public static final String EXCHANGE_TOPIC_NAME = "topicExchange";

    }

    /**
     * Rabbitmq exchange type（Exchange 定义类型，决定到底是哪些Queue符合条件，可以接收消息）
     */
    public static class MqExchangeType {

        /**
         * 所有 bind 到此 exchange 的 queue 都可以接收消息【广播】
         */
        public static final String EXCHANGE_TYPE_FANOUT = "fanout";


        /**
         * 通过 routingKey 和 exchange 决定的那个【唯一】的 queue 可以接收消息
         */
        public static final String EXCHANGE_TYPE_DIRECT = "direct";


        /**
         * 所有符合 routingKey （可以是一个表达式）的 routingKey 所 bind 的 queue 都可以接收消息
         * 表达式符号说明：# 代表一个或多个字符匹配，* 代表任何字符匹配
         * eg:
         *      “#” 表示 0 个或若干个关键字，“*” 表示一个关键字。如： “log.*” 能与 “log.warn” 匹配，无法与 “log.warn.timeout” 匹配， “log.#” 则能与上述两者都匹配。
         *      “#.log.#” 表示该队列关心所有涉及 log 的消息(一个 Routing Key 为 ”mq.log.error” 的消息会被转发到该队列)。
         */
        public static final String EXCHANGE_TYPE_TOPIC = "topic";
    }



}
