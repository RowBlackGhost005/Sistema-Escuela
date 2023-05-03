package not;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Paulina Cortez Alamilla.
 */
import com.rabbitmq.client.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Notificaciones {

    private final String EXCHANGE_NAME = "notificaciones";
    private final String QUEUE_NAME = "cola_notificaciones";//Cola tareas Pendientes

    public void enviarNotificacion(String tarea, String destinatario, String origen) {
        // P A R T E       D O S
        // Enviar notificación a la aplicación a través de RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            // Declarar exchange y cola
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

            // Crear headers con información adicional
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .headers(java.util.Map.of(
                            "destinatario", destinatario,
                            "origen", origen))
                    .build();

            // Publicar mensaje en el exchange
            channel.basicPublish(EXCHANGE_NAME, "", props, tarea.getBytes());
            System.out.println("Mensaje enviado: " + tarea);

        } catch (Exception e) {
            System.out.print("aquí falla");
            e.printStackTrace();
        }
    }

    public void comunicacionModulo() throws IOException, TimeoutException {
        // Crear una conexión y un canal a RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Declarar la cola desde la que se consumirán los mensajes
        String cola = "envio_modulo_notificaciones"; // reemplaza con el nombre de tu cola
        channel.queueDeclare(cola, true, false, false, null);

        // Crear un consumidor
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws UnsupportedEncodingException {
                String mensajeRecibido = new String(body, "UTF-8");
                System.out.println("Mensaje recibido de la cola: " + mensajeRecibido);

                String destinatario = properties.getHeaders().get("destinatario").toString();
                String origen = properties.getHeaders().get("origen").toString();

                // String origen = (String) properties.getHeaders().get("origen");
                enviarNotificacion(mensajeRecibido, destinatario, origen);
            }
        };

        // Registrar el consumidor en el canal
        channel.basicConsume(cola, true, consumer);

        // Mantener el consumidor activo
        while (true) {
            // El consumidor se mantiene activo indefinidamente
        }
    }

    public void consumirMensajes() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            String ColaMensaje = "validacion_regreso"; // nombre de la cola a consumir
            channel.queueDeclare(ColaMensaje, false, false, false, null);

            // Crear consumer y procesar mensajes
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                        AMQP.BasicProperties properties, byte[] body) throws UnsupportedEncodingException, IOException {
                    String mensaje = new String(body, "UTF-8");
                    System.out.println("Mensaje recibido: " + mensaje);

                    try {
                        publicarMensaje("validacion_correcta_regreso", mensaje);
                    } catch (TimeoutException ex) {
                        Logger.getLogger(Notificaciones.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            };
            channel.basicConsume(ColaMensaje, true, consumer);

        } catch (Exception e) {
            System.out.print("Error al consumir mensajes");
            e.printStackTrace();
        }
    }

    public void publicarMensaje(String cola, String mensaje) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(cola, false, false, false, null);
            channel.basicPublish("", cola, null, mensaje.getBytes("UTF-8"));
            System.out.println("Mensaje publicado en la cola '" + cola + "': " + mensaje);
        }
    }
}
