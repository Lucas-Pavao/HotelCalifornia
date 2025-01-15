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

    public ClienteService(
            ClienteRepository clienteRepository,
            HotelCaliforniaRepository hotelRepository,
            GenericConverter<ClienteDto, ClienteModel> converter) {
        this.clienteRepository = clienteRepository;
        this.hotelRepository = hotelRepository;
        this.converter = converter;
    }

    public List<ClienteDto> findAll() {
        logger.info("Iniciando busca de todos os clientes.");
        try {
            List<ClienteModel> clientes = clienteRepository.findAll();
            logger.info("Foram encontrados {} clientes.", clientes.size());

            return clientes.stream()
                    .map(converter::convertToDto)
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a busca de todos os clientes.", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar clientes no banco de dados.", ex);
        }
    }

    public ClienteDto findById(UUID id) {
        logger.info("Iniciando busca de cliente com ID: {}", id);
        try {
            ClienteModel cliente = clienteRepository.findById(id)
                    .orElseThrow(() -> new ClienteNotFoundException("Cliente com ID " + id + " não encontrado."));

            logger.info("Cliente com ID {} encontrado.", id);
            return converter.convertToDto(cliente);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a busca de cliente por ID.", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar cliente por ID no banco de dados.", ex);
        }
    }

    public ClienteDto findByCpf(String cpf) {
        logger.info("Iniciando busca de cliente com CPF: {}", cpf);

        try {
            ClienteModel cliente = clienteRepository.findByCpf(cpf)
                    .orElseThrow(() -> {
                        logger.warn("Cliente com CPF {} não encontrado", cpf);
                        return new ClienteNotFoundException("Cliente com CPF " + cpf + " não encontrado.");
                    });

            // Log dos hotéis associados
            logger.info("Cliente encontrado: {}, hotéis associados: {}", cliente.getName(),
                    cliente.getHotels().stream().map(HotelCaliforniaModel::getCnpj).collect(Collectors.toSet()));

            return converter.convertToDto(cliente);
        } catch (Exception ex) {
            logger.error("Erro ao buscar cliente com CPF: {}", cpf, ex);
            throw ex;
        }
    }


    @Transactional
    public ClienteDto create(ClienteDto clienteDto) {
        logger.info("Iniciando criação de cliente.");
        try {
            ClienteModel cliente = converter.convertToModel(clienteDto);

            Set<HotelCaliforniaModel> hotels = clienteDto.getCnpjs().stream()
                    .map(cnpj -> hotelRepository.findByCnpjWithClientes(cnpj)
                            .orElseThrow(() -> new IllegalArgumentException("Hotel com CNPJ " + cnpj + " não encontrado.")))
                    .collect(Collectors.toSet());

            cliente.setHotels(hotels);
            ClienteModel savedCliente = clienteRepository.save(cliente);

            logger.info("Cliente criado com sucesso: {}", savedCliente);
            return converter.convertToDto(savedCliente);
        } catch (DataIntegrityViolationException ex) {
            logger.error("Violação de integridade ao salvar o cliente: {}", clienteDto, ex);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Dados inválidos ou duplicados para o cliente.", ex);
        } catch (IllegalArgumentException ex) {
            logger.error("Erro de validação ao criar cliente: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a criação do cliente.", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar o cliente no banco de dados.", ex);
        }
    }

    @Transactional
    public ClienteDto update(String cpf, @Valid ClienteDto clienteDto) {
        logger.info("Iniciando atualização do cliente com CPF: {}", cpf);
        try {
            ClienteModel existingCliente = clienteRepository.findByCpf(cpf)
                    .orElseThrow(() -> new ClienteNotFoundException("Cliente com CPF " + cpf + " não encontrado."));

            existingCliente = converter.converToModelUpdate(existingCliente, clienteDto, cpf);

            ClienteModel updatedCliente = clienteRepository.save(existingCliente);
            logger.info("Cliente atualizado com sucesso: {}", updatedCliente);
            return converter.convertToDto(updatedCliente);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a atualização do cliente.", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar o cliente no banco de dados.", ex);
        }
    }

    @Transactional
    public void delete(UUID id) {
        logger.info("Iniciando exclusão do cliente com ID: {}", id);
        try {
            if (!clienteRepository.existsById(id)) {
                throw new ClienteNotFoundException("Cliente com ID " + id + " não encontrado.");
            }
            clienteRepository.deleteById(id);
            logger.info("Cliente com ID {} excluído com sucesso.", id);
        } catch (DataAccessException ex) {
            logger.error("Erro ao acessar o banco de dados durante a exclusão do cliente.", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao excluir o cliente no banco de dados.", ex);
        }
    }
}
