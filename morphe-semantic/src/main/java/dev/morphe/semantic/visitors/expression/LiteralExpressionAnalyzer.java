package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.type.LiteralTypeResolver;

/**
 * Performs semantic analysis of {@link LiteralExpression} nodes.
 *
 * <p>The analyzer determines the Morphe AST type represented by a literal
 * expression by delegating its Java value to a {@link LiteralTypeResolver}.
 * The resolver maps supported literal values to their corresponding
 * {@link IASTType} instances.</p>
 *
 * <p>This analyzer does not perform type compatibility checks or validate
 * surrounding expressions. Its responsibility is limited to determining
 * the type represented by the literal.</p>
 *
 * <p>For example, the following literals are resolved as:</p>
 *
 * <pre>{@code
 * 42       -> PrimitiveType.INTEGER
 * 3.14     -> PrimitiveType.DOUBLE
 * "hello"  -> PrimitiveType.STRING
 * true     -> PrimitiveType.BOOLEAN
 * null     -> NullType.NULL
 * }</pre>
 */
public final class LiteralExpressionAnalyzer {

  private final LiteralTypeResolver resolver = new LiteralTypeResolver();

  /**
   * Determines the type represented by a literal expression.
   *
   * <p>The expression's value is passed to the configured
   * {@link LiteralTypeResolver}, which determines the corresponding
   * Morphe AST type.</p>
   *
   * @param expression the literal expression to analyze
   * @return the AST type represented by the literal
   * @throws NullSemanticArgumentException if {@code expression} is null
   */
  public IASTType analyze(LiteralExpression expression) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    return resolver.resolve(expression.value());
  }
}