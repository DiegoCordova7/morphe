package dev.morphe.semantic.type;

import dev.morphe.ast.type.*;
import dev.morphe.semantic.exception.argument.NullSemanticArgumentException;
import dev.morphe.semantic.symbol.SymbolTable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TypeCheckerTest {

  private TypeChecker checker() {
    return new TypeChecker(new SymbolTable());
  }

  @Test
  void rejectsNullSymbolTable() {
    assertThrows(NullSemanticArgumentException.class, () -> new TypeChecker(null));
  }

  @Test
  void rejectsNullLeftType() {
    TypeChecker checker = checker();
    assertThrows(NullSemanticArgumentException.class, () -> checker.areCompatible(null, PrimitiveType.INTEGER));
  }

  @Test
  void rejectsNullRightType() {
    TypeChecker checker = checker();
    assertThrows(NullSemanticArgumentException.class, () -> checker.areCompatible(PrimitiveType.INTEGER, null));
  }

  @Test
  void considersSamePrimitiveTypesCompatible() {
    TypeChecker checker = checker();
    assertTrue(checker.areCompatible(PrimitiveType.INTEGER, PrimitiveType.INTEGER));
  }

  @Test
  void considersDifferentPrimitiveTypesIncompatible() {
    TypeChecker checker = checker();
    assertFalse(checker.areCompatible(PrimitiveType.INTEGER, PrimitiveType.DOUBLE));
  }

  @Test
  void considersNullCompatibleWithPrimitiveTypes() {
    TypeChecker checker = checker();
    assertTrue(checker.areCompatible(NullType.NULL, PrimitiveType.INTEGER));
    assertTrue(checker.areCompatible(NullType.NULL, PrimitiveType.DOUBLE));
    assertTrue(checker.areCompatible(NullType.NULL, PrimitiveType.BOOLEAN));
    assertTrue(checker.areCompatible(NullType.NULL, PrimitiveType.STRING));
  }

  @Test
  void considersPrimitiveTypesCompatibleWithNull() {
    TypeChecker checker = checker();
    assertTrue(checker.areCompatible(PrimitiveType.INTEGER, NullType.NULL));
    assertTrue(checker.areCompatible(PrimitiveType.DOUBLE, NullType.NULL));
    assertTrue(checker.areCompatible(PrimitiveType.BOOLEAN, NullType.NULL));
    assertTrue(checker.areCompatible(PrimitiveType.STRING, NullType.NULL));
  }

  @Test
  void considersNullCompatibleWithNull() {
    TypeChecker checker = checker();
    assertTrue(checker.areCompatible(NullType.NULL, NullType.NULL));
  }

  @Test
  void considersNullIncompatibleWithVoid() {
    TypeChecker checker = checker();
    assertFalse(checker.areCompatible(NullType.NULL, PrimitiveType.VOID));
  }

  @Test
  void considersVoidIncompatibleWithNull() {
    TypeChecker checker = checker();
    assertFalse(checker.areCompatible(PrimitiveType.VOID, NullType.NULL));
  }

  @Test
  void considersNullCompatibleWithArrayTypes() {
    TypeChecker checker = checker();
    IASTType array = new ArrayType(PrimitiveType.INTEGER);
    assertTrue(checker.areCompatible(NullType.NULL, array));
    assertTrue(checker.areCompatible(array, NullType.NULL));
  }

  @Test
  void considersNullCompatibleWithCompositeTypes() {
    TypeChecker checker = checker();
    CompositeType person = new CompositeType(
        "Person",
        List.of(new CompositeType.CompositeField("name", PrimitiveType.STRING))
    );
    assertTrue(checker.areCompatible(NullType.NULL, person));
    assertTrue(checker.areCompatible(person, NullType.NULL));
  }

  @Test
  void considersNullCompatibleWithFunctionTypes() {
    TypeChecker checker = checker();
    FunctionType function = new FunctionType(List.of(PrimitiveType.INTEGER), PrimitiveType.STRING);
    assertTrue(checker.areCompatible(NullType.NULL, function));
    assertTrue(checker.areCompatible(function, NullType.NULL));
  }

  @Test
  void considersNullIncompatibleWithVoidInsideFunctionType() {
    TypeChecker checker = checker();
    FunctionType left = new FunctionType(List.of(PrimitiveType.INTEGER), NullType.NULL);
    FunctionType right = new FunctionType(List.of(PrimitiveType.INTEGER), PrimitiveType.VOID);
    assertFalse(checker.areCompatible(left, right));
  }

  @Test
  void considersArraysWithCompatibleElementsCompatible() {
    TypeChecker checker = checker();
    IASTType left = new ArrayType(PrimitiveType.INTEGER);
    IASTType right = new ArrayType(PrimitiveType.INTEGER);
    assertTrue(checker.areCompatible(left, right));
  }

  @Test
  void considersArraysWithDifferentElementsIncompatible() {
    TypeChecker checker = checker();
    IASTType left = new ArrayType(PrimitiveType.INTEGER);
    IASTType right = new ArrayType(PrimitiveType.DOUBLE);
    assertFalse(checker.areCompatible(left, right));
  }

  @Test
  void resolvesArrayCompatibilityRecursively() {
    TypeChecker checker = checker();
    IASTType left = new ArrayType(new ArrayType(PrimitiveType.INTEGER));
    IASTType right = new ArrayType(new ArrayType(PrimitiveType.INTEGER));
    assertTrue(checker.areCompatible(left, right));
  }

  @Test
  void considersNestedNullTypesCompatible() {
    TypeChecker checker = checker();
    IASTType left = new ArrayType(new ArrayType(NullType.NULL));
    IASTType right = new ArrayType(new ArrayType(PrimitiveType.INTEGER));
    assertTrue(checker.areCompatible(left, right));
  }

  @Test
  void rejectsArraysWithDifferentDimensions() {
    TypeChecker checker = checker();
    IASTType left = new ArrayType(new ArrayType(PrimitiveType.INTEGER));
    IASTType right = new ArrayType(PrimitiveType.INTEGER);
    assertFalse(checker.areCompatible(left, right));
  }

  @Test
  void considersCompatibleFunctionTypesCompatible() {
    TypeChecker checker = checker();
    FunctionType left = new FunctionType(List.of(PrimitiveType.INTEGER, PrimitiveType.STRING), PrimitiveType.BOOLEAN);
    FunctionType right = new FunctionType(List.of(PrimitiveType.INTEGER, PrimitiveType.STRING), PrimitiveType.BOOLEAN);
    assertTrue(checker.areCompatible(left, right));
  }

  @Test
  void rejectsFunctionTypesWithDifferentParameters() {
    TypeChecker checker = checker();
    FunctionType left = new FunctionType(List.of(PrimitiveType.INTEGER), PrimitiveType.BOOLEAN);
    FunctionType right = new FunctionType(List.of(PrimitiveType.DOUBLE), PrimitiveType.BOOLEAN);
    assertFalse(checker.areCompatible(left, right));
  }

  @Test
  void rejectsFunctionTypesWithDifferentParameterCounts() {
    TypeChecker checker = checker();
    FunctionType left = new FunctionType(List.of(PrimitiveType.INTEGER), PrimitiveType.BOOLEAN);
    FunctionType right = new FunctionType(List.of(PrimitiveType.INTEGER, PrimitiveType.INTEGER), PrimitiveType.BOOLEAN);
    assertFalse(checker.areCompatible(left, right));
  }

  @Test
  void rejectsFunctionTypesWithDifferentReturnTypes() {
    TypeChecker checker = checker();
    FunctionType left = new FunctionType(List.of(PrimitiveType.INTEGER), PrimitiveType.STRING);
    FunctionType right = new FunctionType(List.of(PrimitiveType.INTEGER), PrimitiveType.BOOLEAN);
    assertFalse(checker.areCompatible(left, right));
  }

  @Test
  void considersNullCompatibleWithFunctionParameterTypes() {
    TypeChecker checker = checker();
    FunctionType left = new FunctionType(List.of(NullType.NULL), PrimitiveType.STRING);
    FunctionType right = new FunctionType(List.of(PrimitiveType.INTEGER), PrimitiveType.STRING);
    assertTrue(checker.areCompatible(left, right));
  }

  @Test
  void considersCompositeWithRequiredFieldsCompatible() {
    TypeChecker checker = checker();
    CompositeType persona = new CompositeType(
        "Persona", List.of(
            new CompositeType.CompositeField("nombre", PrimitiveType.STRING),
            new CompositeType.CompositeField("apellido", PrimitiveType.STRING),
            new CompositeType.CompositeField("edad", PrimitiveType.INTEGER)
        )
    );
    CompositeType trabajador = new CompositeType(
        "Trabajador", List.of(
            new CompositeType.CompositeField("nombre", PrimitiveType.STRING),
            new CompositeType.CompositeField("apellido", PrimitiveType.STRING)
        )
    );
    assertTrue(checker.areCompatible(persona, trabajador));
  }

  @Test
  void rejectsCompositeMissingRequiredFields() {
    TypeChecker checker = checker();
    CompositeType persona = new CompositeType(
        "Persona",
        List.of(
            new CompositeType.CompositeField("nombre", PrimitiveType.STRING),
            new CompositeType.CompositeField("apellido", PrimitiveType.STRING),
            new CompositeType.CompositeField("edad", PrimitiveType.INTEGER)
        )
    );
    CompositeType trabajador = new CompositeType(
        "Trabajador",
        List.of(
            new CompositeType.CompositeField("nombre", PrimitiveType.STRING),
            new CompositeType.CompositeField("apellido", PrimitiveType.STRING)
        )
    );
    assertFalse(checker.areCompatible(trabajador, persona));
  }

  @Test
  void rejectsCompositeWithIncompatibleFieldType() {
    TypeChecker checker = checker();
    CompositeType left = new CompositeType(
        "A", List.of(new CompositeType.CompositeField("value", PrimitiveType.INTEGER))
    );
    CompositeType right = new CompositeType(
        "B", List.of(new CompositeType.CompositeField("value", PrimitiveType.STRING))
    );
    assertFalse(checker.areCompatible(left, right));
  }

  @Test
  void considersNullCompatibleWithCompositeFieldTypes() {
    TypeChecker checker = checker();
    CompositeType provided = new CompositeType(
        "Provided", List.of(new CompositeType.CompositeField("value", NullType.NULL))
    );
    CompositeType required = new CompositeType(
        "Required", List.of(new CompositeType.CompositeField("value", PrimitiveType.STRING))
    );
    assertTrue(checker.areCompatible(provided, required));
  }

  @Test
  void ignoresCompositeTypeNames() {
    TypeChecker checker = checker();

    CompositeType first = new CompositeType(
        "Persona",
        List.of(new CompositeType.CompositeField("nombre", PrimitiveType.STRING))
    );
    CompositeType second = new CompositeType(
        "CosaCompletamenteDistinta",
        List.of(new CompositeType.CompositeField("nombre", PrimitiveType.STRING))
    );
    assertTrue(checker.areCompatible(first, second));
  }
}