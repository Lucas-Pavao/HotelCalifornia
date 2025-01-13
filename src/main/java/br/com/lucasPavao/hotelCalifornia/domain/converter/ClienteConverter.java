package br.com.lucasPavao.hotelCalifornia.domain.converter;

import br.com.lucasPavao.hotelCalifornia.api.dtos.ClienteDto;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.ClienteModel;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ClienteConverter implements GenericConverter<ClienteDto, ClienteModel> {

    @Override
    public ClienteDto convertToDto(ClienteModel clienteModel) {
        if (clienteModel == null) {
            return null;
        }

        return new ClienteDto(
                clienteModel.getName(),
                clienteModel.getCpf(),
                clienteModel.getHotel().stream()
                        .map(HotelCaliforniaModel::getId)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public ClienteModel convertToModel(ClienteDto clienteDto) {
        if (clienteDto == null) {
            return null;
        }

        return ClienteModel.builder()
                .id(clienteDto.getId() != null ? clienteDto.getId() : UUID.randomUUID())
                .name(clienteDto.getName())
                .cpf(clienteDto.getCpf())
                .hotel(clienteDto.getHotel() != null
                        ? clienteDto.getHotel().stream()
                        .map(id -> HotelCaliforniaModel.builder().id(id.getId()).build())
                        .collect(Collectors.toSet())
                        : null)
                .build();
    }


    public ClienteModel converToModelUpdate(ClienteModel clienteModel, ClienteDto clienteDto, String cnpjCpf) {
        if (clienteModel == null || clienteDto == null) {
            throw new IllegalArgumentException("ClienteModel e ClienteDto não podem ser nulos para a atualização.");
        }

        return ClienteModel.builder()
                .id(clienteModel.getId())
                .name(clienteDto.getName() != null ? clienteDto.getName() : clienteModel.getName())
                .cpf(cnpjCpf)
                .hotel(clienteDto.getHotel() != null
                        ? clienteDto.getHotel().stream()
                        .map(id -> HotelCaliforniaModel.builder().id(id.getId()).build())
                        .collect(Collectors.toSet())
                        : clienteModel.getHotel())
                .build();
    }
}
