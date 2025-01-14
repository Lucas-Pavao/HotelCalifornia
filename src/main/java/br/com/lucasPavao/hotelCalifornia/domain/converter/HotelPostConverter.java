package br.com.lucasPavao.hotelCalifornia.domain.converter;

import br.com.lucasPavao.hotelCalifornia.api.dtos.HotelCaliforniaPostDto;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HotelPostConverter implements GenericConverter<HotelCaliforniaPostDto, HotelCaliforniaModel> {

    private final ClienteConverter clienteConverter;


    public HotelPostConverter(ClienteConverter clienteConverter) {
        this.clienteConverter = clienteConverter;
    }

    @Override
    public HotelCaliforniaPostDto convertToDto(HotelCaliforniaModel hotelModel) {
        if (hotelModel == null) {
            return null;
        }

        return new HotelCaliforniaPostDto(
                hotelModel.getName(),
                hotelModel.getLocal(),
                hotelModel.getCapacidade(),
                hotelModel.getCnpj()
        );
    }



    @Override
    public HotelCaliforniaModel convertToModel(HotelCaliforniaPostDto hotelDto) {
        if (hotelDto == null) {
            return null;
        }

        return HotelCaliforniaModel.builder()
                .name(hotelDto.getName())
                .local(hotelDto.getLocal())
                .capacidade(hotelDto.getCapacidade())
                .cnpj(hotelDto.getCnpj())
                .build();
    }

    public HotelCaliforniaModel converToModelUpdate(HotelCaliforniaModel hotelModel, HotelCaliforniaPostDto hotelDto, String cnpj) {
        return HotelCaliforniaModel.builder()
                .id(hotelModel.getId())
                .name(hotelDto.getName() != null ? hotelDto.getName() : hotelModel.getName())
                .local(hotelDto.getLocal() != null ? hotelDto.getLocal() : hotelModel.getLocal())
                .capacidade(hotelDto.getCapacidade() != null ? hotelDto.getCapacidade() : hotelModel.getCapacidade())
                .cnpj(cnpj)
                .build();
    }
}
