package dev.morphe.ast.statement;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.visitor.IStatementVisitor;

/**
 * Represents a variable declaration statement in the Morphe abstract
 * syntax tree.
 *
 * <p>A variable declaration introduces a variable with a declared type,
 * mutability, and an optional initializer expression.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * Integer value;
 * Integer count = 10;
 * #Integer limit = 100;
 * }</pre>
 *
 * <p>The semantic validity of the declaration, including whether a
 * constant requires an initializer and whether the initializer is
 * compatible with the declared type, is determined during semantic
 * analysis.</p>
 */
public final class VariableDeclarationStatement implements IStatement {

  private final SourceRange range;
  private final String name;
  private final IASTType type;
  private final boolean constant;
  private final IExpression initializer;

  /**
   * Creates a variable declaration statement.
   *
   * @param range the source range occupied by the declaration
   * @param name the name of the declared variable
   * @param type the declared variable type
   * @param constant whether the declared variable is constant
   * @param initializer the expression initializing the variable, or null
   * if the declaration has no initializer
   * @throws NullArgumentException if {@code range}, {@code name}, or
   * {@code type} is null
   */
  public VariableDeclarationStatement(SourceRange range, String name, IASTType type, boolean constant,
                                      IExpression initializer) {
    if (range == null) throw new NullArgumentException("range");
    if (name == null) throw new NullArgumentException("name");
    if (type == null) throw new NullArgumentException("type");
    this.range = range;
    this.name = name;
    this.type = type;
    this.constant = constant;
    this.initializer = initializer;
  }

  /**
   * Returns the source range occupied by this statement.
   *
   * @return the declaration source range
   */
  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the name of the declared variable.
   *
   * @return the variable name
   */
  public String name() {
    return name;
  }

  /**
   * Returns the declared variable type.
   *
   * @return the variable type
   */
  public IASTType type() {
    return type;
  }

  /**
   * Returns whether the declared variable is constant.
   *
   * @return {@code true} if the variable is constant
   */
  public boolean isConstant() {
    return constant;
  }

  /**
   * Returns the initializer expression.
   *
   * @return the initializer expression, or null if the variable is not
   * initialized by the declaration
   */
  public IExpression initializer() {
    return initializer;
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