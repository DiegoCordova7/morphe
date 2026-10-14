package dev.morphe.semantic.visitors.pattern;

import dev.morphe.ast.pattern.DefaultPattern;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of default patterns in the Morphe AST.
 *
 * <p>A default pattern matches any value compatible with the surrounding
 * match expression. Therefore, no additional type validation is required
 * at the pattern level.</p>
 */
public final class DefaultPatternAnalyzer {

  /**
   * Analyzes a default pattern.
   *
   * @param pattern the default pattern being analyzed
   * @param context the pattern analysis context
   * @return always {@code null}
   */
  public Void analyze(DefaultPattern pattern, SemanticContext context) {
    validateArguments(pattern, "pattern", context);
    return null;
  }
}