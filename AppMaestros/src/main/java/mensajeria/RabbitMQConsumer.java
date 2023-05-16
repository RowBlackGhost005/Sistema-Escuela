/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mensajeria;

import app.frmChat;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import conexionBD.ConexionMongo;
import entidad.Chat;
import entidad.Mensaje;
import implementacion.ChatDAO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bson.types.ObjectId;

/**
 *
 * @author jvale
 */
public class RabbitMQConsumer implements Runnable {

    private final static String QUEUE_NAME = "cola2";
    public static String message;

    public RabbitMQConsumer() {
        historial();
    }

    private void historial() {
        conexionBD.ConexionMongo conexion = conexionBD.ConexionMongo.getInstance();
        ChatDAO dao = new ChatDAO(conexion);
        Chat chat = dao.consultarChatPorId(new ObjectId("6462be9ec3d53e1e63442ad3"));
        List<Mensaje> mensajes = chat.getMensajes();
        for (int i = 0; i < mensajes.size(); i++) {
            frmChat.textAreaChat.append(mensajes.get(i).toString());
        }
    }

    @Override
    public void run() {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            com.rabbitmq.client.Connection connection = factory.newConnection();
            com.rabbitmq.client.Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(
                        String consumerTag,
                        Envelope envelope,
                        AMQP.BasicProperties properties,
                        byte[] body) throws IOException {

                    try {
                        // Desencriptar el mensaje
                        byte[] decryptedMessage = decrypt(body);
                        

                        try (ByteArrayInputStream bis = new ByteArrayInputStream(decryptedMessage); ObjectInputStream ois = new ObjectInputStream(bis)) {

                            Mensaje mensaje = (Mensaje) ois.readObject();
                            
                            System.out.println(mensaje.toString());

                            ConexionMongo conexion = ConexionMongo.getInstance();

                            ChatDAO dao = new ChatDAO(conexion);

                            Chat chat = dao.consultarChatPorId(new ObjectId("6462be9ec3d53e1e63442ad3"));

                            List<Mensaje> mensajes = chat.getMensajes();

                            mensajes.add(mensaje);

                            chat.setMensajes(mensajes);

                            dao.actualizarChat(chat);
                            // Realiza cualquier acción con el mensaje deserializado
                            System.out.println("Mensaje recibido: " + mensaje.toString());
                            frmChat.textAreaChat.append(mensaje.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                        throw new IOException("Error al desencriptar el mensaje", e);
                    }
                }
            };

            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] decrypt(byte[] encryptedMessage) throws IOException, GeneralSecurityException {
        try {
            // Generar una clave secreta basada en una contraseña
            String password = "12345";
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] key = md.digest(password.getBytes("UTF-8"));
            key = Arrays.copyOf(key, 16);
            SecretKey secretKey = new SecretKeySpec(key, "AES");

            // Crear un cifrador AES en modo de descifrado
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Desencriptar los bytes del mensaje
            return cipher.doFinal(encryptedMessage);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new IOException("Error al desencriptar el mensaje", e);
        }
    }
}
