package com.example.activemq_demo.Topic_Persist;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @description: 订阅主题
 *          先启动订阅，再发布（不然发送的消息是废消息），只要订阅了的消费者每个消费者收到的消息数量总数和内容都是一样的
 * @author: zheng-fx
 * @time: 2019/12/22 0022 20:40
 */
public class JmsConsumer_Topic_Persist {

    private static final String ACTIVEMQ_URL = "tcp://192.168.199.184:61616";
    private static final String TOPIC_NAME = "Topic_Persist";
    
    public static void main(String[] args) throws JMSException, IOException {

        System.out.println("z5");
        
        //1、创建工厂连接对象，需要制定ip和端口号
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、使用连接工厂创建一个连接对象
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.setClientID("z5");//谁订阅了
        
        //3、使用连接对象创建会话（session）对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、创建目的地
        Topic topic = session.createTopic(TOPIC_NAME);
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "remark.......");//持久化订阅者
        
        //5、发布订阅
        connection.start();
        
        //
        Message message = topicSubscriber.receive();
        while (null!=message){

            TextMessage textMessage = (TextMessage) message;
            System.out.println("收到的持久化topic。。。。"+textMessage.getText());
//            message=topicSubscriber.receive();
            message=topicSubscriber.receive(5000L);
        }


        session.close();
        connection.close();

    }
}
