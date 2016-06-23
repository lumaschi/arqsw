/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Usuario;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vgalarza
 */
@Stateless
public class UsuarioBean implements UsuarioBeanLocal {
    
    @PersistenceContext
    public EntityManager em;
 
    public void crearUsuario(String nombre, String email, String contrasena) {
        System.out.println("entry" + nombre);
       //Gson gson = new GsonBuilder().create();
        Usuario newUsuario = new Usuario(nombre, email, contrasena, true);    
        em.persist(newUsuario);
        System.out.println("pass");   
    }
    
   /* public Usuario obtenerUsuario(int id) {
        return null;    
//   return em.find(entityClass, id)
    }*/
}
