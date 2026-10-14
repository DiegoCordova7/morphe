package dev.morphe.semantic.visitors.expression;

import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.symbol.InvalidSymbolKindException;
import dev.morphe.semantic.exception.symbol.UnknownSymbolException;
import dev.morphe.semantic.result.VariableResult;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;
import dev.morphe.semantic.symbol.SymbolTable;
import dev.morphe.semantic.visitors.context.SemanticContext;

/**
 * Performs semantic analysis of {@link VariableExpression} nodes.
 *
 * <p>The analyzer resolves the variable name through the
 * {@link SymbolTable} associated with the supplied {@link SemanticContext}
 * and returns the semantic information associated with the resolved symbol.</p>
 *
 * <p>Variable expressions may reference symbols representing variables,
 * constants, parameters, or functions. Named types cannot be referenced
 * as variable expressions.</p>
 *
 * <p>Symbol resolution respects the lexical scope rules implemented by the
 * {@link SymbolTable}, including resolution through enclosing scopes.</p>
 *
 * <p>This analyzer does not perform type compatibility checks. Its
 * responsibility is limited to resolving the referenced symbol and obtaining
 * the information required to determine the semantic properties of the
 * expression.</p>
 */
public final class VariableExpressionAnalyzer {

  /**
   * Resolves the symbol referenced by a variable expression.
   *
   * <p>The referenced symbol must represent a variable, constant, parameter,
   * or function. Symbols representing named types are rejected because they
   * cannot be used as variable expressions.</p>
   *
   * @param expression the variable expression to analyze
   * @param context the semantic analysis context
   * @return the semantic information associated with the resolved symbol
   * @throws NullSemanticArgumentException if {@code expression} or
   *     {@code context} is null
   * @throws UnknownSymbolException if the referenced symbol cannot be found
   * @throws InvalidSymbolKindException if the resolved symbol cannot be used
   *     as a variable expression
   */
  public VariableResult analyze(VariableExpression expression, SemanticContext context) {
    if (expression == null) throw new NullSemanticArgumentException("expression");
    if (context == null) throw new NullSemanticArgumentException("context");
    Symbol symbol = context.symbols().resolve(expression.name());
    if (symbol == null) throw new UnknownSymbolException(expression.name());
    SymbolKind kind = symbol.kind();
    if (kind != SymbolKind.VARIABLE && kind != SymbolKind.CONSTANT
        && kind != SymbolKind.PARAMETER && kind != SymbolKind.FUNCTION) {
      throw new InvalidSymbolKindException(expression.name(), kind);
    }
    context.symbol(expression, symbol);
    return new VariableResult(symbol.type(), kind);
  }
}