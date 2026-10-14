package dev.morphe.parser.expression.compound;

import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.expression.MatchExpression;
import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.pattern.*;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchExpressionParserTest extends AParserTest {

  @Test
  void parsesSingleCase() {
    IExpression expression = new MatchExpressionParser(
        cursor("""
            match (value) {
              1 => "one"
            }
            """)
    ).parse();
    assertInstanceOf(MatchExpression.class, expression);
    MatchExpression match = (MatchExpression) expression;
    assertInstanceOf(VariableExpression.class, match.value());
    assertEquals("value", ((VariableExpression) match.value()).name());
    assertEquals(1, match.cases().size());
    MatchExpression.MatchCase matchCase = match.cases().get(0);
    assertInstanceOf(LiteralPattern.class, matchCase.pattern());
    assertEquals(1, ((LiteralPattern) matchCase.pattern()).value());
    assertInstanceOf(LiteralExpression.class, matchCase.result());
    assertEquals("one", ((LiteralExpression) matchCase.result()).value());
  }

  @Test
  void parsesMultipleCases() {
    IExpression expression = new MatchExpressionParser(
        cursor("""
          match (value) {
            1 => "one"
            2 => "two"
            3 => "three"
          }
          """)
    ).parse();
    assertInstanceOf(MatchExpression.class, expression);
    MatchExpression match = (MatchExpression) expression;
    assertEquals(3, match.cases().size());
    assertEquals(1, ((LiteralPattern) match.cases().get(0).pattern()).value());
    assertEquals("one", ((LiteralExpression) match.cases().get(0).result()).value());
    assertEquals(2, ((LiteralPattern) match.cases().get(1).pattern()).value());
    assertEquals("two", ((LiteralExpression) match.cases().get(1).result()).value());
    assertEquals(3, ((LiteralPattern) match.cases().get(2).pattern()).value());
    assertEquals("three", ((LiteralExpression) match.cases().get(2).result()).value());
  }

  @Test
  void parsesComplexPatterns() {
    IExpression expression = new MatchExpressionParser(
        cursor("""
          match (value) {
            1 | 2 => "small"
            3 .. 10 => "medium"
            _ => "other"
          }
          """)
    ).parse();
    MatchExpression match = (MatchExpression) expression;
    assertEquals(3, match.cases().size());
    assertInstanceOf(OrPattern.class, match.cases().get(0).pattern());
    assertInstanceOf(RangePattern.class, match.cases().get(1).pattern());
    assertInstanceOf(DefaultPattern.class, match.cases().get(2).pattern());
  }

  @Test
  void preservesSourceRange() {
    IExpression expression = new MatchExpressionParser(
        cursor("""
          match (value) {
            1 => "one"
          }
          """)
    ).parse();
    SourceRange range = expression.range();
    assertEquals(0, range.start().offset());
    assertEquals(30, range.end().offset());
  }

  @Test
  void parsesDifferentPatternTypes() {
    IExpression expression = new MatchExpressionParser(
        cursor("""
          match (value) {
            1 => "literal"
            x => "variable"
            2 | 3 => "or"
            4 .. 10 => "range"
            _ => "default"
          }
          """)
    ).parse();
    MatchExpression match = (MatchExpression) expression;
    assertEquals(5, match.cases().size());
    assertInstanceOf(LiteralPattern.class, match.cases().get(0).pattern());
    assertInstanceOf(VariablePattern.class, match.cases().get(1).pattern());
    assertInstanceOf(OrPattern.class, match.cases().get(2).pattern());
    assertInstanceOf(RangePattern.class, match.cases().get(3).pattern());
    assertInstanceOf(DefaultPattern.class, match.cases().get(4).pattern());
  }

  @Test
  void rejectsMissingClosingBrace() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new MatchExpressionParser(
            cursor("match (value) { 1 => \"one\"")
        ).parse()
    );
  }

}