package br.com.lucasPavao.hotelCalifornia.services;


import br.com.lucasPavao.hotelCalifornia.model.HotelCaliforniaModel;
import br.com.lucasPavao.hotelCalifornia.repository.HotelCaliforniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class HotelCaliforniaService  {


    @Autowired
    HotelCaliforniaRepository repository;

    public List<HotelCaliforniaModel> findAll(){
        return repository.findAll();
    }

    public HotelCaliforniaModel findById(UUID id){

        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Hotel com ID " + id + " não encontrado."));

    }

    public HotelCaliforniaModel create (HotelCaliforniaModel hotel){
       return repository.save(hotel);
    }

    public HotelCaliforniaModel update (UUID  id, HotelCaliforniaModel hotel){
        if (repository.getReferenceById(id) != null){
            HotelCaliforniaModel updatedHotel = repository.findById(hotel.getId()).get();
            updatedHotel.setName(hotel.getName());
            updatedHotel.setLocal(hotel.getLocal());
            updatedHotel.setCapacidade(hotel.getCapacidade());
            updatedHotel.setCnpj(hotel.getCnpj());
            repository.save(updatedHotel);
            return updatedHotel;
        }
        return hotel;
    }

    public void delete (UUID id){

            repository.deleteById(id);

    }
}
