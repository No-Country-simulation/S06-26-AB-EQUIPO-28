package tech.nocountry.talent.appbitservice.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Docs URLs:
 * <ul>
 *   <li>Scalar UI: http://localhost:8089/scalar</li>
 *   <li>OpenAPI JSON: http://localhost:8089/v3/api-docs</li>
 *   <li>Grouped V1: http://localhost:8089/v3/api-docs/v1</li>
 * </ul>
 */
@Configuration
public class OpenApiConfiguration {
    /**
     * Global OpenAPI configuration with security schemes.
     *
     * @return OpenAPI configuration bean
     */
    @Bean
    public OpenAPI globalOpenApi() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("APP BIT API")
                        .description("""
                            <div align="center">
                                <h2>Bienvenido a la API Restful App Bit | TEAM NO COUNTRY #28</h2>
                            </div>
                            
                            <hr>
                            
                            Documentación oficial para la integración con el sistema App Bit.
                            Todos los endpoints disponibles para el negocio.
                            <br><br>
                            
                            <img src="https://cdn.prod.website-files.com/60b6aab79e7b19b0ad7df147/611d722cbabae14dffd2d0a5_ecommerce%20b2g.png" alt="B2G" width="100%" style="border-radius: 8px;"/>
                            """
                        )
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("No Country #28 Team")
                                .email("support@team28.com")
                                .url("https://appbit.team28.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token. Obtén uno en POST `/api/v1/auth/sing-in`")));
    }

    /**
     * Customizador global que agrega respuestas de error estandar (RFC 7807)
     * a todos los paths de la API.
     *
     * @return OpenApiCustomizer bean
     */
    @Bean
    public OpenApiCustomizer globalOpenApiCustomizer() {
        return openApi -> {
            Schema<?> problemDetailSchema = new Schema<>()
                .name("ProblemDetail")
                .type("object")
                .description("Cuerpo de error estándar RFC 7807 (Problem Detail)")
                .addProperty("type", new StringSchema().description("URI que identifica el tipo de error"))
                .addProperty("title", new StringSchema().description("Título corto del error"))
                .addProperty("status", new IntegerSchema().description("Código HTTP status"))
                .addProperty("detail", new StringSchema().description("Descripción detallada del error"))
                .addProperty("instance", new StringSchema().description("URI del endpoint que generó el error"));

            if (openApi.getComponents() == null) {
                openApi.setComponents(new Components());
            }
            openApi.getComponents().addSchemas("ProblemDetail", problemDetailSchema);

            Schema<?> problemSchema = new Schema<>().$ref("#/components/schemas/ProblemDetail");
            MediaType problemMediaType = new MediaType().schema(problemSchema);
            Content problemContent = new Content().addMediaType("application/problem+json", problemMediaType);

            ApiResponse badRequest = new ApiResponse().description("Datos inválidos o regla de negocio fallida").content(problemContent);
            ApiResponse unauthorized = new ApiResponse().description("No autenticado (Token faltante o inválido)").content(problemContent);
            ApiResponse forbidden = new ApiResponse().description("No tienes permisos para esta acción").content(problemContent);
            ApiResponse serverError = new ApiResponse().description("Error interno del servidor").content(problemContent);

            if (openApi.getPaths() != null) {
                openApi.getPaths().values().forEach(pathItem -> {
                    pathItem.readOperations().forEach(operation -> {
                        ApiResponses resp = operation.getResponses();
                        resp.putIfAbsent("400", badRequest);
                        resp.putIfAbsent("401", unauthorized);
                        resp.putIfAbsent("403", forbidden);
                        resp.putIfAbsent("500", serverError);
                    });
                });
            }
        };
    }
}