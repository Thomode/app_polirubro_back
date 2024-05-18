package app.polirubro.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Configuration
public class OpenAPIConfig {

    final String securitySchemeName = "bearerAuth";

    @Bean
    public OpenAPI customizeOpenAPI(Environment env){
        OpenAPI openAPI = new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));

        String localServerUrl = String.format("http://localhost:%s", env.getProperty("server.port"));
        Server localServer = new Server().url(localServerUrl)
                .description("Local Server URL");
        openAPI.addServersItem(localServer);

        return openAPI;
    }

}
