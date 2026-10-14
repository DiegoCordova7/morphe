package dev.morphe.compiler.context;

import dev.morphe.compiler.visitors.CompilerExpressionVisitor;
import dev.morphe.compiler.visitors.CompilerPatternVisitor;
import dev.morphe.compiler.visitors.CompilerStatementVisitor;

public final class CompilerVisitorContext {

  private final CompilerExpressionVisitor expressions;
  private final CompilerStatementVisitor statements;
  private final CompilerPatternVisitor patterns;

  public CompilerVisitorContext(CompilerExpressionVisitor expressions, CompilerStatementVisitor statements,
                                CompilerPatternVisitor patterns) {
    this.expressions = expressions;
    this.statements = statements;
    this.patterns = patterns;
  }

  public CompilerExpressionVisitor expressions() {
    return expressions;
  }

  public CompilerStatementVisitor statements() {
    return statements;
  }

  public CompilerPatternVisitor patterns() {
    return patterns;
  }
}