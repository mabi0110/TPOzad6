package org.example;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Hello world!
 *
 */
public class FirstQueue
{
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
            TextMessage message = session.createTextMessage("First messageee");
            producer.send(message);
            System.out.println("Message send: " + message.getText());

            MessageConsumer consumer1 = session.createConsumer(queue);
            connection.start();
            TextMessage messageReceived = (TextMessage) consumer1.receive(3000);
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
