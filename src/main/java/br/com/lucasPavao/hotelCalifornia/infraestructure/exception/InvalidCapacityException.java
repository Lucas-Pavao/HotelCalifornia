package br.com.lucasPavao.hotelCalifornia.infraestructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class InvalidCapacityException extends ResponseStatusException {


    public InvalidCapacityException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
