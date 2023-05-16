/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidad;

import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author jvale
 */
public class Chat {
    private List<Mensaje>mensajes;
    private Date date;
    private ObjectId id;

    public Chat(List<Mensaje> mensajes, Date date, ObjectId id) {
        this.mensajes = mensajes;
        this.date = date;
        this.id = id;
    }

    public Chat(List<Mensaje> mensajes, Date date) {
        this.mensajes = mensajes;
        this.date = date;
    }

    public Chat() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    
    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Chat{" + "mensajes=" + mensajes + ", date=" + date + ", id=" + id + '}';
    }
}
