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
    private Map<String, String> tareasValidadas = new HashMap<>();

    public ValidacionTareas() {
    }
    
    public void setTareasPendientes(List<String> tareasPendientes) {
        this.tareasPendientes = tareasPendientes;
    }

    public List<String> getTareasPendientes() {
        return tareasPendientes;
    }

    public Map<String, String> getTareasValidadas() {
        return tareasValidadas;
    }

    public void setTareasValidadas(Map<String, String> tareasValidadas) {
        this.tareasValidadas = tareasValidadas;
    }

    public void agregarTareaPendiente(String tarea, String destinatario) throws IOException, TimeoutException {

        // Crear una conexión y un canal a RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Crear los headers
        Map<String, Object> headers = new HashMap<>();
        headers.put("destinatario", destinatario);

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
        if (tareasPendientes.contains(tarea)) {
            tareasPendientes.remove(tarea);
            System.out.println("La tarea '" + tarea + "' ha sido eliminada correctamente.");
        }
    }
    
    public void validarTarea(String tarea) {
        tareasPendientes.add("Reporte Investigacion");
        if (tareasPendientes.contains(tarea)) {
            tareasValidadas.put(tarea, "Maria");
            tareaValidada(tarea);
        } else {
            System.out.println("La tarea '" + tarea + "' no está pendiente para validación.");
        }
    }

    // Este método se llama cuando se recibe una tarea a través de la aplicación
    public void recibirTareaDesdeAPI() throws IOException, TimeoutException {
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

        tareasPendientes.add(tarea);
        // Llamar al método recibirTarea con el mensaje extraído del JSON
        agregarTareaPendiente(tarea, "Maria");

        System.out.println();

        // Cerrar el cliente de Jersey
        client.close();
    }

    public void consumirYValidarMensaje() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            String colaRegresoValidacion = "cola_regreso_validacion";

            // Declarar la cola de regreso de validación
            channel.queueDeclare(colaRegresoValidacion, false, false, false, null);

            // Crear consumer y procesar mensajes
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                        AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String mensaje = new String(body, "UTF-8");
                    System.out.println("Mensaje recibido desde 'cola_regreso_validacion': " + mensaje);

                    // Llamar al método validarTarea con el mensaje recibido
                    validarTarea(mensaje);
                }
            };

            // Comenzar a consumir mensajes de la cola de regreso de validación
            channel.basicConsume(colaRegresoValidacion, true, consumer);
        } catch (Exception e) {
            System.out.print("Error al consumir y validar mensajes");
            e.printStackTrace();
        }
    }

}
