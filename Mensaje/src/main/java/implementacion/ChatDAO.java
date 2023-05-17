/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implementacion;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import conexionBD.ConexionMongo;
import entidad.Chat;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author jvale
 */
public class ChatDAO {

    private ConexionMongo conexion = ConexionMongo.getInstance();
    private final MongoDatabase baseDatos;

    public ChatDAO(ConexionMongo conexion) {
        this.conexion = conexion;
        this.baseDatos = this.conexion.crearConexion();
    }

    private MongoCollection getCollection() {
        return this.baseDatos.getCollection("Chat", Chat.class);
    }

    public boolean agregarChat(Chat chat) {
        MongoCollection<Chat> coleccion = this.getCollection();
        coleccion.insertOne(chat);
        return true;
    }

    public boolean actualizarChat(Chat chat) {
        MongoCollection<Chat> coleccion = this.getCollection();
        ObjectId chatId = chat.getId();
        if (chatId != null) {
            Bson filter = Filters.eq("_id", chatId);
            Document updateDocument = new Document("$set", new Document()
                    .append("mensajes", chat.getMensajes())
                    .append("date", chat.getDate()));
            UpdateResult result = coleccion.updateOne(filter, updateDocument);
            return result.getModifiedCount() > 0;
        }
        return false;
    }

    public Chat consultarChatPorId(ObjectId chatId) {
        MongoCollection<Chat> coleccion = this.getCollection();
        Bson filter = Filters.eq("_id", chatId);
        Chat chat = coleccion.find(filter).first();
        return chat;
    }
}
