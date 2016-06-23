
package com.gosoccer.token;

import com.gosoccer.excepcion.EntidadNoExisteException;
import javax.ejb.Local;


@Local
public interface TokenBeanLocal {

    void registarToken(Token t);
    void borrarToken(Token t);
    Token obtenerToken(Long id) throws EntidadNoExisteException;
    Token obtenerTokenPorUsuario(String nombre) throws EntidadNoExisteException;
}
