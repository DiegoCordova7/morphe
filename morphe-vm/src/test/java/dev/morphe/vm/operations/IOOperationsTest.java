package dev.morphe.vm.operations;

import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.vm.exception.argument.NullArgumentException;
import dev.morphe.vm.io.IVMIOHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IOOperationsTest {
  private static class TestIOHandler implements IVMIOHandler {
    private String output;
    @Override
    public void print(String output) {
      this.output = output;
    }
  }

  @Test
  void printShouldDelegateValueToIOHandler() {
    TestIOHandler handler = new TestIOHandler();
    IValue value = new StringValue("Hello");
    IOOperations.print(handler, value);
    assertEquals("Hello", handler.output);
  }

  @Test
  void printShouldRejectNullIOHandler() {
    IValue value = new StringValue("Hello");
    assertThrows(NullArgumentException.class, () -> IOOperations.print(null, value));
  }

  @Test
  void printShouldRejectNullValue() {
    TestIOHandler handler = new TestIOHandler();
    assertThrows(NullArgumentException.class, () -> IOOperations.print(handler, null));
  }
}