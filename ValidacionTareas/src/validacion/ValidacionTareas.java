/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validacion;

import com.rabbitmq.client.AMQP;
import java.util.ArrayList;
import java.util.List;
import not.Notificaciones;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author Paulina Cortez Alamilla.
 */
public class ValidacionTareas {

    private List<String> tareasPendientes = new ArrayList<>();

    public ValidacionTareas() {
    }

    public void agregarTareaPendiente(String tarea, String destinatario, String remitente) throws IOException, TimeoutException {

        //P A R T E     D O S
        tareasPendientes.add(tarea);

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
        channel.basicPublish("", cola, new AMQP.BasicProperties.Builder().headers(headers).build(), tarea.getBytes("UTF-8"));
        System.out.println("Mensaje publicado en la cola: " + tarea);

        // Cerrar la conexión y el canal
        channel.close();
        connection.close();

    }

    public void tareaValidada(String tarea) {
        tareasPendientes.remove(tarea);
    }

    // Este método se llama cuando se recibe una tarea a través de la aplicación
    public void recibirTarea(String tarea,String destinatario, String remitente) throws IOException, TimeoutException {
        agregarTareaPendiente(tarea, destinatario, remitente);
    }
}
