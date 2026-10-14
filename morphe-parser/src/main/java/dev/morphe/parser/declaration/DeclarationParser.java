package dev.morphe.parser.declaration;

import dev.morphe.ast.declaration.IDeclaration;
import dev.morphe.lexer.kind.ITokenKind;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;

import java.util.Map;
import java.util.function.Function;

/**
 * Dispatches declaration parsing to the appropriate specialized parser.
 *
 * <p>The parser determines which kind of declaration is present by
 * inspecting the current token and selecting the parser registered for
 * that token kind.</p>
 *
 * <p>Supported declarations currently include:</p>
 *
 * <ul>
 *   <li>{@code func} declarations, parsed by {@link FunctionParser}</li>
 *   <li>{@code type} declarations, parsed by {@link CompositeParser}</li>
 * </ul>
 */
public final class DeclarationParser extends AbstractParser {

  private final Map<ITokenKind, Function<TokenCursor, IDeclarationParser>> parsers;

  /**
   * Creates a declaration dispatcher using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public DeclarationParser(TokenCursor cursor) {
    super(cursor);
    this.parsers = Map.of(
        KeywordTokenKind.FUNC, FunctionParser::new,
        KeywordTokenKind.TYPE, CompositeParser::new
    );
  }

  /**
   * Parses the declaration beginning at the current parser position.
   *
   * @return the parsed declaration
   * @throws UnexpectedTokenException if the current token does not start
   *     a supported declaration
   */
  public IDeclaration parse() {
    Function<TokenCursor, IDeclarationParser> factory = parsers.get(cursor.current().kind());
    if (factory == null) throw new UnexpectedTokenException(cursor.current());
    return factory.apply(cursor).parse();
  }
}