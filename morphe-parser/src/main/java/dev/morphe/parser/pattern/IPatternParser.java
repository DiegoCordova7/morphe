package dev.morphe.parser.pattern;

import dev.morphe.ast.pattern.IPattern;

/**
 * Defines the contract for parser components that produce AST patterns.
 *
 * <p>Implementations are responsible for consuming the tokens required by
 * their specific pattern grammar and returning the corresponding
 * {@link IPattern} node.</p>
 *
 * <p>Pattern parsers may be composed to handle nested or compound patterns,
 * while the resulting AST remains independent of the parser implementation.</p>
 */
@FunctionalInterface
public interface IPatternParser {

  /**
   * Parses a pattern from the current parser position.
   *
   * @return the parsed AST pattern
   */
  IPattern parse();
}