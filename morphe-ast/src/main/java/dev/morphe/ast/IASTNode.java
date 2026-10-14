package dev.morphe.ast;

import dev.morphe.ast.source.SourceRange;

/**
 * Represents a node in the Morphe abstract syntax tree.
 *
 * <p>
 * Every AST node is associated with the source range from which it was
 * produced.
 */
public interface IASTNode {

  /**
   * Returns the source range occupied by this node.
   *
   * @return the node source range
   */
  SourceRange range();
}