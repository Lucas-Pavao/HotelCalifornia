package br.com.lucasPavao.hotelCalifornia.domain.services;

import br.com.lucasPavao.hotelCalifornia.api.dtos.HotelCaliforniaDto;
import br.com.lucasPavao.hotelCalifornia.domain.converter.GenericConverter;
import br.com.lucasPavao.hotelCalifornia.infraestructure.exception.CnpjExistsException;
import br.com.lucasPavao.hotelCalifornia.infraestructure.exception.HotelNotFoundException;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;
import br.com.lucasPavao.hotelCalifornia.infraestructure.repository.HotelCaliforniaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelCaliforniaServiceTest {

    @Mock
    private HotelCaliforniaRepository repository;

    @Mock
    private GenericConverter<HotelCaliforniaDto, HotelCaliforniaModel> converter;

    private HotelCaliforniaService hotelCaliforniaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hotelCaliforniaService = new HotelCaliforniaService(repository, converter);
    }

    @Test
    void testFindByIdSuccess() {
        // Arrange
        UUID hotelId = UUID.randomUUID();
        HotelCaliforniaModel hotelModel = new HotelCaliforniaModel();
        hotelModel.setId(hotelId);
        hotelModel.setName("Hotel California");
        hotelModel.setCnpj("12345678000195");

        HotelCaliforniaDto hotelDto = new HotelCaliforniaDto();
        hotelDto.setName("Hotel California");
        hotelDto.setCnpj("12.345.678/0001-95");

        when(repository.findById(hotelId)).thenReturn(Optional.of(hotelModel));
        when(converter.convertToDto(hotelModel)).thenReturn(hotelDto);

        // Act
        HotelCaliforniaDto result = hotelCaliforniaService.findById(hotelId);

        // Assert
        assertNotNull(result);
        assertEquals(hotelModel.getCnpj(), result.getCnpj());
        assertEquals("Hotel California", result.getName());
        verify(repository, times(1)).findById(hotelId);
    }

    @Test
    void testFindByIdNotFound() {
        // Arrange
        UUID hotelId = UUID.randomUUID();
        when(repository.findById(hotelId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException thrown = assertThrows(HotelNotFoundException.class, () -> {
            hotelCaliforniaService.findById(hotelId);
        });
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("Hotel com ID " + hotelId + " não encontrado.", thrown.getReason());
    }

    @Test
    void testCreateHotelSuccess() {
        // Arrange
        HotelCaliforniaDto hotelDto = new HotelCaliforniaDto();
        hotelDto.setName("Hotel Test");
        hotelDto.setLocal("Local Test");
        hotelDto.setCapacidade(100);
        hotelDto.setCnpj("12.345.678/0001-95");

        HotelCaliforniaModel hotelModel = new HotelCaliforniaModel();
        hotelModel.setName("Hotel Test");
        hotelModel.setLocal("Local Test");
        hotelModel.setCapacidade(100);
        hotelModel.setCnpj("12345678000195");

        HotelCaliforniaModel savedHotel = new HotelCaliforniaModel();
        savedHotel.setId(UUID.randomUUID());
        savedHotel.setName("Hotel Test");
        savedHotel.setLocal("Local Test");
        savedHotel.setCapacidade(100);
        savedHotel.setCnpj("12345678000195");

        when(converter.convertToModel(hotelDto)).thenReturn(hotelModel);
        when(repository.save(hotelModel)).thenReturn(savedHotel);
        when(converter.convertToDto(savedHotel)).thenReturn(hotelDto);

        // Act
        HotelCaliforniaDto result = hotelCaliforniaService.create(hotelDto);

        // Assert
        assertNotNull(result);
        assertEquals("Hotel Test", result.getName());
        assertEquals(100, result.getCapacidade());
        verify(repository, times(1)).save(hotelModel);
    }

    @Test
    void testCreateHotelCnpjExists() {
        // Arrange
        HotelCaliforniaDto hotelDto = new HotelCaliforniaDto();
        hotelDto.setName("Hotel Test");
        hotelDto.setLocal("Local Test");
        hotelDto.setCapacidade(100);
        hotelDto.setCnpj("12.345.678/0001-95");

        // Inicializa corretamente o HotelCaliforniaModel com dados válidos
        HotelCaliforniaModel hotelModel = new HotelCaliforniaModel();
        hotelModel.setName("Hotel Test");
        hotelModel.setLocal("Local Test");
        hotelModel.setCapacidade(100);
        hotelModel.setCnpj("12345678000195");

        // Simula a conversão de DTO para modelo e a verificação do CNPJ
        when(converter.convertToModel(hotelDto)).thenReturn(hotelModel);
        when(repository.existsByCnpj("12345678000195")).thenReturn(true);

        // Act & Assert
        assertThrows(CnpjExistsException.class, () -> hotelCaliforniaService.create(hotelDto));
    }

}
