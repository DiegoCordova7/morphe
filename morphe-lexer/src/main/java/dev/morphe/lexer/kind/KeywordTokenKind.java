package dev.morphe.lexer.kind;

/**
 * Represents lexical tokens corresponding to reserved keywords in Morphe
 * source code.
 */
public enum KeywordTokenKind implements ITokenKind {

  /** Conditional statement. */
  IF("if"),

  /** Alternative conditional branch. */
  ELSE("else"),

  /** Pattern matching expression. */
  MATCH("match"),

  /** Do-while loop. */
  DO("do"),

  /** While loop. */
  WHILE("while"),

  /** For loop. */
  FOR("for"),

  /** Break */
  BREAK("break"),

  /** Continue */
  CONTINUE("continue"),

  /** Function declaration. */
  FUNC("func"),

  /** Function return statement. */
  RETURN("return"),

  /** Error handler. */
  ATTEMPT("attempt"),

  /** Error handling branch. */
  ON_ERROR("onError"),

  /** Composite type declaration. */
  TYPE("type"),

  /** Array creation expression. */
  NEW("new");

  private final String lexeme;

  /**
   * Creates a keyword token kind with the specified source-level lexeme.
   *
   * @param lexeme the source-level representation of the keyword
   */
  KeywordTokenKind(String lexeme) {
    this.lexeme = lexeme;
  }

  /**
   * Returns the source-level representation of this keyword token.
   *
   * @return the keyword lexeme
   */
  @Override
  public String lexeme() {
    return lexeme;
  }
}