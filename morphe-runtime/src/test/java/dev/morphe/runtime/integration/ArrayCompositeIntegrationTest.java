package dev.morphe.runtime.integration;

import dev.morphe.runtime.exception.type.TypeMismatchException;
import dev.morphe.runtime.type.collection.ArrayType;
import dev.morphe.runtime.type.composite.CompositeType;
import dev.morphe.runtime.type.composite.FieldType;
import dev.morphe.runtime.type.primitive.IntegerType;
import dev.morphe.runtime.type.primitive.StringType;
import dev.morphe.runtime.value.collection.ArrayValue;
import dev.morphe.runtime.value.composite.CompositeValue;
import dev.morphe.runtime.value.primitive.StringValue;
import dev.morphe.runtime.value.primitive.numeric.IntegerValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ArrayCompositeIntegrationTest {

  @Test
  void acceptsCompositeContainingArray() {
    ArrayType scoresType = new ArrayType(IntegerType.INSTANCE);
    CompositeType userType = new CompositeType(
        "User", List.of(new FieldType("name", StringType.INSTANCE), new FieldType("scores", scoresType))
    );
    ArrayValue scores = new ArrayValue(
        scoresType, List.of(new IntegerValue(10), new IntegerValue(20), new IntegerValue(30))
    );
    CompositeValue user = new CompositeValue(userType, Map.of("name", new StringValue("Diego"), "scores", scores));
    assertTrue(userType.accepts(user));
    assertEquals(scores, user.get("scores"));
  }

  @Test
  void acceptsArrayContainingComposites() {
    CompositeType userType = userType();
    ArrayType usersType = new ArrayType(userType);
    CompositeValue firstUser = userValue(userType, "Diego", 21);
    CompositeValue secondUser = userValue(userType, "Ana", 25);
    ArrayValue users = new ArrayValue(usersType, List.of(firstUser, secondUser));
    assertTrue(usersType.accepts(users));
    assertEquals(2, users.size());
    assertEquals(List.of(firstUser, secondUser), users.values());
  }

  @Test
  void acceptsCompositeContainingEmptyArray() {
    ArrayType scoresType = new ArrayType(IntegerType.INSTANCE);
    CompositeType userType = new CompositeType(
        "User", List.of(new FieldType("name", StringType.INSTANCE), new FieldType("scores", scoresType))
    );
    ArrayValue scores = new ArrayValue(scoresType, List.of());
    CompositeValue user = new CompositeValue(userType, Map.of("name", new StringValue("Diego"), "scores", scores));
    assertTrue(userType.accepts(user));
    assertTrue(scores.isEmpty());
  }

  @Test
  void acceptsArrayContainingStructurallyCompatibleComposites() {
    CompositeType userType = userType();
    CompositeType personType = new CompositeType(
        "Person", List.of(new FieldType("name", StringType.INSTANCE), new FieldType("age", IntegerType.INSTANCE)
    ));
    ArrayType usersType = new ArrayType(userType);
    CompositeValue person = new CompositeValue(
        personType, Map.of("name", new StringValue("Diego"), "age", new IntegerValue(21)
    ));
    ArrayValue users = new ArrayValue(usersType, List.of(person));
    assertTrue(usersType.accepts(users));
  }

  @Test
  void rejectsCompositeContainingArrayWithIncompatibleElement() {
    ArrayType integerArrayType = new ArrayType(IntegerType.INSTANCE);
    CompositeType userType = new CompositeType(
        "User", List.of(new FieldType("name", StringType.INSTANCE), new FieldType("scores", integerArrayType)
    ));
    ArrayValue stringArray = new ArrayValue(new ArrayType(StringType.INSTANCE), List.of(new StringValue("invalid")));
    assertThrows(
        TypeMismatchException.class, () -> new CompositeValue(
            userType, Map.of("name", new StringValue("Diego"), "scores", stringArray)
        )
    );
  }

  private CompositeType userType() {
    return new CompositeType(
        "User", List.of(new FieldType("name", StringType.INSTANCE), new FieldType("age", IntegerType.INSTANCE)
    ));
  }

  private CompositeValue userValue(CompositeType type, String name, int age) {
    return new CompositeValue(type, Map.of("name", new StringValue(name), "age", new IntegerValue(age)));
  }
}