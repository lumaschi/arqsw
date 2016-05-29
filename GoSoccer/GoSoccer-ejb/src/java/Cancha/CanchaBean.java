package Cancha;

import java.io.IOException;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean

public class CanchaBean {
    
    @PersistenceContext
    EntityManager em;
    
     public void guardarCancha(Cancha unaCancha) throws IOException {
        em.persist(unaCancha);
    }
}

