package dev.morphe.parser.statement.compound;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.IfStatement;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.IStatementParser;

/**
 * Parses {@code if} statements.
 *
 * <p>An if statement consists of a condition, a block executed when the
 * condition evaluates to true, and an optional {@code else} block.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * if (condition) {
 *     statement;
 * } else {
 *     otherStatement;
 * }
 * }</pre>
 *
 * <p>The {@code else} branch is optional.</p>
 *
 * <p>The semantic validity of the condition is determined during
 * semantic analysis.</p>
 */
public final class IfStatementParser extends AbstractParser implements IStatementParser {

  /**
   * Creates an if statement parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public IfStatementParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses an if statement from the current token.
   *
   * <p>The parser consumes the {@code if} keyword, parses the parenthesized
   * condition, and then parses the required {@code then} block. If an
   * {@code else} keyword follows, its block is parsed as the optional
   * else branch.</p>
   *
   * @return the parsed if statement
   */
  @Override
  public IfStatement parse() {
    Token start = consume(KeywordTokenKind.IF);
    consume(PunctuationTokenKind.LEFT_PAREN);
    IExpression condition = new ExpressionParser(cursor).parse();
    consume(PunctuationTokenKind.RIGHT_PAREN);
    BlockStatement thenBranch = new BlockStatementParser(cursor).parse();
    BlockStatement elseBranch = null;
    if (cursor.match(KeywordTokenKind.ELSE)) elseBranch = new BlockStatementParser(cursor).parse();
    SourceRange range = new SourceRange(
        SourceConverter.toASTPosition(start.span().start()),
        (elseBranch != null ? elseBranch.range() : thenBranch.range()).end()
    );
    return new IfStatement(range, condition, thenBranch, elseBranch);
  }
}