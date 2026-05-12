package com.thropic.talki.gamification.infrastructure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI 3 para el gamification-service.
 *
 * Publica la especificación en /v3/api-docs y la UI en /swagger-ui.html.
 * Cumple el criterio "RESTful API con documentación de cada servicio" de la
 * rúbrica TP1 del curso SI657. El servicio implementa la lógica de streaks
 * y achievements: consume scoring.completed, calcula XP y secuencias de
 * sesiones consecutivas, y publica achievement.unlocked cuando un usuario
 * cruza umbrales (primera sesión, racha de 7 días, 1000 XP, etc.).
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gamificationServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Talki — Gamification Service API")
                        .description("Microservicio de engagement del producto Talki. Consume "
                                + "scoring.completed del exchange talki.events, mantiene la entidad "
                                + "UserStreak con la racha de sesiones consecutivas y los puntos XP "
                                + "acumulados, y publica achievement.unlocked cuando el usuario alcanza "
                                + "hitos predefinidos (primera sesión, racha de 7 días, racha de 30 días, "
                                + "1000 XP). Los logros son consumidos por notification-service para "
                                + "enviar push al cliente Angular.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Thropic — Equipo Talki")
                                .url("https://github.com/upc-pre-202601-si657-7940-thropic"))
                        .license(new License()
                                .name("Académico — UPC SI657 2026-10")))
                .servers(List.of(
                        new Server().url("http://localhost:8090").description("Local (perfil dev)"),
                        new Server().url("https://api.talki.lat").description("Producción (Railway)")
                ));
    }
}
