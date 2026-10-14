package dev.morphe.ast.visitor;

import dev.morphe.ast.declaration.CompositeDeclaration;
import dev.morphe.ast.declaration.FunctionDeclaration;

/**
 * Visitor for traversing declaration nodes in the Morphe abstract
 * syntax tree.
 *
 * <p>This visitor separates operations performed on declarations,
 * such as semantic analysis, compilation, or AST inspection, from
 * declaration nodes themselves.</p>
 *
 * <p>Declaration visits do not produce a result and therefore return
 * {@link Void}. Implementations may use the supplied context to carry
 * the state required during traversal.</p>
 *
 * @param <C> the contextual data passed during traversal
 */
public interface IDeclarationVisitor<C> {

  /**
   * Visits a function declaration.
   *
   * @param declaration the function declaration being visited
   * @param context the operational context passed through the traversal
   * @return always {@code null}
   */
  Void visit(FunctionDeclaration declaration, C context);

  /**
   * Visits a composite declaration.
   *
   * @param declaration the composite declaration being visited
   * @param context the operational context passed through the traversal
   * @return always {@code null}
   */
  Void visit(CompositeDeclaration declaration, C context);
}