package dev.morphe.semantic.visitors.declaration;

import dev.morphe.ast.declaration.CompositeDeclaration;
import dev.morphe.ast.type.CompositeType;
import dev.morphe.semantic.visitors.context.SemanticContext;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;

import java.util.List;

import static dev.morphe.semantic.visitors.validator.SemanticArgumentValidator.validateArguments;

/**
 * Performs semantic analysis of composite type declarations.
 *
 * <p>A composite declaration introduces a named composite type into the
 * semantic environment. This analyzer constructs the corresponding
 * {@link CompositeType} from the declaration and registers it in the
 * current semantic scope as a {@link SymbolKind#TYPE} symbol.</p>
 *
 * <p>This analyzer is responsible only for the declaration-level operation
 * of constructing and registering the composite type. Validation of nested
 * or declaration-specific semantic rules is performed by the appropriate
 * semantic analysis components.</p>
 */
public final class CompositeDeclarationAnalyzer {

  /**
   * Analyzes and registers a composite type declaration.
   *
   * <p>The declaration's name and fields are used to construct a
   * {@link CompositeType}. The resulting type is then associated with a
   * {@link Symbol} and defined in the current semantic scope.</p>
   *
   * @param declaration the composite declaration being analyzed
   * @param context the shared semantic analysis context
   * @return always {@code null}
   * @throws NullSemanticArgumentException if {@code declaration} or {@code context} is {@code null}
   */
  public Void analyze(CompositeDeclaration declaration, SemanticContext context) {
    validateArguments(declaration, "declaration", context);
    List<CompositeType.CompositeField> fields = declaration.fields().stream()
        .map(field -> new CompositeType.CompositeField(
            field.name(),
            context.resolver().resolve(field.type())
        ))
        .toList();
    CompositeType type = new CompositeType(declaration.name(), fields);
    Symbol symbol = new Symbol(declaration.name(), SymbolKind.TYPE, type, false);
    context.symbols().define(symbol);
    return null;
  }
}