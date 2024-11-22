package br.com.lucasPavao.hotelCalifornia.api.controllers;

import java.util.List;
import java.util.UUID;

import br.com.lucasPavao.hotelCalifornia.api.dtos.HotelCaliforniaDto;
import br.com.lucasPavao.hotelCalifornia.domain.services.HotelCaliforniaService;
import com.sun.istack.NotNull;
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

@RestController
@RequestMapping("/api/hotelCalifornia")
@Tag(name = "Hotels", description = "Endpoints for managing hotels")
public class HotelCaliforniaController {

    private final HotelCaliforniaService service;

    public HotelCaliforniaController(HotelCaliforniaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Find all hotels", description = "Retrieve a list of all hotels",
            tags = {"Hotels"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HotelCaliforniaDto.class)))),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<List<HotelCaliforniaDto>> getAllHotels() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a hotel by ID", description = "Retrieve hotel details by its ID",
            tags = {"Hotels"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = HotelCaliforniaDto.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<HotelCaliforniaDto> getHotelById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("getByCnpj/{cnpj}")
    @Operation(summary = "Find a hotel by CNPJ", description = "Retrieve hotel details by its CNPJ",
            tags = {"Hotels"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = HotelCaliforniaDto.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<HotelCaliforniaDto> getHotelByCnpj(@PathVariable String cnpj) {
        return ResponseEntity.ok(service.findByCnpj(cnpj));
    }

    @PostMapping
    @Operation(summary = "Create a hotel", description = "Add a new hotel to the system",
            tags = {"Hotels"}, responses = {
            @ApiResponse(description = "Created", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = HotelCaliforniaDto.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<HotelCaliforniaDto> createHotel(@Valid @NotNull @RequestBody HotelCaliforniaDto hotel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(hotel));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a hotel", description = "Update details of an existing hotel",
            tags = {"Hotels"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = HotelCaliforniaDto.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
            @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<HotelCaliforniaDto> updateHotel(
            @PathVariable UUID id,
            @Valid @NotNull @RequestBody HotelCaliforniaDto hotel) {

        return ResponseEntity.ok(service.update(id, hotel));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a hotel by ID", description = "Remove a hotel from the system by its ID",
            tags = {"Hotels"}, responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<Void> deleteHotel(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("deleteByCnpj/{cnpj}")
    @Operation(summary = "Delete a hotel by CNPJ", description = "Remove a hotel from the system by its CNPJ",
            tags = {"Hotels"}, responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<HotelCaliforniaDto> deleteHotelByCnpj(@PathVariable String cnpj) {
        service.deleteByCnpj(cnpj);
        return ResponseEntity.noContent().build();
    }
}
