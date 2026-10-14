package dev.morphe.parser.source;

import dev.morphe.parser.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourcePosition;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.token.Token;

/**
 * Provides conversions between source-location types used by the lexer
 * and those used by the abstract syntax tree.
 *
 * <p>The lexer and AST maintain independent source-location types so that
 * their models remain decoupled. This class bridges those representations
 * while preserving the offset, line, and column information.</p>
 */
public final class SourceConverter {

  /**
   * Prevents instantiation of this utility class.
   */
  private SourceConverter() {}

  /**
   * Converts a lexer source position into an AST source position.
   *
   * @param position the lexer source position
   * @return the equivalent AST source position
   * @throws NullArgumentException if {@code position} is {@code null}
   */
  public static SourcePosition toASTPosition(dev.morphe.lexer.source.SourcePosition position) {
    if (position == null) throw new NullArgumentException("position");
    return new SourcePosition(position.offset(), position.line(), position.column());
  }

  /**
   * Converts the source span of a token into an AST source range.
   *
   * @param token the token whose source span is converted
   * @return an AST source range covering the token
   * @throws NullArgumentException if {@code token} is {@code null}
   */
  public static SourceRange toASTRange(Token token) {
    if (token == null) throw new NullArgumentException("token");
    return new SourceRange(toASTPosition(token.span().start()), toASTPosition(token.span().end()));
  }

  /**
   * Creates an AST source range spanning from the beginning of one token
   * to the end of another token.
   *
   * <p>This method is useful for AST nodes whose source range covers
   * multiple tokens, such as complete statements, declarations, and
   * compound expressions.</p>
   *
   * @param start the first token covered by the range
   * @param end the last token covered by the range
   * @return an AST source range spanning both tokens
   * @throws NullArgumentException if {@code start} or {@code end} is
   *     {@code null}
   */
  public static SourceRange toASTRange(Token start, Token end) {
    if (start == null) throw new NullArgumentException("start");
    if (end == null) throw new NullArgumentException("end");
    return new SourceRange(toASTPosition(start.span().start()), toASTPosition(end.span().end()));
  }
}