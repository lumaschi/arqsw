
package com.gosoccer.token;

import com.gosoccer.excepcion.EntidadNoExisteException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
@LocalBean
public class TokenBean {

    @PersistenceContext
    public EntityManager em;

    public void registarToken(Token t) {
        em.persist(t);
    }

    public void borrarToken(Token t) {
        em.remove(t);
    }

    public Token obtenerToken(Long id) throws EntidadNoExisteException {
        try {
            return em.find(Token.class, id);
        } catch (Exception e) {
            throw new EntidadNoExisteException("Entidad no existe");
        }
    }

    public Token obtenerTokenPorUsuario(Long id) throws EntidadNoExisteException {
        System.out.println("idddddddddd" + id);
        return (Token)em.createQuery("select t from Token t where t.usuario.id=:id")
                .setParameter("id", id).getSingleResult();
    }
}
