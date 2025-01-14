package br.com.lucasPavao.hotelCalifornia.domain.converter;

import br.com.lucasPavao.hotelCalifornia.api.dtos.ClienteDto;
import br.com.lucasPavao.hotelCalifornia.api.dtos.HotelCaliforniaResponseDto;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HotelResponseConverter implements GenericConverter<HotelCaliforniaResponseDto, HotelCaliforniaModel> {

    private final ClienteConverter clienteConverter;


    public HotelResponseConverter(ClienteConverter clienteConverter) {
        this.clienteConverter = clienteConverter;
    }

    @Override
    public HotelCaliforniaResponseDto convertToDto(HotelCaliforniaModel hotelModel) {
        if (hotelModel == null) {
            return null;
        }

        return new HotelCaliforniaResponseDto(
                hotelModel.getName(),
                hotelModel.getLocal(),
                hotelModel.getCapacidade(),
                hotelModel.getCnpj(),
                hotelModel.getClientes() != null
                        ? hotelModel.getClientes().stream()
                        .map(clienteConverter::convertToDto)
                        .collect(Collectors.toList())
                        : null
        );
    }



    @Override
    public HotelCaliforniaModel convertToModel(HotelCaliforniaResponseDto hotelDto) {
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

    public HotelCaliforniaModel converToModelUpdate(HotelCaliforniaModel hotelModel, HotelCaliforniaResponseDto hotelDto, String cnpj) {
        return HotelCaliforniaModel.builder()
                .id(hotelModel.getId())
                .name(hotelDto.getName() != null ? hotelDto.getName() : hotelModel.getName())
                .local(hotelDto.getLocal() != null ? hotelDto.getLocal() : hotelModel.getLocal())
                .capacidade(hotelDto.getCapacidade() != null ? hotelDto.getCapacidade() : hotelModel.getCapacidade())
                .cnpj(cnpj)
                .build();
    }
}
