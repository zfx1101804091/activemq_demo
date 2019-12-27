package com.example.activemq_demo.Topic_Persist;

import com.sun.jndi.rmi.registry.RegistryContextFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @description:        先启动订阅，再启动消费者
 *          topic的持久化(类似于微信公众号，订阅号，高可用)
 * @author: zheng-fx
 * @time: 2019/12/27 0027 19:30
 */
public class JmsProduce_Topic_Persist {

    private static final String ACTIVEMQ_URL = "tcp://192.168.199.184:61616";
    private static final String TOPIC_NAME = "Topic_Persist";
    
    public static void main(String[] args) throws JMSException {

        //创建工厂连接对象，需要制定ip和端口号
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //使用连接工厂创建一个连接对象
        Connection connection = activeMQConnectionFactory.createConnection();
     

        //使用连接对象创建会话（session）对象
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
        Topic topic = session.createTopic(TOPIC_NAME);
        //创建消息生产者
        MessageProducer messageProducer = session.createProducer(topic);
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);//持久化topic
        
        //开启连接
        connection.start();
        
        //通过使用messageProducer生产消息发送给MQ队列里
        for (int i = 0; i <3 ; i++) {
            //8.创建消息
            TextMessage textMessage = session.createTextMessage("Topic_Persist----" + i);
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
