package dev.morphe.ast.visitor;

/**
 * Visitor that can process every category of node in the Morphe abstract
 * syntax tree.
 *
 * <p>The visitor uses separate result and contextual types for expressions,
 * statements, declarations, and patterns because these categories may require
 * different results and traversal state.</p>
 *
 * @param <E> the result type produced when visiting expression nodes
 * @param <S> the result type produced when visiting statement nodes
 * @param <D> the contextual data passed to declarations
 * @param <C> the contextual data passed to expressions and statements
 * @param <P> the contextual data passed to patterns
 */
public interface IASTVisitor<E, S, D, C, P>
    extends
    IExpressionVisitor<E, C>,
    IStatementVisitor<S, C>,
    IPatternVisitor<P>,
    IDeclarationVisitor<D> {
}