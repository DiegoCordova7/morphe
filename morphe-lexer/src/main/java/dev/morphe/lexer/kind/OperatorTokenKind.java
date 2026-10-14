package dev.morphe.lexer.kind;

/**
 * Represents operators recognized by the Morphe lexer.
 */
public enum OperatorTokenKind implements ITokenKind {

  // Arithmetic

  /** Addition operator. */
  PLUS("+"),

  /** Subtraction operator. */
  MINUS("-"),

  /** Multiplication operator. */
  MULTIPLY("*"),

  /** Division operator. */
  DIVIDE("/"),

  /** Remainder operator. */
  MODULO("%"),

  // Assignment

  /** Assignment operator. */
  ASSIGN("="),

  /** Addition assignment operator. */
  PLUS_ASSIGN("+="),

  /** Subtraction assignment operator. */
  MINUS_ASSIGN("-="),

  /** Multiplication assignment operator. */
  MULTIPLY_ASSIGN("*="),

  /** Division assignment operator. */
  DIVIDE_ASSIGN("/="),

  /** Modulo assigment operator. */
  MODULO_ASSIGN("%="),

  // Increment / decrement

  /** Increment operator. */
  INCREMENT("++"),

  /** Decrement operator. */
  DECREMENT("--"),

  // Equality & Structural

  /** Equality operator. */
  EQUAL("=="),

  /** Inequality operator. */
  NOT_EQUAL("!="),

  /** Structural match operator. */
  STRUCT_MATCH("~="),

  // Relational

  /** Greater-than operator. */
  GREATER(">"),

  /** Less-than operator. */
  LESS("<"),

  /** Greater-than-or-equal operator. */
  GREATER_EQUAL(">="),

  /** Less-than-or-equal operator. */
  LESS_EQUAL("<="),

  // Logical

  /** Logical AND operator. */
  AND("&&"),

  /** Logical OR operator. */
  OR("||"),

  /** Logical NOT operator. */
  NOT("!"),

  // Pipeline / functions

  /** Pipeline operator. */
  PIPELINE("|>"),

  /** Lambda/function expression operator. */
  ARROW("->"),

  // Mutability

  /** Immutable binding modifier. */
  IMMUTABLE("#");

  private final String lexeme;

  OperatorTokenKind(String lexeme) {
    this.lexeme = lexeme;
  }

  /**
   * Returns the source-level representation of this operator.
   *
   * @return the operator lexeme
   */
  public String lexeme() {
    return lexeme;
  }
}