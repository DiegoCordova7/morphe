package dev.morphe.semantic.visitors.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.LiteralPattern;
import dev.morphe.ast.pattern.RangePattern;
import dev.morphe.ast.pattern.VariablePattern;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.pattern.InvalidRangeException;
import dev.morphe.semantic.exception.pattern.UnsupportedRangeTypeException;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.visitors.SemanticPatternVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

public final class RangePatternAnalyzer {

  public Void analyze(RangePattern pattern, SemanticContext context, SemanticPatternVisitor visitor) {
    validateArguments(pattern, "pattern", context);
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    IASTType expectedType = context.expectedType();
    if (!isRangeType(expectedType)) throw new UnsupportedRangeTypeException(expectedType);
    validateBoundary(pattern.start(), expectedType, context);
    validateBoundary(pattern.end(), expectedType, context);
    validateBounds(pattern.start(), pattern.end());
    return null;
  }

  private boolean isRangeType(IASTType type) {
    return type == PrimitiveType.INTEGER || type == PrimitiveType.DOUBLE;
  }

  private void validateBoundary(IPattern boundary, IASTType expectedType, SemanticContext context) {
    if (boundary instanceof LiteralPattern literal) {
      validateLiteralBoundary(literal, expectedType);
      return;
    }
    if (boundary instanceof VariablePattern variable) {
      validateVariableBoundary(variable, expectedType, context);
      return;
    }
    throw new UnsupportedRangeTypeException(expectedType);
  }

  private void validateLiteralBoundary(LiteralPattern literal, IASTType expectedType) {
    Object value = literal.value();
    if (!(value instanceof Number)) throw new UnsupportedRangeTypeException(expectedType);
    IASTType actualType = resolveNumericType(value);
    if (actualType != expectedType) throw new UnsupportedRangeTypeException(expectedType);

  }

  private void validateVariableBoundary(VariablePattern variable, IASTType expectedType, SemanticContext context) {
    Symbol symbol = context.symbols().resolve(variable.name());
    IASTType actualType = symbol.type();
    if (actualType != expectedType) throw new UnsupportedRangeTypeException(actualType);
  }

  private IASTType resolveNumericType(Object value) {
    if (value instanceof Integer) return PrimitiveType.INTEGER;
    if (value instanceof Double) return PrimitiveType.DOUBLE;
    throw new IllegalStateException("Unsupported numeric literal: " + value);
  }

  private void validateBounds(IPattern start, IPattern end) {
    if (!(start instanceof LiteralPattern left)) return;
    if (!(end instanceof LiteralPattern right)) return;
    if (!(left.value() instanceof Number leftValue)) return;
    if (!(right.value() instanceof Number rightValue)) return;
    if (leftValue.doubleValue() > rightValue.doubleValue()) throw new InvalidRangeException(leftValue, rightValue);
  }
}