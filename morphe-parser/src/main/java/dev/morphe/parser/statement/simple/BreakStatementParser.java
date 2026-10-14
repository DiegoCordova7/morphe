package dev.morphe.parser.statement.simple;

import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.BreakStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.IStatementParser;

/**
 * Parses {@code break} statements.
 *
 * <p>A break statement consists of the {@code break} keyword followed by
 * a statement terminator.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * break;
 * }</pre>
 *
 * <p>The resulting AST node represents a request to terminate the
 * innermost enclosing loop. Whether the statement is valid in its
 * surrounding context is determined during semantic analysis.</p>
 */
public final class BreakStatementParser extends AbstractParser implements IStatementParser {

  /**
   * Creates a break statement parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public BreakStatementParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a break statement from the current token.
   *
   * <p>The parser consumes the {@code break} keyword and the following
   * statement terminator, then creates a {@link BreakStatement} whose
   * source range spans both tokens.</p>
   *
   * @return the parsed break statement
   */
  @Override
  public IStatement parse() {
    Token start = consume(KeywordTokenKind.BREAK);
    Token end = consume(PunctuationTokenKind.SEMICOLON);
    SourceRange range = new SourceRange(
        SourceConverter.toASTPosition(start.span().start()),
        SourceConverter.toASTPosition(end.span().end())
    );
    return new BreakStatement(range);
  }
}