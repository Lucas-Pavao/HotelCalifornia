package br.com.lucasPavao.hotelCalifornia.domain.services;

import br.com.lucasPavao.hotelCalifornia.api.dtos.ClienteDto;
import br.com.lucasPavao.hotelCalifornia.domain.converter.GenericConverter;
import br.com.lucasPavao.hotelCalifornia.infraestructure.exception.ClienteNotFoundException;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.ClienteModel;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;
import br.com.lucasPavao.hotelCalifornia.infraestructure.repository.ClienteRepository;
import br.com.lucasPavao.hotelCalifornia.infraestructure.repository.HotelCaliforniaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final HotelCaliforniaRepository hotelRepository;
    private final GenericConverter<ClienteDto, ClienteModel> converter;
    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    public ClienteService(ClienteRepository clienteRepository, HotelCaliforniaRepository hotelRepository, GenericConverter<ClienteDto, ClienteModel> converter) {
        this.clienteRepository = clienteRepository;
        this.hotelRepository = hotelRepository;
        this.converter = converter;
    }

    public List<ClienteDto> findAll() {
        logger.info("Iniciando busca de todos os clientes");

        try {
            List<ClienteModel> clientes = clienteRepository.findAll();

            logger.info("Foram encontrados {} clientes", clientes.size());

            return clientes.stream()
                    .map(converter::convertToDto)
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a busca de todos os clientes", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar clientes no banco de dados", ex);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao buscar todos os clientes", ex);
            throw ex;
        }
    }

    public ClienteDto findById(UUID id) {
        logger.info("Iniciando busca de cliente com ID: {}", id);

        try {
            ClienteModel cliente = clienteRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Cliente com ID {} não encontrado", id);
                        return new ClienteNotFoundException("Cliente com ID " + id + " não encontrado.");
                    });

            logger.info("Cliente com ID {} encontrado", id);
            return converter.convertToDto(cliente);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a busca de cliente por ID", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar cliente por ID no banco de dados", ex);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao buscar cliente com ID: {}", id, ex);
            throw ex;
        }
    }

    public ClienteDto findByCpf(String cpf) {
        logger.info("Iniciando busca de cliente com Cpf: {}", cpf);

        try {
            ClienteModel cliente = clienteRepository.findByCpf(cpf)
                    .orElseThrow(() -> {
                        logger.warn("Cliente com cpf {} não encontrado", cpf);
                        return new ClienteNotFoundException("Cliente com cpf " + cpf + " não encontrado.");
                    });

            logger.info("Cliente com cpf {} encontrado", cpf);
            return converter.convertToDto(cliente);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a busca de cliente por CPF", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar cliente por CPF no banco de dados", ex);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao buscar cliente com CPF: {}", cpf, ex);
            throw ex;
        }
    }

    @Transactional
    public ClienteDto create(ClienteDto clienteDto) {
        try {
            // Converter o DTO para o modelo de cliente
            ClienteModel cliente = converter.convertToModel(clienteDto);

            // Localizar os hotéis pelos CNPJs informados
            Set<HotelCaliforniaModel> hotels = clienteDto.getCnpjs().stream()
                    .map(cnpj -> hotelRepository.findByCnpj(cnpj)
                            .orElseThrow(() -> new IllegalArgumentException("Hotel com CNPJ " + cnpj + " não encontrado")))
                    .collect(Collectors.toSet());

            // Configurar os hotéis encontrados no cliente
            cliente.setHotel(hotels);

            // Salvar o cliente no banco de dados
            ClienteModel savedCliente = clienteRepository.save(cliente);

            // Logar o sucesso
            logger.info("Cliente criado com sucesso: {}", savedCliente);

            // Retornar o DTO do cliente salvo
            return converter.convertToDto(savedCliente);
        } catch (DataIntegrityViolationException ex) {
            logger.error("Violação de integridade ao salvar o cliente: {}", clienteDto, ex);
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Dados inválidos ou duplicados para o cliente",
                    ex
            );
        } catch (IllegalArgumentException ex) {
            logger.error("Erro de validação ao criar cliente: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ex.getMessage(),
                    ex
            );
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a criação do cliente", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao salvar o cliente no banco de dados",
                    ex
            );
        } catch (Exception ex) {
            logger.error("Erro inesperado ao criar cliente", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro inesperado ao criar o cliente",
                    ex
            );
        }
    }

    @Transactional
    public ClienteDto update(String cpf, @Valid ClienteDto clienteDto) {
        try {
            ClienteModel existingCliente = clienteRepository.findByCpf(cpf)
                    .orElseThrow(() -> new ClienteNotFoundException("Cliente com ID " + cpf + " não encontrado."));

            existingCliente = converter.converToModelUpdate(existingCliente, clienteDto, cpf);

            ClienteModel updatedCliente = clienteRepository.save(existingCliente);
            logger.info("Cliente atualizado com sucesso: {}", updatedCliente);

            return converter.convertToDto(updatedCliente);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a atualização do cliente", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar o cliente no banco de dados", ex);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao atualizar cliente", ex);
            throw ex;
        }
    }

    @Transactional
    public void delete(UUID id) {
        try {
            if (!clienteRepository.existsById(id)) {
                throw new ClienteNotFoundException("Cliente com ID " + id + " não encontrado.");
            }
            clienteRepository.deleteById(id);
            logger.info("Cliente com ID {} excluído com sucesso", id);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a exclusão do cliente", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao excluir o cliente no banco de dados", ex);
        }
    }
}
