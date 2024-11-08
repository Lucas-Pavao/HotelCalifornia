package br.com.lucasPavao.hotelCalifornia.services;


import br.com.lucasPavao.hotelCalifornia.model.HotelCaliforniaModel;
import br.com.lucasPavao.hotelCalifornia.repository.HotelCaliforniaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class HotelCaliforniaService {

    private final HotelCaliforniaRepository repository;

    public HotelCaliforniaService(HotelCaliforniaRepository repository) {
        this.repository = repository;
    }

    public List<HotelCaliforniaModel> findAll() {
        return repository.findAll();
    }

    public HotelCaliforniaModel findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Hotel com ID " + id + " não encontrado."));
    }

    @Transactional
    public HotelCaliforniaModel create(HotelCaliforniaModel hotel) {
        return repository.save(hotel);
    }

    @Transactional
    public HotelCaliforniaModel update(UUID id, HotelCaliforniaModel hotel) {
        HotelCaliforniaModel existingHotel = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Hotel com ID " + id + " não encontrado."));

        existingHotel.setName(hotel.getName());
        existingHotel.setLocal(hotel.getLocal());
        existingHotel.setCapacidade(hotel.getCapacidade());
        existingHotel.setCnpj(hotel.getCnpj());

        return repository.save(existingHotel);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Hotel com ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
    }
}
