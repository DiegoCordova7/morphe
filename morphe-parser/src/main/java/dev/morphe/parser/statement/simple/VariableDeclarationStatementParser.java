package dev.morphe.parser.statement.simple;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.VariableDeclarationStatement;
import dev.morphe.ast.type.IASTType;
import dev.morphe.lexer.kind.IdentifierTokenKind;
import dev.morphe.lexer.kind.OperatorTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.IStatementParser;
import dev.morphe.parser.type.TypeParser;

/**
 * Parses variable declaration statements.
 *
 * <p>A variable declaration consists of a variable name, a colon, a type
 * (which may be marked as immutable using the {@code #} operator), and an
 * optional initializer.</p>
 *
 * <p>For example:</p>
 * <pre>{@code
 *  value : Integer;
 *  value : Integer = 10;
 *  constant : #Integer = 10;
 * }</pre>
 *
 * <p>Immutable declarations must provide an initializer. This syntactic
 * requirement is validated by the parser.</p>
 */
public final class VariableDeclarationStatementParser extends AbstractParser implements IStatementParser {

  /**
   * Creates a variable declaration parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public VariableDeclarationStatementParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a variable declaration statement from the current token.
   *
   * <p>The parser consumes the variable name, expects a colon, parses the
   * declared type (checking if it's marked as constant), and then optionally
   * parses an initializer. The declaration must end with a semicolon.</p>
   *
   * @return the parsed variable declaration statement
   * @throws UnexpectedTokenException if an immutable declaration does not
   *     provide an initializer
   */
  @Override
  public VariableDeclarationStatement parse() {
    Token start = cursor.current();
    Token name = consume(IdentifierTokenKind.IDENTIFIER);
    consume(PunctuationTokenKind.COLON);
    IASTType type = new TypeParser(cursor).parse();
    boolean constant = type.isImmutable();
    IExpression initializer = null;
    boolean hasInitializer = cursor.match(OperatorTokenKind.ASSIGN);
    if (hasInitializer) initializer = new ExpressionParser(cursor).parse();
    if (constant && !hasInitializer) throw new UnexpectedTokenException(cursor.current());
    Token end = consume(PunctuationTokenKind.SEMICOLON);
    SourceRange range = SourceConverter.toASTRange(start, end);
    return new VariableDeclarationStatement(range, name.lexeme(), type, constant, initializer);
  }
}