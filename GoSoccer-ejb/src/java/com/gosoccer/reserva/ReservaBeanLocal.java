package com.gosoccer.reserva;

import com.gosoccer.excepcion.EntidadNoExisteException;
import com.gosoccer.excepcion.EntidadYaExisteException;
import java.util.List;

import javax.ejb.Local;

@Local
public interface ReservaBeanLocal {

    Reserva obtenerReserva(Long id) throws EntidadNoExisteException;
    
    void procesarReserva(String map);

    Reserva crearReserva(String map) throws EntidadNoExisteException, EntidadYaExisteException;

    List<Reserva> obtenerReservas(Long id) throws EntidadNoExisteException;

    void editarReserva(Reserva unaReserva) throws EntidadNoExisteException;
}
