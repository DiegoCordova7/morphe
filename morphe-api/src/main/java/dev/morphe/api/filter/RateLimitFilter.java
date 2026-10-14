package dev.morphe.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory rate limiting (no external dependencies), good enough
 * for a single instance on a free tier. If you scale to multiple instances,
 * this should move to Redis or similar.
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {

  private static final int MAX_REQUESTS_PER_MINUTE = 10;
  private static final String PROTECTED_PATH = "/api/morphe";

  private final ConcurrentHashMap<String, Deque<Long>> requestHistoryByIp = new ConcurrentHashMap<>();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    if (!request.getRequestURI().startsWith(PROTECTED_PATH)) {
      chain.doFilter(request, response);
      return;
    }

    String ip = request.getRemoteAddr();
    long now = Instant.now().getEpochSecond();
    Deque<Long> timestamps = requestHistoryByIp.computeIfAbsent(ip, k -> new ArrayDeque<>());
    synchronized (timestamps) {
      while (!timestamps.isEmpty() && now - timestamps.peekFirst() > 60) timestamps.pollFirst();
      if (timestamps.size() >= MAX_REQUESTS_PER_MINUTE) {
        response.setStatus(429);
        response.setContentType("application/json");
        response.getWriter().write(
            "{\"success\":false,\"error\":\"Too many requests, please wait a moment\"}");
        return;
      }
      timestamps.addLast(now);
    }
    chain.doFilter(request, response);
  }
}