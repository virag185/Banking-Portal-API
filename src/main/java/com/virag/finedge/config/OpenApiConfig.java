package com.virag.finedge.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Banking Portal API",
                version = "1.0",
                description = "REST API for Banking Portal built using Spring Boot",
                contact = @Contact(
                        name = "Virag Khade",
                        email = "khadevirag5@gmail.com",
                        url = "https://github.com/virag185"
                ),
                license = @License(
                        name = "MIT License"
                )
        ),
        servers = {
                @Server(
                        description = "Local Server",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApiConfig {
}