package dev.morphe.compiler.visitors.pattern;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.OrPattern;
import dev.morphe.bytecode.builder.BytecodeBuilder;
import dev.morphe.bytecode.builder.Label;
import dev.morphe.compiler.context.CompilerPatternContext;
import dev.morphe.compiler.visitors.CompilerPatternVisitor;

import java.util.List;

public final class OrPatternCompiler {

  public Void compile(OrPattern pattern, CompilerPatternContext context) {
    List<IPattern> patterns = pattern.patterns();
    CompilerPatternVisitor visitor = context.function().compiler().dispatch().patterns();
    BytecodeBuilder builder = context.function().compiler().builder();
    for (int i = 0; i < patterns.size(); i++) {
      IPattern current = patterns.get(i);
      boolean last = i == patterns.size() - 1;
      Label failed = last ? context.failed() : builder.createLabel();
      CompilerPatternContext nestedContext = new CompilerPatternContext(
          context.function(), context.subjectSlot(), context.matched(), failed
      );
      current.accept(visitor, nestedContext);
      if (!last) builder.mark(failed);
    }
    return null;
  }
}