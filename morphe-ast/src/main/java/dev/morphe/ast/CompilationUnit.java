package dev.morphe.ast;

import dev.morphe.ast.declaration.IDeclaration;
import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;

import java.util.List;
import java.util.Objects;

/**
 * Represents the root node of a Morphe abstract syntax tree.
 *
 * <p>A compilation unit contains the top-level declarations parsed from
 * a Morphe source file.</p>
 */
public final class CompilationUnit implements IASTNode {

  private final List<IDeclaration> declarations;
  private final SourceRange range;

  /**
   * Creates a compilation unit containing the given top-level declarations.
   *
   * @param declarations the top-level declarations contained in this unit
   * @param range the source range occupied by this compilation unit
   * @throws NullArgumentException if {@code declarations}, {@code range},
   * or any declaration is {@code null}
   */
  public CompilationUnit(List<IDeclaration> declarations, SourceRange range) {
    if (declarations == null) throw new NullArgumentException("declarations");
    if (range == null) throw new NullArgumentException("range");
    if (declarations.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("declaration");
    this.declarations = List.copyOf(declarations);
    this.range = range;
  }

  /**
   * Returns the top-level declarations contained in this compilation unit.
   *
   * @return an immutable list of top-level declarations
   */
  public List<IDeclaration> declarations() {
    return declarations;
  }

  /**
   * Returns the source range occupied by this compilation unit.
   *
   * @return the compilation unit source range
   */
  @Override
  public SourceRange range() {
    return range;
  }
}