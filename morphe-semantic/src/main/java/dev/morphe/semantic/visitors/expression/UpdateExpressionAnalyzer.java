package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.expression.UpdateExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.InvalidUpdateOperandException;
import dev.morphe.semantic.exception.expression.InvalidUpdateTargetException;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;
import dev.morphe.semantic.visitors.context.SemanticContext;

/**
 * Analyzes update expressions.
 *
 * <p>An update expression requires a mutable numeric variable or parameter.
 * The result of the expression has the same type as its operand.</p>
 *
 * <p>Both increment and decrement operations follow the same semantic
 * rules.</p>
 */
public final class UpdateExpressionAnalyzer {

  /**
   * Analyzes an update expression.
   *
   * <p>The operand must be a variable expression whose resolved symbol is
   * mutable and whose type is numeric.</p>
   *
   * @param expression the update expression being analyzed
   * @param context the semantic context
   * @return the type produced by the update expression
   * @throws NullSemanticArgumentException if {@code expression} or
   *     {@code context} is null
   * @throws InvalidUpdateTargetException if the operand does not reference
   *     a mutable symbol
   * @throws InvalidUpdateOperandException if the operand type is not
   *     numeric
   */
  public IASTType analyze(UpdateExpression expression, SemanticContext context) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    if (context == null) throw new NullSemanticArgumentException("context");
    if (!(expression.operand() instanceof VariableExpression variable))
      throw new InvalidUpdateTargetException("<expression>");
    Symbol symbol = context.symbols().resolve(variable.name());
    if (symbol.kind() != SymbolKind.VARIABLE && symbol.kind() != SymbolKind.PARAMETER)
      throw new InvalidUpdateTargetException(variable.name());
    context.symbol(variable, symbol);
    if (symbol.kind() != SymbolKind.VARIABLE && symbol.kind() != SymbolKind.PARAMETER)
      throw new InvalidUpdateTargetException(variable.name());
    IASTType operandType = symbol.type();
    if (operandType != PrimitiveType.INTEGER && operandType != PrimitiveType.DOUBLE)
      throw new InvalidUpdateOperandException(operandType);
    return operandType;
  }
}