package br.com.lucasPavao.hotelCalifornia.infraestructure.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "hotel_california")
public class HotelCaliforniaModel {

    @Id
    @Column(columnDefinition = "UUID")
    @NotNull
    private UUID id = UUID.randomUUID();

    @Column(name = "name")
    @NotNull
    @NotBlank(message = "Nome do hotel é obrigatório")
    @Size(max = 100, message = "O nome do hotel não pode ter mais que 100 caracteres")
    private String name;

    @Column(name = "local")
    @NotNull
    @NotBlank(message = "Local é obrigatório")
    @Size(max = 200, message = "A localização do hotel não pode ter mais que 200 caracteres")
    private String local;

    @Column(name = "capacidade")
    @NotNull(message = "Capacidade é obrigatória")
    @Min(value = 1, message = "Capacidade deve ser pelo menos 1")
    private Integer capacidade;

    @Column(name = "cnpj")
    @NotNull
    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos")
    private String cnpj;

    public @NotNull UUID getId() {
        return id;
    }

    public void setId(@NotNull UUID id) {
        this.id = id;
    }

    public @NotNull @NotBlank(message = "Nome do hotel é obrigatório") @Size(max = 100, message = "O nome do hotel não pode ter mais que 100 caracteres") String getName() {
        return name;
    }

    public void setName(@NotNull @NotBlank(message = "Nome do hotel é obrigatório") @Size(max = 100, message = "O nome do hotel não pode ter mais que 100 caracteres") String name) {
        this.name = name;
    }

    public @NotNull @NotBlank(message = "Local é obrigatório") @Size(max = 200, message = "A localização do hotel não pode ter mais que 200 caracteres") String getLocal() {
        return local;
    }

    public void setLocal(@NotNull @NotBlank(message = "Local é obrigatório") @Size(max = 200, message = "A localização do hotel não pode ter mais que 200 caracteres") String local) {
        this.local = local;
    }

    public @NotNull(message = "Capacidade é obrigatória") @Min(value = 1, message = "Capacidade deve ser pelo menos 1") Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(@NotNull(message = "Capacidade é obrigatória") @Min(value = 1, message = "Capacidade deve ser pelo menos 1") Integer capacidade) {
        this.capacidade = capacidade;
    }

    public @NotNull @NotBlank(message = "CNPJ é obrigatório") @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos") String getCnpj() {
        return cnpj;
    }

    public void setCnpj(@NotNull @NotBlank(message = "CNPJ é obrigatório") @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos") String cnpj) {
        this.cnpj = cnpj;
    }
}
