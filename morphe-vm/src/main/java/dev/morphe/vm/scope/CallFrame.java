package dev.morphe.vm.scope;

import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.runtime.environment.ClosureEnvironment;

/**
 * Represents a single active function invocation.
 *
 * <p>
 * A call frame stores the return address, the current lexical scope,
 * and the closure environment associated with a function invocation.
 * </p>
 *
 * <p>
 * During execution, the active scope may change as nested blocks are
 * entered and exited. Therefore, while the return address and closure
 * environment remain immutable for the lifetime of the frame, the current
 * scope can be updated.
 * </p>
 */
public final class CallFrame {

  private final int returnAddress;
  private final ClosureEnvironment environment;
  private Scope scope;

  /**
   * Represents an immutable snapshot of the state of a call frame.
   *
   * <p>
   * The snapshot preserves the original active scope together with its
   * captured state. This allows a rollback to restore both the scope
   * reference and the contents of that scope.
   * </p>
   *
   * <p>
   * The closure environment reference is also preserved together with
   * its captured state. The environment itself is immutable as a
   * reference, but its contents are mutable.
   * </p>
   *
   * @param scope the original active scope
   * @param scopeState the captured state of the active scope
   * @param environment the closure environment associated with the frame
   * @param environmentState the captured state of the closure environment
   */
  public record Snapshot(Scope scope, Scope.Snapshot scopeState,
                         ClosureEnvironment environment, ClosureEnvironment.Snapshot environmentState) {

    public Snapshot {
      if (scope == null) throw new NullArgumentException("scope");
      if (scopeState == null) throw new NullArgumentException("scopeState");
      if (environment == null) throw new NullArgumentException("environment");
      if (environmentState == null) throw new NullArgumentException("environmentState");
    }
  }

  /**
   * Creates a call frame for a function invocation.
   *
   * @param returnAddress the bytecode address to resume execution after
   *     returning from this frame
   * @param scope the initial active scope of the invocation
   * @param environment the closure environment associated with the invocation
   *
   * @throws NullArgumentException if {@code scope} or {@code environment}
   *     is {@code null}
   */
  public CallFrame(int returnAddress, Scope scope, ClosureEnvironment environment) {
    if (scope == null) throw new NullArgumentException("scope");
    if (environment == null) throw new NullArgumentException("environment");
    this.returnAddress = returnAddress;
    this.scope = scope;
    this.environment = environment;
  }

  /**
   * Returns the address to resume execution after returning from this frame.
   *
   * @return the return address
   */
  public int returnAddress() {
    return returnAddress;
  }

  /**
   * Returns the current active scope of this call frame.
   *
   * @return the current scope
   */
  public Scope scope() {
    return scope;
  }

  /**
   * Replaces the current active scope.
   *
   * <p>This method is used when entering or leaving nested lexical scopes
   * during the execution of the same function invocation.</p>
   *
   * @param scope the new active scope
   * @throws NullArgumentException if {@code scope} is {@code null}
   */
  public void scope(Scope scope) {
    if (scope == null) throw new NullArgumentException("scope");
    this.scope = scope;
  }

  public ClosureEnvironment environment() {
    return environment;
  }

  /**
   * Captures the current state of this call frame.
   *
   * <p>
   * The returned snapshot captures the complete mutable state associated
   * with the frame while preserving its return address.
   * </p>
   *
   * @return an immutable snapshot of the current frame state
   */
  public Snapshot snapshot() {
    return new Snapshot(scope, scope.snapshot(), environment, environment.snapshot());
  }

  /**
   * Restores this call frame to the state represented by the specified
   * snapshot.
   *
   * <p>
   * The existing scope and closure environment instances are preserved.
   * Only their internal state is restored from the snapshot.
   * </p>
   *
   * <p>
   * The return address is not modified because it is immutable for the
   * lifetime of the frame.
   * </p>
   *
   * @param snapshot the snapshot from which to restore the frame
   *
   * @throws NullArgumentException if {@code snapshot} is {@code null}
   */
  public void restore(Snapshot snapshot) {
    if (snapshot == null) throw new NullArgumentException("snapshot");
    this.scope = snapshot.scope();
    snapshot.scope.restore(snapshot.scopeState());
    environment.restore(snapshot.environmentState());
  }
}