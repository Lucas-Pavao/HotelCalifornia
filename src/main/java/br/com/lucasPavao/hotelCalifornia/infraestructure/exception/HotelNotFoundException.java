package br.com.lucasPavao.hotelCalifornia.infraestructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class HotelNotFoundException extends ResponseStatusException {
    public HotelNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND,message);
    }
}
