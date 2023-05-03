/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package productor;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author jvale
 */
public class Productor {
    
    private String mensaje;

    public Productor(String mensaje) {
        this.mensaje = mensaje;
    }

    public Productor() {
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public Boolean producirMensaje(String mensaje) throws IOException, TimeoutException{
        // Crear la conexión y el canal
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            // Declarar la cola a la que se van a enviar los mensajes
            channel.queueDeclare("hello", false, false, false, null);

            // Mensaje que se va a enviar
            String message = mensaje;

            // Enviar el mensaje a la cola
            channel.basicPublish("", "hello", null, message.getBytes("UTF-8"));
            System.out.println("Enviado '" + message + "' a la cola '" + "hello" + "'");
            // Cerrar la conexión y el canal
            channel.close();
            return true;
        }catch(Exception e){

        }
        return false;
        
    }

    @Override
    public String toString() {
        return "Productor{" + "mensaje=" + mensaje + '}';
    }
    
    
    
}
