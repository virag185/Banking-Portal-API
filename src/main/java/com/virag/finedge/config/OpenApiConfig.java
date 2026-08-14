package com.virag.finedge.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI finEdgeOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("FinEdge Banking API")
                                .description(
                                        "Secure digital banking REST API " +
                                        "for managing accounts, deposits, " +
                                        "withdrawals, transfers and transactions."
                                )
                                .version("1.0.0")
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}