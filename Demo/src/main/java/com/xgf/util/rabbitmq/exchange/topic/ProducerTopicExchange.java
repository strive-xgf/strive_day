package com.xgf.util.rabbitmq.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xgf.mq.rabbitmq.RabbitMqCommonUtil;
import com.xgf.mq.rabbitmq.RabbitMqConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xgf
 * @create 2022-09-12 11:11
 * @description  通配符模式【Exchange type = topic通配符】（通过 routingKey 来匹配生产消费）
 */
@Slf4j
public class ProducerTopicExchange {

    public static void main(String[] args) throws Exception {

        // 创建Connection
        Connection connection = RabbitMqCommonUtil.createChannelDefault();
        //创建Channel
        Channel channel = connection.createChannel();


        // routing key
        // 使用通配符进行匹配（*、#）
        // “#” 表示 0 个或若干个关键字，“*” 表示一个关键字。如： “log.*” 能与 “log.warn” 匹配，无法与 “log.warn.timeout” 匹配， “log.#” 则能与上述两者都匹配
        // “#.log.#” 表示该队列关心所有涉及 log 的消息(一个 Routing Key 为 ”mq.log.error” 的消息会被转发到该队列)
        String routingKey1 = "topic.save";
        String routingKey2 = "topic.update.*";
        String routingKey3 = "topic.delete.#";

        System.out.println("====== " + log.getName() + " start generate message");
        System.out.println("====== routingKey1 = " + routingKey1 + "routingKey2 = " + routingKey2 + "routingKey3 = " + routingKey3);
        for(int i = 1; i <= 20; i++){

            String message = "ProducerDirectExchange 消息 = " + i;
            System.out.println("====== " + log.getName() + " start generate message, id = " + i + ", messageContent = " + message);

            /**
             * 发送消息basicPublish
             *  void basicPublish(String exchangeName, String routingKey, BasicProperties props, byte[] message)
             *  参数1（String） exchangeName：将消息发送到的路由器/交换机的名称,（默认AMQP default交换机） 通过routingkey进行匹配
             *  参数2（String） routingKey：路由键，在topic exchange做消息转发用（#：匹配0个或多个单词，*：匹配一个单词）
             *  参数3（BasicProperties） props：指定消息的基本属性
             *  参数4（byte[]） message：消息体，是字节数组，消息的内容
             */
            channel.basicPublish(RabbitMqConstant.MqExchange.EXCHANGE_TOPIC_NAME, routingKey1, null, message.getBytes());
            channel.basicPublish(RabbitMqConstant.MqExchange.EXCHANGE_TOPIC_NAME, routingKey2, null, message.getBytes());
            channel.basicPublish(RabbitMqConstant.MqExchange.EXCHANGE_TOPIC_NAME, routingKey3, null, message.getBytes());

            // 休眠1秒
            Thread.sleep(1000);
        }

        // 关闭相关的连接资源
        channel.close();
        connection.close();

    }

}
