package dev.morphe.parser.statement.simple;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.ReturnStatement;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.IStatementParser;

/**
 * Parses {@code return} statements.
 *
 * <p>A return statement may optionally contain an expression whose value
 * is returned from the enclosing function.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * return;
 * return value;
 * return calculate();
 * }</pre>
 *
 * <p>The returned expression is optional, allowing functions to return
 * without producing a value.</p>
 */
public final class ReturnStatementParser extends AbstractParser implements IStatementParser {

  /**
   * Creates a return statement parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public ReturnStatementParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a return statement from the current token.
   *
   * <p>The parser consumes the {@code return} keyword, optionally parses
   * the return expression, and finally consumes the terminating semicolon.</p>
   *
   * @return the parsed return statement
   */
  @Override
  public ReturnStatement parse() {
    Token start = consume(KeywordTokenKind.RETURN);
    IExpression value = null;
    if (!cursor.check(PunctuationTokenKind.SEMICOLON)) value = new ExpressionParser(cursor).parse();
    Token end = consume(PunctuationTokenKind.SEMICOLON);
    SourceRange range = new SourceRange(
        SourceConverter.toASTPosition(start.span().start()),
        SourceConverter.toASTPosition(end.span().end())
    );
    return new ReturnStatement(range, value);
  }
}