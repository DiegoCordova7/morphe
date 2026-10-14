package dev.morphe.parser.expression.lambda;

import dev.morphe.lexer.kind.*;
import dev.morphe.parser.cursor.TokenCursor;

/**
 * Detects whether the current token sequence starts a lambda expression.
 *
 * <p>The detector is used to distinguish lambda expressions from other
 * expressions beginning with a left parenthesis. A lambda parameter list
 * consists of zero or more explicitly typed parameters followed by the
 * {@code ->} operator.</p>
 *
 * <p>Supported parameter list forms include:</p>
 *
 * <pre>{@code
 * () -> expression
 * (Integer value) -> expression
 * (Integer[] values) -> expression
 * (Integer[][] matrix) -> expression
 * (Integer a, String b) -> expression
 * (Person person) -> expression
 * }</pre>
 *
 * <p>This class performs syntactic lookahead only. It does not consume
 * tokens or construct any AST nodes.</p>
 */
public final class LambdaDetector {

  private LambdaDetector() {}

  /**
   * Determines whether the current token sequence starts a lambda
   * expression.
   *
   * <p>The current token must be a left parenthesis. The detector then
   * verifies that the contents form a valid typed parameter list and
   * that the list is immediately followed by the lambda arrow.</p>
   *
   * @param cursor the token cursor used for lookahead
   * @return {@code true} if the current token sequence starts a lambda
   *     expression; {@code false} otherwise
   */
  public static boolean starts(TokenCursor cursor) {
    if (!cursor.check(PunctuationTokenKind.LEFT_PAREN)) return false;
    int offset = 1;
    if (cursor.peek(offset).kind() == PunctuationTokenKind.RIGHT_PAREN)
      return cursor.peek(offset + 1).kind() == OperatorTokenKind.ARROW;
    while (true) {
      if (!isTypeStart(cursor.peek(offset).kind())) return false;
      offset++;
      while (isArraySuffix(cursor, offset)) offset += 2;
      if (cursor.peek(offset).kind() != IdentifierTokenKind.IDENTIFIER) return false;
      offset++;
      ITokenKind next = cursor.peek(offset).kind();
      if (next == PunctuationTokenKind.RIGHT_PAREN) return cursor.peek(offset + 1).kind() == OperatorTokenKind.ARROW;
      if (next != PunctuationTokenKind.COMMA) return false;
      offset++;
    }
  }

  /**
   * Determines whether the token sequence at the given offset starts
   * an array type suffix.
   *
   * @param cursor the token cursor used for lookahead
   * @param offset the offset at which the suffix is checked
   * @return {@code true} if the sequence is {@code []}
   */
  private static boolean isArraySuffix(TokenCursor cursor, int offset) {
    return cursor.peek(offset).kind() == PunctuationTokenKind.LEFT_BRACKET
        && cursor.peek(offset + 1).kind() == PunctuationTokenKind.RIGHT_BRACKET;
  }

  /**
   * Determines whether a token kind can begin a lambda parameter type.
   *
   * <p>Primitive, void, and named types are supported.</p>
   *
   * @param kind the token kind being inspected
   * @return {@code true} if the token kind can start a type
   */
  private static boolean isTypeStart(ITokenKind kind) {
    return kind == TypeTokenKind.INTEGER
        || kind == TypeTokenKind.DOUBLE
        || kind == TypeTokenKind.BOOLEAN
        || kind == TypeTokenKind.STRING
        || kind == TypeTokenKind.VOID_TYPE
        || kind == IdentifierTokenKind.IDENTIFIER;
  }
}