package dev.morphe.compiler.visitors;

import dev.morphe.ast.pattern.*;
import dev.morphe.ast.visitor.IPatternVisitor;
import dev.morphe.compiler.context.CompilerPatternContext;
import dev.morphe.compiler.type.CompilerTypeResolver;
import dev.morphe.compiler.visitors.pattern.*;

public final class CompilerPatternVisitor implements IPatternVisitor<CompilerPatternContext> {

  private final DefaultPatternCompiler defaultPattern;
  private final LiteralPatternCompiler literalPattern;
  private final OrPatternCompiler orPattern;
  private final RangePatternCompiler rangePattern;
  private final VariablePatternCompiler variablePattern;
  private final ArrayPatternCompiler arrayPattern;
  private final CompositePatternCompiler compositePattern;

  public CompilerPatternVisitor(CompilerTypeResolver typeResolver) {
    if (typeResolver == null) throw new IllegalArgumentException("typeResolver cannot be null");
    this.defaultPattern = new DefaultPatternCompiler();
    this.literalPattern = new LiteralPatternCompiler();
    this.orPattern = new OrPatternCompiler();
    this.rangePattern = new RangePatternCompiler();
    this.variablePattern = new VariablePatternCompiler();
    this.arrayPattern = new ArrayPatternCompiler();
    this.compositePattern = new CompositePatternCompiler(typeResolver);
  }

  @Override
  public Void visit(DefaultPattern pattern, CompilerPatternContext context) {
    return defaultPattern.compile(pattern, context);
  }

  @Override
  public Void visit(LiteralPattern pattern, CompilerPatternContext context) {
    return literalPattern.compile(pattern, context);
  }

  @Override
  public Void visit(OrPattern pattern, CompilerPatternContext context) {
    return orPattern.compile(pattern, context);
  }

  @Override
  public Void visit(RangePattern pattern, CompilerPatternContext context) {
    return rangePattern.compile(pattern, context);
  }

  @Override
  public Void visit(VariablePattern pattern, CompilerPatternContext context) {
    return variablePattern.compile(pattern, context);
  }

  @Override
  public Void visit(ArrayPattern pattern, CompilerPatternContext context) {
    return arrayPattern.compile(pattern, context);
  }

  @Override
  public Void visit(CompositePattern pattern, CompilerPatternContext context) {
    return compositePattern.compile(pattern, context);
  }
}