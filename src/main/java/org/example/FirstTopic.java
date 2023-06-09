package org.example;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstTopic {
    public static void main( String[] args ) {

        InitialContext initialContext = null;
        Connection connection = null;

        try {
            initialContext = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = cf.createConnection();
            Session session = connection.createSession();
            Topic topic = (Topic) initialContext.lookup("topic1");
            MessageProducer producer = session.createProducer(topic);
            TextMessage message = session.createTextMessage("Topic First message");

            MessageConsumer consumer1 = session.createConsumer(topic);
            MessageConsumer consumer2 = session.createConsumer(topic);

            producer.send(message);

            System.out.println("Message send: " + message.getText());

            connection.start();

            TextMessage message1 = (TextMessage) consumer1.receive();
            System.out.println("Consumer 1 received message: " + message1.getText());

            TextMessage message2 = (TextMessage) consumer2.receive();
            System.out.println("Consumer 2 received message: " + message2.getText());


        } catch (NamingException | JMSException e) {
            throw new RuntimeException(e);
        } finally {
            if (initialContext != null) {
                try {
                    initialContext.close();
                } catch (NamingException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
