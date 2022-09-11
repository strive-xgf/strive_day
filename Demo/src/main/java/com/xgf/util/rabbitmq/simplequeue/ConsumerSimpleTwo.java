package com.xgf.util.rabbitmq.simplequeue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.xgf.date.DateUtil;
import com.xgf.mq.rabbitmq.RabbitMqCommonUtil;
import com.xgf.mq.rabbitmq.RabbitMqConstant;

import java.util.Date;

/**
 * @author xgf
 * @create 2022-09-06 17:02
 * @description 消费者（开启连接和通道之后，不需要关闭，消费者一直等待随时可能发来的消息，拿到消息就消费 ）
 * 不公平分发【轮询分发】：的方式在消费者处理效率不同的情况下是不公平的，真正的公平应该是遵循能者多劳的前提。
 */
public class ConsumerSimpleTwo {

    public static void main(String[] args) throws Exception{

        // 创建连接
        Connection connection = RabbitMqCommonUtil.createChannelDefault();
        // 通过 Connection 创建 Channel 通道/频道
        Channel channel = connection.createChannel();

        // 为通道声明队列
        // queueDeclare(String queueName, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> args) throws IOException;
        // 参数1（String）queueName：队列名称
        // 参数2（boolean）durable：该队列是否需要持久化（ 队列的声明默认是存放到内存中的，如果rabbitmq重启会丢失，如果想重启之后还存在就要使队列持久化，保存到Erlang自带的Mnesia数据库中，当rabbitmq重启之后会读取该数据库）
        // 参数3（boolean）exclusive：该队列是否为该通道独占的（其他通道是否可以消费该队列），是否是排他的,相当于加锁（true：排他，对首次声明它的连接可见，并在连接断开时自动删除）
        // 参数4（boolean）autoDelete：该队列不再使用的时候，是否让RabbitMQ服务器自动删除掉（自动删除前提：至少有一个消息者连接到这个队列，之后所有与这个队列连接的消息都断开时，才会自动删除）
        // 参数5（Map<String, Object>）args：其它一些参数（如：x-message-ttl,之类，目前一般不用，置为null）
        //无参情况：queueDeclare()不带参数方法默认创建一个由RabbitMq命名的（amq.gen-LHQZz...）的名称，这种队列也称之为匿名队列，是排他的，自动删除的，非持久化的队列
        channel.queueDeclare(RabbitMqConstant.MqQueue.QUEUE_DEFAULT_NAME,true,false,false,null);

        // 创建队列消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        // channel消费信息
        // String basicConsume(String queueName, boolean autoAck, Consumer var3) throws IOException;
        // 参数1（String）queueName：队列的名称（从哪个队列中接收信息）
        // 参数2（boolean） autoAck： 是否自动ack，也就是是否自动确认/签收
        // ACK概念: 当一条消息从生产端发到消费端，消费端接收到消息后会马上回送一个ACK信息给broker,告诉它这条消息收到了
        // true(默认值)：自动签收 当消费者一收到消息就表示消费者收到了消息，消费者收到了消息就会立即从队列中删除
        // false 手动签收 当消费者收到消息在合适的时候来显示的进行确认，说我已经接收到了该消息了，RabbitMQ可以从队列中删除该消息
        // 参数3：接收消息的消费者
        channel.basicConsume(RabbitMqConstant.MqQueue.QUEUE_DEFAULT_NAME, true, queueingConsumer);

        // 获取消息进行消费
        System.out.println("====== ConsumerSimpleTwo 开始消费 ======");

        int count = 1;
        while(true){
            // consumer.nextDelivery()：无参构造，当这个consumer消费者没有消息消费时，线程会阻塞。
            // 因为consumer内部是blockqueue，没有数据继续去取取数据时，就会阻塞，直到有数据放进此blockqueue中。
            // consumer.nextDelivery(long timeout)：有参数，超时时间
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            // 轮询分发消费，休眠2s
            Thread.sleep(2000);
            String msg = new String(delivery.getBody());
            System.err.println("消费端 ConsumerSimpleTwo 消费信息: " + msg + "\t time = " + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_MILL) + "\t消费数: " + count++);
        }
    }
}
