package br.com.lucasPavao.hotelCalifornia.api.controllers;

import br.com.lucasPavao.hotelCalifornia.api.dtos.ClienteDto;
import br.com.lucasPavao.hotelCalifornia.domain.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista de todos os clientes cadastrados",
            tags = {"Clientes"}, responses = {
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClienteDto.class)))),
            @ApiResponse(description = "Erro interno", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<List<ClienteDto>> getAllClientes() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna os detalhes de um cliente pelo seu ID",
            tags = {"Clientes"}, responses = {
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ClienteDto.class))),
            @ApiResponse(description = "Não encontrado", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Erro interno", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<ClienteDto> getClienteById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar cliente por CPF", description = "Retorna os detalhes de um cliente pelo seu CPF",
            tags = {"Clientes"}, responses = {
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ClienteDto.class))),
            @ApiResponse(description = "Não encontrado", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Erro interno", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<ClienteDto> getClienteByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.findByCpf(cpf));
    }


    @PostMapping
    @Operation(summary = "Criar um cliente", description = "Adiciona um novo cliente ao sistema",
            tags = {"Clientes"}, responses = {
            @ApiResponse(description = "Criado", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = ClienteDto.class))),
            @ApiResponse(description = "Requisição inválida", responseCode = "400", content = {@Content}),
            @ApiResponse(description = "Erro interno", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<ClienteDto> createCliente(@Valid @RequestBody ClienteDto clienteDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(clienteDto));
    }

    @PutMapping("/{cpf}")
    @Operation(summary = "Atualizar um cliente", description = "Atualiza os detalhes de um cliente existente pelo CPF",
            tags = {"Clientes"}, responses = {
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ClienteDto.class))),
            @ApiResponse(description = "Requisição inválida", responseCode = "400", content = {@Content}),
            @ApiResponse(description = "Não encontrado", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Erro interno", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<ClienteDto> updateCliente(
            @PathVariable String cpf,
            @Valid @RequestBody ClienteDto clienteDto) {
        return ResponseEntity.ok(service.update(cpf, clienteDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um cliente por ID", description = "Remove um cliente do sistema pelo seu ID",
            tags = {"Clientes"}, responses = {
            @ApiResponse(description = "Sem conteúdo", responseCode = "204", content = @Content),
            @ApiResponse(description = "Não encontrado", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Erro interno", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<Void> deleteCliente(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
