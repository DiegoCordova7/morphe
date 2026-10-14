package dev.morphe.semantic.type;

import dev.morphe.ast.type.*;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.exception.type.InvalidTypeSymbolException;
import dev.morphe.semantic.exception.type.UnknownTypeException;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;
import dev.morphe.semantic.symbol.SymbolTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Resolves type references during semantic analysis.
 *
 * <p>A {@link NamedType} represents a reference to a type declared elsewhere
 * in the program. The resolver uses the current {@link SymbolTable} to find
 * the corresponding type symbol and returns the type associated with it.</p>
 *
 * <p>Type resolution is recursive. Named types nested inside
 * {@link ArrayType} and {@link FunctionType} instances are resolved as part
 * of resolving the containing type.</p>
 *
 * <p>For example:</p>
 *
 * <pre>{@code
 * ArrayType(NamedType("Person"))
 *             |
 *             v
 * ArrayType(CompositeType("Person", ...))
 * }</pre>
 *
 * <p>Multidimensional arrays are resolved recursively as well:</p>
 *
 * <pre>{@code
 * ArrayType(
 *     ArrayType(
 *         NamedType("Person")
 *     )
 * )
 *             |
 *             v
 * ArrayType(
 *     ArrayType(
 *         CompositeType("Person", ...)
 *     )
 * )
 * }</pre>
 *
 * <p>Function parameter and return types are also resolved:</p>
 *
 * <pre>{@code
 * FunctionType(
 *     [NamedType("Person")],
 *     NamedType("Result")
 * )
 *             |
 *             v
 * FunctionType(
 *     [CompositeType("Person", ...)],
 *     CompositeType("Result", ...)
 * )
 * }</pre>
 *
 * <p>Types that do not contain references requiring resolution are returned
 * unchanged. When a composite or function type contains at least one
 * unresolved reference, a new type instance containing the resolved types
 * is created.</p>
 *
 * <p>The resolver does not perform type compatibility checks. Those
 * responsibilities belong to the type checker.</p>
 */
public final class TypeResolver {

  private final SymbolTable symbols;

  /**
   * Creates a type resolver using the specified symbol table.
   *
   * @param symbols the symbol table used to resolve named types
   * @throws NullSemanticArgumentException if {@code symbols} is {@code null}
   */
  public TypeResolver(SymbolTable symbols) {
    if (symbols == null) throw new NullSemanticArgumentException("symbols");
    this.symbols = symbols;
  }

  /**
   * Resolves the specified AST type.
   *
   * <p>{@link NamedType} instances are resolved through the current symbol
   * scope. {@link ArrayType} and {@link FunctionType} instances are resolved
   * recursively so that nested named types are also resolved.</p>
   *
   * <p>If a type does not contain any references requiring resolution, the
   * original instance is returned unchanged.</p>
   *
   * @param type the AST type to resolve
   * @return the resolved type
   * @throws NullSemanticArgumentException if {@code type} is {@code null}
   * @throws UnknownTypeException if a named type cannot be found
   * @throws InvalidTypeSymbolException if a named type resolves to a symbol
   *     that does not represent a type
   */
  public IASTType resolve(IASTType type) {
    if (type == null) throw new NullSemanticArgumentException("type");
    if (type instanceof NamedType named) return resolveNamed(named);
    if (type instanceof ArrayType array) return resolveArray(array);
    if (type instanceof FunctionType function) return resolveFunction(function);
    if (type instanceof CompositeType composite) return resolveComposite(composite);
    if (type instanceof AnonymousCompositeType anonymous) return resolveAnonymousComposite(anonymous);
    return type;
  }

  /**
   * Resolves a named type through the current symbol scope.
   *
   * <p>The symbol associated with the type name must represent a declared
   * type. The symbol's AST type is returned as the resolved result.</p>
   *
   * @param type the named type reference to resolve
   * @return the type associated with the named type
   * @throws UnknownTypeException if no symbol exists for the type name
   * @throws InvalidTypeSymbolException if the resolved symbol is not a type
   */
  private IASTType resolveNamed(NamedType type) {
    Symbol symbol = symbols.resolve(type.name());
    if (symbol == null) throw new UnknownTypeException(type.name());
    if (symbol.kind() != SymbolKind.TYPE) throw new InvalidTypeSymbolException(type.name());
    return symbol.type();
  }

  /**
   * Resolves the element type of an array recursively.
   *
   * <p>If resolving the element type does not change it, the original array
   * instance is returned. Otherwise, a new array type containing the resolved
   * element type is created.</p>
   *
   * @param type the array type to resolve
   * @return the resolved array type
   */
  private IASTType resolveArray(ArrayType type) {
    IASTType elementType = resolve(type.elementType());
    if (elementType == type.elementType()) return type;
    return new ArrayType(elementType);
  }

  /**
   * Resolves the parameter and return types of a function type recursively.
   *
   * <p>The original function type is preserved when none of its parameter
   * or return types require resolution. Otherwise, a new function type is
   * created using the resolved types.</p>
   *
   * @param type the function type to resolve
   * @return the resolved function type
   */
  private IASTType resolveFunction(FunctionType type) {
    List<IASTType> parameterTypes = new ArrayList<>(type.parameterTypes().size());
    boolean changed = false;
    for (IASTType parameterType : type.parameterTypes()) {
      IASTType resolved = resolve(parameterType);
      parameterTypes.add(resolved);
      if (resolved != parameterType) changed = true;
    }
    IASTType returnType = resolve(type.returnType());
    if (returnType != type.returnType()) changed = true;
    if (!changed) return type;
    return new FunctionType(parameterTypes, returnType);
  }

  private List<CompositeType.CompositeField> resolveFields(List<CompositeType.CompositeField> fields, boolean[] changed) {
    List<CompositeType.CompositeField> resolvedFields = new ArrayList<>(fields.size());
    for (CompositeType.CompositeField field : fields) {
      IASTType resolvedFieldType = resolve(field.type());
      resolvedFields.add(new CompositeType.CompositeField(field.name(), resolvedFieldType));
      if (resolvedFieldType != field.type()) {
        changed[0] = true;
      }
    }
    return resolvedFields;
  }

  private IASTType resolveComposite(CompositeType type) {
    boolean[] changed = {false};
    List<CompositeType.CompositeField> resolvedFields = resolveFields(type.fields(), changed);
    if (!changed[0]) return type;
    return new CompositeType(type.name(), resolvedFields);
  }

  private IASTType resolveAnonymousComposite(AnonymousCompositeType type) {
    boolean[] changed = {false};
    List<CompositeType.CompositeField> resolvedFields = resolveFields(type.fields(), changed);
    if (!changed[0]) return type;
    return new AnonymousCompositeType(resolvedFields);
  }
}