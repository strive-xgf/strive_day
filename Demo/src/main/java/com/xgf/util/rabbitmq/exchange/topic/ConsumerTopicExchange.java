package com.xgf.util.rabbitmq.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.xgf.date.DateUtil;
import com.xgf.mq.rabbitmq.RabbitMqCommonUtil;
import com.xgf.mq.rabbitmq.RabbitMqConstant;

import java.util.Date;

/**
 * @author xgf
 * @create 2022-09-12 11:22
 * @description 路由模式（关键字模式）【Exchange type = direct】
 */
public class ConsumerTopicExchange {
    public static void main(String[] args) throws Exception {

        // 创建连接
        Connection connection = RabbitMqCommonUtil.createChannelDefault();
        // 通过Connection 创建一个 Channel 通道
        Channel channel = connection.createChannel();

        String queueName = "consumerTopicExchange";
        String exchangeName = RabbitMqConstant.MqExchange.EXCHANGE_TOPIC_NAME;
        String exchangeType = RabbitMqConstant.MqExchangeType.EXCHANGE_TYPE_TOPIC;
        // 同时匹配 topic.save、topic.update.*、topic.delete.#
        String routingKey = "topic.#";

        // 声明一个交换机
        // Exchange.DeclareOk exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete, boolean internal, Map<String,Object> arguments) throws IOException;
        // 参数1（String）exchange：交换机名称
        // 参数2（String）type：交换机类型，常见的如 fanout、direct、topic
        // 参数3（boolean）durable：设置是否持久化。durable = true 表示持久化持久化可以将将换机存盘，在服务器重启时不会丢失相关信息
        // 参数4（boolean）autoDelete：设置是否自动删除。autoDelete = true 表示自动删除。自动删除的前提是至少有一个队列或者交换机与这个交换器绑定的队列或者交换器都与之解绑
        // 参数5（boolean）internal：设置是否内置的。如果设置为true，则表示是内置的交换器，客户端程序无法直接发送消息到这个交换器中，只能通过交换器路由到交换器这种方式
        // 参数6 (Map<String,Object> arguments) argument：其他一些结构化参数，比如alternate-exchange
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        // 声明队列
        channel.queueDeclare(queueName, false, false, false, null);
        // 建立绑定关系
        // 参数1（String）queue：队列的名字
        // 参数2（String）exchange：交换器的名字
        // 参数3（String）routingKey：用于绑定的路由键
        // 参数4（Map<String, Object>）arguments：用于绑定的参数
        channel.queueBind(queueName, exchangeName, routingKey);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 参数：队列名称, 是否自动ACK, Consumer
        channel.basicConsume(queueName, true, consumer);

        System.out.println("====== ConsumerTopicExchange 开始消费 ======");
        int count = 1;
        // 循环获取消息
        while (true) {
            // consumer.nextDelivery()：无参构造，当这个consumer消费者没有消息消费时，线程会阻塞。
            // 因为 consumer 内部是 blockqueue，没有数据继续去取取数据时，就会阻塞，直到有数据放进此 blockqueue 中。
            // consumer.nextDelivery(long timeout)：有参数，超时时间
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.err.println("消费端 ConsumerTopicExchange 消费信息: " + msg + "\t time = " + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_MILL) + "\t消费数: " + count++);
        }
    }
}