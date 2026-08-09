package com.subrata.studentmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI studentManagementOpenAPI() {

        Server localServer = new Server();

        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development Server");

        Contact contact = new Contact();

        contact.setName("Subrata Mallick");
        contact.setEmail("subrata@gmail.com");

        License license = new License();

        license.setName("Apache 2.0");
        license.setUrl("https://www.apache.org/licenses/LICENSE-2.0");

        Info apiInfo = new Info();

        apiInfo.setTitle("Student Management API");

        apiInfo.setDescription(
                "REST API for managing students with CRUD operations, " +
                        "validation, exception handling, pagination, sorting and search."
        );

        apiInfo.setVersion("1.0.0");

        apiInfo.setContact(contact);

        apiInfo.setLicense(license);

        return new OpenAPI()
                .info(apiInfo)
                .servers(List.of(localServer));
    }
}