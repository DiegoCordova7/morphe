package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.DefaultPattern;
import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.MatchTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.source.SourceConverter;

/**
 * Parses default patterns into AST nodes.
 *
 * <p>A default pattern is represented by the wildcard token {@code _} and
 * matches any value that is not matched by a preceding pattern.</p>
 */
public final class DefaultPatternParser extends AbstractParser implements IPatternParser {

  /**
   * Creates a default pattern parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public DefaultPatternParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a default pattern.
   *
   * <p>The parser consumes the wildcard token and creates the corresponding
   * {@link DefaultPattern} node.</p>
   *
   * @return the parsed default pattern
   */
  @Override
  public IPattern parse() {
    Token token = consume(MatchTokenKind.WILDCARD);
    SourceRange range = SourceConverter.toASTRange(token, token);
    return new DefaultPattern(range);
  }
}