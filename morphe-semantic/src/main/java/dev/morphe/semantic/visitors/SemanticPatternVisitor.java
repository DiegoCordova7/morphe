package dev.morphe.semantic.visitors;

import dev.morphe.ast.pattern.*;
import dev.morphe.ast.visitor.IPatternVisitor;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.visitors.context.*;
import dev.morphe.semantic.visitors.pattern.*;
import dev.morphe.semantic.visitors.validator.SemanticArgumentValidator;

/**
 * Coordinates semantic analysis of pattern nodes in the Morphe AST.
 *
 * <p>This visitor dispatches each pattern to the semantic analyzer
 * responsible for its specific pattern kind.</p>
 *
 * <p>The visitor itself does not perform pattern-specific semantic
 * validation. That responsibility belongs to the corresponding
 * pattern analyzers.</p>
 *
 * <p>All analyzers operate using the shared {@link SemanticContext}.</p>
 */
public final class SemanticPatternVisitor implements IPatternVisitor<SemanticContext> {

  private final DefaultPatternAnalyzer defaultPattern = new DefaultPatternAnalyzer();
  private final LiteralPatternAnalyzer literalPattern = new LiteralPatternAnalyzer();
  private final OrPatternAnalyzer orPattern = new OrPatternAnalyzer();
  private final RangePatternAnalyzer rangePattern = new RangePatternAnalyzer();
  private final VariablePatternAnalyzer variablePattern = new VariablePatternAnalyzer();
  private final ArrayPatternAnalyzer arrayPattern = new ArrayPatternAnalyzer();
  private final CompositePatternAnalyzer compositePattern = new CompositePatternAnalyzer();

  /**
   * Performs semantic analysis of a pattern using the appropriate
   * pattern-specific analyzer.
   *
   * <p>This method provides a generic entry point for analyzing any
   * {@link IPattern}. The concrete pattern type is dispatched through
   * the visitor mechanism, while the corresponding analyzer performs
   * the pattern-specific semantic validation.</p>
   *
   * <p>This method is also used by pattern analyzers when recursively
   * analyzing nested patterns.</p>
   *
   * @param pattern the pattern being analyzed
   * @param context the pattern analysis context
   * @return always {@code null}
   * @throws NullSemanticArgumentException if {@code pattern} or {@code context} is null
   */
  public Void analyze(IPattern pattern, SemanticContext context) {
    SemanticArgumentValidator.validateArguments(pattern, "pattern", context);
    return pattern.accept(this, context);
  }

  /**
   * Dispatches a default pattern to its semantic analyzer.
   *
   * @param pattern the default pattern being analyzed
   * @param context the shared semantic analysis context
   * @return always {@code null}
   */
  @Override
  public Void visit(DefaultPattern pattern, SemanticContext context) {
    return defaultPattern.analyze(pattern, context);
  }

  /**
   * Dispatches a literal pattern to its semantic analyzer.
   *
   * @param pattern the literal pattern being analyzed
   * @param context the shared semantic analysis context
   * @return always {@code null}
   */
  @Override
  public Void visit(LiteralPattern pattern, SemanticContext context) {
    return literalPattern.analyze(pattern, context);
  }

  /**
   * Dispatches an or-pattern to its semantic analyzer.
   *
   * @param pattern the or-pattern being analyzed
   * @param context the shared semantic analysis context
   * @return always {@code null}
   */
  @Override
  public Void visit(OrPattern pattern, SemanticContext context) {
    return orPattern.analyze(pattern, context, this);
  }

  /**
   * Dispatches a range pattern to its semantic analyzer.
   *
   * @param pattern the range pattern being analyzed
   * @param context the shared semantic analysis context
   * @return always {@code null}
   */
  @Override
  public Void visit(RangePattern pattern, SemanticContext context) {
    return rangePattern.analyze(pattern, context, this);
  }

  /**
   * Visits a variable binding pattern.
   *
   * @param pattern the variable pattern being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public Void visit(VariablePattern pattern, SemanticContext context) {
    return variablePattern.analyze(pattern, context);
  }

  /**
   * Visits an array pattern.
   *
   * @param pattern the array pattern being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public Void visit(ArrayPattern pattern, SemanticContext context) {
    return arrayPattern.analyze(pattern, context, this);
  }

  /**
   * Visits a composite pattern.
   *
   * @param pattern the composite pattern being visited
   * @param context the operational context passed through the traversal
   * @return the result produced by the visitor
   */
  @Override
  public Void visit(CompositePattern pattern, SemanticContext context) {
    return compositePattern.analyze(pattern, context, this);
  }
}