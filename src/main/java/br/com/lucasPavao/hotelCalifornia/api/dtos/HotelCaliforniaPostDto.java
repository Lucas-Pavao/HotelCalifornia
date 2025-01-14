package br.com.lucasPavao.hotelCalifornia.api.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class HotelCaliforniaPostDto {

    @NotNull
    @NotBlank(message = "Nome do hotel é obrigatório")
    @Size(max = 100, message = "O nome do hotel não pode ter mais que 100 caracteres")
    private String name;

    @NotNull
    @NotBlank(message = "Local é obrigatório")
    @Size(max = 200, message = "A localização do hotel não pode ter mais que 200 caracteres")
    private String local;

    @NotNull(message = "Capacidade é obrigatória")
    private Integer capacidade;

    @NotNull
    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos")
    private String cnpj;

    public HotelCaliforniaPostDto() {}

    public HotelCaliforniaPostDto(String name, String local, Integer capacidade, String cnpj) {
        this.name = name;
        this.local = local;
        this.capacidade = capacidade;
        this.cnpj = cnpj;
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
