package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.OrPattern;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.lexer.kind.MatchTokenKind;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses alternative patterns into AST nodes.
 *
 * <p>An alternative pattern consists of two or more range-level patterns
 * separated by the {@code |} operator.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * 1 | 2 | 3
 * }</pre>
 *
 * <p>Each individual pattern is delegated to
 * {@link RangePatternParser}. If no {@code |} operator is present, the
 * first parsed pattern is returned directly.</p>
 */
public final class OrPatternParser extends AbstractParser implements IPatternParser {

  /**
   * Creates an alternative pattern parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public OrPatternParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses an alternative pattern from the current parser position.
   *
   * @return the parsed alternative pattern, or the first pattern when no
   *     alternative operator is present
   */
  @Override
  public IPattern parse() {
    IPattern first = new RangePatternParser(cursor).parse();
    if (!cursor.match(MatchTokenKind.OR)) return first;
    List<IPattern> patterns = new ArrayList<>();
    patterns.add(first);
    do patterns.add(new RangePatternParser(cursor).parse());
    while (cursor.match(MatchTokenKind.OR));
    SourceRange range = new SourceRange(
        first.range().start(),
        patterns.get(patterns.size() - 1).range().end());
    return new OrPattern(range, patterns);
  }
}