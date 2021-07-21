import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";
    private String severity1 = "orange";
    private String severity2 = "red";
    private String message1 = "orange ...";
    private String message2 = "red ...";
    private  ConnectionFactory factory;
    private Channel channel;



    public  EmitLogDirect(String severity1,String severity2) throws IOException, TimeoutException {


       this.severity1 = "orange";
        this.severity2 = "red";




        this.factory = new ConnectionFactory();
        this.factory.setHost("localhost");
        try{
            Connection connection = factory.newConnection();
            this.channel=connection.createChannel();
            this.channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.DIRECT);

        }catch(Exception e){
            System.out.println("Connection Problem --> " + e);
        }


    }

    public void test() throws IOException {

        this.channel.basicPublish(EXCHANGE_NAME, severity1, null, message1.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + severity1 + "':'" + message1 + "'");


        this.channel.basicPublish(EXCHANGE_NAME, severity2, null, message2.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + severity2 + "':'" + message2 + "'");
    }
    public void changeMessageOne(String str){
        this.message1 = str;
    }
    public void changeMessageSec(String str){
        this.message2 = str;
    }


}