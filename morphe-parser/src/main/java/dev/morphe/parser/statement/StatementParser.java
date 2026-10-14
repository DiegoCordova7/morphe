package dev.morphe.parser.statement;

import dev.morphe.ast.statement.IStatement;
import dev.morphe.lexer.kind.*;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.expression.postfix.AssignmentTargetDetector;
import dev.morphe.parser.statement.compound.*;
import dev.morphe.parser.statement.match.MatchStatementParser;
import dev.morphe.parser.statement.simple.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Dispatches statement parsing to the parser responsible for the current
 * statement construct.
 *
 * <p>Statement parsers are selected primarily by the current token kind.
 * Constructs that cannot be identified from a single token, such as
 * variable declarations and assignments, are detected using lookahead.</p>
 *
 * <p>If no specialized statement parser is selected, the statement is
 * parsed as an expression statement.</p>
 */
public final class StatementParser extends AbstractParser {

  private final Map<ITokenKind, Function<TokenCursor, IStatementParser>> parsers;

  /**
   * Creates a statement parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public StatementParser(TokenCursor cursor) {
    super(cursor);
    Map<ITokenKind, Function<TokenCursor, IStatementParser>> parsers = new HashMap<>();
    parsers.put(PunctuationTokenKind.LEFT_BRACE, BlockStatementParser::new);
    parsers.put(OperatorTokenKind.IMMUTABLE, VariableDeclarationStatementParser::new);
    parsers.put(KeywordTokenKind.RETURN, ReturnStatementParser::new);
    parsers.put(KeywordTokenKind.BREAK, BreakStatementParser::new);
    parsers.put(KeywordTokenKind.CONTINUE, ContinueStatementParser::new);
    parsers.put(KeywordTokenKind.IF, IfStatementParser::new);
    parsers.put(KeywordTokenKind.MATCH, MatchStatementParser::new);
    parsers.put(KeywordTokenKind.WHILE, WhileStatementParser::new);
    parsers.put(KeywordTokenKind.DO, DoWhileStatementParser::new);
    parsers.put(KeywordTokenKind.FOR, ForStatementParser::new);
    this.parsers = Map.copyOf(parsers);
  }

  /**
   * Parses the statement beginning at the current token.
   *
   * <p>The parser first attempts to dispatch based on the current token
   * kind. If no direct parser is registered, it checks for variable
   * declarations and assignment statements before falling back to an
   * expression statement.</p>
   *
   * @return the parsed AST statement
   */
  public IStatement parse() {
    if (cursor.check(PunctuationTokenKind.LEFT_BRACE) && startsAnonymousCompositeTypeVariable())
      return new VariableDeclarationStatementParser(cursor).parse();
    Function<TokenCursor, IStatementParser> factory = parsers.get(cursor.current().kind());
    if (factory != null) return factory.apply(cursor).parse();
    if (startsVariableDeclaration()) return new VariableDeclarationStatementParser(cursor).parse();
    if (AssignmentTargetDetector.starts(cursor)) return new AssignmentStatementParser(cursor).parse();
    return new ExpressionStatementParser(cursor).parse();
  }

  private boolean startsAnonymousCompositeTypeVariable() {
    int offset = 1;
    int braceDepth = 1;
    while (braceDepth > 0 && cursor.peek(offset).kind() != SpecialTokenKind.EOF) {
      if (cursor.peek(offset).kind() == PunctuationTokenKind.LEFT_BRACE) braceDepth++;
      else if (cursor.peek(offset).kind() == PunctuationTokenKind.RIGHT_BRACE) braceDepth--;
      offset++;
    }
    while (cursor.peek(offset).kind() == PunctuationTokenKind.LEFT_BRACKET) {
      if (cursor.peek(offset + 1).kind() != PunctuationTokenKind.RIGHT_BRACKET) return false;
      offset += 2;
    }
    return cursor.peek(offset).kind() == IdentifierTokenKind.IDENTIFIER;
  }

  /**
   * Determines whether the current token sequence starts a variable
   * declaration using the name : Type syntax.
   *
   * @return {@code true} if the current token sequence starts a variable
   *     declaration
   */
  public boolean startsVariableDeclaration() {
    if (!cursor.check(IdentifierTokenKind.IDENTIFIER)) return false;
    return cursor.peek(1).kind() == PunctuationTokenKind.COLON;
  }

  /**
   * Determines whether the specified token kind represents a primitive type.
   *
   * @param kind the token kind to inspect
   * @return {@code true} if the token kind represents a supported primitive
   *     type
   */
  private boolean isPrimitiveType(ITokenKind kind) {
    return kind == TypeTokenKind.INTEGER
        || kind == TypeTokenKind.DOUBLE
        || kind == TypeTokenKind.BOOLEAN
        || kind == TypeTokenKind.STRING
        || kind == TypeTokenKind.VOID_TYPE;
  }
}