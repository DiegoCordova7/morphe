package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.expression.CompositeInstanceExpression;
import dev.morphe.ast.type.CompositeType;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.DuplicateFieldInitializerException;
import dev.morphe.semantic.exception.expression.FieldNotFoundException;
import dev.morphe.semantic.exception.expression.IncompatibleFieldInitializerException;
import dev.morphe.semantic.exception.expression.InvalidCompositeInstanceException;
import dev.morphe.semantic.exception.expression.MissingFieldInitializerException;
import dev.morphe.semantic.exception.expression.UndefinedTypeException;
import dev.morphe.semantic.symbol.*;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Analyzes composite instance expressions.
 *
 * <p>The referenced type must resolve to a {@link CompositeType}. Each field
 * initializer must reference an existing field exactly once and its value
 * must be compatible with the declared field type. All declared fields must
 * be initialized.</p>
 *
 * <p>The result of the analysis is the {@link CompositeType} represented by
 * the instance expression.</p>
 */
public final class CompositeInstanceExpressionAnalyzer {

  private final SemanticExpressionVisitor visitor;

  /**
   * Creates a composite instance expression analyzer.
   *
   * @param visitor the semantic expression visitor used to analyze field
   *     initializer values
   * @throws NullSemanticArgumentException if {@code visitor} is null
   */
  public CompositeInstanceExpressionAnalyzer(SemanticExpressionVisitor visitor) {
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    this.visitor = visitor;
  }

  /**
   * Analyzes a composite instance expression.
   *
   * @param expression the composite instance expression being analyzed
   * @param context the semantic context
   * @return the composite type instantiated by the expression
   * @throws NullSemanticArgumentException if {@code expression} or
   *     {@code context} is null
   * @throws UndefinedTypeException if the referenced type does not exist
   * @throws InvalidCompositeInstanceException if the referenced symbol is
   *     not a composite type
   * @throws FieldNotFoundException if an initializer references an unknown
   *     field
   * @throws DuplicateFieldInitializerException if a field is initialized
   *     more than once
   * @throws IncompatibleFieldInitializerException if an initializer value
   *     is incompatible with its field type
   * @throws MissingFieldInitializerException if a declared field has no
   *     initializer
   */
  public CompositeType analyze(CompositeInstanceExpression expression, SemanticContext context) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    if (context == null) throw new NullSemanticArgumentException("context");
    CompositeType compositeType = resolveCompositeType(expression, context);
    validateInitializers(expression, compositeType, context);
    validateMissingFields(expression, compositeType);
    return compositeType;
  }

  private CompositeType resolveCompositeType(CompositeInstanceExpression expression, SemanticContext context) {
    Symbol symbol = context.symbols().resolve(expression.typeName());
    if (symbol == null) throw new UndefinedTypeException(expression.typeName());
    if (symbol.kind() != SymbolKind.TYPE || !(symbol.type() instanceof CompositeType compositeType))
      throw new InvalidCompositeInstanceException(expression.typeName(), symbol.type());
    return compositeType;
  }

  private void validateInitializers(CompositeInstanceExpression expression, CompositeType compositeType,
                                    SemanticContext context) {
    Set<String> initializedFields = new HashSet<>();
    for (CompositeInstanceExpression.FieldInitializer initializer : expression.fields()) {
      CompositeType.CompositeField field = findField(compositeType, initializer.name());
      if (!initializedFields.add(initializer.name()))
        throw new DuplicateFieldInitializerException(initializer.name());
      IASTType valueType = initializer.value().accept(visitor, context).type();
      if (!context.checker().areCompatible(valueType, field.type()))
        throw new IncompatibleFieldInitializerException(initializer.name(), field.type(), valueType);
    }
  }

  private CompositeType.CompositeField findField(CompositeType compositeType, String name) {
    return compositeType.fields()
        .stream()
        .filter(field -> field.name().equals(name))
        .findFirst()
        .orElseThrow(() -> new FieldNotFoundException(name, compositeType.name()));
  }

  private void validateMissingFields(CompositeInstanceExpression expression, CompositeType compositeType) {
    Set<String> initializedFields = expression.fields()
        .stream()
        .map(CompositeInstanceExpression.FieldInitializer::name)
        .collect(Collectors.toSet());
    compositeType.fields().stream()
        .filter(field -> !initializedFields.contains(field.name()))
        .findFirst()
        .ifPresent(field -> {
          throw new MissingFieldInitializerException(field.name(), compositeType.name());
        });
  }
}