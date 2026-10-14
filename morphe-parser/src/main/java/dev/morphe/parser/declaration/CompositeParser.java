package dev.morphe.parser.declaration;

import dev.morphe.ast.declaration.CompositeDeclaration;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.type.CompositeType;
import dev.morphe.ast.type.IASTType;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.type.TypeParser;

import java.util.ArrayList;
import java.util.List;

import static dev.morphe.lexer.kind.IdentifierTokenKind.IDENTIFIER;

/**
 * Parses composite type declarations into AST nodes.
 *
 * <p>A composite declaration defines a named type containing one or more
 * typed fields.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * type Person {
 *     name: String;
 *     age: Integer;
 * }
 * }</pre>
 *
 * <p>Each field consists of an identifier, a colon, a type expression,
 * and a terminating semicolon.</p>
 */
public final class CompositeParser extends AbstractParser implements IDeclarationParser {

  /**
   * Creates a composite declaration parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public CompositeParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a composite type declaration from the current parser position.
   *
   * @return the parsed composite declaration
   */
  @Override
  public CompositeDeclaration parse() {
    Token start = consume(KeywordTokenKind.TYPE);
    String name = consume(IDENTIFIER).lexeme();
    consume(PunctuationTokenKind.LEFT_BRACE);
    List<CompositeType.CompositeField> fields = new ArrayList<>();
    while (!cursor.check(PunctuationTokenKind.RIGHT_BRACE)) fields.add(parseField());
    Token end = consume(PunctuationTokenKind.RIGHT_BRACE);
    SourceRange range = SourceConverter.toASTRange(start, end);
    return new CompositeDeclaration(range, name, fields);
  }

  /**
   * Parses a single field declaration inside a composite type.
   *
   * <p>A field has the following form:</p>
   *
   * <pre>{@code
   * name: Type;
   * }</pre>
   *
   * @return the parsed composite field
   */
  private CompositeType.CompositeField parseField() {
    String name = consume(IDENTIFIER).lexeme();
    consume(PunctuationTokenKind.COLON);
    IASTType type = new TypeParser(cursor).parse();
    consume(PunctuationTokenKind.SEMICOLON);
    return new CompositeType.CompositeField(name, type);
  }
}