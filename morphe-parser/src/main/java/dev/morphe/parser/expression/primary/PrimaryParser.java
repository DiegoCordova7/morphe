package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.lexer.kind.*;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.argument.NullArgumentException;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import dev.morphe.parser.expression.IExpressionParser;
import dev.morphe.parser.expression.compound.AttemptExpressionParser;
import dev.morphe.parser.expression.compound.IfExpressionParser;
import dev.morphe.parser.expression.compound.MatchExpressionParser;
import dev.morphe.parser.expression.lambda.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Parses primary expressions in the Morphe expression grammar.
 *
 * <p>Primary expressions represent the base expressions from which more
 * complex expressions are constructed. Supported primary expressions
 * include:</p>
 *
 * <ul>
 *   <li>literal values</li>
 *   <li>variable references</li>
 *   <li>array creation expressions</li>
 *   <li>array literals</li>
 *   <li>grouped expressions</li>
 *   <li>conditional expressions</li>
 *   <li>match expressions</li>
 *   <li>attempt expressions</li>
 *   <li>lambda expressions</li>
 * </ul>
 *
 * <p>The parser selects the appropriate specialized parser based on the
 * current token. Lambda expressions are detected separately because they
 * share the left-parenthesis token with grouped expressions.</p>
 */
public final class PrimaryParser extends AbstractParser implements IExpressionParser {

  private final Map<ITokenKind, Function<TokenCursor, IExpressionParser>> parsers = new HashMap<>();

  /**
   * Creates a primary expression parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   * @throws NullArgumentException if {@code cursor} is {@code null}
   */
  public PrimaryParser(TokenCursor cursor) {
    super(cursor);
    parsers.put(LiteralTokenKind.INTEGER, LiteralParser::new);
    parsers.put(LiteralTokenKind.DOUBLE, LiteralParser::new);
    parsers.put(LiteralTokenKind.STRING, LiteralParser::new);
    parsers.put(LiteralTokenKind.BOOLEAN, LiteralParser::new);
    parsers.put(IdentifierTokenKind.IDENTIFIER, VariableParser::new);
    parsers.put(KeywordTokenKind.NEW, NewExpressionParser::new);
    parsers.put(KeywordTokenKind.IF, IfExpressionParser::new);
    parsers.put(KeywordTokenKind.ATTEMPT, AttemptExpressionParser::new);
    parsers.put(KeywordTokenKind.MATCH, MatchExpressionParser::new);
    parsers.put(PunctuationTokenKind.LEFT_BRACKET, ArrayParser::new);
    parsers.put(PunctuationTokenKind.LEFT_PAREN, GroupingParser::new);
  }

  /**
   * Parses a primary expression starting at the current token.
   *
   * <p>Lambda expressions are checked before the regular parser lookup
   * because both lambdas and grouped expressions may begin with a
   * left parenthesis.</p>
   *
   * @return the parsed primary expression
   * @throws UnexpectedTokenException if the current token does not
   *     start a valid primary expression
   */
  @Override
  public IExpression parse() {
    if (LambdaDetector.starts(cursor)) return new LambdaExpressionParser(cursor).parse();
    Function<TokenCursor, IExpressionParser> factory = parsers.get(cursor.current().kind());
    if (factory == null) throw new UnexpectedTokenException(cursor.current());
    return factory.apply(cursor).parse();
  }
}