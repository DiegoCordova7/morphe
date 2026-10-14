package dev.morphe.parser.expression.postfix;

import dev.morphe.ast.expression.*;
import dev.morphe.ast.operator.UpdateOperator;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.expression.ExpressionParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostfixParserTest extends AParserTest {

  @Test
  void parsesFieldAccess() {
    IExpression expression = new ExpressionParser(
        cursor("person.name")
    ).parse();
    assertInstanceOf(FieldAccessExpression.class, expression);
    FieldAccessExpression field = (FieldAccessExpression) expression;
    assertEquals("name", field.field());
    assertInstanceOf(VariableExpression.class, field.target());
    VariableExpression target = (VariableExpression) field.target();
    assertEquals("person", target.name());
  }

  @Test
  void parsesNestedFieldAccess() {
    IExpression expression = new ExpressionParser(
        cursor("person.address.city")
    ).parse();
    assertInstanceOf(FieldAccessExpression.class, expression);
    FieldAccessExpression city = (FieldAccessExpression) expression;
    assertEquals("city", city.field());
    assertInstanceOf(FieldAccessExpression.class, city.target());
    FieldAccessExpression address = (FieldAccessExpression) city.target();
    assertEquals("address", address.field());
    assertInstanceOf(VariableExpression.class, address.target());
    VariableExpression person = (VariableExpression) address.target();
    assertEquals("person", person.name());
  }

  @Test
  void parsesArrayAccess() {
    IExpression expression = new ExpressionParser(
        cursor("values[0]")
    ).parse();
    assertInstanceOf(ArrayAccessExpression.class, expression);
    ArrayAccessExpression access = (ArrayAccessExpression) expression;
    assertInstanceOf(VariableExpression.class, access.array());
    assertEquals("values", ((VariableExpression) access.array()).name());
    assertInstanceOf(LiteralExpression.class, access.index());
    assertEquals(0, ((LiteralExpression) access.index()).value());
  }

  @Test
  void parsesNestedArrayAccess() {
    IExpression expression = new ExpressionParser(
        cursor("matrix[0][1]")
    ).parse();
    assertInstanceOf(ArrayAccessExpression.class, expression);
    ArrayAccessExpression second = (ArrayAccessExpression) expression;
    assertInstanceOf(LiteralExpression.class, second.index());
    assertEquals(1, ((LiteralExpression) second.index()).value());
    assertInstanceOf(ArrayAccessExpression.class, second.array());
    ArrayAccessExpression first = (ArrayAccessExpression) second.array();
    assertInstanceOf(LiteralExpression.class, first.index());
    assertEquals(0, ((LiteralExpression) first.index()).value());
    assertInstanceOf(VariableExpression.class, first.array());
    assertEquals("matrix", ((VariableExpression) first.array()).name());
  }

  @Test
  void parsesMixedPostfixAccess() {
    IExpression expression = new ExpressionParser(
        cursor("person.addresses[0].name")
    ).parse();
    assertInstanceOf(FieldAccessExpression.class, expression);
    FieldAccessExpression name = (FieldAccessExpression) expression;
    assertEquals("name", name.field());
    assertInstanceOf(ArrayAccessExpression.class, name.target());
    ArrayAccessExpression access = (ArrayAccessExpression) name.target();
    assertInstanceOf(LiteralExpression.class, access.index());
    assertEquals(0, ((LiteralExpression) access.index()).value());
    assertInstanceOf(FieldAccessExpression.class, access.array());
    FieldAccessExpression addresses = (FieldAccessExpression) access.array();
    assertEquals("addresses", addresses.field());
    assertInstanceOf(VariableExpression.class, addresses.target());
    assertEquals("person", ((VariableExpression) addresses.target()).name());
  }

  @Test
  void preservesRangeAcrossFieldAccess() {
    IExpression expression = new ExpressionParser(
        cursor("person.name")
    ).parse();
    SourceRange range = expression.range();
    assertEquals(0, range.start().offset());
    assertEquals(11, range.end().offset());
  }

  @Test
  void preservesRangeAcrossArrayAccess() {
    IExpression expression = new ExpressionParser(
        cursor("values[0]")
    ).parse();
    SourceRange range = expression.range();
    assertEquals(0, range.start().offset());
    assertEquals(9, range.end().offset());
  }

  @Test
  void parsesFunctionCallWithoutArguments() {
    IExpression expression = new ExpressionParser(
        cursor("foo()")
    ).parse();
    assertInstanceOf(FunctionCallExpression.class, expression);
    FunctionCallExpression call = (FunctionCallExpression) expression;
    assertInstanceOf(VariableExpression.class, call.function());
    assertEquals("foo", ((VariableExpression) call.function()).name());
    assertTrue(call.arguments().isEmpty());
  }

  @Test
  void parsesFunctionCallWithArguments() {
    IExpression expression = new ExpressionParser(
        cursor("foo(10, 20)")
    ).parse();
    assertInstanceOf(FunctionCallExpression.class, expression);
    FunctionCallExpression call = (FunctionCallExpression) expression;
    assertInstanceOf(VariableExpression.class, call.function());
    assertEquals("foo", ((VariableExpression) call.function()).name());
    assertEquals(2, call.arguments().size());
    assertInstanceOf(LiteralExpression.class, call.arguments().get(0));
    assertEquals(10, ((LiteralExpression) call.arguments().get(0)).value());
    assertInstanceOf(LiteralExpression.class, call.arguments().get(1));
    assertEquals(20, ((LiteralExpression) call.arguments().get(1)).value());
  }

  @Test
  void parsesFunctionCallWithExpressionArguments() {
    IExpression expression = new ExpressionParser(
        cursor("foo(1 + 2, value * 3)")
    ).parse();
    assertInstanceOf(FunctionCallExpression.class, expression);
    FunctionCallExpression call = (FunctionCallExpression) expression;
    assertEquals(2, call.arguments().size());
    assertInstanceOf(BinaryExpression.class, call.arguments().get(0));
    assertInstanceOf(BinaryExpression.class, call.arguments().get(1));
  }

  @Test
  void parsesFunctionCallAfterFieldAccess() {
    IExpression expression = new ExpressionParser(
        cursor("person.getName()")
    ).parse();
    assertInstanceOf(FunctionCallExpression.class, expression);
    FunctionCallExpression call = (FunctionCallExpression) expression;
    assertInstanceOf(FieldAccessExpression.class, call.function());
    FieldAccessExpression method = (FieldAccessExpression) call.function();
    assertEquals("getName", method.field());
    assertInstanceOf(VariableExpression.class, method.target());
    assertEquals("person", ((VariableExpression) method.target()).name());
    assertTrue(call.arguments().isEmpty());
  }

  @Test
  void parsesIncrement() {
    IExpression expression = new ExpressionParser(
        cursor("value++")
    ).parse();
    assertInstanceOf(UpdateExpression.class, expression);
    UpdateExpression update = (UpdateExpression) expression;
    assertEquals(UpdateOperator.INCREMENT, update.operator());
    assertInstanceOf(VariableExpression.class, update.operand());
    assertEquals("value", ((VariableExpression) update.operand()).name());
  }

  @Test
  void parsesDecrement() {
    IExpression expression = new ExpressionParser(
        cursor("value--")
    ).parse();
    assertInstanceOf(UpdateExpression.class, expression);
    UpdateExpression update = (UpdateExpression) expression;
    assertEquals(UpdateOperator.DECREMENT, update.operator());
    assertInstanceOf(VariableExpression.class, update.operand());
    assertEquals("value", ((VariableExpression) update.operand()).name());
  }

  @Test
  void parsesUpdateAfterFieldAccess() {
    IExpression expression = new ExpressionParser(
        cursor("person.age++")
    ).parse();
    assertInstanceOf(UpdateExpression.class, expression);
    UpdateExpression update = (UpdateExpression) expression;
    assertEquals(UpdateOperator.INCREMENT, update.operator());
    assertInstanceOf(FieldAccessExpression.class, update.operand());
    FieldAccessExpression field = (FieldAccessExpression) update.operand();
    assertEquals("age", field.field());
    assertInstanceOf(VariableExpression.class, field.target());
    assertEquals("person", ((VariableExpression) field.target()).name());
  }

  @Test
  void parsesChainedPostfixOperations() {
    IExpression expression = new ExpressionParser(
        cursor("person.getAddresses()[0].name")
    ).parse();
    assertInstanceOf(FieldAccessExpression.class, expression);
    FieldAccessExpression name = (FieldAccessExpression) expression;
    assertEquals("name", name.field());
    assertInstanceOf(ArrayAccessExpression.class, name.target());
    ArrayAccessExpression access = (ArrayAccessExpression) name.target();
    assertInstanceOf(LiteralExpression.class, access.index());
    assertEquals(0, ((LiteralExpression) access.index()).value());
    assertInstanceOf(FunctionCallExpression.class, access.array());
    FunctionCallExpression call = (FunctionCallExpression) access.array();
    assertTrue(call.arguments().isEmpty());
    assertInstanceOf(FieldAccessExpression.class, call.function());
    FieldAccessExpression method = (FieldAccessExpression) call.function();
    assertEquals("getAddresses", method.field());
  }

  @Test
  void preservesRangeAcrossUpdate() {
    IExpression expression = new ExpressionParser(
        cursor("value++")
    ).parse();
    SourceRange range = expression.range();
    assertEquals(0, range.start().offset());
    assertEquals(7, range.end().offset());
  }
}