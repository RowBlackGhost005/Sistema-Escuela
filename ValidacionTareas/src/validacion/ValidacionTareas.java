/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validacion;

import com.rabbitmq.client.AMQP;
import java.util.ArrayList;
import java.util.List;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 *
 * @author Paulina Cortez Alamilla.
 */
public class ValidacionTareas {

    private List<String> tareasPendientes = new ArrayList<>();

    public ValidacionTareas() {
    }
    
    public void setTareasPendientes(List<String> tareasPendientes) {
        this.tareasPendientes = tareasPendientes;
    }

    public List<String> getTareasPendientes() {
        return tareasPendientes;
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
        List<String> tareasPendientes = getTareasPendientes();
        tareasPendientes.removeIf(t -> t.equals(tarea));
        setTareasPendientes(tareasPendientes);
        System.out.println("La tarea '" + tarea + "' ha sido eliminada correctamente.");
    }

    // Este método se llama cuando se recibe una tarea a través de la aplicación
    public void recibirTareaDesdeAPI(String destinatario, String remitente) throws IOException, TimeoutException {
        // Construir el cliente de Jersey
        Client client = ClientBuilder.newClient();

        // Hacer una solicitud HTTP GET a la API
        Response response = client.target("http://localhost:8080/AccesoDatosMoodle/webresources/MoodleTareas")
                .request(MediaType.APPLICATION_JSON)
                .get();

        // Extraer el cuerpo de la respuesta como una cadena JSON
        String responseBody = response.readEntity(String.class);

        // Extraer el mensaje del JSON
        JSONObject json = new JSONObject(responseBody);
        String tarea = json.getString("titulo");

        // Llamar al método recibirTarea con el mensaje extraído del JSON
        agregarTareaPendiente(tarea, "Maria", "Juan");

        System.out.println();

        // Cerrar el cliente de Jersey
        client.close();
    }

    // Se cosume la cola que regresa las tareas que ya están validadas. 
    public void consumirMensajes() {
        String nombreCola = "validacion_correcta_regreso";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(nombreCola, false, false, false, null); // se declara la cola
            channel.basicQos(1); // se procesa un solo mensaje a la vez

            System.out.println("Esperando mensajes...");

            // Crear un objeto Consumer para recibir y procesar los mensajes
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                        AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String mensaje = new String(body, "UTF-8");
                    System.out.println("Mensaje recibido: " + mensaje);

                    tareaValidada(mensaje);
                    // confirmar recepción del mensaje para que sea eliminado de la cola
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };

            // Iniciar el consumo de mensajes de la cola
            channel.basicConsume(nombreCola, false, consumer);

            // Esperar a que se cierre la conexión
            while (connection.isOpen()) {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            // Manejar excepción

        }
    }

}
