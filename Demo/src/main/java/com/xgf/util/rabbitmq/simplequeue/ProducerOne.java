package com.xgf.util.rabbitmq.simplequeue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xgf.mq.rabbitmq.RabbitMqCommonUtil;
import com.xgf.mq.rabbitmq.RabbitMqConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xgf
 * @create 2022-09-06 16:52
 * @description 消息生产者（简单队列模式）
 *
 *  * 关于port端口
 *  *  AMQP: 5672
 *  *  HTTP: 15672
 *  *  集群: 25672
 */

@Slf4j
public class ProducerOne {

    public static void main(String[] args) throws Exception {

        // 创建连接
        Connection connection = RabbitMqCommonUtil.createChannelDefault();
        // 通过 Connection 创建 Channel 通道/频道
        Channel channel = connection.createChannel();
        // 声明队列信息 channel.queueDeclare(名称，是否持久化（true先存硬盘, 读完再删）, 是否独占本连接, 是否自动删除(false读完再删), 附加参数)
        // queueDeclare(String queueName, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> args) throws IOException;
        // 参数1（String）queueName：队列名称
        // 参数2（boolean）durable：该队列是否需要持久化（ 队列的声明默认是存放到内存中的，如果rabbitmq重启会丢失，如果想重启之后还存在就要使队列持久化，保存到Erlang自带的Mnesia数据库中，当rabbitmq重启之后会读取该数据库）
        // 参数3（boolean）exclusive：该队列是否为该通道独占的（其他通道是否可以消费该队列），是否是排他的,相当于加锁（true：排他，对首次声明它的连接可见，并在连接断开时自动删除）
        // 参数4（boolean）autoDelete：该队列不再使用的时候，是否让RabbitMQ服务器自动删除掉（自动删除前提：至少有一个消息者连接到这个队列，之后所有与这个队列连接的消息都断开时，才会自动删除）
        // 参数5（Map<String, Object>）args：其它一些参数（如：x-message-ttl,之类，目前一般不用，置为null）
        //无参情况：queueDeclare()不带参数方法默认创建一个由RabbitMq命名的（amq.gen-LHQZz...）的名称，这种队列也称之为匿名队列，是排他的，自动删除的，非持久化的队列
        channel.queueDeclare(RabbitMqConstant.MqQueue.QUEUE_DEFAULT_NAME, true, false, false, null);

        // 通过Channel发送数据
        System.out.println("====== " + log.getName() + " start generate message");
        for(int i = 1; i <= 20; i++){
            String message = "ProducerOne 消息 = " + i;

            System.out.println("====== " + log.getName() + " start generate message, id = " + i + ", messageContent = " + message);

            /**
             * 发送消息basicPublish
             * void basicPublish(String exchangeName, String routingKey, BasicProperties props, byte[] message)
             *  参数1（String） exchangeName：将消息发送到的路由器/交换机的名称,（默认AMQP default交换机） 通过routingkey进行匹配
             *  参数2（String） routingKey：路由键，在topic exchange做消息转发用（#：匹配0个或多个单词，*：匹配一个单词）
             *  参数3（BasicProperties） props：指定消息的基本属性
             *  参数4（byte[]） message：消息体，是字节数组，消息的内容
             */
            channel.basicPublish("", RabbitMqConstant.MqQueue.QUEUE_DEFAULT_NAME, null, message.getBytes());

            // 休眠1秒
            Thread.sleep(1000);
        }

        // 关闭相关的连接资源
        channel.close();
        connection.close();
    }
}

