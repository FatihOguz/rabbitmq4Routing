import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect {

    private static final String EXCHANGE_NAME = "direct_logs";
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private String queueName;
    private String severity;
    private String message;

    public ReceiveLogsDirect(String orange) throws IOException, TimeoutException {
        connection(orange);
    }

    public void connection(String severity) throws IOException, TimeoutException {
        this.severity = severity;
        this.factory = new ConnectionFactory();
        factory.setHost("localhost");

        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        this.queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, severity);


    }
    public void test() throws IOException {
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            this.message = new String(delivery.getBody(), "UTF-8");
            System.out.println(this.queueName + " :: severity name " + this.severity + " --> [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    public String getMessageText() {
        String name = this.queueName.substring(this.queueName.length()-6);
        return "queue name : " + name + " -> " + this.message;
    }

}
