package dev.morphe.semantic.visitors.pattern;

import dev.morphe.ast.pattern.ArrayPattern;
import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.type.ArrayType;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.pattern.UnsupportedPatternTypeException;
import dev.morphe.semantic.visitors.SemanticPatternVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of array patterns.
 *
 * <p>An array pattern is analyzed against an expected {@link ArrayType}.
 * Each element pattern is analyzed using the element type of the array.
 * If a rest pattern is present, it is also analyzed against the same
 * element type.</p>
 *
 * <p>The recursive analysis of contained patterns is delegated to the
 * shared {@link SemanticPatternVisitor}.</p>
 */
public final class ArrayPatternAnalyzer {

  /**
   * Analyzes an array pattern against its expected array type.
   *
   * <p>Each element pattern is analyzed using the element type of the
   * expected array type. If a rest pattern is present, it is analyzed
   * against the same element type.</p>
   *
   * @param pattern the array pattern being analyzed
   * @param context the shared semantic analysis context
   * @param visitor the shared pattern visitor used to analyze nested patterns
   * @return always {@code null}
   * @throws NullSemanticArgumentException if {@code pattern}, {@code context},
   *     or {@code visitor} is null
   * @throws UnsupportedPatternTypeException if the expected type is not
   *     an array type
   */
  public Void analyze(ArrayPattern pattern, SemanticContext context, SemanticPatternVisitor visitor) {
    validateArguments(pattern, "pattern", context);
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    IASTType expectedType = context.expectedType();
    if (!(expectedType instanceof ArrayType arrayType)) throw new UnsupportedPatternTypeException(expectedType);
    IASTType elementType = arrayType.elementType();
    IASTType previousType = context.expectedType();
    try {
      context.expectedType(elementType);
      for (IPattern element : pattern.elements()) visitor.analyze(element, context);
      if (pattern.rest() != null) visitor.analyze(pattern.rest(), context);
    } finally {
      context.expectedType(previousType);
    }
    return null;
  }
}