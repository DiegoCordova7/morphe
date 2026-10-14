package dev.morphe.parser.expression.compound;

import dev.morphe.ast.expression.CompositeInstanceExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.IdentifierTokenKind;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.kind.OperatorTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.expression.IExpressionParser;
import dev.morphe.parser.source.SourceConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses composite instance expressions.
 *
 * <p>A composite instance expression creates a value of a named composite
 * type and initializes its fields using explicit field initializers.</p>
 *
 * <p>The general syntax is:</p>
 *
 * <pre>{@code
 * new TypeName {
 *     field = expression;
 *     otherField = expression;
 * }
 * }</pre>
 *
 * <p>Each field initializer associates a field name with an expression
 * whose value will be used to initialize that field. The semantic
 * validation of the field names and their values is performed during
 * semantic analysis.</p>
 */
public final class CompositeInstanceExpressionParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates a composite instance expression parser using the specified
   * token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public CompositeInstanceExpressionParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a composite instance expression from the current parser
   * position.
   *
   * <p>The parser consumes the composite type name, parses all field
   * initializers enclosed by braces, and creates the corresponding AST
   * expression.</p>
   *
   * @return the parsed composite instance expression
   * @throws UnexpectedTokenException if the input reaches EOF before the
   *     closing brace is found
   */
  @Override
  public IExpression parse() {
    Token start = consume(KeywordTokenKind.NEW);
    String typeName = consume(IdentifierTokenKind.IDENTIFIER).lexeme();
    consume(PunctuationTokenKind.LEFT_BRACE);
    List<CompositeInstanceExpression.FieldInitializer> fields = new ArrayList<>();
    while (!cursor.check(PunctuationTokenKind.RIGHT_BRACE)) {
      if (cursor.isAtEnd()) throw new UnexpectedTokenException(cursor.current());
      fields.add(parseFieldInitializer());
    }
    Token end = consume(PunctuationTokenKind.RIGHT_BRACE);
    SourceRange range = SourceConverter.toASTRange(start, end);
    return new CompositeInstanceExpression(range, typeName, fields);
  }

  /**
   * Parses a single composite field initializer.
   *
   * <p>A field initializer has the form:</p>
   *
   * <pre>{@code
   * field = expression;
   * }</pre>
   *
   * @return the parsed field initializer
   */
  private CompositeInstanceExpression.FieldInitializer parseFieldInitializer() {
    Token name = consume(IdentifierTokenKind.IDENTIFIER);
    consume(OperatorTokenKind.ASSIGN);
    IExpression value = new ExpressionParser(cursor).parse();
    consume(PunctuationTokenKind.SEMICOLON);
    return new CompositeInstanceExpression.FieldInitializer(name.lexeme(), value);
  }
}