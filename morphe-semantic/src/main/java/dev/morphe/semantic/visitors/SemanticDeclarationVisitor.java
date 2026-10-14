package dev.morphe.semantic.visitors;

import dev.morphe.ast.declaration.*;
import dev.morphe.ast.visitor.IDeclarationVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;
import dev.morphe.semantic.visitors.declaration.*;

/**
 * Coordinates semantic analysis of declaration nodes in the Morphe AST.
 *
 * <p>This visitor dispatches each declaration to the semantic analyzer
 * responsible for its specific declaration kind. The visitor itself does
 * not perform declaration-specific semantic validation or symbol
 * registration.</p>
 *
 * <p>Declaration analyzers operate on the shared {@link SemanticContext},
 * which provides access to the symbol table, type resolver, type checker,
 * and other state required during semantic analysis.</p>
 *
 * <p>Analysis of nested constructs, such as function parameters, function
 * bodies, statements, expressions, and patterns, is delegated to their
 * corresponding semantic visitors and analyzers.</p>
 */
public final class SemanticDeclarationVisitor implements IDeclarationVisitor<SemanticContext> {

  private final CompositeDeclarationAnalyzer compositeDeclaration;
  private final FunctionDeclarationAnalyzer functionDeclaration;

  public SemanticDeclarationVisitor(SemanticStatementVisitor statementVisitor) {
    compositeDeclaration = new CompositeDeclarationAnalyzer();
    functionDeclaration = new FunctionDeclarationAnalyzer(statementVisitor);
  }

  /**
   * Dispatches a function declaration to its semantic analyzer.
   *
   * <p>The analyzer is responsible for performing all semantic operations
   * specific to the function declaration.</p>
   *
   * @param declaration the function declaration being analyzed
   * @param semanticContext the shared semantic analysis context
   * @return always {@code null}
   */
  @Override
  public Void visit(FunctionDeclaration declaration, SemanticContext semanticContext) {
    return functionDeclaration.analyze(declaration, semanticContext);
  }

  /**
   * Dispatches a composite declaration to its semantic analyzer.
   *
   * <p>The analyzer is responsible for performing all semantic operations
   * specific to the composite declaration.</p>
   *
   * @param declaration the composite declaration being analyzed
   * @param semanticContext the shared semantic analysis context
   * @return always {@code null}
   */
  @Override
  public Void visit(CompositeDeclaration declaration, SemanticContext semanticContext) {
    return compositeDeclaration.analyze(declaration, semanticContext);
  }
}