package com.example.myapplication.receptor;

import android.media.tv.TvContract;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Receptor {
    private String QUEUE_NAME = "";
    public String mensaje="";

    public Receptor(String nombre){
        this.QUEUE_NAME = nombre;
    }

    public String main() throws Exception {

        while(true) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.0.14");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                this.mensaje = message;
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
            return mensaje;
        }
    }
}
