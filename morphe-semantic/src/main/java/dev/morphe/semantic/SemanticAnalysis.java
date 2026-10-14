package dev.morphe.semantic;

import dev.morphe.ast.CompilationUnit;
import dev.morphe.ast.declaration.IDeclaration;
import dev.morphe.semantic.exception.SemanticException;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.natives.INativeFunction;
import dev.morphe.semantic.natives.PrintNativeFunction;
import dev.morphe.semantic.natives.ReadNativeFunction;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;
import dev.morphe.semantic.visitors.context.SemanticContext;

/**
 * Entry point for semantic analysis of a Morphe compilation unit.
 *
 * <p>This class provides the public API for semantic analysis and hides
 * the internal visitors and semantic context used during traversal.</p>
 *
 * <p>A successful analysis completes normally and guarantees that the
 * compilation unit satisfies the semantic rules checked by this phase.
 * Any semantic violation is reported by throwing a corresponding
 * {@code SemanticException}.</p>
 */
public final class SemanticAnalysis {

  private final SemanticAnalyzer analyzer;

  /**
   * Creates a semantic analysis service.
   */
  public SemanticAnalysis() {
    this.analyzer = new SemanticAnalyzer();
  }

  /**
   * Analyzes a compilation unit.
   *
   * <p>The compilation unit itself is not modified. The analysis operates
   * using a new semantic context for the duration of the analysis.</p>
   *
   * @param compilationUnit the compilation unit to analyze
   * @throws NullSemanticArgumentException if {@code compilationUnit} is null
   * @throws SemanticException if a semantic error is detected
   */
  public SemanticContext analyze(CompilationUnit compilationUnit) {
    if (compilationUnit == null) throw new NullSemanticArgumentException("compilationUnit");
    SemanticContext context = new SemanticContext();
    context.expressionVisitor(analyzer.expressionVisitor());
    context.statementVisitor(analyzer.statementVisitor());
    context.patternVisitor(analyzer.patternVisitor());
    for (IDeclaration declaration : compilationUnit.declarations()) declaration.accept(analyzer, context);
    return context;
  }
}