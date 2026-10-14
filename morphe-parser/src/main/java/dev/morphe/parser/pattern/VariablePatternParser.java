package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.VariablePattern;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.source.SourceConverter;

import static dev.morphe.lexer.kind.IdentifierTokenKind.IDENTIFIER;

/**
 * Parses variable patterns into AST nodes.
 *
 * <p>A variable pattern consists of an identifier that binds the matched
 * value to a name.</p>
 *
 * <p>For example, the pattern:</p>
 *
 * <pre>{@code
 * value
 * }</pre>
 *
 * <p>is represented by a {@link VariablePattern} whose name is
 * {@code value}.</p>
 */
public final class VariablePatternParser extends AbstractParser implements IPatternParser {

  /**
   * Creates a variable pattern parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public VariablePatternParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a variable pattern from the current parser position.
   *
   * @return the parsed variable pattern
   */
  @Override
  public IPattern parse() {
    Token token = consume(IDENTIFIER);
    SourceRange range = SourceConverter.toASTRange(token, token);
    return new VariablePattern(range, token.lexeme());
  }
}