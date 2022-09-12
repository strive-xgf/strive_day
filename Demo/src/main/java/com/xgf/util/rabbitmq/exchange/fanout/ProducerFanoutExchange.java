package com.xgf.util.rabbitmq.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xgf.mq.rabbitmq.RabbitMqCommonUtil;
import com.xgf.mq.rabbitmq.RabbitMqConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xgf
 * @create 2022-09-12 11:28
 * @description 发布订阅模式，广播模式【Exchange type = fanout】
 * 不需要队列名来推送消息，通过 routingKey 匹配
 */
@Slf4j
public class ProducerFanoutExchange {

    public static void main(String[] args) throws Exception {

        // 创建Connection
        Connection connection = RabbitMqCommonUtil.createChannelDefault();
        // 创建Channel
        Channel channel = connection.createChannel();

        System.out.println("====== " + log.getName() + " start generate message");
        for(int i = 1; i <= 20; i++){

            String message = "ProducerFanoutExchange 消息 = " + i;
            System.out.println("====== " + log.getName() + " start generate message, id = " + i + ", messageContent = " + message);

            /**
             * 发送消息basicPublish
             *  void basicPublish(String exchangeName, String routingKey, BasicProperties props, byte[] message)
             *  参数1（String） exchangeName：将消息发送到的路由器/交换机的名称,（默认AMQP default交换机） 通过routingkey进行匹配
             *  参数2（String） routingKey：路由键，在topic exchange做消息转发用（#：匹配0个或多个单词，*：匹配一个单词）
             *  参数3（BasicProperties） props：指定消息的基本属性
             *  参数4（byte[]） message：消息体，是字节数组，消息的内容
             */
            channel.basicPublish(RabbitMqConstant.MqExchange.EXCHANGE_FANOUT_NAME, "", null, message.getBytes());

            // 休眠1秒
            Thread.sleep(1000);
        }

        // 关闭相关的连接资源
        channel.close();
        connection.close();

    }

}
