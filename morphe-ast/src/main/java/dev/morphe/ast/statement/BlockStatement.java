package dev.morphe.ast.statement;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.visitor.IStatementVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents a block of statements in the Morphe abstract syntax tree.
 *
 * <p>A block groups zero or more statements into a single statement node.
 * Blocks are used by constructs such as conditionals, loops, functions,
 * and other constructs that contain a sequence of statements.</p>
 *
 * <p>The statements contained in the block are preserved in their
 * original order.</p>
 */
public final class BlockStatement implements IStatement {

  private final SourceRange range;
  private final List<IStatement> statements;

  /**
   * Creates a block statement.
   *
   * @param range the source range occupied by the block
   * @param statements the statements contained in the block
   * @throws NullArgumentException if {@code range} or {@code statements}
   * is null, or if any statement is null
   */
  public BlockStatement(SourceRange range, List<IStatement> statements) {
    if (range == null) throw new NullArgumentException("range");
    if (statements == null) throw new NullArgumentException("statements");
    if (statements.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("statement");
    this.range = range;
    this.statements = List.copyOf(statements);
  }

  /**
   * Returns the source range occupied by this block.
   *
   * @return the block source range
   */
  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the statements contained in this block.
   *
   * @return an immutable list of statements
   */
  public List<IStatement> statements() {
    return statements;
  }

  /**
   * Accepts a statement visitor and dispatches this statement to the
   * appropriate visitor operation.
   *
   * @param visitor the visitor processing this statement
   * @param <S> the result type produced by the visitor
   * @param <C> the context type passed to the visitor
   * @return the result produced by the visitor
   * @throws NullArgumentException if {@code visitor} is null
   */
  @Override
  public <S, C> S accept(IStatementVisitor<S, C> visitor, C context) {
    if (visitor == null) throw new NullArgumentException("visitor");
    return visitor.visit(this, context);
  }
}