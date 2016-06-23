package com.gosoccer.reserva;

public class CanchaInhabilitadaException extends Exception {
      
    public CanchaInhabilitadaException() {
        super();
    }
    
    public CanchaInhabilitadaException(String message){
       super(message);
    }
    
    public CanchaInhabilitadaException(String message, Throwable cause){
        super(message, cause);
    }
    
    public CanchaInhabilitadaException(Throwable cause){
        super(cause);
    }
}
