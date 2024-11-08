package br.com.lucasPavao.hotelCalifornia.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
}
