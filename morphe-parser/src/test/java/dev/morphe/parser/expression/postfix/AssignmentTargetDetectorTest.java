package dev.morphe.parser.expression.postfix;

import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssignmentTargetDetectorTest extends AParserTest {

  @Test
  void detectsVariableAssignment() {
    assertTrue(AssignmentTargetDetector.starts(cursor("value = 10;")));
  }

  @Test
  void detectsFieldAssignment() {
    assertTrue(AssignmentTargetDetector.starts(cursor("person.name = 10;")));
  }

  @Test
  void detectsNestedFieldAssignment() {
    assertTrue(AssignmentTargetDetector.starts(cursor("person.address.city = 10;")));
  }

  @Test
  void detectsArrayElementAssignment() {
    assertTrue(AssignmentTargetDetector.starts(cursor("values[0] = 10;")));
  }

  @Test
  void detectsNestedArrayAccessAssignment() {
    assertTrue(AssignmentTargetDetector.starts(cursor("matrix[0][1] = 10;")));
  }

  @Test
  void detectsArrayFieldAssignment() {
    assertTrue(AssignmentTargetDetector.starts(cursor("person.values[0] = 10;")));
  }

  @Test
  void detectsFieldArrayAssignment() {
    assertTrue(AssignmentTargetDetector.starts(cursor("values[0].field = 10;")));
  }

  @Test
  void rejectsExpression() {
    assertFalse(AssignmentTargetDetector.starts(cursor("value + 10;")));
  }

  @Test
  void rejectsFunctionCall() {
    assertFalse(AssignmentTargetDetector.starts(cursor("foo();")));
  }

  @Test
  void rejectsMalformedFieldAccess() {
    assertFalse(AssignmentTargetDetector.starts(cursor("person. = 10;")));
  }

  @Test
  void rejectsMalformedArrayAccess() {
    assertFalse(AssignmentTargetDetector.starts(cursor("values[ = 10;")));
  }

  @Test
  void rejectsUnclosedArrayAccess() {
    assertFalse(AssignmentTargetDetector.starts(cursor("values[0 = 10;")));
  }

  @ParameterizedTest
  @MethodSource("assignmentSources")
  void detectsAssignment(String source) {
    assertTrue(AssignmentTargetDetector.starts(cursor(source)));
  }

  static Stream<Arguments> assignmentSources() {
    return Stream.of(
        Arguments.of("value = 10;"),
        Arguments.of("value += 10;"),
        Arguments.of("value -= 10;"),
        Arguments.of("value *= 10;"),
        Arguments.of("value /= 10;"),
        Arguments.of("value %= 10;"),
        Arguments.of("values[0] = 10;"),
        Arguments.of("values[0] += 10;"),
        Arguments.of("matrix[0][1] -= 10;"),
        Arguments.of("person.values[0] *= 10;")
    );
  }

  @ParameterizedTest
  @MethodSource("nonAssignmentSources")
  void rejectsNonAssignment(String source) {
    assertFalse(
        AssignmentTargetDetector.starts(cursor(source))
    );
  }

  static Stream<Arguments> nonAssignmentSources() {
    return Stream.of(
        Arguments.of("value + 10;"),
        Arguments.of("value - 10;"),
        Arguments.of("value * 10;"),
        Arguments.of("value / 10;"),
        Arguments.of("value % 10;"),
        Arguments.of("value == 10;"),
        Arguments.of("value != 10;"),
        Arguments.of("values[0] + 10;"),
        Arguments.of("values[0] == 10;")
    );
  }
}