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

import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.ExpressionStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.expression.EmptyAttemptBodyException;
import dev.morphe.semantic.exception.expression.InvalidAttemptBodyException;
import dev.morphe.semantic.result.ExpressionResult;
import dev.morphe.semantic.result.StatementResult;
import dev.morphe.semantic.scope.ScopeKind;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.SemanticStatementVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of {@link BlockStatement} nodes.
 *
 * <p>A block introduces a new lexical {@link ScopeKind#BLOCK} scope.
 * Statements contained within the block are analyzed sequentially using
 * that scope.</p>
 *
 * <p>The scope is entered before analyzing the first statement and is
 * always exited after analysis, including when semantic analysis of a
 * contained statement fails.</p>
 *
 * <p>Symbols declared within the block are therefore visible to subsequent
 * statements in the same block and are removed from the active lexical
 * scope when the block finishes.</p>
 */
public final class BlockStatementAnalyzer {

  /**
   * Analyzes a block statement.
   *
   * <p>The block creates a new lexical scope and analyzes each contained
   * statement in its original order.</p>
   *
   * @param statement the block statement to analyze
   * @param context the shared semantic analysis context
   * @param visitor the statement visitor used to analyze child statements
   * @return a statement result indicating whether the block always returns
   * @throws NullSemanticArgumentException if {@code statement},
   *     {@code context}, or {@code visitor} is null
   */
  public StatementResult analyze(BlockStatement statement, SemanticContext context, SemanticStatementVisitor visitor) {
    validateArguments(statement, "statement", context);
    context.enterScope(ScopeKind.BLOCK);
    boolean alwaysReturns = false;
    try {
      for (IStatement child : statement.statements()) {
        StatementResult result = child.accept(visitor, context);
        if (result != null && result.alwaysReturns()) alwaysReturns = true;
      }
    } finally {
      context.exitScope();
    }
    return new StatementResult(alwaysReturns);
  }

  /**
   * Analyzes a block statement and obtains the result produced by its
   * final expression statement.
   *
   * <p>The final statement must be an {@link ExpressionStatement}.
   * All preceding statements are analyzed normally.</p>
   *
   * @param statement the block statement being analyzed
   * @param context the semantic analysis context
   * @param expressionVisitor the shared expression visitor
   * @param statementVisitor the shared statement visitor
   * @return the semantic result of the final expression
   * @throws NullSemanticArgumentException if any argument is null
   * @throws EmptyAttemptBodyException if the block contains no statements
   * @throws InvalidAttemptBodyException if the final statement is not
   * an expression statement
   */
  public ExpressionResult analyzeResult(BlockStatement statement, SemanticContext context,
                                        SemanticExpressionVisitor expressionVisitor,
                                        SemanticStatementVisitor statementVisitor) {
    validateArguments(statement, "statement", context);
    if (expressionVisitor == null) throw new NullSemanticArgumentException("expressionVisitor");
    if (statementVisitor == null) throw new NullSemanticArgumentException("statementVisitor");
    if (statement.statements().isEmpty()) throw new EmptyAttemptBodyException();
    for (int i = 0; i < statement.statements().size() - 1; i++) {
      statement.statements().get(i).accept(statementVisitor, context);
    }
    IStatement last = statement.statements().get(statement.statements().size() - 1);
    if (!(last instanceof ExpressionStatement expressionStatement)) throw new InvalidAttemptBodyException(last);
    return expressionStatement.expression().accept(expressionVisitor, context);
  }
}