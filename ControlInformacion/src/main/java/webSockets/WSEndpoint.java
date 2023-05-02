package webSockets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

@ClientEndpoint
public class WSEndpoint {
    
    private Session sesion;
    
    @OnOpen
    public void open(Session s) throws IOException{
        this.sesion = s;
    }
    
    @OnMessage
    public void onMessage(String receivedMessage) {

    }
    
    public void sendMessage(){
        
        try {
            sesion.getBasicRemote().sendText("{\"id\":\"1\",\"maestro\":\"Juan\",\"calificaciones\":\"5/20\"}");
        } catch (IOException ex) {
            Logger.getLogger(WSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
