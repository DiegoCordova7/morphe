package dev.morphe.parser.statement.simple;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.ExpressionStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.IStatementParser;

/**
 * Parses expression statements.
 */
public final class ExpressionStatementParser extends AbstractParser implements IStatementParser {

  public ExpressionStatementParser(TokenCursor cursor) {
    super(cursor);
  }

  @Override
  public IStatement parse() {
    IExpression expression = new ExpressionParser(cursor).parse();
    SourceRange range;
    if (cursor.match(PunctuationTokenKind.SEMICOLON)) {
      Token semicolon = cursor.previous();
      range = new SourceRange(
          expression.range().start(),
          SourceConverter.toASTPosition(semicolon.span().end())
      );
    } else range = expression.range();
    return new ExpressionStatement(range, expression);
  }
}