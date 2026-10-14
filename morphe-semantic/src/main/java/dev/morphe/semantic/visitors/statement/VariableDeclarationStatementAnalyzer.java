package dev.morphe.semantic.visitors.statement;

import dev.morphe.ast.statement.VariableDeclarationStatement;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.statement.MissingInitializerException;
import dev.morphe.semantic.exception.type.IncompatibleTypesException;
import dev.morphe.semantic.result.StatementResult;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;
import dev.morphe.semantic.type.TypeResolver;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

public final class VariableDeclarationStatementAnalyzer {

  public StatementResult analyze(VariableDeclarationStatement statement, SemanticContext context,
                                 SemanticExpressionVisitor visitor) {
    validateArguments(statement, "statement", context);
    if (statement.isConstant() && statement.initializer() == null)
      throw new MissingInitializerException(statement.name());
    IASTType declaredType = new TypeResolver(context.symbols()).resolve(statement.type());
    SymbolKind kind = statement.isConstant() ? SymbolKind.CONSTANT : SymbolKind.VARIABLE;
    Symbol symbol = new Symbol(statement.name(), kind, declaredType, false);
    context.symbols().define(symbol);
    context.symbol(statement, symbol);
    if (statement.initializer() != null) {
      IASTType initializerType = statement.initializer().accept(visitor, context).type();
      if (!context.checker().areCompatible(initializerType, declaredType))
        throw new IncompatibleTypesException(declaredType, initializerType);
    }
    return new StatementResult(false);
  }
}