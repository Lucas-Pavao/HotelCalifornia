package br.com.lucasPavao.hotelCalifornia.infraestructure.repository;

import br.com.lucasPavao.hotelCalifornia.infraestructure.model.ClienteModel;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, UUID>{
    @Query(value = "SELECT * FROM CLIENTE_HOTEL WHERE cpf = :cpf", nativeQuery = true)
    Optional<ClienteModel> findByCpf(@Param("cpf") String cpf);

}
