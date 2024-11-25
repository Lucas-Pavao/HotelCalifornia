package br.com.lucasPavao.hotelCalifornia.infraestructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HotelCaliforniaRepository extends JpaRepository<HotelCaliforniaModel, UUID>{

    @Query(value = "SELECT * FROM HOTEL_CALIFORNIA WHERE cnpj = :cnpj", nativeQuery = true)
    Optional<HotelCaliforniaModel> findByCnpj(@Param("cnpj") String cnpj);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM HOTEL_CALIFORNIA WHERE cnpj = :cnpj", nativeQuery = true)
     void deleteByCnpj(@Param("cnpj") String cnpj);

    boolean existsByCnpj(String cnpj);
}
