package dev.morphe.semantic.visitors.validator;

import dev.morphe.ast.IASTNode;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.visitors.context.SemanticContext;

/**
 * Provides common argument validation for semantic analysis operations.
 */
public final class SemanticArgumentValidator {

  private SemanticArgumentValidator() {}

  /**
   * Validates the arguments required to perform semantic analysis of an AST
   * node.
   *
   * @param node the AST node being analyzed
   * @param argumentName the name used to identify the node argument in an
   *     exception
   * @param context the semantic analysis context
   * @throws NullSemanticArgumentException if {@code node} or {@code context}
   *     is {@code null}
   */
  public static void validateArguments(IASTNode node, String argumentName, SemanticContext context) {
    if (node == null) throw new NullSemanticArgumentException(argumentName);
    if (context == null) throw new NullSemanticArgumentException("context");
  }
}