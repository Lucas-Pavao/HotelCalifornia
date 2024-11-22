package br.com.lucasPavao.hotelCalifornia.infraestructure.exception;

public class InvalidCnpjException extends RuntimeException {
    public InvalidCnpjException(String message) {
        super(message);
    }
}
