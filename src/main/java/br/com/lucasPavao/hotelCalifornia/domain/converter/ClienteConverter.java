package br.com.lucasPavao.hotelCalifornia.domain.converter;

import br.com.lucasPavao.hotelCalifornia.api.dtos.ClienteDto;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.ClienteModel;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ClienteConverter implements GenericConverter<ClienteDto, ClienteModel> {

    @Override
    public ClienteDto convertToDto(ClienteModel clienteModel) {
        return new ClienteDto(
                clienteModel.getId(),
                clienteModel.getName(),
                clienteModel.getCpf(),
                clienteModel.getHotels() != null
                        ? clienteModel.getHotels().stream()
                        .map(HotelCaliforniaModel::getCnpj)
                        .collect(Collectors.toSet())
                        : new HashSet<>()
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
                .hotels(clienteDto.getCnpjs() != null
                        ? clienteDto.getCnpjs().stream()
                        .map(cnpj -> HotelCaliforniaModel.builder().cnpj(cnpj).build())
                        .collect(Collectors.toSet())
                        : null)
                .build();
    }

    @Override
    public ClienteModel converToModelUpdate(ClienteModel clienteModel, ClienteDto clienteDto, String cnpjCpf) {
        if (clienteModel == null || clienteDto == null) {
            throw new IllegalArgumentException("ClienteModel e ClienteDto não podem ser nulos para a atualização.");
        }

        return ClienteModel.builder()
                .id(clienteModel.getId())
                .name(clienteDto.getName() != null ? clienteDto.getName() : clienteModel.getName())
                .cpf(clienteDto.getCpf() != null ? clienteDto.getCpf() : clienteModel.getCpf())
                .hotels(clienteDto.getCnpjs() != null
                        ? clienteDto.getCnpjs().stream()
                        .map(cnpj -> HotelCaliforniaModel.builder().cnpj(cnpj).build())
                        .collect(Collectors.toSet())
                        : clienteModel.getHotels())
                .build();
    }



}
