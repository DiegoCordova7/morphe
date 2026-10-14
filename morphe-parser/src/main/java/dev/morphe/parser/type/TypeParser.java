package dev.morphe.parser.type;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.type.*;
import dev.morphe.lexer.kind.ErrorTokenKind;
import dev.morphe.lexer.kind.IdentifierTokenKind;
import dev.morphe.lexer.kind.OperatorTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.kind.TypeTokenKind;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses type expressions into AST types.
 *
 * <p>This parser supports primitive types, named types, array types,
 * and immutable type modifiers ({@code #}). Array dimensions may be repeated
 * to represent multidimensional arrays.</p>
 *
 * <p>Examples of supported types include:</p>
 * <pre>
 *   Integer
 *   #Integer
 *   Person
 *   #Person[]
 * </pre>
 *
 * <p>The parser creates AST type nodes only. Resolution of named types and
 * semantic validation of the resulting type are performed during later
 * compilation stages.</p>
 */
public final class TypeParser extends AbstractParser {

  /**
   * Creates a type parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   * @throws NullArgumentException if {@code cursor} is {@code null}
   */
  public TypeParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a complete type expression.
   *
   * <p>The parser optionally checks for an immutability modifier ({@code #}),
   * reads the base type, and then consumes any number of array dimensions following it.
   * If the immutability modifier was present, the resulting type is wrapped in an {@link ImmutableType}.</p>
   *
   * @return the parsed AST type
   * @throws UnexpectedTokenException if the current token does not start
   *     a valid type
   */
  public IASTType parse() {
    boolean immutable = cursor.match(OperatorTokenKind.IMMUTABLE);
    IASTType type = parseBaseType();
    type = parseArrayType(type);
    return immutable ? new ImmutableType(type) : type;
  }

  /**
   * Parses the base portion of a type.
   *
   * <p>Inspects the current token to determine whether it corresponds to a primitive type,
   * a built-in error type, an anonymous composite type, or a named type reference.</p>
   *
   * @return the parsed base AST type
   * @throws UnexpectedTokenException if the current token does not represent a valid base type
   */
  public IASTType parseBaseType() {
    if (cursor.check(TypeTokenKind.INTEGER)) return consumeBaseType(PrimitiveType.INTEGER);
    if (cursor.check(TypeTokenKind.DOUBLE)) return consumeBaseType(PrimitiveType.DOUBLE);
    if (cursor.check(TypeTokenKind.BOOLEAN)) return consumeBaseType(PrimitiveType.BOOLEAN);
    if (cursor.check(TypeTokenKind.STRING)) return consumeBaseType(PrimitiveType.STRING);
    if (cursor.check(TypeTokenKind.VOID_TYPE)) return consumeBaseType(PrimitiveType.VOID);
    if (cursor.check(ErrorTokenKind.DIVISION_BY_ZERO)) return consumeErrorType(ErrorKind.DIVISION_BY_ZERO);
    if (cursor.check(ErrorTokenKind.MODULO_BY_ZERO)) return consumeErrorType(ErrorKind.MODULO_BY_ZERO);
    if (cursor.check(ErrorTokenKind.INVALID_ARRAY_INDEX)) return consumeErrorType(ErrorKind.INVALID_ARRAY_INDEX);
    if (cursor.check(PunctuationTokenKind.LEFT_BRACE)) return parseAnonymousCompositeType();
    if (cursor.check(IdentifierTokenKind.IDENTIFIER)) return parseNamedType();
    throw new UnexpectedTokenException(cursor.current());
  }

  /**
   * Consumes a primitive type token and advances the cursor.
   *
   * @param type the primitive type being consumed
   * @return the corresponding primitive type node
   */
  private IASTType consumeBaseType(PrimitiveType type) {
    cursor.advance();
    return type;
  }

  /**
   * Consumes an error-related token and produces a corresponding {@link ErrorType}.
   *
   * @param kind the specific error kind represented by the token
   * @return the constructed error AST type
   */
  private IASTType consumeErrorType(ErrorKind kind) {
    cursor.advance();
    return new ErrorType(kind);
  }

  /**
   * Parses a named type reference from the current identifier token.
   *
   * @return the constructed {@link NamedType} node
   */
  private IASTType parseNamedType() {
    String name = cursor.current().lexeme();
    cursor.advance();
    return new NamedType(name);
  }

  /**
   * Parses zero or more array dimensions following a base type.
   *
   * <p>Each {@code []} pair wraps the previously parsed type in an
   * {@link ArrayType}. Consequently, repeated dimensions naturally produce
   * nested array types.</p>
   *
   * @param type the base type from which array nesting begins
   * @return the resulting AST type
   * @throws UnexpectedTokenException if an opening bracket is not followed
   *     by a closing bracket
   */
  private IASTType parseArrayType(IASTType type) {
    while (cursor.check(PunctuationTokenKind.LEFT_BRACKET)) {
      cursor.advance();
      if (!cursor.check(PunctuationTokenKind.RIGHT_BRACKET)) throw new UnexpectedTokenException(cursor.current());
      cursor.advance();
      type = new ArrayType(type);
    }
    return type;
  }

  /**
   * Parses an anonymous composite type definition enclosed in braces.
   *
   * <p>Expects a sequence of fields defined by a name, a colon, and a type,
   * separated optionally by semicolons, terminated by a closing right brace.</p>
   *
   * @return the constructed {@link AnonymousCompositeType} node
   * @throws UnexpectedTokenException if a field definition or punctuation is malformed
   */
  private IASTType parseAnonymousCompositeType() {
    cursor.advance();
    List<CompositeType.CompositeField> fields = new ArrayList<>();
    while (!cursor.check(PunctuationTokenKind.RIGHT_BRACE)) {
      String fieldName = consume(IdentifierTokenKind.IDENTIFIER).lexeme();
      consume(PunctuationTokenKind.COLON);
      IASTType fieldType = parse();
      fields.add(new CompositeType.CompositeField(fieldName, fieldType));
      if (cursor.check(PunctuationTokenKind.SEMICOLON)) cursor.advance();
    }
    cursor.advance();
    return new AnonymousCompositeType(fields);
  }
}