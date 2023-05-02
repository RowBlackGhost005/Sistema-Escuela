/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package ws;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Paulina Cortez Alamilla.
 */
@Path("generic")
public class NotificacionesResource {

    @Context
    private UriInfo context;

    private final String QUEUE_NAME = "cola_notificaciones";
    private final String EXCHANGE_NAME = "notificaciones";

    /**
     * Creates a new instance of NotificacionesResource
     */
    public NotificacionesResource() {
        
    }

    @GET
    @Path("/consumircola")
    @Produces(MediaType.TEXT_PLAIN)
    public Response consumirCola() throws IOException, InterruptedException {
        // P A R T E    D O S
        // Crear una lista para almacenar los mensajes recibidos
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

            final StringBuilder respuestaBuilder = new StringBuilder(); // variable dentro del método

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String mensajeRecibido = new String(body, "UTF-8");
                    String remitente = properties.getHeaders().get("origen").toString();
                    String destinatario = properties.getHeaders().get("destinatario").toString();

                    respuestaBuilder.append("Mensaje recibido de la cola: ");
                    respuestaBuilder.append(mensajeRecibido);
                    respuestaBuilder.append(" - Remitente: ");
                    respuestaBuilder.append(remitente);
                    respuestaBuilder.append(" - Destinatario: ");
                    respuestaBuilder.append(destinatario);
                    respuestaBuilder.append("\n");
                }
            };

            channel.basicConsume(QUEUE_NAME, true, consumer);

            Thread.sleep(100); // Tiempo de espera para recibir mensajes

            channel.close();
            connection.close();

            return Response.status(200).entity(respuestaBuilder.toString()).build();

        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
            return Response.status(500).entity("Ocurrió un error al consumir la cola").build();
        }
    }
}
