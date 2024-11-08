package br.com.lucasPavao.hotelCalifornia.services;


import br.com.lucasPavao.hotelCalifornia.model.HotelCaliforniaModel;
import br.com.lucasPavao.hotelCalifornia.repository.HotelCaliforniaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Pattern;

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
    public HotelCaliforniaModel create(@Valid HotelCaliforniaModel hotel) {
        validateHotelFields(hotel);
        validateCnpj(hotel);
        return repository.save(hotel);
    }

    @Transactional
    public HotelCaliforniaModel update(UUID id,@Valid HotelCaliforniaModel hotel) {
        validateHotelFields(hotel);
        validateCnpj(hotel);
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

    private void validateHotelFields(HotelCaliforniaModel hotel) {
        if (hotel.getName() == null || hotel.getLocal() == null ||
                hotel.getCapacidade() == null || hotel.getCnpj() == null) {
            throw new IllegalArgumentException("Todos os campos devem ser preenchidos.");
        }
    }

    private void validateCnpj(HotelCaliforniaModel hotel){
        boolean retorno = false;
        // Verifica se é um CNPJ alfanumérico com 12 primeiros caracteres alfanuméricos e 2 últimos numéricos
        String regex = "^[A-Za-z0-9]{12}[0-9]{2}$";
        if (!Pattern.matches(regex, hotel.getCnpj())) {
            retorno = false;
        }

        // Verifica se é uma sequência repetida de caracteres
        if (hotel.getCnpj().equals("00000000000000") || hotel.getCnpj().equals("11111111111111") ||
                hotel.getCnpj().equals("22222222222222") || hotel.getCnpj().equals("33333333333333") ||
                hotel.getCnpj().equals("44444444444444") || hotel.getCnpj().equals("55555555555555") ||
                hotel.getCnpj().equals("66666666666666") || hotel.getCnpj().equals("77777777777777") ||
                hotel.getCnpj().equals("88888888888888") || hotel.getCnpj().equals("99999999999999")) {
            retorno = false;
        }

        // Conversão de caracteres para seus valores ASCII ajustados (alfanuméricos)
        int[] valores = new int[12];

        for (int i = 0; i < 12; i++) {
            char c = hotel.getCnpj().charAt(i);
            if (Character.isDigit(c)) {
                valores[i] = c - '0'; // Para números (0-9)
            } else if (Character.isLetter(c)) {
                valores[i] = c - 'A' + 17; // Para letras (A-Z), ajustado conforme regras
            }
        }

        // Cálculo do primeiro dígito verificador (13º dígito)
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

        // Cálculo do segundo dígito verificador (14º dígito)
        peso = 6; // Pesos começam em 6 para o segundo dígito
        soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += valores[i] * peso;
            peso--;
            if (peso < 2) {
                peso = 9;
            }
        }

        soma += (dig13 - '0') * 2; // Inclui o primeiro dígito verificador no cálculo
        resto = soma % 11;
        char dig14 = (resto < 2) ? '0' : (char) (11 - resto + '0');

        // Verifica se os dígitos verificadores calculados coincidem com os últimos 2 dígitos
        retorno = dig13 == hotel.getCnpj().charAt(12) && dig14 == hotel.getCnpj().charAt(13);

        if(!retorno){
            throw new IllegalArgumentException("Cnpj Invalido");
        }
    }
}
