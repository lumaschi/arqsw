package com.gosoccer.cancha;

import com.gosoccer.excepcion.DatoErroneoException;
import com.gosoccer.excepcion.EntidadNoExisteException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CanchaBean implements CanchaBeanLocal {

    @PersistenceContext
    public EntityManager em;

    @Override
    public List<Cancha> obtenerCanchas() {
        return em.createQuery("select c from Cancha c").getResultList();
    }

    @Override
    public Cancha crearCancha(Cancha unaCancha) throws DatoErroneoException {
        try {
            Cancha cancha = new Cancha();
            cancha.setDireccion(unaCancha.getDireccion());
            cancha.setPrecio(unaCancha.getPrecio());
            em.persist(cancha);
            return cancha;
        } catch (Exception e) {
            throw new DatoErroneoException("Debe igresar todos los datos.");
        }
    }

    @Override
    public Cancha obtenerCancha(Long id) throws EntidadNoExisteException {
        try {
            return em.find(Cancha.class, id);
        } catch (Exception e) {
            throw new EntidadNoExisteException("La cancha con el id: " + id + " no se encuentra.");
        }
    }

}
