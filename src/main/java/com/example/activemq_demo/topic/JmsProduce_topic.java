package com.example.activemq_demo.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @description: 发布主题生产者
 * @author: zheng-fx
 * @time: 2019/12/22 0022 20:25
 */
public class JmsProduce_topic {

    private static final String ACTIVEMQ_URL = "tcp://192.168.199.184:61616";
    private static final String TOPIC_NAME = "topic-demo";

    public static void main(String[] args) throws JMSException {
        //1、创建工厂连接对象，需要制定ip和端口号
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、使用连接工厂创建一个连接对象
        Connection connection = activeMQConnectionFactory.createConnection();
        //3、开启连接
        connection.start();
        //4、使用连接对象创建会话（session）对象
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
        Topic topic = session.createTopic(TOPIC_NAME);
        //6、创建消息生产者
        MessageProducer messageProducer = session.createProducer(topic);
        //7、通过使用messageProducer生产消息发送给MQ队列里
        for (int i = 0; i <6 ; i++) {
            //8.创建消息
            TextMessage textMessage = session.createTextMessage("TOPIC_NAME----" + i);
            //9.通过messageProducer发生给MQ
            messageProducer.send(textMessage);
        }
        //10.关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("*****************TOPIC_NAME消息发布到MQ完成"); 
    }

}
