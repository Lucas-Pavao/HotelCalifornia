package br.com.lucasPavao.hotelCalifornia.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lucasPavao.hotelCalifornia.model.HotelCaliforniaModel;
import br.com.lucasPavao.hotelCalifornia.repository.HotelCaliforniaRepository;

@RestController
@RequestMapping("/api/hotelCalifornia") 
public class HotelCaliforniaController {

    private final HotelCaliforniaRepository repository;

    @Autowired
    public HotelCaliforniaController(HotelCaliforniaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<HotelCaliforniaModel> hotelCaliforniaGetAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<HotelCaliforniaModel> createHotel(@RequestBody HotelCaliforniaModel hotel) {
        HotelCaliforniaModel savedHotel = repository.save(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHotel);
    }
}
