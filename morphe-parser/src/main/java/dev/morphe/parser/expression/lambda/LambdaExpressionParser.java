package dev.morphe.parser.expression.lambda;

import dev.morphe.ast.declaration.Parameter;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.expression.LambdaExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.type.IASTType;
import dev.morphe.lexer.kind.IdentifierTokenKind;
import dev.morphe.lexer.kind.OperatorTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.expression.IExpressionParser;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.type.TypeParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses lambda expressions.
 *
 * <p>A lambda expression consists of a parenthesized list of explicitly
 * typed parameters, followed by the lambda arrow and an expression body.</p>
 *
 * <p>The general syntax is:</p>
 *
 * <pre>{@code
 * (parameter, ...) -> expression
 * }</pre>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * () -> 42
 * (Integer value) -> value + 1
 * (Integer a, Integer b) -> a + b
 * (Person person) -> person.name
 * }</pre>
 *
 * <p>The parser is responsible for constructing the corresponding
 * {@link LambdaExpression}. Lambda detection is handled separately by
 * {@link LambdaDetector}.</p>
 */
public final class LambdaExpressionParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates a lambda expression parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public LambdaExpressionParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a lambda expression from the current parser position.
   *
   * <p>The resulting source range begins at the opening parenthesis and
   * ends at the end of the lambda body.</p>
   *
   * @return the parsed lambda expression
   */
  @Override
  public IExpression parse() {
    Token start = consume(PunctuationTokenKind.LEFT_PAREN);
    List<Parameter> parameters = parseParameters();
    consume(PunctuationTokenKind.RIGHT_PAREN);
    consume(OperatorTokenKind.ARROW);
    IExpression body = new ExpressionParser(cursor).parse();
    SourceRange range = new SourceRange(
        SourceConverter.toASTPosition(start.span().start()),
        body.range().end()
    );
    return new LambdaExpression(range, parameters, body);
  }

  /**
   * Parses the parameter list of the lambda.
   *
   * <p>Each parameter consists of an explicitly declared type followed
   * by its identifier. The parameter list may be empty.</p>
   *
   * @return the parsed lambda parameters
   */
  private List<Parameter> parseParameters() {
    List<Parameter> parameters = new ArrayList<>();
    if (cursor.check(PunctuationTokenKind.RIGHT_PAREN)) return parameters;
    do {
      IASTType type = new TypeParser(cursor).parse();
      Token name = consume(IdentifierTokenKind.IDENTIFIER);
      parameters.add(new Parameter(name.lexeme(), type));
    } while (cursor.match(PunctuationTokenKind.COMMA));
    return parameters;
  }
}