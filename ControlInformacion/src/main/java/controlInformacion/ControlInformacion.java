package controlInformacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
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
            System.out.println("Generando reportes . . .");
            
            WebSocketContainer container=null;//
            Session session=null;
            //Tyrus is plugged via ServiceLoader API. See notes above
            container = ContainerProvider.getWebSocketContainer();
            //WS1 is the context-root of my web.app
            //ratesrv is the  path given in the ServerEndPoint annotation on server implementation
            session=container.connectToServer(WSEndpoint.class,            
                    URI.create("ws://localhost:8080/ReportesRegistroEscolar/reportsEndpoint"));
            
            //Conectando al acceso a datos moodle
            URL url = new URL("http://localhost:8080/AccesoDatosMoodle/webresources/moodleMaestros");
            HttpURLConnection connector = (HttpURLConnection) url.openConnection();
            connector.setRequestMethod("GET");
            connector.setRequestProperty("Content-Type", "application/json");
            connector.setConnectTimeout(5000);
            connector.setReadTimeout(5000);
            
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connector.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            
            connector.disconnect();
            
            session.getBasicRemote().sendText(content.toString());
            
        } catch (DeploymentException ex) {
            Logger.getLogger(ControlInformacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControlInformacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
