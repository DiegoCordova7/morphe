package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.type.*;
import dev.morphe.ast.expression.FieldAccessExpression;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.FieldNotFoundException;
import dev.morphe.semantic.exception.expression.InvalidFieldAccessException;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import java.util.List;

/**
 * Analyzes field access expressions.
 *
 * <p>A field access expression consists of a target expression and a field
 * name. The target expression is first analyzed to determine its AST type.
 * The target must resolve to a composite type (either a named {@link CompositeType}
 * or an {@link AnonymousCompositeType}); otherwise, the field access is invalid.
 * </p>
 *
 * <p>Once the target has been resolved to a composite structure, the analyzer
 * searches for a field matching the requested name. If the field exists,
 * its declared AST type is returned. If no matching field is found, a
 * {@link FieldNotFoundException} is thrown.</p>
 */
public final class FieldAccessExpressionAnalyzer {

  private final SemanticExpressionVisitor visitor;

  /**
   * Creates a field access expression analyzer.
   *
   * @param visitor the semantic expression visitor used to analyze the target
   * @throws NullSemanticArgumentException if {@code visitor} is null
   */
  public FieldAccessExpressionAnalyzer(SemanticExpressionVisitor visitor) {
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    this.visitor = visitor;
  }

  /**
   * Analyzes a field access expression.
   *
   * <p>The target expression is resolved using the semantic expression
   * visitor. The resulting type must be a composite-like type. The analyzer
   * then resolves the requested field and returns its declared AST type.
   * </p>
   *
   * @param expression the field access expression being analyzed
   * @param context the semantic context used during analysis
   * @return the AST type of the accessed field
   * @throws NullSemanticArgumentException if {@code expression} or
   *     {@code context} is null
   * @throws InvalidFieldAccessException if the target expression does not
   *     resolve to a composite type
   * @throws FieldNotFoundException if the requested field is not declared
   *     by the target composite type
   */
  public IASTType analyze(FieldAccessExpression expression, SemanticContext context) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    if (context == null) throw new NullSemanticArgumentException("context");
    IASTType targetType = expression.target().accept(visitor, context).type();

    if (targetType instanceof ArrayType) {
      if (expression.field().equals("length")) return PrimitiveType.INTEGER;
      throw new InvalidFieldAccessException(expression.field(), targetType);
    }

    if (!isCompositeLike(targetType)) {
      throw new InvalidFieldAccessException(expression.field(), targetType);
    }

    List<CompositeType.CompositeField> fields = getCompositeFields(targetType);
    String compositeName = (targetType instanceof CompositeType comp) ? comp.name() : "$anonymous";

    return fields.stream()
        .filter(field -> field.name().equals(expression.field()))
        .map(CompositeType.CompositeField::type)
        .findFirst()
        .orElseThrow(() -> new FieldNotFoundException(expression.field(), compositeName));
  }

  /**
   * Determines whether an AST type behaves like a composite structure.
   */
  private boolean isCompositeLike(IASTType type) {
    return type instanceof CompositeType || type instanceof AnonymousCompositeType;
  }

  /**
   * Extracts the list of fields from a composite-like AST type.
   */
  private List<CompositeType.CompositeField> getCompositeFields(IASTType type) {
    if (type instanceof CompositeType comp) return comp.fields();
    if (type instanceof AnonymousCompositeType anon) return anon.fields();
    throw new IllegalStateException("Expected composite-like type, got: " + type.getClass().getSimpleName());
  }
}