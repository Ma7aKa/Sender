import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;


import javax.jms.*;

public class MessageSender
{
    private static QueueConnection connection= null;
    private static Session session = null;
    private static MessageProducer producer = null;
    public static void main(String[] args)
    {
        try
        {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setProperty(ConnectionConfiguration.imqAddressList,"mq://127.0.0.1:7677,mq://127.0.0.1:7677");
            connection = factory.createQueueConnection("admin","admin");
            connection.start();
            session = connection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("Alarm");
            producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage("Мах лох");
            producer.send(message);
        } catch (JMSException e){ System.out.println("JMS error"); }
        finally
        {
            try
            {
                connection.close();
                session.close();
                producer.close();
            } catch (JMSException e) { System.out.println("Some error with closing"); }

        }
    }

}
