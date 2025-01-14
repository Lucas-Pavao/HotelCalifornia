package br.com.lucasPavao.hotelCalifornia.domain.services;


import br.com.lucasPavao.hotelCalifornia.api.dtos.HotelCaliforniaPostDto;
import br.com.lucasPavao.hotelCalifornia.api.dtos.HotelCaliforniaResponseDto;
import br.com.lucasPavao.hotelCalifornia.domain.Utils.CnpjUtils;
import br.com.lucasPavao.hotelCalifornia.domain.converter.GenericConverter;
import br.com.lucasPavao.hotelCalifornia.infraestructure.exception.CnpjExistsException;
import br.com.lucasPavao.hotelCalifornia.infraestructure.exception.HotelNotFoundException;
import br.com.lucasPavao.hotelCalifornia.infraestructure.exception.InvalidCapacityException;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;
import br.com.lucasPavao.hotelCalifornia.infraestructure.repository.HotelCaliforniaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HotelCaliforniaService {

    private final HotelCaliforniaRepository repository;
    private final GenericConverter<HotelCaliforniaPostDto, HotelCaliforniaModel> converterPost;
    private final GenericConverter<HotelCaliforniaResponseDto, HotelCaliforniaModel> converterResponse;
    private static final Logger logger = LoggerFactory.getLogger(HotelCaliforniaService.class);

    public HotelCaliforniaService(HotelCaliforniaRepository repository, GenericConverter<HotelCaliforniaPostDto, HotelCaliforniaModel> converterPost, GenericConverter<HotelCaliforniaResponseDto, HotelCaliforniaModel> converterResponse) {
        this.repository = repository;
        this.converterPost = converterPost;
        this.converterResponse = converterResponse;
    }

    public List<HotelCaliforniaResponseDto> findAll() {
        logger.info("Iniciando busca de todos os hotéis");

        try {
            List<HotelCaliforniaModel> hotels = repository.findAll();

            logger.info("Foram encontrados {} hotéis", hotels.size());

            return hotels.stream()
                    .map(hotel -> {
                        String cnpjSemMascara = CnpjUtils.removerMascaraCNPJ(hotel.getCnpj());
                        String cnpjComMascara = CnpjUtils.aplicarMascaraCNPJ(cnpjSemMascara);
                        hotel.setCnpj(cnpjComMascara);
                        return converterResponse.convertToDto(hotel);
                    })
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a busca de todos os hotéis", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar hotéis no banco de dados", ex);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao buscar todos os hotéis", ex);
            throw ex;
        }
    }

    public HotelCaliforniaResponseDto findById(UUID id) {
        logger.info("Iniciando busca de hotel com ID: {}", id);

        try {
            HotelCaliforniaModel hotel = repository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Hotel com ID {} não encontrado", id);
                        return new HotelNotFoundException("Hotel com ID " + id + " não encontrado.");
                    });

            hotel.setCnpj(CnpjUtils.aplicarMascaraCNPJ(hotel.getCnpj()));

            logger.info("Hotel com ID {} encontrado", id);
            return converterResponse.convertToDto(hotel);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a busca de hotel por ID", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar hotel por ID no banco de dados", ex);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao buscar hotel com ID: {}", id, ex);
            throw ex;
        }
    }

    public HotelCaliforniaResponseDto findByCnpj(String cnpj) {
        logger.info("Iniciando busca de hotel com CNPJ: {}", cnpj);

        try {
            String cnpjSemMascara = CnpjUtils.removerMascaraCNPJ(cnpj);

            HotelCaliforniaModel hotel = repository.findByCnpj(cnpjSemMascara)
                    .orElseThrow(() -> {
                        String cnpjComMascara = CnpjUtils.aplicarMascaraCNPJ(cnpjSemMascara);
                        logger.warn("Hotel com CNPJ {} não encontrado", cnpjComMascara);
                        return new HotelNotFoundException("Hotel com CNPJ " + cnpjComMascara + " não encontrado.");
                    });

            hotel.setCnpj(CnpjUtils.aplicarMascaraCNPJ(hotel.getCnpj()));

            logger.info("Hotel com CNPJ {} encontrado", cnpj);
            return converterResponse.convertToDto(hotel);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a busca de hotel por CNPJ", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar hotel por CNPJ no banco de dados", ex);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao buscar hotel com CNPJ: {}", cnpj, ex);
            throw ex;
        }
    }

    @Transactional
    public HotelCaliforniaResponseDto create(@Valid HotelCaliforniaPostDto hotelPostDto) {
        try {
            HotelCaliforniaModel hotel = converterPost.convertToModel(hotelPostDto);


            hotel.setCnpj(CnpjUtils.removerMascaraCNPJ(hotel.getCnpj()));


            validateHotelFields(hotelPostDto);
            validateCapacidade(hotel.getCapacidade());
            CnpjUtils.validate(hotel.getCnpj());
            ifExists(hotel.getCnpj());


            hotel.setId(UUID.randomUUID());
            HotelCaliforniaModel savedHotel = repository.save(hotel);

            logger.info("Hotel criado com sucesso: {}", savedHotel);
            return converterResponse.convertToDto(savedHotel);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a criação do hotel", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar o hotel no banco de dados", ex);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao criar hotel", ex);
            throw ex;
        }
    }

    @Transactional
    public HotelCaliforniaResponseDto update(String cnpj, @Valid HotelCaliforniaPostDto hotelPostDto) {
        try {


            validateCapacidade(hotelPostDto.getCapacidade());
            CnpjUtils.validate(hotelPostDto.getCnpj());


            HotelCaliforniaModel existingHotel = repository.findByCnpj(cnpj)
                    .orElseThrow(() -> new HotelNotFoundException("Hotel com Cnpj " + cnpj + " não encontrado."));

            existingHotel = converterPost.converToModelUpdate(existingHotel, hotelPostDto, cnpj);

            HotelCaliforniaModel updatedHotel = repository.save(existingHotel);
            logger.info("Hotel atualizado com sucesso: {}", updatedHotel);

            return converterResponse.convertToDto(updatedHotel);

        }
        catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a atualização do hotel", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar o hotel no banco de dados", ex);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao atualizar hotel", ex);
            throw ex;
        }
    }

    @Transactional
    public void delete(UUID id) {
        try {
            if (!repository.existsById(id)) {
                throw new HotelNotFoundException("Hotel com ID " + id + " não encontrado.");
            }
            repository.deleteById(id);
            logger.info("Hotel com ID {} excluído com sucesso", id);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a exclusão do hotel", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao excluir o hotel no banco de dados", ex);
        }
    }

    @Transactional
    public void deleteByCnpj(String cnpj){
        try {
            repository.findByCnpj(cnpj).orElseThrow(() -> new HotelNotFoundException("Hotel com CNPJ " + cnpj + " não encontrado."));
            repository.deleteByCnpj(cnpj);
            logger.info("Hotel com CNPJ {} excluído com sucesso", cnpj);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a exclusão do hotel", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao excluir o hotel no banco de dados", ex);
        }
    }

    private void validateHotelFields(HotelCaliforniaPostDto hotel) {
        if (hotel.getName() == null || hotel.getLocal() == null ||
                hotel.getCapacidade() == null || hotel.getCnpj() == null) {
            throw new IllegalArgumentException("Todos os campos devem ser preenchidos.");
        }
    }

    private void validateCapacidade(int capacidade){
        if(capacidade < 0){
            throw new InvalidCapacityException("Capacidade não pode ser menor que zero!");
        }
    }

    private void ifExists(String cnpj) {
        String cnpjSemMascara = CnpjUtils.removerMascaraCNPJ(cnpj);
        if (repository.existsByCnpj(cnpjSemMascara)) {
            throw new CnpjExistsException("Hotel com CNPJ " + CnpjUtils.aplicarMascaraCNPJ(cnpjSemMascara) + " já existe.");
        }
    }

}
