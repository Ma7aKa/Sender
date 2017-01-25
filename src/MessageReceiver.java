import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;

import javax.jms.*;

public class MessageReceiver implements MessageListener
{
    private static QueueConnection connection= null;
    private static Session session = null;
    private static MessageConsumer consumer = null;

    public static void main(String[] args)
    {
        MessageReceiver receiver = new MessageReceiver();
        receiver.start();
    }

    private void start()
    {
        try
        {
            System.out.println("Connection...");
            ConnectionFactory factory = new ConnectionFactory();
            factory.setProperty(ConnectionConfiguration.imqAddressList,"mq://192.168.0.101:7677,mq://192.168.0.101:7677");
            connection = factory.createQueueConnection("admin","admin");
            connection.start();
            session = connection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("Alarm");
            consumer = session.createConsumer(queue);

            consumer.setMessageListener(this);

            Thread.sleep(100000);

        } catch (JMSException e){ System.out.println("JMS error"); } catch (InterruptedException e) {
            e.printStackTrace();
        } finally
        {
            try
            {
                connection.close();
                session.close();
                consumer.close();
                System.out.println("Connection closing...");
            } catch (JMSException e) { System.out.println("Some error with closing"); }
        }
    }

    @Override
    public void onMessage(Message message)
    {
        if (message instanceof TextMessage)
        {
            try {
                String text = ((TextMessage) message).getText();
                System.out.println(text);
            } catch (JMSException e) { System.out.println("Some error with casting"); }
        }
        else System.out.println("Another type/Not a TextMessage");
    }
}
