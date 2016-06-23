
package com.gosoccer.reserva;

import com.google.gson.Gson;
import com.gosoccer.excepcion.EntidadNoExisteException;
import com.gosoccer.cancha.Cancha;
import com.gosoccer.cancha.CanchaBeanLocal;
import com.gosoccer.MailBean;
import com.gosoccer.usuario.Usuario;
import com.gosoccer.usuario.UsuarioBeanLocal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ReservaBean implements ReservaBeanLocal {

    @PersistenceContext
    public EntityManager em;

    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/Queue")
    private Queue queue;

    @EJB
    CanchaBeanLocal canchaBean;

    @EJB
    UsuarioBeanLocal usuarioBean;

    @EJB
    MailBean mailBean;

    @Override
    public List<Reserva> obtenerReservas(Long id) throws EntidadNoExisteException {
        Usuario usr = usuarioBean.obtenerUsuario(id);
        return em.createQuery("select r from Reserva r where r.usrCreador=:usr")
                .setParameter("usr", usr)
                .getResultList();
    }

    @Override
    public void procesarReserva(String json) {
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            TextMessage msg = session.createTextMessage(json);
            System.out.println("msgGGGGGGGGG" + msg);
            MessageProducer producer = session.createProducer(queue);
            producer.send(msg);
            session.close();
            connection.close();
            System.out.println("FINNN" + producer);
        } catch (JMSException ex) {
            Logger.getLogger(ReservaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Reserva crearReserva(String json) {
        Reserva reserva = null;
        Usuario usuario = null;
        System.out.println("msg2" + json);
        try {
            Gson gson = new Gson();
            Map<String, Object> map = new HashMap<>();
            map = (Map<String, Object>) gson.fromJson(json, map.getClass());
            double doubleIdUsr = (double) map.get("usuario");
            long usrID = (new Double(doubleIdUsr)).longValue();
            usuario = usuarioBean.obtenerUsuario(usrID);
            if (usuarioBean.estaLogueado(usuario)) {
                System.out.println("usrID" + usrID);
                SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
                System.out.println("format" + format);
                String fecha = map.get("fecha").toString();
                Date date = format.parse(fecha);
                String hora = map.get("hora").toString();
                double doubleIdCancha = (double) map.get("cancha");
                long canchaID = (new Double(doubleIdCancha)).longValue();
                System.out.println("canchaID" + canchaID);
                Cancha cancha = canchaBean.obtenerCancha(canchaID);

                reserva = new Reserva();
                reserva.setCancha(cancha);
                reserva.setFecha(fecha);
                reserva.setHora(hora);
                reserva.setUsrCreador(usuario);
                if (existeReserva(reserva)) {
                    em.persist(reserva);
                    em.flush();
                    System.out.println("gosoccer.reserva.PASOOO");
                    enviarMail(usuario, "Su reserva se ha realizado con exito");
                    return reserva;
                }
            }
        } catch (EntidadNoExisteException ex) {
            enviarMail(usuario, "La cancha igresada no existe.");
        } catch (ParseException ex) {
            enviarMail(usuario, "La frcha ingresada no es correcta.");
        }
        /*catch (EntidadYaExisteException ex) {
            enviarMail(usuario, "La cancha seleccionada no esta disponible para esa fecha y hora.");
        }*/
        return reserva;
    }

    private void enviarMail(Usuario usuario, String mensaje) {
        String msgProcesando = usuario.getEmail().concat("-Reserva").concat("-Su reserva se ha realizado con exito.");
        mailBean.enviarMail(msgProcesando);
    }

    @Override
    public void editarReserva(Reserva unaReserva) throws EntidadNoExisteException {
        System.out.println("unaReserva" + unaReserva.getId());
        Reserva reserva = obtenerReserva(unaReserva.getId());
        reserva.setFecha(unaReserva.getFecha());
        reserva.setHora(unaReserva.getHora());
        em.merge(unaReserva);
    }

    @Override
    public Reserva obtenerReserva(Long id) throws EntidadNoExisteException {
        try {
            return em.find(Reserva.class, id);
        } catch (Exception e) {
            throw new EntidadNoExisteException("Entidad no existe");
        }
    }

    private boolean existeReserva(Reserva r) {
        System.out.println("EXISTE" + r.getCancha().getId() + r.getFecha() + r.getHora());
        Reserva reserva = (Reserva) em.createQuery("SELECT r FROM Reserva r WHERE r.cancha.id =:rId and r.fecha=:rFecha and r.hora=:rHora")
                .setParameter("rId", r.getCancha().getId())
                .setParameter("rFecha", r.getFecha())
                .setParameter("rHora", r.getHora())
                .getSingleResult();
        System.out.println("result" + reserva);
        if (reserva != null) {
            return false;
        } else {
            return true;
        }
    }
}
