/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reserva;

import Usuario.Usuario;
import javax.ejb.Stateless;

/**
 *
 * @author vgalarza
 */
@Stateless
public class ReservaBean implements ReservaBeanLocal {

   
    public Usuario getUsuarioCreador(int id) {
        return null; // to be implemented
    }
    
}
