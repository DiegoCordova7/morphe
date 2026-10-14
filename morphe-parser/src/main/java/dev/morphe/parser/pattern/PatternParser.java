package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.parser.AbstractParser;
import dev.morphe.parser.cursor.TokenCursor;

/**
 * Parses pattern expressions into AST nodes.
 *
 * <p>{@code PatternParser} is the entry point for the pattern grammar.
 * Pattern precedence and composition are delegated to
 * {@link OrPatternParser}.</p>
 *
 * <p>For example, a pattern such as:</p>
 *
 * <pre>{@code
 * 1..10 | 20 | 30..40
 * }</pre>
 *
 * <p>is parsed through the pattern precedence hierarchy and produces
 * the corresponding {@link IPattern} tree.</p>
 */
public final class PatternParser extends AbstractParser implements IPatternParser {

  /**
   * Creates a pattern parser using the specified token cursor.
   *
   * @param cursor the token cursor used to read the source tokens
   */
  public PatternParser(TokenCursor cursor) {
    super(cursor);
  }

  /**
   * Parses a pattern from the current parser position.
   *
   * @return the parsed pattern
   */
  @Override
  public IPattern parse() {
    return new OrPatternParser(cursor).parse();
  }
}