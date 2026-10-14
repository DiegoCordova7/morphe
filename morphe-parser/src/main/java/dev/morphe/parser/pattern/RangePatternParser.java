package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.RangePattern;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.MatchTokenKind;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;

/**
 * Parses range patterns into AST nodes.
 *
 * <p>A range pattern consists of two primary patterns separated by the
 * {@code ..} operator.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * 1..10
 * }</pre>
 *
 * <p>is represented by a {@link RangePattern} containing the patterns
 * {@code 1} and {@code 10} as its start and end patterns.</p>
 *
 * <p>If the range operator is not present, the primary pattern is returned
 * directly.</p>
 */
public final class RangePatternParser extends AbstractParser implements IPatternParser {

  /**
   * Creates a range pattern parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public RangePatternParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a range pattern from the current parser position.
   *
   * @return the parsed range pattern, or the primary pattern when no
   *     range operator is present
   */
  @Override
  public IPattern parse() {
    IPattern start = new PrimaryPatternParser(cursor).parse();
    if (!cursor.match(MatchTokenKind.RANGE)) return start;
    IPattern end = new PrimaryPatternParser(cursor).parse();
    SourceRange range = new SourceRange(start.range().start(), end.range().end());
    return new RangePattern(range, start, end);
  }
}