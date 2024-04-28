package org.sb.ebankingbackend.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException (String message){
        super(message);
    }
}
