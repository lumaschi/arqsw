
package Usuario;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
@LocalBean
public class UsuarioBean {
@PersistenceContext
EntityManager em;


public void guardarUsuario(Usuario usr){
        em.persist(usr);
}
}
