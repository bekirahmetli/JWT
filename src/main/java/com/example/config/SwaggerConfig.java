package com.example.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI yapılandırmasını sağlayan konfigürasyon sınıfıdır.
 * Bu sınıf API dokümantasyonunun başlık, açıklama, versiyon gibi metadata bilgilerini
 * ve JWT ile güvenli istek yapılabilmesi için gerekli security scheme ayarlarını içerir.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("My API Documentation")
                        .version("1.0.0")
                        .description("Spring Boot projesi için Swagger/OpenAPI dokümantasyonu"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Token değerini girin:")
                        )
                );


    }
}
