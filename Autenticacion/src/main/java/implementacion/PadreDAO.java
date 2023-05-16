/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implementacion;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import conexion.ConexionMongo;
import entidades.Maestro;
import entidades.Padre;
import java.util.LinkedList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author jvale
 */
public class PadreDAO {
    private ConexionMongo conexion = ConexionMongo.getInstance();
    private final MongoDatabase baseDatos;


    public PadreDAO(ConexionMongo conexion) {
        this.conexion = conexion;
        this.baseDatos = this.conexion.crearConexion();
    }

    private MongoCollection getCollection() {
        return this.baseDatos.getCollection("Padre", Padre.class);
    }

    public Padre consultar(Padre padre){
        MongoCollection<Padre>coleccion = getCollection();
        List<Padre>lista = new LinkedList<>();
        coleccion.find(new Document().append("usuario",new Document("$eq",padre.getUsuario())).append("contraseña", new Document("$eq",padre.getContraseña()))).into(lista);
        Padre papa=null;
        try{
             papa = lista.get(0);
        }catch(Exception e){
        }
        return papa;
    }
}
