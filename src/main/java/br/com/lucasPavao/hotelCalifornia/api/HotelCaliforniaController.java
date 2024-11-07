package br.com.lucasPavao.hotelCalifornia.api;

import java.util.List;
import java.util.UUID;

import br.com.lucasPavao.hotelCalifornia.services.HotelCaliforniaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lucasPavao.hotelCalifornia.model.HotelCaliforniaModel;
import br.com.lucasPavao.hotelCalifornia.repository.HotelCaliforniaRepository;

@RestController
@RequestMapping("/api/hotelCalifornia") 
public class HotelCaliforniaController {

    @Autowired
    private  HotelCaliforniaService service;

    @GetMapping
    public List<HotelCaliforniaModel> hotelCaliforniaGetAll() {
    return service.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<HotelCaliforniaModel> getHotelById(@PathVariable(value = "id") UUID id) {
    return ResponseEntity.ok(service.findById(id));
    }
  
    @PostMapping
    public ResponseEntity<HotelCaliforniaModel> createHotel(@RequestBody HotelCaliforniaModel hotel) {
        return ResponseEntity.ok(service.create(hotel));
    }

    @PutMapping(value ="/{id}")
    public ResponseEntity<HotelCaliforniaModel> updateHotel(@PathVariable(value = "id") UUID id, @RequestBody HotelCaliforniaModel hotel) {
        return ResponseEntity.ok(service.update(id, hotel));
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> deleteHotel(@PathVariable(value = "id") UUID id) {
       service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
