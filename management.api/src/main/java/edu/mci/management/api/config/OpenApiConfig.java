package edu.mci.management.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Game Server Management API")
                        .version("v1.0.0")
                        .description("This API enables the registration, discovery, and lifecycle management of game servers for 'The Amazing Labyrinth'. Game servers use this API to make themselves discoverable, while clients use it to find active games to join.")
                )
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Local development server")
                ));
    }
}
