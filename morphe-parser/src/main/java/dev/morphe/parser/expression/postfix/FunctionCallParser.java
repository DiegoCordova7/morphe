package dev.morphe.parser.expression.postfix;

import dev.morphe.ast.expression.FunctionCallExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.ExpressionParser;
import dev.morphe.parser.source.SourceConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses function call expressions.
 *
 * <p>A function call consists of a target expression followed by a
 * parenthesized list of argument expressions.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * print(value)
 * add(a, b)
 * object.method(argument)
 * getFunction()(value)
 * }</pre>
 *
 * <p>The opening parenthesis is consumed by the surrounding postfix
 * parser. This parser is responsible for parsing the arguments and
 * consuming the closing parenthesis.</p>
 */
public final class FunctionCallParser extends AbstractParser {

  /**
   * Creates a function call parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public FunctionCallParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a function call using the specified function expression.
   *
   * <p>Each argument is parsed as a complete expression, allowing
   * nested calls, operators, field accesses, and other expression
   * constructs.</p>
   *
   * <p>The resulting source range begins at the start of the function
   * expression and ends at the closing parenthesis.</p>
   *
   * @param function the expression being invoked
   * @return the parsed function call expression
   */
  public IExpression parse(IExpression function) {
    List<IExpression> arguments = parseArguments();
    Token end = consume(PunctuationTokenKind.RIGHT_PAREN);
    SourceRange range = new SourceRange(
        function.range().start(),
        SourceConverter.toASTPosition(end.span().end())
    );
    return new FunctionCallExpression(range, function, arguments);
  }

  /**
   * Parses the comma-separated argument list.
   *
   * <p>The argument list may be empty.</p>
   *
   * @return the parsed function arguments
   */
  private List<IExpression> parseArguments() {
    List<IExpression> arguments = new ArrayList<>();
    if (cursor.check(PunctuationTokenKind.RIGHT_PAREN)) return arguments;
    do arguments.add(new ExpressionParser(cursor).parse());
    while (cursor.match(PunctuationTokenKind.COMMA));
    return arguments;
  }
}