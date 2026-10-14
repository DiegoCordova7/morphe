package dev.morphe.parser.declaration;

import dev.morphe.ast.declaration.FunctionDeclaration;
import dev.morphe.ast.declaration.Parameter;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.type.IASTType;
import dev.morphe.lexer.kind.KeywordTokenKind;
import dev.morphe.lexer.kind.PunctuationTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.statement.compound.BlockStatementParser;
import dev.morphe.parser.type.TypeParser;

import java.util.ArrayList;
import java.util.List;

import static dev.morphe.lexer.kind.IdentifierTokenKind.IDENTIFIER;

/**
 * Parses function declarations into AST nodes.
 *
 * <p>A function declaration consists of a name, a comma-separated list of
 * typed parameters, a return type, and a block body.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * func add(a: Integer, b: Integer): Integer {
 *     return a + b;
 * }
 * }</pre>
 *
 * <p>Parameter and return types are delegated to {@link TypeParser}, while
 * the function body is delegated to {@link BlockStatementParser}.</p>
 */
public final class FunctionParser extends AbstractParser implements IDeclarationParser {

  /**
   * Creates a function declaration parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public FunctionParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a function declaration from the current parser position.
   *
   * @return the parsed function declaration
   */
  @Override
  public FunctionDeclaration parse() {
    Token start = consume(KeywordTokenKind.FUNC);
    String name = consume(IDENTIFIER).lexeme();
    consume(PunctuationTokenKind.LEFT_PAREN);
    List<Parameter> parameters = parseParameters();
    consume(PunctuationTokenKind.RIGHT_PAREN);
    consume(PunctuationTokenKind.COLON);
    IASTType returnType = new TypeParser(cursor).parse();
    BlockStatement body = new BlockStatementParser(cursor).parse();
    Token end = cursor.previous();
    SourceRange range = SourceConverter.toASTRange(start, end);
    return new FunctionDeclaration(range, name, parameters, returnType, body);
  }

  /**
   * Parses the comma-separated parameter list of a function.
   *
   * <p>An empty parameter list is valid.</p>
   *
   * @return the parsed function parameters
   */
  private List<Parameter> parseParameters() {
    List<Parameter> parameters = new ArrayList<>();
    if (cursor.check(PunctuationTokenKind.RIGHT_PAREN)) return parameters;
    do parameters.add(parseParameter());
    while (cursor.match(PunctuationTokenKind.COMMA));
    return parameters;
  }

  /**
   * Parses a single function parameter.
   *
   * <p>A parameter has the form:</p>
   *
   * <pre>{@code
   * name: Type
   * }</pre>
   *
   * @return the parsed parameter
   */
  private Parameter parseParameter() {
    String name = consume(IDENTIFIER).lexeme();
    consume(PunctuationTokenKind.COLON);
    IASTType type = new TypeParser(cursor).parse();
    return new Parameter(name, type);
  }
}