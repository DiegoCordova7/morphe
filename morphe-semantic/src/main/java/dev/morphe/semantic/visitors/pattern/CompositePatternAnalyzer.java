package dev.morphe.semantic.visitors.pattern;

import dev.morphe.ast.pattern.CompositePattern;
import dev.morphe.ast.type.CompositeType;
import dev.morphe.ast.type.IASTType;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.pattern.UnknownCompositeFieldException;
import dev.morphe.semantic.exception.type.InvalidTypeSymbolException;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;
import dev.morphe.semantic.visitors.SemanticPatternVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of composite patterns.
 *
 * <p>A composite pattern is resolved against the expected composite type.
 * The referenced composite type is resolved through the semantic symbol
 * table and each declared field pattern is analyzed against the type of
 * its corresponding composite field.</p>
 *
 * <p>Field names that do not exist in the resolved composite type are
 * rejected during semantic analysis.</p>
 *
 * <p>Nested field patterns are recursively analyzed through the shared
 * {@link SemanticPatternVisitor}.</p>
 */
public final class CompositePatternAnalyzer {

  /**
   * Analyzes a composite pattern against the composite type referenced
   * by the pattern.
   *
   * <p>The referenced type is resolved from the current semantic scope.
   * Each declared field pattern is then analyzed using the AST type of
   * the corresponding composite field.</p>
   *
   * @param pattern the composite pattern being analyzed
   * @param context the shared semantic analysis context
   * @param visitor the shared pattern visitor used to analyze nested patterns
   * @return always {@code null}
   * @throws NullSemanticArgumentException if {@code pattern}, {@code context},
   *     or {@code visitor} is null
   */
  public Void analyze(CompositePattern pattern, SemanticContext context, SemanticPatternVisitor visitor) {
    validateArguments(pattern, "pattern", context);
    if (visitor == null) throw new NullSemanticArgumentException("visitor");
    Symbol symbol = context.symbols().resolve(pattern.typeName());
    if (symbol == null || symbol.kind() != SymbolKind.TYPE) throw new InvalidTypeSymbolException(pattern.typeName());
    IASTType type = symbol.type();
    if (!(type instanceof CompositeType compositeType)) throw new InvalidTypeSymbolException(pattern.typeName());
    IASTType previousType = context.expectedType();
    try {
      for (CompositePattern.FieldPattern field : pattern.fields()) {
        CompositeType.CompositeField compositeField = findField(compositeType, field.name());
        context.expectedType(compositeField.type());
        visitor.analyze(field.pattern(), context);
      }
    } finally {
      context.expectedType(previousType);
    }
    return null;
  }

  private CompositeType.CompositeField findField(CompositeType type, String name) {
    return type.fields()
        .stream()
        .filter(field -> field.name().equals(name))
        .findFirst()
        .orElseThrow(() -> new UnknownCompositeFieldException(type.name(), name));
  }
}