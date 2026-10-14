package dev.morphe.semantic.visitors.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.OrPattern;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.visitors.SemanticPatternVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of or patterns in the Morphe AST.
 *
 * <p>An or pattern represents multiple alternative patterns that are
 * evaluated against the same expected type. Each contained pattern is
 * therefore analyzed using the same {@link SemanticContext}.</p>
 *
 * <p>Pattern-specific semantic rules are delegated to the corresponding
 * pattern analyzers through the provided {@link SemanticPatternVisitor}.</p>
 */
public final class OrPatternAnalyzer {

  /**
   * Analyzes an or pattern and all of its alternatives.
   *
   * @param pattern the or pattern being analyzed
   * @param context the shared semantic analysis context
   * @param visitor the shared pattern visitor used to analyze nested patterns
   * @return always {@code null}
   * @throws NullSemanticArgumentException if {@code pattern}, {@code context},
   *     or {@code visitor} is null
   */
  public Void analyze(OrPattern pattern, SemanticContext context, SemanticPatternVisitor visitor) {
    validateArguments(pattern, "pattern", context);
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    for (IPattern alternative : pattern.patterns()) visitor.analyze(alternative, context);
    return null;
  }
}