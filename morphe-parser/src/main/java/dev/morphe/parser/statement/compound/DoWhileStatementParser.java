package dev.morphe.parser.statement.compound;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.DoWhileStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.IStatementParser;

/**
 * Parses {@code do-while} loop statements.
 *
 * <p>A do-while statement consists of a body block followed by a
 * {@code while} keyword and a condition. Unlike a while statement,
 * the body is always executed at least once before the condition is
 * evaluated.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * do {
 *     statement;
 * } while (condition);
 * }</pre>
 *
 * <p>The semantic validity of the loop condition is determined during
 * semantic analysis.</p>
 */
public final class DoWhileStatementParser extends AbstractParser implements IStatementParser {

  /**
   * Creates a do-while statement parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public DoWhileStatementParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a do-while statement from the current token.
   *
   * <p>The parser consumes the {@code do} keyword, parses the loop body,
   * then parses the {@code while} keyword, the parenthesized condition,
   * and the terminating semicolon.</p>
   *
   * @return the parsed do-while statement
   */
  @Override
  public DoWhileStatement parse() {
    Token doToken = consume(KeywordTokenKind.DO);
    BlockStatement body = new BlockStatementParser(cursor).parse();
    consume(KeywordTokenKind.WHILE);
    consume(PunctuationTokenKind.LEFT_PAREN);
    IExpression condition = new ExpressionParser(cursor).parse();
    consume(PunctuationTokenKind.RIGHT_PAREN);
    Token semicolon = consume(PunctuationTokenKind.SEMICOLON);
    SourceRange range = SourceConverter.toASTRange(doToken, semicolon);
    return new DoWhileStatement(range, body, condition);
  }
}