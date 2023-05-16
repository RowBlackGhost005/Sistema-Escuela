/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package receptor;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author jvale
 */
public class Receptor {

    String nombre_cola;
    String mensaje;

    public Receptor(String nombre_cola, String mensaje) {
        this.nombre_cola = nombre_cola;
        this.mensaje = mensaje;
    }

    public Receptor() {
    }

    public String recibirMensaje(String nombreCola) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(nombreCola, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        final CompletableFuture<String> completableFuture = new CompletableFuture<>();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            completableFuture.complete(message);
        };

        channel.basicConsume(nombreCola, true, deliverCallback, consumerTag -> {
        });

        try {
            return completableFuture.join();
        } catch (CompletionException e) {
            throw new RuntimeException("Error al recibir mensaje de la cola " + nombreCola, e.getCause());
        }
    }

    public String getNombre_cola() {
        return nombre_cola;
    }

    public void setNombre_cola(String nombre_cola) {
        this.nombre_cola = nombre_cola;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
