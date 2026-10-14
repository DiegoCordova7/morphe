package dev.morphe.semantic.visitors.pattern;

import dev.morphe.ast.pattern.LiteralPattern;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.type.IncompatibleTypesException;
import dev.morphe.semantic.type.LiteralTypeResolver;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of {@link LiteralPattern} nodes.
 *
 * <p>A literal pattern is valid when the type of its literal value is
 * compatible with the type expected by the surrounding pattern context.</p>
 *
 * <p>The literal type is determined by {@link LiteralTypeResolver}. The
 * resulting type is then compared with the expected type through the
 * semantic type checker.</p>
 *
 * <p>This analyzer does not modify the semantic symbol table because a
 * literal pattern does not introduce a new binding.</p>
 */
public final class LiteralPatternAnalyzer {

  /**
   * Analyzes a literal pattern against its expected type.
   *
   * @param pattern the literal pattern being analyzed
   * @param context the pattern analysis context containing the expected type
   * @return always {@code null}
   * @throws NullSemanticArgumentException if {@code pattern} or {@code context} is null
   * @throws IncompatibleTypesException if the literal type is not compatible
   *     with the expected pattern type
   */
  public Void analyze(LiteralPattern pattern, SemanticContext context) {
    validateArguments(pattern, "pattern", context);
    IASTType expectedType = context.expectedType();
    IASTType actualType = new LiteralTypeResolver().resolve(pattern.value());
    if (!context.checker().areCompatible(actualType, expectedType))
      throw new IncompatibleTypesException(expectedType, actualType);
    return null;
  }
}