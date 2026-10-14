package dev.morphe.ast.declaration;

import dev.morphe.ast.exception.argument.NullArgumentException;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.type.FunctionType;
import dev.morphe.ast.type.IASTType;
import dev.morphe.ast.visitor.IDeclarationVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Represents a function declaration in the Morphe abstract syntax tree.
 *
 * <p>A function declaration introduces a named function with explicitly
 * typed parameters, a return type, and a statement body.</p>
 *
 * <p>The semantic requirements imposed by the declared return type are
 * determined during semantic analysis.</p>
 *
 * <p>A function declaration can be referenced by a corresponding
 * {@link FunctionType}.</p>
 */
public final class FunctionDeclaration implements IDeclaration {

  private final SourceRange range;
  private final String name;
  private final List<Parameter> parameters;
  private final IASTType returnType;
  private final BlockStatement body;

  /**
   * Creates a function declaration.
   *
   * @param range the source range occupied by the declaration
   * @param name the function identifier
   * @param parameters the parameters declared by the function
   * @param returnType the declared return type
   * @param body the function body
   * @throws NullArgumentException if any argument is null, or if any
   * parameter is null
   */
  public FunctionDeclaration(SourceRange range, String name, List<Parameter> parameters,
                             IASTType returnType, BlockStatement body) {
    if (range == null) throw new NullArgumentException("range");
    if (name == null) throw new NullArgumentException("name");
    if (parameters == null) throw new NullArgumentException("parameters");
    if (parameters.stream().anyMatch(Objects::isNull)) throw new NullArgumentException("parameter");
    if (returnType == null) throw new NullArgumentException("returnType");
    if (body == null) throw new NullArgumentException("body");
    this.range = range;
    this.name = name;
    this.parameters = List.copyOf(parameters);
    this.returnType = returnType;
    this.body = body;
  }

  /**
   * Returns the source range occupied by this declaration.
   *
   * @return the declaration source range
   */
  @Override
  public SourceRange range() {
    return range;
  }

  /**
   * Returns the function identifier.
   *
   * @return the function name
   */
  public String name() {
    return name;
  }

  /**
   * Returns the parameters declared by the function.
   *
   * @return an immutable list of parameters
   */
  public List<Parameter> parameters() {
    return parameters;
  }

  /**
   * Returns the declared return type.
   *
   * @return the function return type
   */
  public IASTType returnType() {
    return returnType;
  }

  /**
   * Returns the body of the function.
   *
   * @return the function body
   */
  public BlockStatement body() {
    return body;
  }

  /**
   * Accepts a declaration visitor and dispatches this declaration to the
   * appropriate visitor operation.
   *
   * @param visitor the visitor processing this declaration
   * @param context the operational context passed through the traversal
   * @return always {@code null}
   * @throws NullArgumentException if {@code visitor} is null
   */
  @Override
  public <C> Void accept(IDeclarationVisitor<C> visitor, C context) {
    if (visitor == null) throw new NullArgumentException("visitor");
    return visitor.visit(this, context);
  }
}