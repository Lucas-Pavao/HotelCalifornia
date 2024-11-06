package br.com.lucasPavao.hotelCalifornia.api;

import java.util.List;
import java.util.UUID;

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

    private final HotelCaliforniaRepository repository;

    @Autowired
    public HotelCaliforniaController(HotelCaliforniaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<HotelCaliforniaModel> hotelCaliforniaGetAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelCaliforniaModel> getHotelById(@PathVariable UUID id) {
        return repository.findById(id)
                .map(hotel -> ResponseEntity.ok().body(hotel))
                .orElse(ResponseEntity.notFound().build());
    }
  
    @PostMapping
    public ResponseEntity<HotelCaliforniaModel> createHotel(@RequestBody HotelCaliforniaModel hotel) {
        HotelCaliforniaModel savedHotel = repository.save(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHotel);
    }

    @PutMapping
    public ResponseEntity<HotelCaliforniaModel> updateHotel(@RequestBody HotelCaliforniaModel hotel) {
       
        if (repository.existsById(hotel.getId())) {
            HotelCaliforniaModel updatedHotel = repository.findById(hotel.getId()).get();
            updatedHotel.setName(hotel.getName());
            updatedHotel.setLocal(hotel.getLocal());
            updatedHotel.setCapacidade(hotel.getCapacidade());
            updatedHotel.setCnpj(hotel.getCnpj());
            return ResponseEntity.ok(updatedHotel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
