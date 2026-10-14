package dev.morphe.semantic.result;

import dev.morphe.ast.type.IASTType;

/**
 * Represents the semantic information produced by analyzing an expression.
 *
 * <p>The result contains the expression's resolved type and describes whether
 * the expression represents a location that can be assigned to and whether
 * that location can be modified.</p>
 *
 * <p>Assignability and mutability are distinct properties. An expression may
 * be assignable but immutable, such as a constant variable.</p>
 *
 * <p>For expressions that do not represent assignable locations, both
 * {@code assignable} and {@code mutable} are {@code false}.</p>
 */
public final class ExpressionResult {

  private final IASTType type;
  private final boolean assignable;
  private final boolean mutable;

  /**
   * Creates an expression analysis result.
   *
   * @param type the resolved expression type
   * @param assignable whether the expression represents an assignable location
   * @param mutable whether the assignable location may be modified
   */
  public ExpressionResult(IASTType type, boolean assignable, boolean mutable) {
    this.type = type;
    this.assignable = assignable;
    this.mutable = mutable;
  }

  /**
   * Returns the resolved expression type.
   *
   * @return the expression type
   */
  public IASTType type() {
    return type;
  }

  /**
   * Returns whether the expression represents an assignable location.
   *
   * @return {@code true} if the expression is assignable
   */
  public boolean assignable() {
    return assignable;
  }

  /**
   * Returns whether the assignable location may be modified.
   *
   * @return {@code true} if the expression is mutable
   */
  public boolean mutable() {
    return mutable;
  }
}