package dev.morphe.api.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages active interactive code execution sessions for the Morphe playground,
 * ensuring thread-safe storage, retrieval, and removal of virtual machine instances.
 */
@Component
public class SessionManager {

  private final Map<String, MorpheExecutionService.ExecutionSession> sessions = new ConcurrentHashMap<>();

  /**
   * Stores an active execution session associated with a unique session identifier.
   *
   * @param sessionId The unique session string.
   * @param session   The execution session containing the engine and I/O handler.
   */
  public void save(String sessionId, MorpheExecutionService.ExecutionSession session) {
    sessions.put(sessionId, session);
  }

  /**
   * Retrieves an active execution session by its identifier.
   *
   * @param sessionId The unique session string.
   * @return The corresponding execution session, or null if not found or expired.
   */
  public MorpheExecutionService.ExecutionSession get(String sessionId) {
    return sessions.get(sessionId);
  }

  /**
   * Removes and cleans up an execution session once it has halted, timed out, or failed.
   *
   * @param sessionId The unique session string to remove.
   */
  public void remove(String sessionId) {
    sessions.remove(sessionId);
  }
}