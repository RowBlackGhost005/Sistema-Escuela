/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package not;

/**
 *
 * @author Paulina Cortez Alamilla.
 */
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ValidacionConsumer {
    
    private final String EXCHANGE_NAME = "notificaciones";
    private final String QUEUE_NAME = "validacion-queue";
    private final String ROUTING_KEY = "validacion";
    
    private Channel channel;
    
    public ValidacionConsumer() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
    }
    
    public void consumir() throws IOException {
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String json = new String(body, "UTF-8");
                // Aquí se podría procesar el mensaje recibido, por ejemplo, convertirlo en un objeto Tarea y validar si fue aprobada o no
                // Una vez que se procesa el mensaje, se podría enviar la notificación de que el proceso fue realizado exitosamente
                enviarNotificacion();
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
    
    private void enviarNotificacion() throws IOException {
        // Código para enviar la notificación indicando que el proceso fue realizado exitosamente
    }
    
    public void close() throws IOException, TimeoutException {
        channel.close();
    }
    
}
