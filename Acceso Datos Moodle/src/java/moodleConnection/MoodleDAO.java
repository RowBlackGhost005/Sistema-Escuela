
package moodleConnection;

import java.util.ArrayList;

public class MoodleDAO {
    
    private ArrayList<String> reportesMaestros;
    private ArrayList<String> calificacionesAlumnos;
    private ArrayList<String> tareasMoodle;
    
    public MoodleDAO(){
        reportesMaestros = new ArrayList<String>();
        reportesMaestros.add("{\"id\":\"1\",\"maestro\":\"Juan\",\"calificaciones\":\"5/20\"}");
        reportesMaestros.add("{\"id\":\"2\",\"maestro\":\"Armando\",\"calificaciones\":\"8/25\"}");
        
        calificacionesAlumnos = new ArrayList<String>();
        calificacionesAlumnos.add("{\"id\":\"1\",\"alumno\":\"Andres Obrador\",\"calificaciones\":[\"5\",\"10\",\"8\"]}");
        calificacionesAlumnos.add("{\"id\":\"2\",\"alumno\":\"Luis Perez\",\"calificaciones\":[\"5\",\"10\",\"8\"]}");
        
        tareasMoodle = new ArrayList<String>();
        tareasMoodle.add("{\"id\":\"1\",\"titulo\":\"Reporte Investigacion\",\"id-grupo\":\"ES-624\",\"fecha-entrega\":\"15/05/23\"}");
        tareasMoodle.add("{\"id\":\"1\",\"titulo\":\"Polimorfismo\",\"id-grupo\":\"PO-101\",\"fecha-entrega\":\"12/05/23\"}");
    }
    
    public String getReporteMaestro(int id){
        return reportesMaestros.get(id);
    }
    
    public String getCalificacionesAlumno(int id){
        return calificacionesAlumnos.get(id);
    }
    
    public String getDetallesTarea(int id){
        return tareasMoodle.get(id);
    }
}
