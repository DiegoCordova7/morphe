package dev.morphe.parser.expression.primary;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.IExpressionParser;
import dev.morphe.parser.source.SourceConverter;

import static dev.morphe.lexer.kind.IdentifierTokenKind.IDENTIFIER;

/**
 * Parses variable expressions into {@link VariableExpression} nodes.
 *
 * <p>A variable expression consists of an identifier that refers to a
 * variable in the current semantic scope.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * value
 * counter
 * result
 * }</pre>
 */
public final class VariableParser extends AbstractParser implements IExpressionParser {

  /**
   * Creates a variable parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public VariableParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses the identifier at the current token as a variable expression.
   *
   * @return the parsed variable expression
   */
  @Override
  public IExpression parse() {
    Token token = consume(IDENTIFIER);
    SourceRange range = SourceConverter.toASTRange(token);
    return new VariableExpression(range, token.lexeme());
  }
}