package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.CompositePattern;
import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.source.SourceConverter;

import java.util.ArrayList;
import java.util.List;

import static dev.morphe.lexer.kind.IdentifierTokenKind.IDENTIFIER;

/**
 * Parses composite patterns into AST nodes.
 *
 * <p>A composite pattern identifies a composite type by name and provides
 * patterns for the fields that participate in the match.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * Person {
 *     name : "Diego",
 *     age : 25
 * }
 * }</pre>
 *
 * <p>Field parsing is delegated to {@link FieldPatternParser}, allowing
 * each field pattern to use the complete pattern grammar.</p>
 *
 * <p>The parser preserves the referenced type name as source-level
 * information. Resolution of the composite type and validation of its
 * fields are performed during semantic analysis.</p>
 */
public final class CompositePatternParser extends AbstractParser implements IPatternParser {

  /**
   * Creates a composite pattern parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public CompositePatternParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a composite pattern from the current parser position.
   *
   * <p>The parser consumes the composite type name, parses the enclosed
   * field patterns separated by commas, and consumes the closing brace.</p>
   *
   * @return the parsed composite pattern
   */
  @Override
  public IPattern parse() {
    Token typeName = consume(IDENTIFIER);
    consume(PunctuationTokenKind.LEFT_BRACE);
    List<CompositePattern.FieldPattern> fields = new ArrayList<>();
    if (!cursor.check(PunctuationTokenKind.RIGHT_BRACE)) {
      do fields.add(new FieldPatternParser(cursor).parse());
      while (cursor.match(PunctuationTokenKind.COMMA));
    }
    Token rightBrace = consume(PunctuationTokenKind.RIGHT_BRACE);
    SourceRange range = SourceConverter.toASTRange(typeName, rightBrace);
    return new CompositePattern(range, typeName.lexeme(), fields);
  }
}