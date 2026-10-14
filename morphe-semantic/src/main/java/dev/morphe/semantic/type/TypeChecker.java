package dev.morphe.semantic.type;

import dev.morphe.ast.type.*;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.symbol.SymbolTable;

import java.util.List;

/**
 * Checks type compatibility during semantic analysis.
 *
 * <p>The type checker determines whether one AST type can be used where
 * another type is required according to Morphe's type compatibility rules.
 * Before comparing types, named type references are resolved through the
 * associated {@link TypeResolver}.</p>
 *
 * <p>Type compatibility is structural rather than nominal. In particular,
 * composite types (both named and anonymous) are compared according to the fields they provide and
 * require, rather than according to their declared type names.
 * </p>
 *
 * <p>For example, given:</p>
 * <pre>{@code
 *  type Person {
 *     String  : name,
 *     String  : surname,
 *     Integer : age
 *  }
 *
 * type Worker {
 *     String : name,
 *     String : surname
 * }
 * }</pre>
 *
 * <p>{@code Person} is compatible with {@code Worker}, because a
 * {@code Person} provides every field required by {@code Worker}.
 * The reverse is not true because {@code Worker} does not provide
 * {@code age}. Anonymous composite types follow the same structural rules.</p>
 *
 * <p>Primitive types are compatible only when they represent the same
 * primitive type. Runtime value equality is a separate concern and is
 * handled by runtime values and their equality semantics.</p>
 *
 * <p>The {@link NullType#NULL} type is compatible with every type except
 * {@link PrimitiveType#VOID}. This compatibility is symmetric, meaning
 * that both {@code NULL} and another type are considered compatible when
 * either side represents {@code NULL}, provided the other type is not
 * {@code VOID}.</p>
 *
 * <p>{@link PrimitiveType#VOID} represents the absence of a return value
 * and therefore cannot participate in value compatibility with
 * {@code NULL}.</p>
 *
 * <p>Array types are compatible when their element types are compatible,
 * including recursively nested array types.</p>
 *
 * <p>Function types are compatible when they have the same number of
 * parameters, corresponding parameter types are compatible, and their
 * return types are compatible.</p>
 *
 * <p>The checker is concerned only with type compatibility. It does not
 * validate expressions, statements, declarations, or runtime values;
 * those responsibilities belong to the semantic analysis layer.</p>
 */
public final class TypeChecker {

  private final TypeResolver resolver;

  /**
   * Creates a type checker using the specified symbol table.
   *
   * <p>The symbol table is passed to a {@link TypeResolver}, which is used
   * to resolve named type references before compatibility is checked.</p>
   *
   * @param symbols the symbol table used for named type resolution
   * @throws NullSemanticArgumentException if {@code symbols} is null
   */
  public TypeChecker(SymbolTable symbols) {
    if (symbols == null) throw new NullSemanticArgumentException("symbols");
    this.resolver = new TypeResolver(symbols);
  }

  /**
   * Determines whether two AST types are compatible.
   *
   * <p>Both types are resolved before comparison, allowing named types to
   * participate in compatibility checks. Compatibility is directional for
   * structural composite types: the left type represents the type being
   * provided, while the right type represents the type being required.</p>
   *
   * <p>The null type is an exception to directional compatibility. Since
   * {@link NullType#NULL} is compatible with every type except
   * {@link PrimitiveType#VOID}, compatibility with {@code NULL} is checked
   * symmetrically.</p>
   *
   * <p>For example, if {@code Person} contains all fields required by
   * {@code Worker}, then:</p>
   * <pre>{@code
   *    areCompatible(Person, Worker) == true
   *    areCompatible(Worker, Person) == false
   * }</pre>
   *
   * @param left the type being provided
   * @param right the type being required
   * @return {@code true} if {@code left} satisfies the requirements of
   *     {@code right}; {@code false} otherwise
   * @throws NullSemanticArgumentException if either type is null
   */
  public boolean areCompatible(IASTType left, IASTType right) {
    if (left == null) throw new NullSemanticArgumentException("left");
    if (right == null) throw new NullSemanticArgumentException("right");
    IASTType resolvedLeft = resolver.resolve(left);
    IASTType resolvedRight = resolver.resolve(right);
    return check(resolvedLeft, resolvedRight);
  }

