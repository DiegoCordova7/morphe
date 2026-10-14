package dev.morphe.ast.pattern;

import dev.morphe.ast.IASTNode;
import dev.morphe.ast.visitor.IPatternVisitor;

/**
 * Represents a pattern within the Morphe abstract syntax tree.
 *
 * <p>Patterns describe matching conditions used by constructs such as
 * {@code match}. Each pattern is an AST node and therefore carries
 * source location information and can be processed through a pattern
 * visitor.</p>
 */
public interface IPattern extends IASTNode {

  /**
   * Accepts a pattern visitor and dispatches this pattern to the
   * appropriate visitor operation.
   *
   * @param visitor the visitor processing this pattern
   * @param context the operational context passed through the traversal
   * @param <C> the context type passed to the visitor
   * @return always {@code null}
   */
  <C> Void accept(IPatternVisitor<C> visitor, C context);
}