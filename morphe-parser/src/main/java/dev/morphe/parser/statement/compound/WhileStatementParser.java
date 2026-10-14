package dev.morphe.parser.statement.compound;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.ast.statement.WhileStatement;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.IStatementParser;

/**
 * Parses {@code while} statements.
 *
 * <p>A while statement consists of a condition enclosed in parentheses
 * followed by a block that is executed repeatedly while the condition
 * evaluates to true.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * while (condition) {
 *     statement;
 * }
 * }</pre>
 *
 * <p>The semantic validity of the condition is determined during
 * semantic analysis.</p>
 */
public final class WhileStatementParser extends AbstractParser implements IStatementParser {

  /**
   * Creates a while statement parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public WhileStatementParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a while statement from the current token.
   *
   * <p>The parser consumes the {@code while} keyword, parses the
   * parenthesized condition, and then parses the loop body.</p>
   *
   * @return the parsed while statement
   */
  @Override
  public WhileStatement parse() {
    Token whileToken = consume(KeywordTokenKind.WHILE);
    consume(PunctuationTokenKind.LEFT_PAREN);
    IExpression condition = new ExpressionParser(cursor).parse();
    consume(PunctuationTokenKind.RIGHT_PAREN);
    BlockStatement body = new BlockStatementParser(cursor).parse();
    SourceRange range = SourceConverter.toASTRange(whileToken, cursor.previous());
    return new WhileStatement(range, condition, body);
  }
}