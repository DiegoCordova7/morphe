package dev.morphe.parser.statement;

import dev.morphe.ast.statement.IStatement;

/**
 * Defines a parser capable of producing an AST statement.
 *
 * <p>Statement parser implementations use this interface to provide
 * a common parsing contract for the different statement constructs
 * supported by the Morphe language.</p>
 */
@FunctionalInterface
public interface IStatementParser {

  /**
   * Parses a statement from the current parser state.
   *
   * @return the parsed AST statement
   */
  IStatement parse();
}