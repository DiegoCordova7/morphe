package dev.morphe.parser.statement.match;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.ast.statement.MatchStatement;
import dev.morphe.lexer.kind.MatchTokenKind;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.pattern.PatternParser;
import dev.morphe.parser.statement.StatementParser;
import dev.morphe.parser.statement.compound.BlockStatementParser;

/**
 * Parses individual cases within a {@code match} statement.
 *
 * <p>A match case consists of a pattern followed by the {@code =>}
 * operator and a block containing the statements executed when the
 * pattern matches.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * pattern => {
 *     statement;
 * }
 * }</pre>
 */
public final class MatchStatementCaseParser extends AbstractParser {

  /**
   * Creates a match statement case parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public MatchStatementCaseParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a single match case from the current token.
   *
   * <p>The pattern is parsed first, followed by the match arrow and the
   * case body.</p>
   *
   * @return the parsed match case
   */
  public MatchStatement.MatchCase parse() {
    IPattern pattern = new PatternParser(cursor).parse();
    consume(MatchTokenKind.ARROW);
    IStatement body = new StatementParser(cursor).parse();
    return new MatchStatement.MatchCase(pattern, body);
  }
}