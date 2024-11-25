package br.com.lucasPavao.hotelCalifornia.infraestructure.exception;

import br.com.lucasPavao.hotelCalifornia.api.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public class HotelNotFoundException extends RuntimeException {
        public HotelNotFoundException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse response = new ErrorResponse(
                "Bad Request",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        ErrorResponse response = new ErrorResponse(
                "Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHotelNotFoundException(HotelNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                "Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidCnpjException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCnpjException(InvalidCnpjException ex) {
        ErrorResponse response = new ErrorResponse(
                "Bad Request",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CnpjExistsException.class)
    public ResponseEntity<ErrorResponse> handleCnpjExistsException(CnpjExistsException ex) {
        ErrorResponse response = new ErrorResponse(
                "Bad Request",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
