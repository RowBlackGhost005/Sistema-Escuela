/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implementacion;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import conexion.ConexionMongo;
import entidades.Maestro;
import java.util.LinkedList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author jvale
 */
public class MaestroDAO {

    private ConexionMongo conexion = ConexionMongo.getInstance();
    private final MongoDatabase baseDatos;


    public MaestroDAO(ConexionMongo conexion) {
        this.conexion = conexion;
        this.baseDatos = this.conexion.crearConexion();
    }

    private MongoCollection getCollection() {
        return this.baseDatos.getCollection("Maestro", Maestro.class);
    }

    public Maestro consultar(Maestro maestro){
        MongoCollection<Maestro>coleccion = getCollection();
        List<Maestro>lista = new LinkedList<>();
        coleccion.find(new Document().append("usuario",new Document("$eq",maestro.getUsuario())).append("contraseña", new Document("$eq",maestro.getContraseña()))).into(lista);
        Maestro matro=null;
        try{
             matro = lista.get(0);
        }catch(Exception e){
        }
        
        
        return matro;
    }
}
