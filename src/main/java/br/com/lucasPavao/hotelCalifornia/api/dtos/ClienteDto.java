package br.com.lucasPavao.hotelCalifornia.api.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClienteDto {

    @NotNull
    private UUID id;

    @NotBlank(message = "Nome do cliente é obrigatório")
    @Size(max = 100, message = "O nome do cliente não pode ter mais que 100 caracteres")
    private String name;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos")
    private String cpf;

    @NotNull
    @Size(min = 1, message = "Pelo menos um CNPJ de hotel deve ser informado")
    private Set<@Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos") String> cnpjs = new HashSet<>();

    // Construtor padrão
    public ClienteDto() {
    }

    // Construtor com argumentos
    public ClienteDto(@NotNull UUID id,
                      @NotBlank @Size(max = 100) String name,
                      @NotBlank @Pattern(regexp = "\\d{11}") String cpf,
                      @NotNull @Size(min = 1) Set<@Pattern(regexp = "\\d{14}") String> cnpjs) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.cnpjs = cnpjs;
    }

    // Getters e Setters
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

    public Set<String> getCnpjs() {
        return cnpjs;
    }

    public void setCnpjs(Set<String> cnpjs) {
        this.cnpjs = cnpjs;
    }
}
