package dev.morphe.parser.statement.compound;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.ExpressionStatement;
import dev.morphe.ast.statement.ForStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.lexer.kind.*;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.expression.postfix.AssignmentTargetDetector;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.IStatementParser;
import dev.morphe.parser.statement.simple.AssignmentStatementParser;
import dev.morphe.parser.statement.simple.ExpressionStatementParser;
import dev.morphe.parser.statement.simple.VariableDeclarationStatementParser;

/**
 * Parses {@code for} statements.
 *
 * <p>A {@code for} statement consists of an optional initialization,
 * an optional condition, an optional update expression, and a loop body:</p>
 *
 * <pre>{@code
 * for (initialization; condition; update) {
 *     statements
 * }
 * }</pre>
 *
 * <p>The initialization may be a variable declaration, an assignment,
 * an expression statement, or omitted entirely. The condition and update
 * expression are also optional.</p>
 */
public final class ForStatementParser extends AbstractParser implements IStatementParser {

  /**
   * Creates a {@code for} statement parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public ForStatementParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a {@code for} statement from the current token.
   *
   * <p>The parser reads the initialization, condition, and update portions
   * of the loop before parsing its block body. Missing initialization,
   * condition, or update expressions are represented by {@code null}.</p>
   *
   * @return the parsed {@link ForStatement}
   */
  @Override
  public ForStatement parse() {
    Token forToken = consume(KeywordTokenKind.FOR);
    consume(PunctuationTokenKind.LEFT_PAREN);
    IStatement initialization = parseInitialization();
    IExpression condition = parseCondition();
    IStatement update = parseUpdate();
    consume(PunctuationTokenKind.RIGHT_PAREN);
    BlockStatement body = new BlockStatementParser(cursor).parse();
    SourceRange range = SourceConverter.toASTRange(forToken, cursor.previous());
    return new ForStatement(range, initialization, condition, update, body);
  }

  /**
   * Parses the initialization section of the {@code for} statement.
   *
   * <p>The initialization is optional. When present, it may be a variable
   * declaration, an assignment, or a general expression statement.</p>
   *
   * @return the parsed initialization statement, or {@code null} if the
   *     initialization is omitted
   */
  private IStatement parseInitialization() {
    if (cursor.check(PunctuationTokenKind.SEMICOLON)) {
      cursor.advance();
      return null;
    }
    if (startsVariableDeclaration()) return new VariableDeclarationStatementParser(cursor).parse();
    if (AssignmentTargetDetector.starts(cursor)) return new AssignmentStatementParser(cursor).parse();
    return new ExpressionStatementParser(cursor).parse();
  }

  /**
   * Determines whether the current tokens can start a variable declaration.
   *
   * <p>A declaration is recognized when an identifier is followed by a colon,
   * representing the new syntax (e.g., {@code i : Integer}).
   * </p>
   *
   * @return {@code true} if the current tokens start a variable declaration
   */
  private boolean startsVariableDeclaration() {
    return cursor.check(IdentifierTokenKind.IDENTIFIER) && cursor.peek(1).kind() == PunctuationTokenKind.COLON;
  }

  /**
   * Determines whether the specified token kind represents a primitive type.
   *
   * @param kind the token kind to inspect
   * @return {@code true} if the token kind represents a supported primitive type
   */
  private boolean isPrimitiveType(ITokenKind kind) {
    return kind == TypeTokenKind.INTEGER
        || kind == TypeTokenKind.DOUBLE
        || kind == TypeTokenKind.BOOLEAN
        || kind == TypeTokenKind.STRING
        || kind == TypeTokenKind.VOID_TYPE;
  }

  /**
   * Parses the condition section of the {@code for} statement.
   *
   * <p>The condition is optional. When omitted, the terminating semicolon
   * is consumed and {@code null} is returned.</p>
   *
   * @return the parsed condition expression, or {@code null} if omitted
   */
  private IExpression parseCondition() {
    if (cursor.check(PunctuationTokenKind.SEMICOLON)) {
      cursor.advance();
      return null;
    }
    IExpression condition = new ExpressionParser(cursor).parse();
    consume(PunctuationTokenKind.SEMICOLON);
    return condition;
  }

  /**
   * Parses the update section of the {@code for} statement.
   *
   * <p>The update expression is optional and ends immediately before the
   * closing parenthesis of the loop header. When present, it is wrapped
   * in an {@link ExpressionStatement} because the AST represents the
   * update as a statement.</p>
   *
   * @return the parsed update statement, or {@code null} if omitted
   */
  private IStatement parseUpdate() {
    if (cursor.check(PunctuationTokenKind.RIGHT_PAREN)) return null;
    IExpression expression = new ExpressionParser(cursor).parse();
    return new ExpressionStatement(expression.range(), expression);
  }
}