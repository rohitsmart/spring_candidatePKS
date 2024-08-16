package com.candidate.pks.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Rohit Kumar",
                        email = "rohit.cs95@outlook.com"
                ),
                description = "Candidate Management System REST API spring Boot 3.3.0",
                title = "CMS",
                version = "1.0",
                license = @License(
                        name = "CMS",
                        url = "Not Yet"
                ),
                termsOfService = "nothing to show"
        ),
        servers = {
                @Server(
                        description = "localhost  environment",
                        url = "http://localhost:9090"
                ),
                @Server(
                        description = "docker  environment",
                        url = "http://localhost:8081"
                )

        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth Bearer Token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAiConfig {
}


