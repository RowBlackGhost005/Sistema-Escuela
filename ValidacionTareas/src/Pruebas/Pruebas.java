/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import validacion.ValidacionTareas;

/**
 *
 * @author Paulina Cortez Alamilla.
 */
public class Pruebas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, TimeoutException {
       ValidacionTareas vali = new ValidacionTareas ();
       vali.recibirTareaDesdeAPI("", "");
       
    }
    
}
