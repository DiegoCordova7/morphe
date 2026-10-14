package dev.morphe.compiler.visitors.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.compiler.context.CompilerPatternContext;

public interface IPatternCompiler<T extends IPattern> {
  Void compile(T pattern, CompilerPatternContext context);
}