  /**
   * Checks compatibility between two already resolved AST types.
   *
   * <p>The null type is compatible with every type except {@link PrimitiveType#VOID}.
   * For all other types, compatibility is determined according to their
   * concrete AST type category.</p>
   *
   * <p>Composite, array, and function types are checked recursively.</p>
   *
   * @param left the resolved type being provided
   * @param right the resolved type being required
   * @return {@code true} if the types are compatible
   */
  private boolean check(IASTType left, IASTType right) {
    if (left == AnyType.ANY || right == AnyType.ANY) return true;
    if (left == right) return true;
    if (left == NullType.NULL || right == NullType.NULL) {
      return left != PrimitiveType.VOID && right != PrimitiveType.VOID;
    }
    if (left instanceof PrimitiveType leftPrimitive && right instanceof PrimitiveType rightPrimitive) {
      return leftPrimitive == rightPrimitive;
    }
    if (left instanceof ArrayType leftArray && right instanceof ArrayType rightArray) {
      return check(leftArray.elementType(), rightArray.elementType());
    }
    if (left instanceof FunctionType leftFunction && right instanceof FunctionType rightFunction) {
      return checkFunction(leftFunction, rightFunction);
    }
    if (isCompositeLike(left) && isCompositeLike(right)) {
      return checkComposite(getFields(left), getFields(right));
    }
    return false;
  }

  /**
   * Checks compatibility between two function types.
   *
   * <p>Function types must declare the same number of parameters. Each
   * corresponding parameter type must be compatible, and the return types
   * must also be compatible.</p>
   *
   * @param left the function type being provided
   * @param right the function type being required
   * @return {@code true} if both function types are compatible
   */
  private boolean checkFunction(FunctionType left, FunctionType right) {
    List<IASTType> leftParameters = left.parameterTypes();
    List<IASTType> rightParameters = right.parameterTypes();
    if (leftParameters.size() != rightParameters.size()) return false;
    for (int i = 0; i < leftParameters.size(); i++) {
      if (!check(leftParameters.get(i), rightParameters.get(i))) return false;
    }
    return check(left.returnType(), right.returnType());
  }

  /**
   * Determines whether an AST type behaves like a composite structure
   * (i.e., is either a named {@link CompositeType} or an {@link AnonymousCompositeType}).
   *
   * @param type the AST type to evaluate
   * @return {@code true} if the type is composite-like; {@code false} otherwise
   */
  private boolean isCompositeLike(IASTType type) {
    return type instanceof CompositeType || type instanceof AnonymousCompositeType;
  }

  /**
   * Extracts the list of fields from a composite-like AST type.
   *
   * @param type the composite-like AST type
   * @return the list of composite fields
   * @throws IllegalArgumentException if the type is not composite-like
   */
  private List<CompositeType.CompositeField> getFields(IASTType type) {
    if (type instanceof CompositeType comp) return comp.fields();
    if (type instanceof AnonymousCompositeType anon) return anon.fields();
    throw new IllegalArgumentException("Expected composite-like type, got: " + type.getClass().getSimpleName());
  }

  /**
   * Checks directional structural compatibility between two sets of composite fields.
   *
   * <p>Every field required by the {@code required} fields must be present in
   * the {@code provided} fields and its type must be compatible with the
   * required field type. Additional fields in the provided fields are allowed.
   * </p>
   *
   * <p>Field names are significant because they identify the structural
   * requirements, while the names of the composite types themselves are not.</p>
   *
   * @param provided the list of fields providing the required structure
   * @param required the list of fields whose requirements must be satisfied
   * @return {@code true} if {@code provided} satisfies all requirements of
   *     {@code required}
   */
  private boolean checkComposite(List<CompositeType.CompositeField> provided, List<CompositeType.CompositeField> required) {
    for (CompositeType.CompositeField requiredField : required) {
      CompositeType.CompositeField providedField = findField(provided, requiredField.name());
      if (providedField == null) return false;
      if (!check(providedField.type(), requiredField.type())) return false;
    }
    return true;
  }

  /**
   * Finds a field by name in a list of composite fields using a stream search.
   *
   * @param fields the list of fields to search
   * @param name the field name
   * @return the matching field, or {@code null} if no such field exists
   */
  private CompositeType.CompositeField findField(List<CompositeType.CompositeField> fields, String name) {
    return fields.stream()
        .filter(field -> field.name().equals(name))
        .findFirst()
        .orElse(null);
  }
}