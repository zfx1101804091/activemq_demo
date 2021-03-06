package com.example.activemq_demo.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @description:    消息生产者
 *      队列的消息默认为持久化 
 * @author: zheng-fx
 * @time: 2019/12/21 0021 01:04
 */
public class JmsProduce {

    public static final String ACTIVEMQ_URL = "tcp://192.168.199.184:61616";
    public static final String QUEUE_NAME = "queue01";
    
    public static void main(String[] args) throws JMSException {
        
        //1、创建工厂连接对象，需要制定ip和端口号
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、使用连接工厂创建一个连接对象
        Connection connection = activeMQConnectionFactory.createConnection();
        //3、开启连接
        connection.start();
        //4、使用连接对象创建会话（session）对象
        //两个参数，第1个叫事务，第2个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
        //大白话：创建目的地（具体是队列还是主题topic）
        Queue queue = session.createQueue(QUEUE_NAME);

        //6、创建消息的生产者
        MessageProducer messageProducer = session.createProducer(queue);
        //messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);//消息非持久化--服务器宕机，消息不存在
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);//消息持久化--服务器宕机，消息依然存在（默认持久化）
        //7、通过使用messageProducer生产消息发送给MQ队列里
        for (int i = 0; i <6 ; i++) {
            //8.创建消息
            TextMessage textMessage = session.createTextMessage("textMessage----" + i);
            //textMessage.setStringProperty("c01","VIP");//消息属性（相当于对消息体的增强）
            //9.通过messageProducer发生给MQ
            messageProducer.send(textMessage);
            
         /*   *//*------------键值对消息--------------*//*
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("v1","mapMessage-----"+i);
            messageProducer.send(mapMessage);
            *//*------------键值对消息--------------*/
        }
        //10.关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("*****************消息发布到MQ完成");
    }
}
