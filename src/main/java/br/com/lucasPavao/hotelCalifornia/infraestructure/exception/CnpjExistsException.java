package br.com.lucasPavao.hotelCalifornia.infraestructure.exception;

public class CnpjExistsException extends RuntimeException {
    public CnpjExistsException(String message) {
        super(message);
    }
}
