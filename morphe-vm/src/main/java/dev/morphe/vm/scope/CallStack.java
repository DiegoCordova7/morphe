package dev.morphe.vm.scope;

import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.exception.internal.stack.EmptyStackException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Represents the call stack of the Morphe virtual machine.
 *
 * <p>
 * The call stack stores the active {@link CallFrame call frames} associated
 * with function or procedure invocations during bytecode execution.
 * </p>
 *
 * <p>
 * Each call frame contains the return address and the variable scope
 * associated with a single active invocation. The top frame represents
 * the currently active invocation.
 * </p>
 */
public final class CallStack {

  /**
   * Represents an immutable snapshot of an individual call frame.
   *
   * <p>
   * The original frame instance is preserved together with its captured
   * state. This allows a rollback to restore the exact frame instances
   * that existed when the snapshot was created.
   * </p>
   *
   * @param frame the original call frame
   * @param state the captured state of the call frame
   */
  public record FrameSnapshot(CallFrame frame, CallFrame.Snapshot state) {
    public FrameSnapshot {
      if (frame == null) throw new NullArgumentException("frame");
      if (state == null) throw new NullArgumentException("state");
    }
  }

  /**
   * Represents an immutable snapshot of the call stack state.
   *
   * <p>
   * The snapshot stores the original active call frames in top-to-bottom
   * order together with their individual states.
   * </p>
   *
   * @param frames the snapshots of the active call frames
   */
  public record Snapshot(List<FrameSnapshot> frames) {

    public Snapshot {
      if (frames == null) throw new NullArgumentException("frames");
      frames = List.copyOf(frames);
    }
  }

  private final Deque<CallFrame> frames = new ArrayDeque<>();

  /**
   * Pushes a call frame onto the top of the call stack.
   *
   * @param frame the call frame to push
   *
   * @throws NullArgumentException if {@code frame} is {@code null}
   */
  public void push(CallFrame frame) {
    if (frame == null) throw new NullArgumentException("frame");
    frames.push(frame);
  }

  /**
   * Removes and returns the current call frame.
   *
   * @return the removed call frame
   *
   * @throws EmptyStackException if the call stack is empty
   */
  public CallFrame pop() {
    if (frames.isEmpty()) throw new EmptyStackException();
    return frames.pop();
  }

  /**
   * Returns the current call frame without removing it.
   *
   * @return the current call frame
   *
   * @throws EmptyStackException if the call stack is empty
   */
  public CallFrame peek() {
    if (frames.isEmpty()) throw new EmptyStackException();
    return frames.peek();
  }

  /**
   * Returns the number of active call frames.
   *
   * @return the call stack size
   */
  public int size() {
    return frames.size();
  }

  /**
   * Returns whether the call stack contains no frames.
   *
   * @return {@code true} if the call stack is empty;
   *         {@code false} otherwise
   */
  public boolean isEmpty() {
    return frames.isEmpty();
  }

  /**
   * Captures the current state of the call stack.
   *
   * <p>
   * Each active call frame is independently snapshotted while preserving
   * its original object identity and the current top-to-bottom order.
   * </p>
   *
   * @return an immutable snapshot of the current call stack state
   */
  public Snapshot snapshot() {
    List<FrameSnapshot> snapshots = new ArrayList<>(frames.size());
    for (CallFrame frame : frames) snapshots.add(new FrameSnapshot(frame, frame.snapshot()));
    return new Snapshot(snapshots);
  }

  /**
   * Restores the call stack to a previously captured state.
   *
   * <p>
   * Frames created after the snapshot are discarded, while the original
   * frame instances captured by the snapshot are restored and placed back
   * into their original positions.
   * </p>
   *
   * @param snapshot the snapshot to restore
   *
   * @throws NullArgumentException if {@code snapshot} is {@code null}
   */
  public void restore(Snapshot snapshot) {
    if (snapshot == null) throw new NullArgumentException("snapshot");
    frames.clear();
    for (FrameSnapshot frameSnapshot : snapshot.frames()) {
      CallFrame frame = frameSnapshot.frame();
      frame.restore(frameSnapshot.state());
      frames.addLast(frame);
    }
  }
}