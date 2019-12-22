package com.example.activemq_demo.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @description: 订阅主题
 *          先启动订阅，再发布（不然发送的消息是废消息），只要订阅了的消费者每个消费者收到的消息数量总数和内容都是一样的
 * @author: zheng-fx
 * @time: 2019/12/22 0022 20:40
 */
public class JmsConsumer_topic {

    private static final String ACTIVEMQ_URL = "tcp://192.168.199.184:61616";
    private static final String TOPIC_NAME = "topic-demo";
    
    public static void main(String[] args) throws JMSException, IOException {
        //1、创建工厂连接对象，需要制定ip和端口号
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、使用连接工厂创建一个连接对象
        Connection connection = connectionFactory.createConnection();
        //3、开启连接
        connection.start();
        //4、使用连接对象创建会话（session）对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、使用会话对象创建目标对象，包含queue和topic（一对一和一对多）
        Topic topic = session.createTopic(TOPIC_NAME);
        //6、使用会话对象创建生产者对象
        MessageConsumer consumer = session.createConsumer(topic);
        //7、通过监听的方式来消费消息
        consumer.setMessageListener(message -> {
            if(message instanceof TextMessage){
                TextMessage textMessage = (TextMessage)message;
                try {
                    System.out.println("消费者接收到TOPIC_NAME消息： "+textMessage.getText());
                } catch (JMSException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        //8、程序等待接收用户消息
        System.in.read();
        //9、关闭资源
        consumer.close();
        session.close();
        connection.close();

    }
}
