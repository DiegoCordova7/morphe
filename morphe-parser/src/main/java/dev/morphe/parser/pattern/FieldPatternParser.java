package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.CompositePattern.FieldPattern;
import dev.morphe.ast.pattern.IPattern;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;

import static dev.morphe.lexer.kind.IdentifierTokenKind.IDENTIFIER;

/**
 * Parses field patterns used by composite patterns.
 *
 * <p>A field pattern associates a field name with another pattern:</p>
 *
 * <pre>{@code
 * fieldName: pattern
 * }</pre>
 *
 * <p>The nested pattern is parsed through {@link PatternParser}, allowing
 * field patterns to contain any pattern supported by the pattern grammar.</p>
 */
public final class FieldPatternParser extends AbstractParser {

  /**
   * Creates a field pattern parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public FieldPatternParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a field pattern from the current parser position.
   *
   * @return the parsed field pattern
   */
  public FieldPattern parse() {
    Token name = consume(IDENTIFIER);
    consume(PunctuationTokenKind.COLON);
    IPattern pattern = new PatternParser(cursor).parse();
    return new FieldPattern(name.lexeme(), pattern);
  }
}