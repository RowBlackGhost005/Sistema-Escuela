/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mensajeria;

import app.frmChat;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import entidad.Mensaje;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author jvale
 */
public class RabbitMQConsumerBajoRendimiento implements Runnable {
    private final static String QUEUE_NAME = "cola_notificaciones";

    public RabbitMQConsumerBajoRendimiento() {
    }

    @Override
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(
                        String consumerTag,
                        Envelope envelope,
                        AMQP.BasicProperties properties,
                        byte[] body) throws IOException {

                    String tarea = new String(body, "UTF-8");
                    String destinatario = properties.getHeaders().get("destinatario").toString();
                    
                    frmChat.textAreaNotificaciones.append(tarea+"\n");
                    
                    System.out.println("Mensaje recibido: " + tarea);
                    System.out.println("Destinatario: " + destinatario);

                    // Realiza cualquier acción con la tarea y el destinatario
                    // ...

                }
            };

            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}


