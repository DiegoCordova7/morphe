package dev.morphe.parser.expression.postfix;

import dev.morphe.ast.expression.ArrayAccessExpression;
import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import dev.morphe.parser.expression.primary.PrimaryParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArrayAccessParserTest extends AParserTest {

  @Test
  void parsesArrayAccess() {
    TokenCursor cursor = cursor("numbers[0]");
    VariableExpression target = (VariableExpression) new PrimaryParser(cursor).parse();
    cursor.advance(); // [
    ArrayAccessExpression expression = (ArrayAccessExpression) new ArrayAccessParser(cursor).parse(target);
    assertEquals("numbers", target.name());
    assertEquals(target, expression.array());
    LiteralExpression index = (LiteralExpression) expression.index();
    assertEquals(0, index.value());
  }

  @Test
  void parsesExpressionAsIndex() {
    TokenCursor cursor = cursor("numbers[index + 1]");
    VariableExpression target = (VariableExpression) new PrimaryParser(cursor).parse();
    cursor.advance(); // [
    ArrayAccessExpression expression = (ArrayAccessExpression) new ArrayAccessParser(cursor).parse(target);
    assertEquals(target, expression.array());
  }

  @Test
  void storesSourceRange() {
    TokenCursor cursor = cursor("numbers[0]");
    VariableExpression target = (VariableExpression) new PrimaryParser(cursor).parse();
    cursor.advance(); // [
    ArrayAccessExpression expression = (ArrayAccessExpression) new ArrayAccessParser(cursor).parse(target);
    assertEquals(0, expression.range().start().offset());
    assertEquals(10, expression.range().end().offset());
  }

  @Test
  void rejectsMissingClosingBracket() {
    TokenCursor cursor = cursor("numbers[0");
    VariableExpression target = (VariableExpression) new PrimaryParser(cursor).parse();
    cursor.advance(); // [
    assertThrows(UnexpectedTokenException.class, () -> new ArrayAccessParser(cursor).parse(target));
  }
}