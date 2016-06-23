package com.gosoccer.reserva;

import com.gosoccer.excepcion.EntidadNoExisteException;
import com.gosoccer.excepcion.EntidadYaExisteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/Queue"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class ReservaMDB implements MessageListener {

    @EJB
    ReservaBeanLocal reservaBean;

    public ReservaMDB() {
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("ENTRY MESSSSSSSSSSSSS");
        try {
            TextMessage msg = (TextMessage) message;
            String datos = msg.getText();
            System.out.println("datossssssssssss" + datos);
            reservaBean.crearReserva(datos);
        } catch (JMSException | EntidadNoExisteException | EntidadYaExisteException ex) {
            Logger.getLogger(ReservaMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
