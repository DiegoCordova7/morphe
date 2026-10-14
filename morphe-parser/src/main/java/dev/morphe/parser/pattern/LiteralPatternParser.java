package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.LiteralPattern;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.LiteralTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import dev.morphe.parser.source.SourceConverter;

/**
 * Parses literal patterns into AST nodes.
 *
 * <p>Supported literal patterns include integer, double, string, and
 * boolean literals.</p>
 *
 * <p>The token lexeme is converted into its corresponding Java value before
 * being stored in the resulting {@link LiteralPattern}.</p>
 */
public final class LiteralPatternParser extends AbstractParser implements IPatternParser {

  /**
   * Creates a literal pattern parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public LiteralPatternParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a literal pattern from the current parser position.
   *
   * @return the parsed literal pattern
   * @throws UnexpectedTokenException if the current token is not a
   *     supported literal token
   */
  @Override
  public IPattern parse() {
    Token token = cursor.current();
    Object value = switch ((LiteralTokenKind) token.kind()) {
      case INTEGER -> Integer.parseInt(token.lexeme());
      case DOUBLE -> Double.parseDouble(token.lexeme());
      case STRING -> parseString(token.lexeme());
      case BOOLEAN -> Boolean.parseBoolean(token.lexeme());
      default -> throw new UnexpectedTokenException(token);
    };
    cursor.advance();
    SourceRange range = SourceConverter.toASTRange(token, token);
    return new LiteralPattern(range, value);
  }

  /**
   * Removes the surrounding quotes from a string literal lexeme.
   *
   * @param lexeme the complete string literal lexeme
   * @return the string value without its surrounding quotes
   */
  private String parseString(String lexeme) {
    return lexeme.substring(1, lexeme.length() - 1);
  }
}