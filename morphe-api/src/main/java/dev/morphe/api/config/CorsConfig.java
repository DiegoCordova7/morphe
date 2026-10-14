package dev.morphe.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global Cross-Origin Resource Sharing (CORS) configuration for the Morphe API.
 *
 * <p>Allows configuring allowed origins dynamically via application properties
 * (e.g., restricting production traffic specifically to the GitHub Pages frontend domain),
 * while defaulting to wildcard for local development ease.</p>
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Value("${morphe.cors.allowed-origins:*}")
  private String allowedOrigins;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOrigins(allowedOrigins.split(","))
        .allowedMethods("GET", "POST", "OPTIONS")
        .allowedHeaders("*");
  }
}