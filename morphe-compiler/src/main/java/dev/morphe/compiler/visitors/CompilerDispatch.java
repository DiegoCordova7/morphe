package dev.morphe.compiler.visitors;

public final class CompilerDispatch {

  private final CompilerExpressionVisitor expressions;
  private final CompilerStatementVisitor statements;
  private final CompilerPatternVisitor patterns;

  public CompilerDispatch(CompilerExpressionVisitor expressions, CompilerStatementVisitor statements,
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