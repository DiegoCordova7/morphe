package dev.morphe.parser.expression.postfix;

import dev.morphe.lexer.kind.ITokenKind;
import dev.morphe.lexer.kind.IdentifierTokenKind;
import dev.morphe.lexer.kind.OperatorTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.parser.cursor.TokenCursor;

/**
 * Detects whether the current token sequence starts an assignment
 * statement.
 *
 * <p>This detector performs lookahead without consuming tokens. It is
 * used by the statement parser to distinguish assignment statements
 * from expression statements.</p>
 *
 * <p>Supported assignment targets consist of an identifier optionally
 * followed by field accesses and/or array accesses.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * value = 10;
 * value += 1;
 * person.name = "Diego";
 * person.address.city = "Hermosillo";
 * array[index] = value;
 * matrix[row][column] += value;
 * person.values[0] = value;
 * }</pre>
 *
 * <p>The detector does not parse or consume the assignment. It only
 * determines whether an assignment operator follows a valid target.</p>
 */
public final class AssignmentTargetDetector {

  private AssignmentTargetDetector() {}

  /**
   * Determines whether the current token sequence starts an assignment.
   *
   * <p>The cursor position is not modified by this operation.</p>
   *
   * @param cursor the token cursor used for lookahead
   * @return {@code true} if an assignment target followed by a supported
   *     assignment operator is present
   */
  public static boolean starts(TokenCursor cursor) {
    if (!cursor.check(IdentifierTokenKind.IDENTIFIER)) return false;
    int offset = 1;
    while (true) {
      ITokenKind kind = cursor.peek(offset).kind();
      if (kind == PunctuationTokenKind.DOT) {
        if (cursor.peek(offset + 1).kind() != IdentifierTokenKind.IDENTIFIER) return false;
        offset += 2;
        continue;
      }
      if (kind == PunctuationTokenKind.LEFT_BRACKET) {
        int nextOffset = skipArrayAccess(cursor, offset);
        if (nextOffset < 0) return false;
        offset = nextOffset;
        continue;
      }
      break;
    }
    return isAssignmentOperator(cursor.peek(offset).kind());
  }

  /**
   * Determines whether a token kind represents a supported assignment
   * operator.
   *
   * @param kind the token kind to inspect
   * @return {@code true} if the token is an assignment operator
   */
  private static boolean isAssignmentOperator(ITokenKind kind) {
    if (!(kind instanceof OperatorTokenKind operator)) return false;

    return switch (operator) {
      case ASSIGN, PLUS_ASSIGN, MINUS_ASSIGN, MULTIPLY_ASSIGN, DIVIDE_ASSIGN, MODULO_ASSIGN -> true;
      default -> false;
    };
  }

  /**
   * Skips an array access while performing lookahead.
   *
   * <p>Nested array accesses are supported. For example,
   * {@code matrix[0][1]}.</p>
   *
   * @param cursor the token cursor used for lookahead
   * @param offset the offset pointing to the opening bracket
   * @return the offset immediately after the matching closing bracket,
   *     or {@code -1} if the access is not closed
   */
  private static int skipArrayAccess(TokenCursor cursor, int offset) {
    int depth = 1;
    offset++;
    while (true) {
      ITokenKind kind = cursor.peek(offset).kind();
      if (kind == PunctuationTokenKind.LEFT_BRACKET) depth++;
      else if (kind == PunctuationTokenKind.RIGHT_BRACKET) {
        depth--;
        if (depth == 0) return offset + 1;
      } else if (kind == PunctuationTokenKind.SEMICOLON) return -1;
      offset++;
    }
  }
}