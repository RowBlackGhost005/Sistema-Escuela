/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mensajeria;

import app.frmChat;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import entidad.Mensaje;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 *
 * @author jvale
 */
public class RabbitMQConsumerBajoRendimiento implements Runnable {
    private final static String QUEUE_NAME = "cola1";
    public static String message;
    
    public RabbitMQConsumerBajoRendimiento() {
    }
    


    @Override
    public void run() {
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
    com.rabbitmq.client.Connection connection = factory.newConnection();
    com.rabbitmq.client.Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, true, false, false, null);

    Consumer consumer = new DefaultConsumer(channel) {
        @Override
        public void handleDelivery(
                String consumerTag,
                Envelope envelope,
                AMQP.BasicProperties properties,
                byte[] body) throws IOException {
            
            try (ByteArrayInputStream bis = new ByteArrayInputStream(body);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {

                Mensaje mensaje = (Mensaje) ois.readObject();
                
                // Realiza cualquier acción con el mensaje deserializado
                System.out.println("Mensaje recibido: " + mensaje.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    channel.basicConsume(QUEUE_NAME, true, consumer);
} catch (Exception e) {
    e.printStackTrace();
}

    }
}
