package com.hr.hrapplication.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI hrOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HR Service API")
                        .version("v1")
                        .description("Employee directory + XML import")
                        .contact(new Contact().name("Platform Team").email("platform@example.com"))
                        .license(new License().name("Apache 2.0")))
                .servers(List.of(new Server().url("http://localhost:8080").description("Local")));

    }

    @Bean
    public GroupedOpenApi employeesGroup() {
        return GroupedOpenApi.builder()
                .group("employee")
                .pathsToMatch("/api/employee/**", "/api/employees/**")
                .build();
    }

    @Bean
    public GroupedOpenApi importGroup() {
        return GroupedOpenApi.builder()
                .group("batch import")
                .pathsToMatch("/api/import/**")
                .build();
    }
}
