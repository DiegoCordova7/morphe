package dev.morphe.parser.declaration;

import dev.morphe.ast.declaration.IDeclaration;

/**
 * Defines a parser capable of parsing a declaration into an AST node.
 *
 * <p>Declaration parsers consume the tokens required to recognize a
 * declaration and produce the corresponding {@link IDeclaration} node.</p>
 */
@FunctionalInterface
public interface IDeclarationParser {

  /**
   * Parses a declaration from the current parser position.
   *
   * @return the parsed declaration
   */
  IDeclaration parse();
}