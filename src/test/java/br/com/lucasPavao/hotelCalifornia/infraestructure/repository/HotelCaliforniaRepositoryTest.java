package br.com.lucasPavao.hotelCalifornia.infraestructure.repository;

import br.com.lucasPavao.hotelCalifornia.api.dtos.HotelCaliforniaDto;
import br.com.lucasPavao.hotelCalifornia.domain.converter.GenericConverter;
import br.com.lucasPavao.hotelCalifornia.domain.converter.HotelConverter;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(HotelConverter.class)  // Garantir que a implementação concreta seja injetada nos testes
class HotelCaliforniaRepositoryTest {

    @Autowired
    private GenericConverter<HotelCaliforniaDto, HotelCaliforniaModel> converter;

    @Autowired
    private HotelCaliforniaRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Busca de hotel pelo CNPJ com sucesso")
    void findByCnpjCase1() {
        HotelCaliforniaDto data = new HotelCaliforniaDto(
                "Hotel California", "California", 4, "22134493000195");

        // Criação de hotel
        this.createHotel(data);

        // Verificando a busca pelo CNPJ
        Optional<HotelCaliforniaModel> result = this.repository.findByCnpj("22134493000195");
        assertTrue(result.isPresent(), "O hotel deveria ser encontrado pelo CNPJ");
        assertEquals("Hotel California", result.get().getName(), "O nome do hotel deveria ser o esperado");
    }

    @Test
    @DisplayName("Busca de hotel com CNPJ não existente")
    void findByCnpjCase2() {
        // Verificando a busca pelo CNPJ inexistente
        Optional<HotelCaliforniaModel> result = this.repository.findByCnpj("00000000000000");
        assertFalse(result.isPresent(), "O hotel não deveria ser encontrado com esse CNPJ");
    }

    @Test
    @DisplayName("Deleção de hotel pelo CNPJ com sucesso")
    void deleteByCnpj() {
        HotelCaliforniaDto data = new HotelCaliforniaDto(
                "Hotel California", "California", 4, "22134493000195");

        // Criação de hotel
        this.createHotel(data);

        // Deletando o hotel pelo CNPJ
        this.repository.deleteByCnpj("22134493000195");

        // Verificando que o hotel foi deletado
        Optional<HotelCaliforniaModel> result = this.repository.findByCnpj("22134493000195");
        assertFalse(result.isPresent(), "O hotel deveria ter sido deletado pelo CNPJ");
    }

    @Test
    @DisplayName("Deleção de hotel com CNPJ não existente")
    void deleteByCnpjCase2() {
        // Deletando um hotel que não existe
        this.repository.deleteByCnpj("00000000000000");

        // Verificando que nada foi deletado (deve continuar retornando vazio)
        Optional<HotelCaliforniaModel> result = this.repository.findByCnpj("00000000000000");
        assertFalse(result.isPresent(), "Nenhum hotel deveria ser encontrado com esse CNPJ");
    }

    @Test
    @DisplayName("Verifica se hotel existe pelo CNPJ com sucesso")
    void existsByCnpj() {
        HotelCaliforniaDto data = new HotelCaliforniaDto(
                "Hotel California", "California", 4, "22134493000195");

        // Criação de hotel
        this.createHotel(data);

        // Verificando se o hotel existe
        boolean exists = this.repository.existsByCnpj("22134493000195");
        assertTrue(exists, "O hotel deveria existir com o CNPJ informado");

        // Verificando CNPJ que não existe
        boolean notExists = this.repository.existsByCnpj("00000000000000");
        assertFalse(notExists, "O hotel não deveria existir com esse CNPJ");
    }

    @Test
    @DisplayName("Verifica se hotel não existe pelo CNPJ")
    void notExistsByCnpj() {
        // Verificando CNPJ que não existe
        boolean notExists = this.repository.existsByCnpj("00000000000000");
        assertFalse(notExists, "O hotel não deveria existir com esse CNPJ");
    }

    // Método para criar um hotel e persistir no banco de dados
    private HotelCaliforniaModel createHotel(HotelCaliforniaDto hotelDto) {
        HotelCaliforniaModel hotel = converter.convertToModel(hotelDto);
        this.entityManager.persist(hotel);
        return hotel;
    }
}
