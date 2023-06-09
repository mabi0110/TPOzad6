package org.example;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class QueueBrowserDemo {
    public static void main( String[] args ) {

        InitialContext initialContext = null;
        Connection connection = null;

        try {
            initialContext = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = cf.createConnection();
            Session session = connection.createSession();
            Queue queue = (Queue) initialContext.lookup("queue1");

            MessageProducer producer = session.createProducer(queue);
            TextMessage message1 = session.createTextMessage("First message");
            TextMessage message2 = session.createTextMessage("Second message");
            TextMessage message3 = session.createTextMessage("Third message");


            producer.send(message1);
            producer.send(message2);
            producer.send(message3);

            System.out.println("Message send: " + message1.getText());
            System.out.println("Message send: " + message2.getText());
            System.out.println("Message send: " + message3.getText());

            QueueBrowser browser = session.createBrowser(queue);
            Enumeration messagesEnum = browser.getEnumeration();

            while(messagesEnum.hasMoreElements()){
                TextMessage textMessage = (TextMessage) messagesEnum.nextElement();
                System.out.println("Browsing: " + textMessage.getText());
            }

            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();
            TextMessage messageReceived = (TextMessage) consumer.receive();
            System.out.println("Message received: " + messageReceived.getText());
            messageReceived = (TextMessage) consumer.receive();
            System.out.println("Message received: " + messageReceived.getText());
            messageReceived = (TextMessage) consumer.receive();
            System.out.println("Message received: " + messageReceived.getText());

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
