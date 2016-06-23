package com.gosoccer.cancha;

import com.gosoccer.excepcion.DatoErroneoException;
import com.gosoccer.excepcion.EntidadNoExisteException;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CanchaBeanLocal {
    
    Cancha obtenerCancha(Long id) throws EntidadNoExisteException;
    
    Cancha crearCancha(Cancha unaCancha) throws DatoErroneoException;
    
    List<Cancha> obtenerCanchas();

}
