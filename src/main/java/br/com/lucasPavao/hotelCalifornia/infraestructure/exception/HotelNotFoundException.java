package br.com.lucasPavao.hotelCalifornia.infraestructure.exception;

public class HotelNotFoundException extends RuntimeException{
    public HotelNotFoundException(String message) {
        super(message);
    }
}
