package dev.morphe.parser;

import dev.morphe.ast.CompilationUnit;
import dev.morphe.ast.declaration.IDeclaration;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.SpecialTokenKind;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.declaration.DeclarationParser;
import dev.morphe.parser.source.SourceConverter;
import dev.morphe.parser.exception.argument.NullArgumentException;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for parsing a Morphe source token stream.
 *
 * <p>The parser consumes the tokens produced by the lexer and builds the
 * corresponding abstract syntax tree (AST). Parsing is performed from the
 * top level by repeatedly delegating declaration parsing to
 * {@link DeclarationParser} until the end-of-file token is reached.</p>
 *
 * <p>This class is responsible only for coordinating parsing at the
 * compilation-unit level. The parsing of declarations, statements,
 * expressions, patterns, and types is delegated to their respective
 * parser components.</p>
 *
 * <p>For example, a source file containing several declarations is parsed
 * into a single {@link CompilationUnit} containing those declarations.</p>
 */
public final class Parser {

  /**
   * Cursor used to traverse the lexer token stream.
   */
  private final TokenCursor cursor;

  /**
   * Creates a parser for the specified token stream.
   *
   * @param tokens the tokens produced by the lexer
   * @throws NullArgumentException if {@code tokens} is {@code null}
   */
  public Parser(List<Token> tokens) {
    if (tokens == null) throw new NullArgumentException("tokens");
    this.cursor = new TokenCursor(tokens);
  }

  /**
   * Parses the complete token stream into a compilation unit.
   *
   * <p>Declarations are parsed sequentially until the end-of-file token
   * is reached. The resulting compilation unit covers the parsed source
   * range. If the token stream contains only EOF, the range is derived
   * from that token.</p>
   *
   * @return the parsed compilation unit
   */
  public CompilationUnit parse() {
    List<IDeclaration> declarations = new ArrayList<>();
    Token start = cursor.current();
    while (!cursor.check(SpecialTokenKind.EOF)) declarations.add(new DeclarationParser(cursor).parse());
    Token end = declarations.isEmpty() ? start : cursor.previous();
    SourceRange range = SourceConverter.toASTRange(start, end);
    return new CompilationUnit(declarations, range);
  }
}