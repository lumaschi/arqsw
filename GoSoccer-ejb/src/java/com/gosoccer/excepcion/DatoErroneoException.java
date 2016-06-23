package com.gosoccer.excepcion;

public class DatoErroneoException extends Exception{
     public DatoErroneoException() {
        super();
    }
    
    public DatoErroneoException(String message){
       super(message);
    }
    
    public DatoErroneoException(String message, Throwable cause){
        super(message, cause);
    }
    
    public DatoErroneoException(Throwable cause){
        super(cause);
    }
}
