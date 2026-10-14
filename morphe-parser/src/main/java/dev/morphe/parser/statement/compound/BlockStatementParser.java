package dev.morphe.parser.statement.compound;

import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.IStatementParser;
import dev.morphe.parser.statement.StatementParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses block statements.
 *
 * <p>A block is a sequence of statements enclosed by curly braces.
 * Each statement is parsed using {@link StatementParser} until the
 * closing brace is encountered.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * {
 *     statement;
 *     anotherStatement;
 * }
 * }</pre>
 *
 * <p>Blocks may contain zero or more statements and may be nested
 * inside other blocks.</p>
 */
public final class BlockStatementParser extends AbstractParser implements IStatementParser {

  /**
   * Creates a block statement parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public BlockStatementParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a block statement.
   *
   * <p>The parser consumes the opening brace, parses statements until
   * the corresponding closing brace is reached, and then consumes the
   * closing brace.</p>
   *
   * @return the parsed block statement
   * @throws UnexpectedTokenException if the end of the token stream is
   *     reached before the closing brace is found
   */
  @Override
  public BlockStatement parse() {
    Token start = consume(PunctuationTokenKind.LEFT_BRACE);
    List<IStatement> statements = new ArrayList<>();
    while (!cursor.check(PunctuationTokenKind.RIGHT_BRACE)) {
      if (cursor.isAtEnd()) throw new UnexpectedTokenException(cursor.current());
      statements.add(new StatementParser(cursor).parse());
    }
    Token end = consume(PunctuationTokenKind.RIGHT_BRACE);
    SourceRange range = SourceConverter.toASTRange(start, end);
    return new BlockStatement(range, statements);
  }
}