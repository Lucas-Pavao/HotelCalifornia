package br.com.lucasPavao.hotelCalifornia.infraestructure.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "cliente_hotel")
public class ClienteModel {

    @Id
    @Column(columnDefinition = "UUID")
    @NotNull
    private UUID id = UUID.randomUUID();

    @Column(name = "name")
    @NotNull
    @NotBlank(message = "Nome do cliente é obrigatório")
    @Size(max = 100, message = "O nome do cliente não pode ter mais que 100 caracteres")
    private String name;

    @Column(name = "cpf", unique = true)
    @NotNull
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos")
    private String cpf;

    @ManyToMany(mappedBy = "clientes", fetch = FetchType.EAGER)
    private Set<HotelCaliforniaModel> hotels = new HashSet<>();
}
