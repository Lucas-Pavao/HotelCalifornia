package br.com.lucasPavao.hotelCalifornia.api;

import java.util.List;
import java.util.UUID;

import br.com.lucasPavao.hotelCalifornia.services.HotelCaliforniaService;
import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.lucasPavao.hotelCalifornia.model.HotelCaliforniaModel;

@RestController
@RequestMapping("/api/hotelCalifornia")
public class HotelCaliforniaController {

    private final HotelCaliforniaService service;

    public HotelCaliforniaController(HotelCaliforniaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<HotelCaliforniaModel>> getAllHotels() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelCaliforniaModel> getHotelById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<HotelCaliforniaModel> createHotel(@NotNull @RequestBody HotelCaliforniaModel hotel) {
        HotelCaliforniaModel createdHotel = service.create(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelCaliforniaModel> updateHotel(
            @PathVariable UUID id,
            @NotNull @RequestBody HotelCaliforniaModel hotel) {
        return ResponseEntity.ok(service.update(id, hotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
