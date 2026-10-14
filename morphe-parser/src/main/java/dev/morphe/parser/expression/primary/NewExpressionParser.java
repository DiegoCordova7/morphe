package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.lexer.kind.ITokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.kind.SpecialTokenKind;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import dev.morphe.parser.expression.IExpressionParser;
import dev.morphe.parser.expression.compound.CompositeInstanceExpressionParser;

/**
 * Dispatches new expressions to their specialized parsers.
 * <p>
 * The parser determines the kind of creation expression by inspecting the
 * token following the type:
 * </p>
 *
 * <ul>
 *   <li>{@code []} delegates to {@link ArrayCreationParser}.</li>
 *   <li><code>{</code> delegates to {@link CompositeInstanceExpressionParser}.</li>
 * </ul>
 *
 * <p>
 * This parser does not construct creation expressions directly. It delegates
 * parsing to the appropriate specialized parser.
 * </p>
 *
 * @see ArrayCreationParser
 * @see CompositeInstanceExpressionParser
 */
public final class NewExpressionParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates a parser for {@code new} expressions.
   *
   * @param cursor the token cursor used to read source tokens
   */
  public NewExpressionParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a creation expression by identifying its syntactic form.
   *
   * @return the parsed array creation or composite instance expression
   * @throws UnexpectedTokenException if the token following the type
   *         does not indicate a supported creation expression
   */
  @Override
  public IExpression parse() {
    if (cursor.peek(1).kind().equals(PunctuationTokenKind.LEFT_BRACE)) {
      if (isAnonymousCompositeArrayCreation()) return new ArrayCreationParser(cursor).parse();
      return new CompositeInstanceExpressionParser(cursor).parse();
    }
    ITokenKind kind = cursor.peek(2).kind();
    if (kind.equals(PunctuationTokenKind.LEFT_BRACE)) return new CompositeInstanceExpressionParser(cursor).parse();
    if (kind.equals(PunctuationTokenKind.LEFT_BRACKET)) return new ArrayCreationParser(cursor).parse();
    throw new UnexpectedTokenException(cursor.peek(2));
  }

  private boolean isAnonymousCompositeArrayCreation() {
    int offset = 1;
    int braceDepth = 0;
    while (cursor.peek(offset).kind() != SpecialTokenKind.EOF) {
      ITokenKind kind = cursor.peek(offset).kind();
      if (kind == PunctuationTokenKind.LEFT_BRACE) braceDepth++;
      else if (kind == PunctuationTokenKind.RIGHT_BRACE) {
        braceDepth--;
        if (braceDepth == 0) {
          offset++;
          break;
        }
      }
      offset++;
    }
    return cursor.peek(offset).kind() == PunctuationTokenKind.LEFT_BRACKET;
  }
}