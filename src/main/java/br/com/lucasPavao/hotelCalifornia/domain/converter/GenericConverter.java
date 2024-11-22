package br.com.lucasPavao.hotelCalifornia.domain.converter;

public interface GenericConverter<D, M> {
    D convertToDto(M model);

    M convertToModel(D dto);
}