package dev.morphe.semantic.visitors.declaration;

import dev.morphe.ast.declaration.FunctionDeclaration;
import dev.morphe.ast.declaration.Parameter;
import dev.morphe.ast.type.FunctionType;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.scope.ScopeKind;
import dev.morphe.semantic.visitors.SemanticStatementVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;

import java.util.ArrayList;
import java.util.List;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of function declarations.
 *
 * <p>A function declaration introduces a named function into the semantic
 * environment. This analyzer extracts the declared parameter types and
 * return type, constructs the corresponding {@link FunctionType}, and
 * registers the function as a {@link SymbolKind#FUNCTION} symbol in the
 * current semantic scope.</p>
 *
 * <p>This analyzer is responsible for the declaration-level registration
 * of the function. Analysis of parameters, the function body, statements,
 * expressions, and patterns is delegated to their corresponding semantic
 * analysis components.</p>
 */
public final class FunctionDeclarationAnalyzer {

  private final SemanticStatementVisitor statementVisitor;

  public FunctionDeclarationAnalyzer(SemanticStatementVisitor statementVisitor) {
    if (statementVisitor == null) throw new NullSemanticArgumentException("statementVisitor");
    this.statementVisitor = statementVisitor;
  }

  /**
   * Analyzes and registers a function declaration.
   *
   * <p>The declared parameter types and return type are used to construct
   * a {@link FunctionType}. The resulting function type is then associated
   * with a {@link Symbol} and defined in the current semantic scope.</p>
   *
   * @param declaration the function declaration being analyzed
   * @param context the shared semantic analysis context
   * @return always {@code null}
   * @throws NullSemanticArgumentException if {@code declaration} or {@code context} is {@code null}
   */
  public Void analyze(FunctionDeclaration declaration, SemanticContext context) {
    validateArguments(declaration, "declaration", context);
    List<IASTType> parameterTypes = declaration.parameters()
        .stream()
        .map(Parameter::type)
        .toList();
    FunctionType type = new FunctionType(parameterTypes, declaration.returnType());
    Symbol function = new Symbol(declaration.name(), SymbolKind.FUNCTION, type, false);
    context.symbols().define(function);
    context.symbol(declaration, function);
    context.enterScope(ScopeKind.FUNCTION);
    try {
      List<Symbol> parameterSymbols = new ArrayList<>();
      for (Parameter parameter : declaration.parameters()) {
        Symbol symbol = new Symbol(parameter.name(), SymbolKind.PARAMETER, parameter.type(), false);
        context.symbols().define(symbol);
        parameterSymbols.add(symbol);
      }
      context.parameterSymbols(declaration, parameterSymbols);
      statementVisitor.analyze(declaration.body(), context);
      return null;
    } finally {
      context.exitScope();
    }
  }
}