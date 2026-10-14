package dev.morphe.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Configuration class responsible for setting up execution-related beans
 * for the Morphe interpreter engine.
 */
@Configuration
public class MorpheExecutionConfig {

  /**
   * Creates a dedicated, bounded thread pool for running untrusted user code.
   *
   * <p>By isolating execution in this pool, hanging tasks or infinite loops
   * will not consume the servlet container threads required to serve incoming HTTP requests.</p>
   *
   * @param poolSize Maximum number of concurrent execution threads (configured via properties).
   * @return A configured daemon {@link ExecutorService}.
   */
  @Bean
  public ExecutorService morpheExecutorService(
      @Value("${morphe.execution.pool-size:4}") int poolSize) {
    return Executors.newFixedThreadPool(poolSize, runnable -> {
      Thread thread = new Thread(runnable, "morphe-exec-worker");
      thread.setDaemon(true); // Ensures daemon threads do not block clean JVM shutdowns
      return thread;
    });
  }
}