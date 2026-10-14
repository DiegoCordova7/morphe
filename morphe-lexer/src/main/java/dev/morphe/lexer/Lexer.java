package dev.morphe.lexer;

import dev.morphe.lexer.exception.argument.NullArgumentException;
import dev.morphe.lexer.exception.source.UnexpectedCharacterException;
import dev.morphe.lexer.exception.source.UnterminatedCommentException;
import dev.morphe.lexer.exception.source.UnterminatedStringException;
import dev.morphe.lexer.kind.ITokenKind;
import dev.morphe.lexer.kind.LiteralTokenKind;
import dev.morphe.lexer.kind.SpecialTokenKind;
import dev.morphe.lexer.source.*;
import dev.morphe.lexer.token.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Performs lexical analysis of Morphe source code.
 *
 * <p>
 * The lexer transforms a source string into an immutable sequence of
 * {@link Token tokens}. It recognizes identifiers, keywords, numeric
 * literals, string literals, comments, and symbolic tokens such as operators,
 * punctuation, and pattern-matching symbols.
 * </p>
 *
 * <p>
 * Source traversal and position tracking are delegated to {@link LexerCursor},
 * while token-kind resolution is delegated to {@link TokenKindResolver}.
 * The lexer is therefore responsible primarily for coordinating the
 * tokenization process and constructing tokens with their corresponding
 * source locations.
 * </p>
 *
 * <p>
 * Comments and whitespace are consumed during lexical analysis and do not
 * produce tokens.
 * </p>
 *
 * <p>
 * The lexer does not perform syntactic or semantic analysis. The resulting
 * token sequence is consumed by the parser in a subsequent compilation stage.
 * </p>
 */
public final class Lexer {

  private final LexerCursor cursor;
  private final TokenKindResolver kindResolver;

  /**
   * Creates a lexer for the specified Morphe source.
   *
   * @param source the source code to tokenize
   * @throws NullArgumentException if {@code source} is {@code null}
   */
  public Lexer(String source) {
    if (source == null) throw new NullArgumentException("source");
    this.cursor = new LexerCursor(source);
    this.kindResolver = new TokenKindResolver();
  }

  /**
   * Tokenizes the complete source.
   *
   * <p>
   * Tokenization continues until an {@link SpecialTokenKind#EOF EOF} token
   * is produced. The returned list includes the EOF token and is immutable.
   * </p>
   *
   * @return the tokens produced from the source, including EOF
   * @throws UnexpectedCharacterException if the source contains a character
   *     that cannot begin any recognized token
   * @throws UnterminatedCommentException if a block comment is not terminated
   * @throws UnterminatedStringException if a string literal is not terminated
   */
  public List<Token> tokenize() {
    List<Token> tokens = new ArrayList<>();
    Token token;
    do {
      token = nextToken();
      tokens.add(token);
    } while (token.kind() != SpecialTokenKind.EOF);
    return List.copyOf(tokens);
  }

  /**
   * Scans the next token from the current source position.
   *
   * <p>
   * Whitespace and comments are ignored before attempting to recognize the
   * next token. Comment scanning is performed before symbolic token scanning
   * so that comment delimiters such as {@code //} and {@code /*} take
   * precedence over symbols beginning with {@code /}.
   * </p>
   *
   * <p>
   * Token recognition is performed according to the first character of the
   * remaining source.
   * </p>
   *
   * @return the next token
   * @throws UnexpectedCharacterException if the current character cannot
   *     begin a recognized token
   * @throws UnterminatedCommentException if a block comment is not terminated
   * @throws UnterminatedStringException if a string literal is not terminated
   */
  private Token nextToken() {
    do cursor.skipWhitespace();
    while (skipComment());
    if (cursor.isAtEnd()) return eofToken();
    char character = cursor.current();
    if (isIdentifierStart(character)) return scanIdentifierOrKeyword();
    if (isDigit(character)) return scanNumber();
    if (character == '"') return scanString();
    if (kindResolver.canStartSymbol(character)) return scanSymbol();
    throw new UnexpectedCharacterException(character, cursor.position());
  }

  /**
   * Skips a comment beginning at the current source position.
   *
   * <p>
   * Both line comments beginning with {@code //} and block comments beginning
   * with {@code /*} are recognized. The comment contents are consumed without
   * producing a token.
   * </p>
   *
   * @return {@code true} if a comment was found and skipped; {@code false}
   *     otherwise
   * @throws UnterminatedCommentException if a block comment reaches the end
   *     of the source without a closing {@code *&#47;} delimiter
   */
  private boolean skipComment() {
    if (cursor.startsWith("//")) {
      skipLineComment();
      return true;
    }
    if (cursor.startsWith("/*")) {
      skipBlockComment();
      return true;
    }
    return false;
  }

  /**
   * Skips a line comment.
   *
   * <p>
   * The comment begins with {@code //} and extends to the next line
   * terminator or the end of the source. The line terminator itself is not
   * consumed by this method and is handled as whitespace during the next
   * tokenization step.
   * </p>
   */
  private void skipLineComment() {
    cursor.advance(2);
    while (!cursor.isAtEnd() && cursor.current() != '\n') cursor.advance();
  }

