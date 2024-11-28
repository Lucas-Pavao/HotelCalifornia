package br.com.lucasPavao.hotelCalifornia.infraestructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CnpjExistsException extends ResponseStatusException {
    public CnpjExistsException(String message) {
        super(HttpStatus.NOT_FOUND,message);
    }
}
