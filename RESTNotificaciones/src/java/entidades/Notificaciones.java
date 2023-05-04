/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author Paulina Cortez Alamilla.
 */
public class Notificaciones {
    private String titulo;
    private String mensaje;
    private String destinatario;

    public Notificaciones() {
        
    }

    public Notificaciones(String titulo, String mensaje, String destinatario) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.destinatario = destinatario;
    }

    public void enviarNotificacion(String notificacion, String destinatario, String remitente) throws IOException, TimeoutException{
         // Crear una conexión y un canal a RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Crear los headers
        Map<String, Object> headers = new HashMap<>();
        headers.put("destinatario", destinatario);
        headers.put("origen", remitente);

        // Crear la cola si no existe
        String cola = "envio_modulo_notificaciones";
        boolean durable = true;
        boolean exclusive = false;
        boolean autoDelete = false;
        channel.queueDeclare(cola, durable, exclusive, autoDelete, null);

        // Publicar el mensaje en la cola
        channel.basicPublish("", cola, new AMQP.BasicProperties.Builder().headers(headers).build(), notificacion.getBytes("UTF-8"));
        System.out.println("Mensaje publicado en la cola: " + notificacion);

        // Cerrar la conexión y el canal
        channel.close();
        connection.close();
    }
}