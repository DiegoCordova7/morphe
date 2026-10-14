package dev.morphe.parser.statement.simple;

import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.ContinueStatement;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.IStatementParser;

/**
 * Parses {@code continue} statements.
 *
 * <p>A continue statement consists of the {@code continue} keyword
 * followed by a statement terminator.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * continue;
 * }</pre>
 *
 * <p>The resulting AST node represents a request to skip the remaining
 * statements of the current loop iteration and continue with the next
 * iteration. Whether the statement is valid in its surrounding context
 * is determined during semantic analysis.</p>
 */
public final class ContinueStatementParser extends AbstractParser implements IStatementParser {

  /**
   * Creates a continue statement parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public ContinueStatementParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a continue statement from the current token.
   *
   * <p>The parser consumes the {@code continue} keyword and the following
   * statement terminator, then creates a {@link ContinueStatement} whose
   * source range spans both tokens.</p>
   *
   * @return the parsed continue statement
   */
  @Override
  public ContinueStatement parse() {
    Token start = consume(KeywordTokenKind.CONTINUE);
    Token end = consume(PunctuationTokenKind.SEMICOLON);
    SourceRange range = new SourceRange(
        SourceConverter.toASTPosition(start.span().start()),
        SourceConverter.toASTPosition(end.span().end())
    );
    return new ContinueStatement(range);
  }
}