package dev.morphe.parser.expression.compound;

import dev.morphe.ast.expression.AttemptExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.ExpressionStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.ast.type.ErrorType;
import dev.morphe.ast.type.IASTType;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.kind.MatchTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.expression.IExpressionParser;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.compound.BlockStatementParser;
import dev.morphe.parser.type.TypeParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses {@code attempt} expressions.
 */
public final class AttemptExpressionParser extends AbstractParser implements IExpressionParser {

  public AttemptExpressionParser(TokenCursor cursor) {
    super(cursor);
  }

  @Override
  public IExpression parse() {
    Token start = consume(KeywordTokenKind.ATTEMPT);
    BlockStatement body = new BlockStatementParser(cursor).parse();
    consume(MatchTokenKind.ARROW);
    IExpression fallback = new ExpressionParser(cursor).parse();
    List<AttemptExpression.ErrorHandler> handlers = new ArrayList<>();
    while (cursor.match(KeywordTokenKind.ON_ERROR)) {
      consume(PunctuationTokenKind.LEFT_PAREN);
      IASTType type = new TypeParser(cursor).parseBaseType();
      ErrorType errorType = (ErrorType) type;
      consume(PunctuationTokenKind.RIGHT_PAREN);
      consume(MatchTokenKind.ARROW);
      IExpression actionExpression = new ExpressionParser(cursor).parse();
      SourceRange actionRange = actionExpression.range();
      IStatement action = new ExpressionStatement(actionRange, actionExpression);
      handlers.add(new AttemptExpression.ErrorHandler(errorType, action));
    }
    Token end = cursor.previous();
    SourceRange range = SourceConverter.toASTRange(start, end);
    return new AttemptExpression(range, body, fallback, handlers);
  }
}