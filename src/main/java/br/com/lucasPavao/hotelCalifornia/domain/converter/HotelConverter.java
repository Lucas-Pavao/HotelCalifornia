package br.com.lucasPavao.hotelCalifornia.domain.converter;

import br.com.lucasPavao.hotelCalifornia.api.dtos.HotelCaliforniaDto;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;
import org.springframework.stereotype.Component;

@Component
public class HotelConverter  implements GenericConverter<HotelCaliforniaDto, HotelCaliforniaModel>  {
    @Override
    public  HotelCaliforniaDto convertToDto(HotelCaliforniaModel hotelModel) {
        if (hotelModel == null) {
            return null;
        }
        return new HotelCaliforniaDto(
                hotelModel.getName(),
                hotelModel.getLocal(),
                hotelModel.getCapacidade(),
                hotelModel.getCnpj()
        );
    }

    @Override
    public  HotelCaliforniaModel convertToModel(HotelCaliforniaDto hotelDto) {
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
}
