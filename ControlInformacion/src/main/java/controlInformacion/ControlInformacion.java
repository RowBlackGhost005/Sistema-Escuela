package controlInformacion;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import webSockets.WSEndpoint;

public class ControlInformacion {

    public static void main(String[] args) {
        try {
            System.out.println("Enter to send a report.");
            
            WebSocketContainer container=null;//
            Session session=null;
            //Tyrus is plugged via ServiceLoader API. See notes above
            container = ContainerProvider.getWebSocketContainer();
            //WS1 is the context-root of my web.app
            //ratesrv is the  path given in the ServerEndPoint annotation on server implementation
            session=container.connectToServer(WSEndpoint.class,            
                    URI.create("ws://localhost:8080/ReportesRegistroEscolar/reportsEndpoint"));
            
            session.getBasicRemote().sendText("{\"id\":\"1\",\"maestro\":\"Juan\",\"calificaciones\":\"5/20\"}");
            
        } catch (DeploymentException ex) {
            Logger.getLogger(ControlInformacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControlInformacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
