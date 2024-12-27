package com.example.abhijournalwebapp.journalWebApplication.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

//We'll be adding a Swagger UI Configurations Here:

/*
Swagger UI is an open-source tool that lets users interact with and visualize RESTful APIs
(Application Programming Interfaces).
How it works : Swagger UI is automatically generated from an OpenAPI Specification, and it makes an
existing JSON or YAML document interactive
 */

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig(){
        //Adding all new custom configs of open-api and returning an object of it:
        return new OpenAPI().info(
                //Adding Title And Description Of OpenAPI Instance
                new Info().title("Journal Web Application APIs")
                        .description("By Abhirup")
        ).servers(
                //Adding different server ports for different profiles:
                Arrays.asList(new Server().url("http://localhost:8080/").description("local-dev"),
                        new Server().url("http://localhost:8081/").description("prod"))
        ).tags(
              //Adding All Tagged Controller in a sequence:
                Arrays.asList(
                        new Tag().name("Public APIs"),
                        new Tag().name("User APIs"),
                        new Tag().name("Journal Entry APIs"),
                        new Tag().name("Admin APIs"),
                        new Tag().name("Home APIs")
                )
        ).addSecurityItem(
                //Adding Config to Accept JWT token for authentication:
                //Indicating Type Of Auth And From Where To Grab The JWT Token:
                new SecurityRequirement().addList("bearerAuth")
                ).components(
                new Components().addSecuritySchemes(
                        "bearerAuth" , new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));
    }
}
