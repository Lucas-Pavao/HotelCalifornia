package br.com.lucasPavao.hotelCalifornia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel California API")
                        .description("Projeto desenvolvido para o curso CUBO da empresa MV")
                        .version("1.0.0"));
    }
}
