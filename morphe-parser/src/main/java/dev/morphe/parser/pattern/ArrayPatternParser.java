package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.ArrayPattern;
import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.MatchTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.source.SourceConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses array patterns into AST nodes.
 *
 * <p>An array pattern consists of zero or more element patterns and may
 * optionally contain a rest pattern introduced by the {@code ...} token.</p>
 *
 * <p>Examples of supported patterns include:</p>
 *
 * <pre>{@code
 * []
 * [1, 2, 3]
 * [first, second]
 * [head, ...tail]
 * }</pre>
 *
 * <p>The rest pattern is parsed separately from the regular elements and
 * represents the remaining elements of the matched array.</p>
 */
public final class ArrayPatternParser extends AbstractParser implements IPatternParser {

  /**
   * Creates an array pattern parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public ArrayPatternParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses an array pattern from the current parser position.
   *
   * <p>The parser consumes the opening bracket, parses element patterns
   * separated by commas, optionally parses a rest pattern, and finally
   * consumes the closing bracket.</p>
   *
   * @return the parsed array pattern
   */
  @Override
  public IPattern parse() {
    Token leftBracket = consume(PunctuationTokenKind.LEFT_BRACKET);
    List<IPattern> elements = new ArrayList<>();
    IPattern rest = null;
    if (!cursor.check(PunctuationTokenKind.RIGHT_BRACKET)) {
      while (true) {
        if (cursor.match(MatchTokenKind.ELLIPSIS)) {
          rest = new PatternParser(cursor).parse();
          break;
        }
        elements.add(new PatternParser(cursor).parse());
        if (!cursor.match(PunctuationTokenKind.COMMA)) break;
      }
    }
    Token rightBracket = consume(PunctuationTokenKind.RIGHT_BRACKET);
    SourceRange range = SourceConverter.toASTRange(leftBracket, rightBracket);
    return new ArrayPattern(range, elements, rest);
  }
}