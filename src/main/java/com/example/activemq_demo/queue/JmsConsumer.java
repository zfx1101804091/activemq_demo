package com.example.activemq_demo.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @description: 消息消费者
 * @author: zheng-fx
 * @time: 2019/12/22 0022 16:20
 */
public class JmsConsumer {
    
    public static final String ACTIVEMQ_URL = "tcp://192.168.199.184:61616";
    public static final String QUEUE_NAME = "queue01";
    
    public static void main(String[] args) throws JMSException, IOException {
        
        //JmsConsumerTest1();
        JmsConsumerTest2();
    }

    public static void JmsConsumerTest1() throws JMSException {
        //1、创建工厂连接对象，按照给顶的URL，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、使用连接工厂创建一个连接对象
        Connection connection = activeMQConnectionFactory.createConnection();
        //3、开启连接
        connection.start();
        //4、使用连接对象创建会话（session）对象
        //两个参数，第1个叫事务，第2个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建目的地（具体是队列还是主题topic）
        Queue queue = session.createQueue(QUEUE_NAME);
        //6、创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);
        
        /*
        同步阻塞方式（receive（））
            订阅者或接收者调用MessageConsumer的receive（）方法来接收消息，
            receive方法在能够接收到消息之前（或超时之前）将一直阻塞
        */
        while (true){
            //统一消息格式，用什么格式发的就用什么格式接收
            // TextMessage textMessage = (TextMessage) messageConsumer.receive(); //一直等待
            TextMessage textMessage = (TextMessage) messageConsumer.receive(4000L);//过时不候
            if(textMessage!=null){
                System.out.println("******消费者接收到的消息 "+textMessage.getText());
            }else {
                break;
            }
        }
        messageConsumer.close();
        session.close();
        connection.close();
        
    }


    public static void JmsConsumerTest2() throws JMSException, IOException {
        //1、创建工厂连接对象，按照给顶的URL，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、使用连接工厂创建一个连接对象
        Connection connection = activeMQConnectionFactory.createConnection();
        //3、开启连接
        connection.start();
        //4、使用连接对象创建会话（session）对象
        //两个参数，第1个叫事务，第2个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建目的地（具体是队列还是主题topic）
        Queue queue = session.createQueue(QUEUE_NAME);
        //6、创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);
        
        /*
        通过监听的方式来消费消息
        */
        //7、向consumer对象中设置一个messageListener对象，用来接收消息
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //instanceof是Java中的二元运算符，左边是对象，右边是类；当对象是右边类或子类所创建对象时，返回true；否则，返回false。
                if(null!=message&&message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("******消费者接收到的消息 "+textMessage.getText());
                        //System.out.println("******消费者接收到的消息 "+textMessage.getStringProperty("c01"));
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }

               /* if(null!=message&&message instanceof MapMessage){
                    MapMessage mapMessage = (MapMessage) message;
                    try {
                        System.out.println("******消费者接收到的消息 "+mapMessage.getString("v1"));
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }*/
            }
        });
        //8、程序等待接收用户消息
        System.in.read();
        //9、关闭资源
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
