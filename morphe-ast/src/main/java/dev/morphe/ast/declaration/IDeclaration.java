package dev.morphe.ast.declaration;

import dev.morphe.ast.IASTNode;
import dev.morphe.ast.visitor.IDeclarationVisitor;

/**
 * Represents a declaration node in the Morphe abstract syntax tree.
 *
 * <p>Declaration nodes represent named constructs defined at the top
 * level of a Morphe source file, such as functions and composite types.</p>
 */
public interface IDeclaration extends IASTNode {

  /**
   * Accepts a declaration visitor and dispatches this declaration to
   * the appropriate visitor operation.
   *
   * @param visitor the visitor processing this declaration
   * @param context the operational context passed through the traversal
   * @return always {@code null}
   */
  <C> Void accept(IDeclarationVisitor<C> visitor, C context);
}