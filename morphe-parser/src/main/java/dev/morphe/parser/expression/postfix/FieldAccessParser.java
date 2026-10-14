package dev.morphe.parser.expression.postfix;

import dev.morphe.ast.expression.FieldAccessExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.source.SourceConverter;

import static dev.morphe.lexer.kind.IdentifierTokenKind.IDENTIFIER;

/**
 * Parses a field access expression.
 *
 * <p>A field access selects a named field from an already parsed target
 * expression.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * person.name
 * person.address.city
 * object.field
 * }</pre>
 *
 * <p>The dot preceding the field is consumed by the surrounding postfix
 * parser. This parser receives the target expression and consumes only
 * the field identifier.</p>
 */
public final class FieldAccessParser extends AbstractParser {

  /**
   * Creates a field access parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public FieldAccessParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a field access using the specified target expression.
   *
   * <p>The resulting source range begins at the start of the target
   * expression and ends at the end of the field identifier.</p>
   *
   * @param target the expression whose field is being accessed
   * @return the parsed field access expression
   */
  public IExpression parse(IExpression target) {
    Token field = consume(IDENTIFIER);
    SourceRange range = new SourceRange(
        target.range().start(),
        SourceConverter.toASTPosition(field.span().end())
    );
    return new FieldAccessExpression(range, target, field.lexeme());
  }
}