/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Usuario;

import javax.ejb.Local;

/**
 *
 * @author vgalarza
 */
@Local
public interface UsuarioBeanLocal {

      void crearUsuario(String nombre, String email, String contrasena);
}
