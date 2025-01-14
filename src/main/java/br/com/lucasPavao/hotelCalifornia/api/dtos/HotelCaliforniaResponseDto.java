package br.com.lucasPavao.hotelCalifornia.api.dtos;

import java.util.List;

public class HotelCaliforniaResponseDto {

    private String name;
    private String local;
    private Integer capacidade;
    private String cnpj;
    private List<ClienteDto> clientes;

    public HotelCaliforniaResponseDto() {}

    public HotelCaliforniaResponseDto(String name, String local, Integer capacidade, String cnpj, List<ClienteDto> clientes) {
        this.name = name;
        this.local = local;
        this.capacidade = capacidade;
        this.cnpj = cnpj;
        this.clientes = clientes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public List<ClienteDto> getClientes() {
        return clientes;
    }

    public void setClientes(List<ClienteDto> clientes) {
        this.clientes = clientes;
    }
}
