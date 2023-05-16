/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mensajeria;

/**
 *
 * @author jvale
 */
import app.frmChat;
import entidad.Mensaje;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;

public class RabbitMQProducer {
    private static final String QUEUE_NAME = "cola1";

    private Mensaje mensaje;

    public RabbitMQProducer(Mensaje mensaje) {
        this.mensaje = mensaje;
        enviarMensaje();
    }

    public void enviarMensaje() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            frmChat.textAreaChat.append(mensaje.toString());

            // Encriptar el mensaje
            byte[] encryptedMessage = encrypt(mensaje);

            // Publicar el mensaje encriptado
            channel.basicPublish("", QUEUE_NAME, null, encryptedMessage);
            System.out.println("Mensaje enviado correctamente");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static byte[] encrypt(Mensaje mensaje) throws IOException {
        try {
            // Generar una clave secreta basada en una contraseña
            String password = "12345";
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] key = md.digest(password.getBytes("UTF-8"));
            key = Arrays.copyOf(key, 16);
            SecretKey secretKey = new SecretKeySpec(key, "AES");

            // Crear un cifrador AES en modo de cifrado
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Convertir la entidad a bytes
            byte[] messageBytes = serialize(mensaje);

            // Encriptar los bytes del mensaje
            return cipher.doFinal(messageBytes);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new IOException("Error al encriptar el mensaje", e);
        }
    }

    private static byte[] serialize(Serializable object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(object);
            return bos.toByteArray();
        }
    }
}

