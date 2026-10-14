package dev.morphe.parser.statement.match;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.ast.statement.MatchStatement;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.IStatementParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses {@code match} statements.
 *
 * <p>A match statement evaluates a value and selects a case whose pattern
 * matches that value. Each case consists of a pattern, a match arrow, and
 * a block containing the statements to execute.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * match (value) {
 *     pattern => {
 *         statement;
 *     }
 *     otherPattern => {
 *         otherStatement;
 *     }
 * }
 * }</pre>
 *
 * <p>Unlike a {@code match} expression, each case in a match statement
 * contains a block of statements rather than a resulting expression.</p>
 */
public final class MatchStatementParser extends AbstractParser implements IStatementParser {

  /**
   * Creates a match statement parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public MatchStatementParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a match statement from the current token.
   *
   * <p>The parser consumes the {@code match} keyword, parses the matched
   * expression, and then parses all cases contained within the surrounding
   * braces.</p>
   *
   * @return the parsed match statement
   */
  @Override
  public IStatement parse() {
    Token match = consume(KeywordTokenKind.MATCH);
    consume(PunctuationTokenKind.LEFT_PAREN);
    IExpression value = new ExpressionParser(cursor).parse();
    consume(PunctuationTokenKind.RIGHT_PAREN);
    consume(PunctuationTokenKind.LEFT_BRACE);
    List<MatchStatement.MatchCase> cases = new ArrayList<>();
    while (!cursor.check(PunctuationTokenKind.RIGHT_BRACE)) {
      cases.add(new MatchStatementCaseParser(cursor).parse());
    }
    Token rightBrace = consume(PunctuationTokenKind.RIGHT_BRACE);
    SourceRange range = SourceConverter.toASTRange(match, rightBrace);
    return new MatchStatement(range, value, cases);
  }
}