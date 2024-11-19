package br.com.lucasPavao.hotelCalifornia.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.lucasPavao.hotelCalifornia.dtos.HotelCaliforniaDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import br.com.lucasPavao.hotelCalifornia.model.HotelCaliforniaModel;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HotelCaliforniaRepository extends JpaRepository<HotelCaliforniaModel, UUID>{

    @Query(value = "SELECT * FROM HOTEL_CALIFORNIA WHERE cnpj = :cnpj", nativeQuery = true)
    public Optional<HotelCaliforniaModel> findByCnpj(@Param("cnpj") String cnpj);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM HOTEL_CALIFORNIA WHERE cnpj = :cnpj", nativeQuery = true)
    public void deleteByCnpj(@Param("cnpj") String cnpj);
}
