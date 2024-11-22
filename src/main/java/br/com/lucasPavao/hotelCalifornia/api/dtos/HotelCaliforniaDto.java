package br.com.lucasPavao.hotelCalifornia.api.dtos;

import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;

import javax.validation.constraints.*;
import java.util.UUID;

public class HotelCaliforniaDto {

    @NotNull
    private UUID id;

    @NotNull
    @NotBlank(message = "Nome do hotel é obrigatório")
    @Size(max = 100, message = "O nome do hotel não pode ter mais que 100 caracteres")
    private String name;

    @NotNull
    @NotBlank(message = "Local é obrigatório")
    @Size(max = 200, message = "A localização do hotel não pode ter mais que 200 caracteres")
    private String local;

    @NotNull(message = "Capacidade é obrigatória")
    @Min(value = 1, message = "Capacidade deve ser pelo menos 1")
    private Integer capacidade;

    @NotNull
    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos")
    private String cnpj;


    public HotelCaliforniaDto() {}


    public   HotelCaliforniaDto(UUID id, String name, String local, Integer capacidade, String cnpj) {
        this.id = id;
        this.name = name;
        this.local = local;
        this.capacidade = capacidade;
        this.cnpj = cnpj;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
