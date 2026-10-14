package dev.morphe.lexer.source;

import dev.morphe.lexer.exception.argument.*;

/**
 * Maintains the current position while traversing Morphe source code.
 *
 * <p>
 * A cursor tracks both the absolute character offset and the corresponding
 * one-based line and column coordinates. It provides the primitive operations
 * required by the lexer to inspect and advance through the source without
 * exposing the source traversal state directly.
 * </p>
 *
 * <p>
 * The cursor is mutable and represents a single traversal position within
 * one source string. It does not perform lexical analysis or determine token
 * boundaries.
 * </p>
 */
public final class LexerCursor {

  private final String source;
  private int offset;
  private int line;
  private int column;

  /**
   * Creates a cursor positioned at the beginning of the given source.
   *
   * <p>
   * Source positions are one-based for line and column while the character
   * offset is zero-based.
   * </p>
   *
   * @param source the source text to traverse
   * @throws NullArgumentException if {@code source} is {@code null}
   */
  public LexerCursor(String source) {
    if (source == null) throw new NullArgumentException("source");
    this.source = source;
    this.offset = 0;
    this.line = 1;
    this.column = 1;
  }

  /**
   * Returns whether the source beginning at the current position starts with
   * the specified text.
   *
   * @param value the text to look for
   * @return {@code true} if the source starts with {@code value} at the
   *     current cursor position; {@code false} otherwise
   * @throws NullArgumentException if {@code value} is {@code null}
   */
  public boolean startsWith(String value) {
    if (value == null) throw new NullArgumentException("value");
    return source.startsWith(value, offset);
  }

  /**
   * Returns a substring of the underlying source.
   *
   * <p>
   * The indices use the same zero-based offset convention as
   * {@link String#substring(int, int)}.
   * </p>
   *
   * @param start the inclusive starting offset
   * @param end the exclusive ending offset
   * @return the requested source substring
   * @throws IndexOutOfBoundsException if the requested range is invalid
   */
  public String substring(int start, int end) {
    return source.substring(start, end);
  }

  /**
   * Returns whether the cursor has reached the end of the source.
   *
   * @return {@code true} if no characters remain; {@code false} otherwise
   */
  public boolean isAtEnd() {
    return offset >= source.length();
  }

  /**
   * Returns the character at the current cursor position.
   *
   * @return the current source character
   * @throws StringIndexOutOfBoundsException if the cursor is at the end of the source
   */
  public char current() {
    return source.charAt(offset);
  }

  /**
   * Advances the cursor by one character.
   *
   * <p>
   * Line and column information is updated according to the consumed
   * character. A newline advances the line and resets the column to one;
   * every other character increments the column.
   * </p>
   *
   * @throws StringIndexOutOfBoundsException if the cursor is at the end of the source
   */
  public void advance() {
    char current = source.charAt(offset++);
    if (current == '\n') {
      line++;
      column = 1;
    } else {
      column++;
    }
  }

  /**
   * Returns the current source position.
   *
   * @return the current zero-based offset and one-based line and column
   */
  public SourcePosition position() {
    return new SourcePosition(offset, line, column);
  }

  /**
   * Advances the cursor past all consecutive whitespace characters.
   *
   * <p>
   * Whitespace is determined using {@link Character#isWhitespace(char)}.
   * </p>
   */
  public void skipWhitespace() {
    while (!isAtEnd()) {
      if (!Character.isWhitespace(current())) return;
      advance();
    }
  }

  /**
   * Advances the cursor by the specified number of characters.
   *
   * <p>
   * This operation delegates to {@link #advance()} for each character so
   * that line and column information remains consistent across newlines.
   * </p>
   *
   * @param count the number of characters to consume
   * @throws InvalidArgumentException if {@code count} is negative or exceeds
   *     the number of remaining source characters
   */
  public void advance(int count) {
    if (count < 0) throw new InvalidArgumentException("count", "must not be negative");
    if (count > remaining()) throw new InvalidArgumentException("count", "exceeds remaining source length");
    for (int i = 0; i < count; i++) advance();
  }

  /**
   * Returns the number of source characters remaining from the current
   * position.
   *
   * <p>
   * This method is package-private because it is an implementation detail
   * used by lexer components rather than part of the cursor's public API.
   * </p>
   *
   * @return the number of unconsumed source characters
   */
  int remaining() {
    return source.length() - offset;
  }
}