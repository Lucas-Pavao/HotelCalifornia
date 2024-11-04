
# HotelCalifornia

Este repositório é destinado ao projeto "HotelCalifornia", parte do curso de aceleração de carreira CUBO da empresa MV. O projeto foi desenvolvido em Java 11 e Spring Boot 2.7.

## Descrição do Projeto

O HotelCalifornia é uma aplicação web que visa gerenciar operações de um hotel, incluindo reservas, check-in/check-out, gerenciamento de quartos, e muito mais. A aplicação foi projetada para ser escalável e fácil de manter, aproveitando as capacidades do Spring Boot para simplificar o desenvolvimento.

## Tecnologias Utilizadas

- **Java 11**: Linguagem de programação utilizada para o desenvolvimento da aplicação.
- **Spring Boot 2.7**: Framework que fornece uma plataforma para construir aplicações Java de forma rápida e fácil.
- **Banco de Dados**:  H2.

## Pré-requisitos

Antes de iniciar, verifique se você possui os seguintes itens instalados:

- JDK 11
- Maven (ou Gradle, dependendo da sua configuração de projeto)
- IDE (como IntelliJ IDEA, Eclipse, etc.)

## Inicialização do Projeto

Siga os passos abaixo para inicializar o projeto:

1. **Clone o repositório**

   ```bash
   git clone https://github.com/Lucas-Pavao/HotelCalifornia.git
   cd HotelCalifornia
   ```

2. **Instale as dependências**

   Se você estiver usando Maven, execute o seguinte comando:

   ```bash
   mvn install
   ```

   Se estiver usando Gradle, execute:

   ```bash
   ./gradlew build
   ```

3. **Execute a aplicação**

   Para iniciar a aplicação, use o comando Maven:

   ```bash
   mvn spring-boot:run
   ```

   Ou, se você estiver usando Gradle:

   ```bash
   ./gradlew bootRun
   ```

4. **Acesse a aplicação**

   Após a aplicação ser iniciada, você pode acessá-la através do seu navegador em: `http://localhost:8080`.

## Funcionalidades

- Gestão de Reservas
- Check-in e Check-out
- Gerenciamento de Quartos
