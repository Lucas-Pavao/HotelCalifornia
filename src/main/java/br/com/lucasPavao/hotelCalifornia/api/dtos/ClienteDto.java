package br.com.lucasPavao.hotelCalifornia.api.dtos;

import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClienteDto {

    @Id
    @Column(columnDefinition = "UUID")
    @NotNull
    private UUID id = UUID.randomUUID();

    @Column(name = "name")
    @NotNull
    @NotBlank(message = "Nome do hotel é obrigatório")
    @Size(max = 100, message = "O nome do hotel não pode ter mais que 100 caracteres")
    private String name;

    @Column(name = "cpf")
    @NotNull
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos")
    private String cpf;

    @ManyToMany
    @JoinTable(
            name = "hotel_cliente",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "hotel_id")
    )
    private Set<HotelCaliforniaModel> hotel = new HashSet<>();

    public <R> ClienteDto(@NotNull @NotBlank(message = "Nome do hotel é obrigatório")
                          @Size(max = 100, message = "O nome do hotel não pode ter mais que 100 caracteres")
                          String name,
                          @NotNull @NotBlank(message = "CPF é obrigatório")
                          @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos")
                          String cnpj, R collect) {
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Set<HotelCaliforniaModel> getHotel() {
        return hotel;
    }

    public void setHotel(Set<HotelCaliforniaModel> hotel) {
        this.hotel = hotel;
    }
}
