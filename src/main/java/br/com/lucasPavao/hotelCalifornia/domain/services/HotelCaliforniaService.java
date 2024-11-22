package br.com.lucasPavao.hotelCalifornia.domain.services;


import br.com.lucasPavao.hotelCalifornia.api.dtos.HotelCaliforniaDto;
import br.com.lucasPavao.hotelCalifornia.domain.converter.GenericConverter;
import br.com.lucasPavao.hotelCalifornia.domain.converter.HotelConverter;
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
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class HotelCaliforniaService {

    private final HotelCaliforniaRepository repository;
    private final GenericConverter<HotelCaliforniaDto, HotelCaliforniaModel> converter;
    private static final Logger logger = LoggerFactory.getLogger(HotelCaliforniaService.class);

    public HotelCaliforniaService(HotelCaliforniaRepository repository, GenericConverter<HotelCaliforniaDto, HotelCaliforniaModel> converter) {

        this.repository = repository;
        this.converter = converter;
    }

    public List<HotelCaliforniaDto> findAll() {
        logger.info("Iniciando busca de todos os hotéis");

        try {
            List<HotelCaliforniaModel> hotels = repository.findAll();

            logger.info("Foram encontrados {} hotéis", hotels.size());

            return hotels.stream()
                    .map(hotel -> {
                        String cnpjSemMascara = removerMascaraCNPJ(hotel.getCnpj());
                        String cnpjComMascara = aplicarMascaraCNPJ(cnpjSemMascara);
                        hotel.setCnpj(cnpjComMascara);
                        return converter.convertToDto(hotel);
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

    public HotelCaliforniaDto findById(UUID id) {
        logger.info("Iniciando busca de hotel com ID: {}", id);

        try {
            HotelCaliforniaModel hotel = repository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Hotel com ID {} não encontrado", id);
                        return new NoSuchElementException("Hotel com ID " + id + " não encontrado.");
                    });

            hotel.setCnpj(aplicarMascaraCNPJ(hotel.getCnpj()));

            logger.info("Hotel com ID {} encontrado", id);
            return converter.convertToDto(hotel);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a busca de hotel por ID", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar hotel por ID no banco de dados", ex);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao buscar hotel com ID: {}", id, ex);
            throw ex;
        }
    }

    public HotelCaliforniaDto findByCnpj(String cnpj) {
        logger.info("Iniciando busca de hotel com CNPJ: {}", cnpj);

        try {
            String cnpjSemMascara = removerMascaraCNPJ(cnpj);

            HotelCaliforniaModel hotel = repository.findByCnpj(cnpjSemMascara)
                    .orElseThrow(() -> {
                        String cnpjComMascara = aplicarMascaraCNPJ(cnpjSemMascara);
                        logger.warn("Hotel com CNPJ {} não encontrado", cnpjComMascara);
                        return new NoSuchElementException("Hotel com CNPJ " + cnpjComMascara + " não encontrado.");
                    });

            hotel.setCnpj(aplicarMascaraCNPJ(hotel.getCnpj()));

            logger.info("Hotel com CNPJ {} encontrado", cnpj);
            return converter.convertToDto(hotel);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a busca de hotel por CNPJ", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar hotel por CNPJ no banco de dados", ex);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao buscar hotel com CNPJ: {}", cnpj, ex);
            throw ex;
        }
    }

    @Transactional
    public HotelCaliforniaDto create(@Valid HotelCaliforniaDto hotelDto) {
        try{
            HotelCaliforniaModel hotel = converter.convertToModel(hotelDto);

            validateHotelFields(hotel);
            validateCapacidade(hotel.getCapacidade());
            validateCnpj(hotel);

            hotel.setId(UUID.randomUUID());
            hotel.setCnpj(removerMascaraCNPJ(hotel.getCnpj()));
            HotelCaliforniaModel savedHotel = repository.save(hotel);
            logger.info("Hotel criado com sucesso: {}", savedHotel);


            return converter.convertToDto(savedHotel);

        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a criação do hotel", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar o hotel no banco de dados", ex);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao criar hotel", ex);
            throw ex;
        }
    }

    @Transactional
    public HotelCaliforniaDto update(UUID id, @Valid HotelCaliforniaDto hotelDto) {
        try {
            HotelCaliforniaModel hotel = converter.convertToModel(hotelDto);


            validateHotelFields(hotel);
            validateCapacidade(hotel.getCapacidade());
            validateCnpj(hotel);


            HotelCaliforniaModel existingHotel = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Hotel com ID " + id + " não encontrado."));


            existingHotel.setName(hotel.getName());
            existingHotel.setLocal(hotel.getLocal());
            existingHotel.setCapacidade(hotel.getCapacidade());
            existingHotel.setCnpj(removerMascaraCNPJ(hotel.getCnpj()));


            HotelCaliforniaModel updatedHotel = repository.save(existingHotel);
            logger.info("Hotel atualizado com sucesso: {}", updatedHotel);

            return converter.convertToDto(updatedHotel);

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
                throw new NoSuchElementException("Hotel com ID " + id + " não encontrado.");
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
            repository.findByCnpj(cnpj).orElseThrow(() -> new NoSuchElementException("Hotel com CNPJ " + cnpj + " não encontrado."));
            repository.deleteByCnpj(cnpj);
            logger.info("Hotel com ID {} excluído com sucesso", cnpj);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a exclusão do hotel", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao excluir o hotel no banco de dados", ex);
        }
    }


    private void validateHotelFields(HotelCaliforniaModel hotel) {
        if (hotel.getName() == null || hotel.getLocal() == null ||
                hotel.getCapacidade() == null || hotel.getCnpj() == null) {
            throw new IllegalArgumentException("Todos os campos devem ser preenchidos.");
        }
    }

    private void validateCnpj(HotelCaliforniaModel hotel){
        boolean retorno = true;

        hotel.setCnpj(removerMascaraCNPJ(hotel.getCnpj()));

        String regex = "^[A-Za-z0-9]{12}[0-9]{2}$";
        if (!Pattern.matches(regex, hotel.getCnpj())) {
            retorno = false;
        }


        if (hotel.getCnpj().equals("00000000000000") || hotel.getCnpj().equals("11111111111111") ||
                hotel.getCnpj().equals("22222222222222") || hotel.getCnpj().equals("33333333333333") ||
                hotel.getCnpj().equals("44444444444444") || hotel.getCnpj().equals("55555555555555") ||
                hotel.getCnpj().equals("66666666666666") || hotel.getCnpj().equals("77777777777777") ||
                hotel.getCnpj().equals("88888888888888") || hotel.getCnpj().equals("99999999999999")) {
            retorno = false;
        }


        int[] valores = new int[12];

        for (int i = 0; i < 12; i++) {
            char c = hotel.getCnpj().charAt(i);
            if (Character.isDigit(c)) {
                valores[i] = c - '0'; // Para números (0-9)
            } else if (Character.isLetter(c)) {
                valores[i] = c - 'A' + 17; // Para letras (A-Z), ajustado conforme regras
            }
        }


        int peso = 5; // Pesos começam em 5
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += valores[i] * peso;
            peso--;
            if (peso < 2) {
                peso = 9;
            }
        }

        int resto = soma % 11;
        char dig13 = (resto < 2) ? '0' : (char) (11 - resto + '0');


        peso = 6;
        soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += valores[i] * peso;
            peso--;
            if (peso < 2) {
                peso = 9;
            }
        }

        soma += (dig13 - '0') * 2;
        resto = soma % 11;
        char dig14 = (resto < 2) ? '0' : (char) (11 - resto + '0');


        retorno = dig13 == hotel.getCnpj().charAt(12) && dig14 == hotel.getCnpj().charAt(13);

        if(!retorno){
            throw new IllegalArgumentException("Cnpj Invalido");
        }
    }


    public  String aplicarMascaraCNPJ(String cnpj) {

        if (cnpj.length() != 14) {
            throw new IllegalArgumentException("CNPJ deve ter exatamente 14 caracteres");
        }
        return cnpj.substring(0, 2) + "." +
                cnpj.substring(2, 5) + "." +
                cnpj.substring(5, 8) + "/" +
                cnpj.substring(8, 12) + "-" +
                cnpj.substring(12, 14);
    }


    public  String removerMascaraCNPJ(String cnpjComMascara) {

        return cnpjComMascara.replaceAll("[^A-Za-z0-9]", "");
    }


    private void validateCapacidade(int capacidade){
        if(capacidade < 0){
            throw new IllegalArgumentException("Capacidade não pode ser menor que zero!");
        }
    }

}
