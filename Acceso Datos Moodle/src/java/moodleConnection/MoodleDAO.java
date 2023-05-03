
package moodleConnection;

import java.util.ArrayList;

public class MoodleDAO {
    
    private ArrayList<String> reportesMaestros;
    
    public MoodleDAO(){
        reportesMaestros = new ArrayList<String>();
        reportesMaestros.add("{\"id\":\"1\",\"maestro\":\"Juan\",\"calificaciones\":\"5/20\"}");
        reportesMaestros.add("{\"id\":\"2\",\"maestro\":\"Armando\",\"calificaciones\":\"8/25\"}");
    }
    
    public String getReporteMaestro(int id){
        return reportesMaestros.get(id);
    }
}
