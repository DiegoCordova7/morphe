package dev.morphe.runtime.environment;

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import dev.morphe.runtime.value.IValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VariableCellTest {

  @Test
  void rejectsNullValueOnConstruction() {
    assertThrows(NullArgumentException.class, () -> new VariableCell(null));
  }

  @Test
  void storesInitialValue() {
    IValue value = new IntegerValue(10);
    VariableCell cell = new VariableCell(value);
    assertSame(value, cell.get());
  }

  @Test
  void updatesValue() {
    VariableCell cell = new VariableCell(new IntegerValue(10));
    IValue newValue = new IntegerValue(20);
    cell.set(newValue);
    assertSame(newValue, cell.get());
  }

  @Test
  void rejectsNullValueOnSet() {
    VariableCell cell = new VariableCell(new IntegerValue(10));
    assertThrows(NullArgumentException.class, () -> cell.set(null));
  }
}