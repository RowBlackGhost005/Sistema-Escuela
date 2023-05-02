package webSockets;

import dataAccess.Reports;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/reportsEndpoint")
public class WSReports {

    @OnOpen
    public void onOpen(Session sesion){
        
    }
    
    @OnMessage
    public void onMessage(String received, Session sesion){
        Reports reports = new Reports();
        
        reports.addReport("{\"id\":\"1\",\"maestro\":\"Juan\",\"calificaciones\":\"5/20\"}");
    }
    
}
