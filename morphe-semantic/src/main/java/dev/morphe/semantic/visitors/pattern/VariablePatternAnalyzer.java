package dev.morphe.semantic.visitors.pattern;

import dev.morphe.ast.pattern.VariablePattern;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of variable reference patterns in the Morphe AST.
 *
 * <p>A variable pattern refers to an existing variable in the current semantic
 * scope. The value of that variable is used as the pattern value.</p>
 *
 * <p>For example, when matching an {@code Integer} value against the pattern
 * {@code x}, the analyzer resolves {@code x} and verifies that its type is
 * compatible with the type expected by the enclosing match.</p>
 */
public final class VariablePatternAnalyzer {

  public Void analyze(VariablePattern pattern, SemanticContext context) {
    validateArguments(pattern, "pattern", context);
    IASTType expectedType = context.expectedType();
    Symbol symbol = context.symbols().resolve(pattern.name());
    if (symbol.kind() != SymbolKind.VARIABLE)
      throw new IllegalStateException("Pattern variable does not reference a variable: " + pattern.name());
    if (symbol.type() != expectedType) throw new IllegalStateException("Pattern variable has incompatible type: " + pattern.name());
    context.symbol(pattern, symbol);
    return null;
  }
}