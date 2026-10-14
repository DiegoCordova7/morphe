package dev.morphe.parser.statement.simple;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.operator.AssignmentOperator;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.AssignmentStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.lexer.kind.OperatorTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.IStatementParser;

/**
 * Parses assignment statements.
 *
 * <p>An assignment consists of a target expression, an assignment operator,
 * a value expression, and a terminating semicolon.</p>
 *
 * <p>Supported assignment operators are:</p>
 *
 * <ul>
 *   <li>{@code =} for direct assignment</li>
 *   <li>{@code +=} for addition assignment</li>
 *   <li>{@code -=} for subtraction assignment</li>
 *   <li>{@code *=} for multiplication assignment</li>
 *   <li>{@code /=} for division assignment</li>
 * </ul>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * value = 10;
 * counter += 1;
 * result *= factor;
 * object.field = value;
 * array[index] -= amount;
 * }</pre>
 *
 * <p>The semantic validity of the target expression is determined during
 * semantic analysis.</p>
 */
public final class AssignmentStatementParser extends AbstractParser implements IStatementParser {

  /**
   * Creates an assignment statement parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public AssignmentStatementParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses an assignment statement from the current token.
   *
   * <p>The target is first parsed as a complete expression. The remaining
   * assignment syntax is then parsed using that expression as the target.</p>
   *
   * @return the parsed assignment statement
   */
  @Override
  public IStatement parse() {
    IExpression target = new ExpressionParser(cursor).parse();
    return parse(target);
  }

  /**
   * Parses the assignment operator and value following the specified target.
   *
   * @param target the expression receiving the assigned value
   * @return the parsed assignment statement
   * @throws UnexpectedTokenException if the current token is not a supported
   *     assignment operator
   */
  private IStatement parse(IExpression target) {
    Token operatorToken = cursor.current();
    AssignmentOperator operator = parseOperator(operatorToken);
    cursor.advance();
    IExpression value = new ExpressionParser(cursor).parse();
    Token end = consume(PunctuationTokenKind.SEMICOLON);
    SourceRange range = new SourceRange(target.range().start(), SourceConverter.toASTPosition(end.span().end()));
    return new AssignmentStatement(range, target, operator, value);
  }

  /**
   * Converts an assignment operator token into its corresponding AST
   * assignment operator.
   *
   * @param token the token containing the assignment operator
   * @return the corresponding AST assignment operator
   * @throws UnexpectedTokenException if the token does not represent a
   *     supported assignment operator
   */
  private AssignmentOperator parseOperator(Token token) {
    if (!(token.kind() instanceof OperatorTokenKind operator)) throw new UnexpectedTokenException(token);
    return switch (operator) {
      case ASSIGN -> AssignmentOperator.ASSIGN;
      case PLUS_ASSIGN -> AssignmentOperator.ADD;
      case MINUS_ASSIGN -> AssignmentOperator.SUBTRACT;
      case MULTIPLY_ASSIGN -> AssignmentOperator.MULTIPLY;
      case DIVIDE_ASSIGN -> AssignmentOperator.DIVIDE;
      case MODULO_ASSIGN -> AssignmentOperator.MODULO;
      default -> throw new UnexpectedTokenException(token);
    };
  }
}