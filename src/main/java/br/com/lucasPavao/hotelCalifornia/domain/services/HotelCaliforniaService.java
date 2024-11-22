package br.com.lucasPavao.hotelCalifornia.domain.services;


import br.com.lucasPavao.hotelCalifornia.api.dtos.HotelCaliforniaDto;
import br.com.lucasPavao.hotelCalifornia.domain.converter.GenericConverter;
import br.com.lucasPavao.hotelCalifornia.domain.converter.HotelConverter;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.repository.HotelCaliforniaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public HotelCaliforniaService(HotelCaliforniaRepository repository, GenericConverter<HotelCaliforniaDto, HotelCaliforniaModel> converter) {

        this.repository = repository;
        this.converter = converter;
    }

    public List<HotelCaliforniaDto> findAll() {

        List<HotelCaliforniaModel> hotels = repository.findAll();


        return hotels.stream()
                .map(hotel -> {
                    String cnpjSemMascara = removerMascaraCNPJ(hotel.getCnpj());
                    String cnpjComMascara = aplicarMascaraCNPJ(cnpjSemMascara);
                    hotel.setCnpj(cnpjComMascara);


                    return converter.convertToDto(hotel);
                })
                .collect(Collectors.toList());
    }
    public HotelCaliforniaDto findById(UUID id) {
        HotelCaliforniaModel hotel = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Hotel com ID " + id + " não encontrado."));


        hotel.setCnpj(aplicarMascaraCNPJ(hotel.getCnpj()));


        return converter.convertToDto(hotel);
    }

    public HotelCaliforniaDto findByCnpj(String cnpj){

        HotelCaliforniaModel hotel =
                repository.findByCnpj(removerMascaraCNPJ(cnpj))
                .orElseThrow(() -> new NoSuchElementException("Hotel com cnpj " + aplicarMascaraCNPJ(cnpj) + " não encontrado."));

        hotel.setCnpj(aplicarMascaraCNPJ(hotel.getCnpj()));

        return converter.convertToDto(hotel);
    }


    @Transactional
    public HotelCaliforniaDto create(@Valid HotelCaliforniaDto hotelDto) {

        HotelCaliforniaModel hotel = converter.convertToModel(hotelDto);


        validateHotelFields(hotel);
        validateCapacidade(hotel.getCapacidade());
        validateCnpj(hotel);

        hotel.setId(UUID.randomUUID());
        hotel.setCnpj(removerMascaraCNPJ(hotel.getCnpj()));
        HotelCaliforniaModel savedHotel = repository.save(hotel);


        return converter.convertToDto(savedHotel);
    }

    @Transactional
    public HotelCaliforniaDto update(UUID id, @Valid HotelCaliforniaDto hotelDto) {

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


        return converter.convertToDto(updatedHotel);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Hotel com ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
    }

    @Transactional
    public void deleteByCnpj(String cnpj){
        repository.findByCnpj(cnpj).orElseThrow(() -> new NoSuchElementException("Hotel com CNPJ " + cnpj + " não encontrado."));
        repository.deleteByCnpj(cnpj);
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
