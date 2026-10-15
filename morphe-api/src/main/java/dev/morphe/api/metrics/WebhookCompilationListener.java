package dev.morphe.api.metrics;

import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.engine.listener.ICompilationListener;
import org.springframework.web.client.RestTemplate;

public class WebhookCompilationListener implements ICompilationListener {

  private static final String METRICS_URL = "http://localhost:8081/api/metrics/collect";
  private final RestTemplate restTemplate = new RestTemplate();

  @Override
  public void onCompilationSuccess(BytecodeProgram program) {
    try {
      restTemplate.postForEntity(METRICS_URL, program, Void.class);
    } catch (Exception e) {
      System.err.println("[Metrics Webhook] Servidor de métricas inactivo. Ignorando...");
    }
  }
}