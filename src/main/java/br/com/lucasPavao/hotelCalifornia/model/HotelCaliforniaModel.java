package br.com.lucasPavao.hotelCalifornia.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
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
    private UUID id = UUID.randomUUID();

    @Column(name = "name")
    @NotBlank(message = "Nome do hotel é obrigatório")
    private String name;

    @Column(name = "local")
    @NotBlank(message = "Local é obrigatório")
    private String local;

    @Column(name = "capacidade")
    @NotNull(message = "Capacidade é obrigatória")
    @Min(value = 1, message = "Capacidade deve ser pelo menos 1")
    private Integer capacidade;

    @Column(name = "cnpj")
    @NotBlank(message = "CNPJ é obrigatório")
    private String cnpj;
}
