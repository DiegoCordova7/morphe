package dev.morphe.runtime.value.composite;

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.exception.field.MissingFieldException;
import dev.morphe.runtime.exception.field.UnknownFieldException;
import dev.morphe.runtime.exception.type.TypeMismatchException;
import dev.morphe.runtime.exception.value.NullFieldValueException;
import dev.morphe.runtime.kind.ValueKind;
import dev.morphe.runtime.type.composite.CompositeType;
import dev.morphe.runtime.type.composite.FieldType;
import dev.morphe.runtime.type.primitive.IntegerType;
import dev.morphe.runtime.type.primitive.StringType;
import dev.morphe.runtime.value.IValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CompositeValueTest {

  @Test
  void storesCompositeType() {
    CompositeType type = userType();
    CompositeValue value = createValidUser(type);
    assertEquals(type, value.type());
  }

  @Test
  void storesFieldValues() {
    CompositeType type = userType();
    Map<String, IValue> values = Map.of(
        "name", new StringValue("Diego"),
        "age", new IntegerValue(21)
    );
    CompositeValue value = new CompositeValue(type, values);
    assertEquals(values, value.values());
  }

  @Test
  void reportsCorrectKind() {
    CompositeValue value = createValidUser(userType());
    assertEquals(ValueKind.COMPOSITE, value.kind());
  }

  @Test
  void getsExistingField() {
    CompositeValue value = createValidUser(userType());
    assertEquals(new StringValue("Diego"), value.get("name"));
  }

  @Test
  void rejectsNullType() {
    assertThrows(NullArgumentException.class, () -> new CompositeValue(null, Map.of()));
  }

  @Test
  void rejectsNullValues() {
    assertThrows(NullArgumentException.class, () -> new CompositeValue(userType(), null));
  }

  @Test
  void rejectsUnknownField() {
    Map<String, IValue> values = Map.of(
        "name", new StringValue("Diego"),
        "age", new IntegerValue(21),
        "unknown", new StringValue("dev/morphe/runtime/value")
    );
    assertThrows(UnknownFieldException.class, () -> new CompositeValue(userType(), values));
  }

  @Test
  void rejectsMissingField() {
    Map<String, IValue> values = Map.of(
        "name", new StringValue("Diego")
    );
    assertThrows(MissingFieldException.class, () -> new CompositeValue(userType(), values));
  }

  @Test
  void rejectsNullFieldValue() {
    Map<String, IValue> values = new HashMap<>();
    values.put("name", null);
    values.put("age", new IntegerValue(21));
    assertThrows(NullFieldValueException.class, () -> new CompositeValue(userType(), values));
  }

  @Test
  void rejectsIncompatibleFieldValue() {
    Map<String, IValue> values = Map.of(
        "name", new StringValue("Diego"),
        "age", new StringValue("21")
    );
    assertThrows(TypeMismatchException.class, () -> new CompositeValue(userType(), values));
  }

  @Test
  void rejectsNullFieldName() {
    CompositeValue value = createValidUser(userType());
    assertThrows(NullArgumentException.class, () -> value.get(null));
  }

  @Test
  void rejectsUnknownFieldName() {
    CompositeValue value = createValidUser(userType());
    assertThrows(UnknownFieldException.class, () -> value.get("unknown"));
  }

  private CompositeType userType() {
    return new CompositeType(
        "User", List.of(
            new FieldType("name", StringType.INSTANCE),
            new FieldType("age", IntegerType.INSTANCE)
    ));
  }

  private CompositeValue createValidUser(CompositeType type) {
    return new CompositeValue(
        type, Map.of(
            "name", new StringValue("Diego"),
            "age", new IntegerValue(21)
    ));
  }

  @Test
  void returnsImmutableFieldValues() {
    CompositeValue value = createValidUser(userType());
    assertThrows(
        UnsupportedOperationException.class, () -> value.values().put("name", new StringValue("Other"))
    );
  }

  @Test
  void doesNotReflectChangesToOriginalValuesMap() {
    CompositeType type = userType();
    Map<String, IValue> values = new HashMap<>();
    values.put("name", new StringValue("Diego"));
    values.put("age", new IntegerValue(21));
    CompositeValue value = new CompositeValue(type, values);
    values.put("name", new StringValue("Other"));
    assertEquals(new StringValue("Diego"), value.get("name"));
  }

  @Test
  void considersValuesWithSameTypeAndFieldsEqual() {
    CompositeType type = userType();
    CompositeValue first = createValidUser(type);
    CompositeValue second = createValidUser(type);
    assertEquals(first, second);
  }

  @Test
  void considersValuesWithDifferentFieldValuesNotEqual() {
    CompositeType type = userType();
    CompositeValue first = createValidUser(type);
    CompositeValue second = new CompositeValue(
        type, Map.of(
            "name", new StringValue("Other"),
            "age", new IntegerValue(21)
    ));
    assertNotEquals(first, second);
  }

  @Test
  void considersValuesWithSameStructureEqual() {
    CompositeType firstType = userType();
    CompositeType secondType = new CompositeType(
        "OtherUser",
        List.of(
            new FieldType("name", StringType.INSTANCE),
            new FieldType("age", IntegerType.INSTANCE)
        )
    );
    CompositeValue first = createValidUser(firstType);
    CompositeValue second = createValidUser(secondType);
    assertEquals(first, second);
  }

  @Test
  void producesSameHashCodeForEqualValues() {
    CompositeType type = userType();
    CompositeValue first = createValidUser(type);
    CompositeValue second = createValidUser(type);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void isNotEqualToNull() {
    CompositeValue value = createValidUser(userType());
    assertNotEquals(null, value);
  }

  @Test
  void isNotEqualToAnotherType() {
    CompositeValue value = createValidUser(userType());
    assertNotEquals(value, "User");
  }
}