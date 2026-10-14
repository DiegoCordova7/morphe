package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.lexer.kind.*;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;

import java.util.Map;
import java.util.function.Function;

/**
 * Parses primary patterns into AST nodes.
 *
 * <p>Primary patterns are the basic pattern forms from which more complex
 * patterns are constructed. Supported forms include literal, default,
 * variable, array, and composite patterns.</p>
 *
 * <p>Composite patterns require lookahead because they begin with an
 * identifier just like variable patterns. An identifier followed by a
 * left brace is therefore recognized as a composite pattern.</p>
 */
public final class PrimaryPatternParser extends AbstractParser implements IPatternParser {

  private final Map<ITokenKind, Function<TokenCursor, IPatternParser>> parsers;

  /**
   * Creates a primary pattern parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public PrimaryPatternParser(TokenCursor cursor) {
    super(cursor);
    this.parsers = Map.of(
        LiteralTokenKind.INTEGER, LiteralPatternParser::new,
        LiteralTokenKind.DOUBLE, LiteralPatternParser::new,
        LiteralTokenKind.STRING, LiteralPatternParser::new,
        LiteralTokenKind.BOOLEAN, LiteralPatternParser::new,
        MatchTokenKind.WILDCARD, DefaultPatternParser::new,
        IdentifierTokenKind.IDENTIFIER, VariablePatternParser::new,
        PunctuationTokenKind.LEFT_BRACKET, ArrayPatternParser::new
    );
  }

  /**
   * Parses a primary pattern from the current parser position.
   *
   * @return the parsed primary pattern
   * @throws UnexpectedTokenException if the current token does not start
   *     a supported primary pattern
   */
  @Override
  public IPattern parse() {
    if (isCompositePattern()) return new CompositePatternParser(cursor).parse();
    Function<TokenCursor, IPatternParser> factory = parsers.get(cursor.current().kind());
    if (factory == null) throw new UnexpectedTokenException(cursor.current());
    return factory.apply(cursor).parse();
  }

  /**
   * Determines whether the current token sequence starts a composite
   * pattern.
   *
   * <p>Composite patterns have the form:</p>
   *
   * <pre>{@code
   * TypeName {
   *     field: pattern
   * }
   * }</pre>
   *
   * <p>Since variable patterns also begin with an identifier, the parser
   * uses one-token lookahead to distinguish the two forms.</p>
   *
   * @return {@code true} if the current tokens start a composite pattern
   */
  private boolean isCompositePattern() {
    return cursor.check(IdentifierTokenKind.IDENTIFIER) && cursor.peek(1).kind() == PunctuationTokenKind.LEFT_BRACE;
  }
}