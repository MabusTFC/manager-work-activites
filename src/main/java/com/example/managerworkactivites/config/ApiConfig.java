package com.example.managerworkactivites.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Manager Work Activities API")
                        .description("REST API для практического задания Интек")
                        .contact(new Contact()
                                .name("mabus")
                                .url("https://github.com/MabusTFC"))


                );
    }
}