package dev.morphe.api;

import dev.morphe.api.metrics.WebhookCompilationListener;
import dev.morphe.engine.Engine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MorpheApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(MorpheApiApplication.class, args);
  }

  @Bean
  public CommandLineRunner registerMetricsListener() {
    return args -> {
      Engine.setCompilationListener(new WebhookCompilationListener());
      System.out.println("[Metrics] Webhook listener connected to the engine.");
    };
  }
}