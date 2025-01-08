package br.com.lucasPavao.hotelCalifornia.domain.converter;

import br.com.lucasPavao.hotelCalifornia.api.dtos.HotelCaliforniaDto;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;
import org.springframework.stereotype.Component;

@Component
public interface GenericConverter<D, M> {
    D convertToDto(M model);

    M convertToModel(D dto);
    M converToModelUpdate (M model,D dto, String cnpj );
}