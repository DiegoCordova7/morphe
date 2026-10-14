package dev.morphe.ast.visitor;

import dev.morphe.ast.pattern.*;

/**
 * Visitor for traversing pattern nodes in the Morphe abstract syntax tree.
 *
 * <p>The visitor decouples operations performed on pattern nodes from
 * the nodes themselves. Implementations can provide different behaviors
 * such as semantic analysis, compilation, or AST inspection without
 * requiring pattern nodes to depend on those operations.</p>
 *
 * <p>Each concrete pattern type has a corresponding {@code visit}
 * operation, allowing implementations to perform type-specific processing
 * without relying on runtime type checks.</p>
 *
 * @param <C> the contextual data passed during traversal
 */
public interface IPatternVisitor<C> {

  /**
   * Visits a default pattern.
   *
   * @param pattern the default pattern being visited
   * @param context the operational context passed through the traversal
   * @return always {@code null}
   */
  Void visit(DefaultPattern pattern, C context);

  /**
   * Visits a literal pattern.
   *
   * @param pattern the literal pattern being visited
   * @param context the operational context passed through the traversal
   * @return always {@code null}
   */
  Void visit(LiteralPattern pattern, C context);

  /**
   * Visits an or pattern.
   *
   * @param pattern the or pattern being visited
   * @param context the operational context passed through the traversal
   * @return always {@code null}
   */
  Void visit(OrPattern pattern, C context);

  /**
   * Visits a range pattern.
   *
   * @param pattern the range pattern being visited
   * @param context the operational context passed through the traversal
   * @return always {@code null}
   */
  Void visit(RangePattern pattern, C context);

  /**
   * Visits a variable binding pattern.
   *
   * @param pattern the variable pattern being visited
   * @param context the operational context passed through the traversal
   * @return always {@code null}
   */
  Void visit(VariablePattern pattern, C context);

  /**
   * Visits an array pattern.
   *
   * @param pattern the array pattern being visited
   * @param context the operational context passed through the traversal
   * @return always {@code null}
   */
  Void visit(ArrayPattern pattern, C context);

  /**
   * Visits a composite pattern.
   *
   * @param pattern the composite pattern being visited
   * @param context the operational context passed through the traversal
   * @return always {@code null}
   */
  Void visit(CompositePattern pattern, C context);
}