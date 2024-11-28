package br.com.lucasPavao.hotelCalifornia.domain.converter;

import org.springframework.stereotype.Component;

@Component
public interface GenericConverter<D, M> {
    D convertToDto(M model);

    M convertToModel(D dto);
}