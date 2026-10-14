package dev.morphe.semantic.visitors.context;

import dev.morphe.ast.IASTNode;
import dev.morphe.ast.declaration.FunctionDeclaration;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.natives.INativeFunction;
import dev.morphe.semantic.natives.NativeFunctionRegistry;
import dev.morphe.semantic.result.ExpressionResult;
import dev.morphe.semantic.result.StatementResult;
import dev.morphe.semantic.scope.Scope;
import dev.morphe.semantic.scope.ScopeKind;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;
import dev.morphe.semantic.symbol.SymbolTable;
import dev.morphe.semantic.type.*;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.SemanticPatternVisitor;
import dev.morphe.semantic.visitors.SemanticStatementVisitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the shared state and semantic analysis services used during
 * semantic analysis of a Morphe compilation unit.
 *
 * <p>The context owns the {@link SymbolTable} used to track declarations,
 * scopes, and symbol resolution. Type-related analysis is performed through
 * the {@link TypeResolver} and {@link TypeChecker} associated with the same
 * symbol table.</p>
 *
 * <p>All type analysis services exposed by this context operate on the same
 * symbol table, ensuring that named type resolution and type compatibility
 * checks observe the same semantic scope.</p>
 *
 * <p>The context also provides operations for entering and leaving lexical
 * scopes while preserving the shared semantic state.</p>
 */
public final class SemanticContext {

  private final NativeFunctionRegistry natives;
  private final Map<IASTNode, Symbol> symbolsByNode = new HashMap<>();
  private final Map<FunctionDeclaration, List<Symbol>> functionParameters = new HashMap<>();
  private final SymbolTable symbols;
  private final TypeResolver resolver;
  private final TypeChecker checker;
  private final Map<IExpression, ExpressionResult> expressionResults = new HashMap<>();
  private final Map<IExpression, StatementResult> statementResults = new HashMap<>();
  private IASTType expectedType;
  private SemanticPatternVisitor patternVisitor;
  private SemanticExpressionVisitor expressionVisitor;
  private SemanticStatementVisitor statementVisitor;
  /**
   * Creates a semantic context with a new symbol table and the type analysis
   * services associated with it.
   *
   * <p>The created {@link TypeResolver} and {@link TypeChecker} share the
   * symbol table owned by this context.</p>
   */
  public SemanticContext() {
    this.symbols = new SymbolTable();
    this.resolver = new TypeResolver(symbols);
    this.checker = new TypeChecker(symbols);
    this.natives = NativeFunctionRegistry.defaults();
    registerNativeSymbols();
  }

  /**
   * Returns the symbol table associated with this semantic context.
   *
   * @return the semantic symbol table
   */
  public SymbolTable symbols() {
    return symbols;
  }

  /**
   * Returns the type resolver associated with this semantic context.
   *
   * @return the type resolver
   */
  public TypeResolver resolver() {
    return resolver;
  }

  /**
   * Returns the type checker associated with this semantic context.
   *
   * @return the type checker
   */
  public TypeChecker checker() {
    return checker;
  }

  /**
   * Returns the currently active lexical scope.
   *
   * @return the current scope
   */
  public Scope scope() {
    return symbols.currentScope();
  }

  /**
   * Enters a new lexical scope of the specified kind.
   *
   * @param kind the kind of scope to enter
   */
  public void enterScope(ScopeKind kind) {
    symbols.enterScope(kind);
  }

  /**
   * Leaves the current lexical scope.
   *
   * <p>The global scope cannot be exited.</p>
   */
  public void exitScope() {
    symbols.exitScope();
  }

  /**
   * Returns the expected type currently supplied by the surrounding
   * semantic context.
   *
   * @return the expected type, or {@code null} if none is defined
   */
  public IASTType expectedType() {
    return expectedType;
  }

  /**
   * Sets the expected type supplied by the surrounding semantic context.
   *
   * @param expectedType the expected type
   */
  public void expectedType(IASTType expectedType) {
    this.expectedType = expectedType;
  }

  public void expressionResult(IExpression expression, ExpressionResult result) {
    if (expression == null) throw new IllegalArgumentException("expression cannot be null");
    if (result == null) throw new IllegalArgumentException("result cannot be null");
    expressionResults.put(expression, result);
  }

  public ExpressionResult expressionResult(IExpression expression) {
    if (expression == null) throw new IllegalArgumentException("expression cannot be null");
    ExpressionResult result = expressionResults.get(expression);
    if (result == null)
      throw new IllegalStateException("No semantic result found for expression: " + expression.getClass().getSimpleName());
    return result;
  }

  public void expressionVisitor(SemanticExpressionVisitor expressionVisitor) {
    if (expressionVisitor == null) throw new IllegalArgumentException("expressionVisitor cannot be null");
    this.expressionVisitor = expressionVisitor;
  }

  public SemanticExpressionVisitor expressionVisitor() {
    if (expressionVisitor == null) throw new IllegalStateException("Expression visitor has not been initialized");
    return expressionVisitor;
  }

  public void statementVisitor(SemanticStatementVisitor statementVisitor) {
    if (statementVisitor == null) throw new IllegalArgumentException("statementVisitor cannot be null");
    this.statementVisitor = statementVisitor;
  }

  public SemanticStatementVisitor statementVisitor() {
    if (statementVisitor == null) throw new IllegalStateException("Statement visitor has not been initialized");
    return statementVisitor;
  }

  public void patternVisitor(SemanticPatternVisitor patternVisitor) {
    if (patternVisitor == null) throw new IllegalArgumentException("patternVisitor cannot be null");
    this.patternVisitor = patternVisitor;
  }

  public SemanticPatternVisitor patternVisitor() {
    if (patternVisitor == null) throw new IllegalStateException("Pattern visitor has not been initialized");
    return patternVisitor;
  }

  public void symbol(IASTNode node, Symbol symbol) {
    if (node == null) throw new IllegalArgumentException("node cannot be null");
    if (symbol == null) throw new IllegalArgumentException("symbol cannot be null");
    symbolsByNode.put(node, symbol);
  }

  public Symbol symbol(IASTNode node) {
    if (node == null) throw new IllegalArgumentException("node cannot be null");
    Symbol symbol = symbolsByNode.get(node);
    if (symbol == null) {
      throw new IllegalStateException("No semantic symbol found for node: " + node.getClass().getSimpleName());
    }
    return symbol;
  }

  public void parameterSymbols(FunctionDeclaration declaration, List<Symbol> symbols) {
    functionParameters.put(declaration, symbols);
  }

  public List<Symbol> parameterSymbols(FunctionDeclaration declaration) {
    return functionParameters.get(declaration);
  }

  public NativeFunctionRegistry natives() {
    return natives;
  }

  private void registerNativeSymbols() {
    for (INativeFunction nativeFunction : natives.functions()) {
      symbols.define(new Symbol(nativeFunction.name(), SymbolKind.FUNCTION, nativeFunction.type(), true));
    }
  }
}