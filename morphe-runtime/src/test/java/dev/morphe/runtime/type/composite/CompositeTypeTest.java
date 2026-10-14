package dev.morphe.runtime.type.composite;

import dev.morphe.runtime.exception.argument.NullArgumentException;
import dev.morphe.runtime.exception.field.DuplicateFieldException;
import dev.morphe.runtime.kind.TypeKind;
import dev.morphe.runtime.type.primitive.IntegerType;
import dev.morphe.runtime.type.primitive.StringType;
import dev.morphe.runtime.value.composite.CompositeValue;
import dev.morphe.runtime.value.primitive.BooleanValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CompositeTypeTest {

  @Test
  void storesTypeName() {
    CompositeType type = new CompositeType("User", List.of(new FieldType("name", StringType.INSTANCE)));
    assertEquals("User", type.name());
  }

  @Test
  void reportsCorrectKind() {
    CompositeType type = new CompositeType("User", List.of(new FieldType("name", StringType.INSTANCE)));
    assertEquals(TypeKind.COMPOSITE, type.kind());
  }

  @Test
  void storesFields() {
    FieldType name = new FieldType("name", StringType.INSTANCE);
    FieldType age = new FieldType("age", IntegerType.INSTANCE);
    CompositeType type = new CompositeType("User", List.of(name, age));
    assertEquals(List.of(name, age), type.fields());
  }

  @Test
  void findsFieldByName() {
    FieldType name = new FieldType("name", StringType.INSTANCE);
    CompositeType type = new CompositeType("User", List.of(name));
    assertEquals(name, type.field("name"));
  }

  @Test
  void returnsNullWhenFieldDoesNotExist() {
    CompositeType type = new CompositeType("User", List.of(new FieldType("name", StringType.INSTANCE)));
    assertNull(type.field("unknown"));
  }

  @Test
  void reportsWhetherFieldExists() {
    CompositeType type = new CompositeType("User", List.of(new FieldType("name", StringType.INSTANCE)));
    assertTrue(type.hasField("name"));
    assertFalse(type.hasField("unknown"));
  }

  @Test
  void acceptsCompatibleCompositeValue() {
    CompositeType type = userType();
    CompositeValue value = new CompositeValue(
        type, Map.of("name", new StringValue("Diego"), "age", new IntegerValue(21))
    );
    assertTrue(type.accepts(value));
  }

  @Test
  void rejectsNonCompositeValue() {
    CompositeType type = userType();
    assertFalse(type.accepts(new StringValue("Diego")));
    assertFalse(type.accepts(new IntegerValue(21)));
    assertFalse(type.accepts(new BooleanValue(true)));
  }

  @Test
  void acceptsStructurallyCompatibleDifferentCompositeType() {
    CompositeType userType = userType();
    CompositeType personType = new CompositeType(
        "Person", List.of(new FieldType("name", StringType.INSTANCE), new FieldType("age", IntegerType.INSTANCE))
    );
    CompositeValue person = new CompositeValue(
        personType, Map.of("name", new StringValue("Diego"), "age", new IntegerValue(21))
    );
    assertTrue(userType.accepts(person));
  }

  @Test
  void rejectsNullValue() {
    CompositeType type = userType();
    assertFalse(type.accepts(null));
  }

  @Test
  void rejectsNullTypeName() {
    assertThrows(
        NullArgumentException.class, () -> new CompositeType(null, List.of(new FieldType("name", StringType.INSTANCE)))
    );
  }

  @Test
  void rejectsNullFields() {
    assertThrows(
        NullArgumentException.class, () -> new CompositeType("User", null)
    );
  }

  @Test
  void rejectsNullField() {
    assertThrows(
        NullArgumentException.class, () -> new CompositeType("User", new ArrayList<>(Arrays.asList((FieldType) null)))
    );
  }

  @Test
  void rejectsDuplicateFieldNames() {
    assertThrows(
        DuplicateFieldException.class,
        () -> new CompositeType(
            "User", List.of(new FieldType("name", StringType.INSTANCE), new FieldType("name", StringType.INSTANCE)
        ))
    );
  }

  private CompositeType userType() {
    return new CompositeType(
        "User", List.of(new FieldType("name", StringType.INSTANCE), new FieldType("age", IntegerType.INSTANCE)
    ));
  }

  @Test
  void rejectsNullFieldNameWhenLookingUpField() {
    CompositeType type = userType();
    assertThrows(NullArgumentException.class, () -> type.field(null));
  }

  @Test
  void rejectsNullFieldNameWhenCheckingFieldExistence() {
    CompositeType type = userType();
    assertThrows(NullArgumentException.class, () -> type.hasField(null));
  }

  @Test
  void exposesImmutableFields() {
    CompositeType type = userType();
    assertThrows(
        UnsupportedOperationException.class, () -> type.fields().add(new FieldType("email", StringType.INSTANCE))
    );
  }

  @Test
  void doesNotExposeOriginalFieldsList() {
    List<FieldType> fields = new ArrayList<>();
    fields.add(new FieldType("name", StringType.INSTANCE));
    CompositeType type = new CompositeType("User", fields);
    fields.add(new FieldType("age", IntegerType.INSTANCE));
    assertEquals(1, type.fields().size());
    assertFalse(type.hasField("age"));
  }
}