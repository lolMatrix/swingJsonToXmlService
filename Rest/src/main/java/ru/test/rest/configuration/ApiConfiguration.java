package ru.test.rest.configuration;

import com.google.common.base.Predicate;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * It response for configure Swagger
 */
@Configuration
@OpenAPIDefinition
public class ApiConfiguration {
    @Bean
    public GroupedOpenApi publicUserApi() {
        return GroupedOpenApi.builder()
                .group("client")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title("my api")
                .version("1.3.3.7")
                .description("demo app")
                .license(new License().name("Apache 2.0")
                        .url("http://springdoc.org"))
                .contact(new Contact().name("yes")
                        .email("no@yes.no")))
                .servers(List.of(new Server().url("http://localhost:8080")
                                .description("Rest service")));
    }
}
