/*
 * Copyright 2026 DiegoCordova
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.morphe.semantic.visitors.statement;

import dev.morphe.ast.statement.ContinueStatement;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.statement.InvalidContinueContextException;
import dev.morphe.semantic.result.StatementResult;
import dev.morphe.semantic.scope.Scope;
import dev.morphe.semantic.scope.ScopeKind;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of {@link ContinueStatement} nodes.
 *
 * <p>A continue statement is valid only when it appears inside a loop.</p>
 */
public final class ContinueStatementAnalyzer {

  /**
   * Analyzes a continue statement.
   *
   * @param statement the continue statement to analyze
   * @param context the shared semantic analysis context
   * @return a statement result indicating whether the statement always returns (always {@code false})
   * @throws NullSemanticArgumentException if an argument is null
   * @throws InvalidContinueContextException if the statement is not inside
   *     a loop
   */
  public StatementResult analyze(ContinueStatement statement, SemanticContext context) {
    validateArguments(statement, "statement", context);
    Scope scope = context.scope();
    while (scope != null) {
      if (scope.kind() == ScopeKind.LOOP) return new StatementResult(false);
      scope = scope.parent();
    }
    throw new InvalidContinueContextException();
  }
}