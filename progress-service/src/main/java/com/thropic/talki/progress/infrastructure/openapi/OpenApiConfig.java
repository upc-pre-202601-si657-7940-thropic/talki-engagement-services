package com.thropic.talki.progress.infrastructure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI 3 para el progress-service.
 *
 * Publica la especificación en /v3/api-docs y la UI en /swagger-ui.html.
 * Cumple el criterio "RESTful API con documentación de cada servicio" de la
 * rúbrica TP1 del curso SI657. El servicio implementa el bounded context
 * Progress & Metrics (DDD) con separación CQRS — comandos actualizan la
 * vista materializada al consumir scoring.completed; queries entregan
 * dashboards de progreso histórico al frontend.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI progressServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Talki — Progress Service API")
                        .description("Microservicio del bounded context Progress & Metrics (DDD) del producto "
                                + "Talki. Aplica el patrón CQRS: el comando se ejecuta al consumir "
                                + "scoring.completed del exchange talki.events y actualiza la vista "
                                + "materializada UserProgress; las queries del frontend Angular consultan "
                                + "estadísticas históricas y comparativas de sesiones sin contención con "
                                + "el camino de escritura.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Thropic — Equipo Talki")
                                .url("https://github.com/upc-pre-202601-si657-7940-thropic"))
                        .license(new License()
                                .name("Académico — UPC SI657 2026-10")))
                .servers(List.of(
                        new Server().url("http://localhost:8089").description("Local (perfil dev)"),
                        new Server().url("https://api.talki.lat").description("Producción (Railway)")
                ));
    }
}
