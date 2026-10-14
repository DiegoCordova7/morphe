package dev.morphe.semantic.scope;

/**
 * Identifies the kind of lexical scope represented by a {@link Scope}.
 *
 * <p>Scope kinds allow the semantic analyzer to distinguish between
 * different contexts in which symbols can be declared and resolved.</p>
 */
public enum ScopeKind {

  /**
   * The outermost scope of a compilation unit.
   */
  GLOBAL,

  /**
   * A scope belonging to a function declaration.
   */
  FUNCTION,

  /**
   * A scope created by a statement block.
   */
  BLOCK,

  /**
   * A scope belonging to a loop statement.
   */
  LOOP,

  /**
   * A scope belonging to a match statement.
   */
  MATCH,

  /**
   * A scope belonging to a lambda expression.
   */
  LAMBDA
}