  /**
   * Skips a block comment.
   *
   * <p>
   * The comment begins with {@code /*} and ends at the next {@code *&#47;}
   * delimiter. Block comments may span multiple lines and are discarded
   * without producing a token.
   * </p>
   *
   * @throws UnterminatedCommentException if the end of the source is reached
   *     before a closing {@code *&#47;} delimiter is found
   */
  private void skipBlockComment() {
    SourcePosition start = cursor.position();
    cursor.advance(2);
    while (!cursor.isAtEnd() && !cursor.startsWith("*/")) cursor.advance();
    if (cursor.isAtEnd()) throw new UnterminatedCommentException(start);
    cursor.advance(2);
  }

  /**
   * Creates the end-of-file token at the current source position.
   *
   * <p>
   * EOF is represented by an empty lexeme and a zero-width source span.
   * </p>
   *
   * @return the EOF token
   */
  private Token eofToken() {
    SourcePosition position = cursor.position();
    return new Token(SpecialTokenKind.EOF, "", new SourceSpan(position, position));
  }

  /**
   * Scans an identifier or keyword.
   *
   * <p>
   * The complete identifier lexeme is first extracted and then passed to
   * {@link TokenKindResolver} to determine whether it represents an
   * identifier or a reserved token such as a keyword or type.
   * </p>
   *
   * @return the resulting identifier or keyword token
   */
  private Token scanIdentifierOrKeyword() {
    SourcePosition start = cursor.position();
    do cursor.advance();
    while (!cursor.isAtEnd() && isIdentifierPart(cursor.current()));
    SourcePosition end = cursor.position();
    String lexeme = cursor.substring(start.offset(), end.offset());
    ITokenKind kind = kindResolver.resolve(lexeme);
    return new Token(kind, lexeme, new SourceSpan(start, end));
  }

  /**
   * Scans an integer or floating-point numeric literal.
   *
   * <p>
   * A decimal point is considered part of a floating-point literal only
   * when it does not begin the range operator {@code ..}. This allows
   * constructs such as {@code 1..10} to be tokenized as an integer followed
   * by a range operator rather than as an invalid numeric literal.
   * </p>
   *
   * @return the resulting integer or double literal token
   */
  private Token scanNumber() {
    SourcePosition start = cursor.position();
    while (!cursor.isAtEnd() && isDigit(cursor.current())) cursor.advance();
    boolean isDouble = false;
    if (!cursor.isAtEnd() && cursor.current() == '.' && !cursor.startsWith("..")) {
      isDouble = true;
      do cursor.advance();
      while (!cursor.isAtEnd() && isDigit(cursor.current()));
    }
    SourcePosition end = cursor.position();
    String lexeme = cursor.substring(start.offset(), end.offset());
    LiteralTokenKind kind = isDouble ? LiteralTokenKind.DOUBLE : LiteralTokenKind.INTEGER;
    return new Token(kind, lexeme, new SourceSpan(start, end));
  }

  /**
   * Scans a string literal delimited by double quotes.
   *
   * <p>
   * The resulting lexeme includes both delimiter characters. String
   * interpretation and escape-sequence processing, if supported by the
   * language, are handled by later compilation stages.
   * </p>
   *
   * @return the resulting string literal token
   * @throws UnterminatedStringException if the closing quote is not found
   */
  private Token scanString() {
    SourcePosition start = cursor.position();
    do cursor.advance();
    while (!cursor.isAtEnd() && cursor.current() != '"');
    if (cursor.isAtEnd()) throw new UnterminatedStringException(start);
    cursor.advance();
    SourcePosition end = cursor.position();
    String lexeme = cursor.substring(start.offset(), end.offset());
    return new Token(LiteralTokenKind.STRING, lexeme, new SourceSpan(start, end));
  }

  /**
   * Scans a symbolic token using longest-match resolution.
   *
   * <p>
   * {@link TokenKindResolver} determines the longest registered symbol that
   * matches the current source position. This is necessary for symbols that
   * share prefixes, such as {@code =} and {@code ==}, or {@code .} and
   * {@code ..}.
   * </p>
   *
   * @return the resulting symbolic token
   * @throws UnexpectedCharacterException if no registered symbol matches the
   *     current position
   */
  private Token scanSymbol() {
    SourcePosition start = cursor.position();
    String lexeme = kindResolver.resolveSymbol(cursor);
    if (lexeme == null) throw new UnexpectedCharacterException(cursor.current(), start);
    cursor.advance(lexeme.length());
    SourcePosition end = cursor.position();
    ITokenKind kind = kindResolver.resolve(lexeme);
    return new Token(kind, lexeme, new SourceSpan(start, end));
  }

  /**
   * Returns whether the character can begin an identifier.
   *
   * <p>
   * Morphe identifiers currently begin with an ASCII letter.
   * </p>
   *
   * @param character the character to test
   * @return {@code true} if the character can begin an identifier;
   *     {@code false} otherwise
   */
  private boolean isIdentifierStart(char character) {
    return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
  }

  /**
   * Returns whether the character can occur after the first character
   * of an identifier.
   *
   * <p>
   * Identifier parts may contain letters, digits, and underscores.
   * </p>
   *
   * @param character the character to test
   * @return {@code true} if the character can occur in an identifier;
   *     {@code false} otherwise
   */
  private boolean isIdentifierPart(char character) {
    return isIdentifierStart(character) || isDigit(character) || character == '_';
  }

  /**
   * Returns whether the character represents an ASCII decimal digit.
   *
   * @param character the character to test
   * @return {@code true} if the character is an ASCII decimal digit;
   *     {@code false} otherwise
   */
  private boolean isDigit(char character) {
    return character >= '0' && character <= '9';
  }
}