package dev.morphe.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller responsible for system health and liveness checks.
 *
 * <p>Used by the frontend playground or hosting infrastructure to verify
 * that the Morphe API service is up, running, and reachable.</p>
 */
@RestController
public class HealthController {

  /**
   * Simple health check endpoint.
   *
   * @return A map containing a basic status indicator ("status": "ok").
   */
  @GetMapping("/api/health")
  public Map<String, String> health() {
    return Map.of("status", "ok");
  }
}