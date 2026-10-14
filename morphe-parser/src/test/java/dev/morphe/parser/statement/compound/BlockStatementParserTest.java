package dev.morphe.parser.statement.compound;

import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.ExpressionStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockStatementParserTest extends AParserTest {

  @Test
  void parsesEmptyBlock() {
    BlockStatement block = new BlockStatementParser(
        cursor("{}")
    ).parse();
    assertTrue(block.statements().isEmpty());
  }

  @Test
  void parsesSingleStatement() {
    BlockStatement block = new BlockStatementParser(
        cursor("{ 42; }")
    ).parse();
    assertEquals(1, block.statements().size());
    ExpressionStatement statement = assertInstanceOf(ExpressionStatement.class, block.statements().get(0));
    LiteralExpression literal = assertInstanceOf(LiteralExpression.class, statement.expression());
    assertEquals(42, literal.value());
  }

  @Test
  void parsesMultipleStatements() {
    BlockStatement block = new BlockStatementParser(
        cursor("""
          {
            1;
            2;
            3;
          }
          """)
    ).parse();
    assertEquals(3, block.statements().size());
    List<IStatement> statements = block.statements();
    for (int i = 0; i < statements.size(); i++) {
      ExpressionStatement statement = assertInstanceOf(ExpressionStatement.class, statements.get(i));
      LiteralExpression literal = assertInstanceOf(LiteralExpression.class, statement.expression());
      assertEquals(i + 1, literal.value());
    }
  }

  @Test
  void parsesNestedBlock() {
    BlockStatement block = new BlockStatementParser(
        cursor("{ { 42; } }")
    ).parse();
    assertEquals(1, block.statements().size());
    BlockStatement nested = assertInstanceOf(BlockStatement.class, block.statements().get(0));
    assertEquals(1, nested.statements().size());
    ExpressionStatement statement = assertInstanceOf(ExpressionStatement.class, nested.statements().get(0));
    LiteralExpression literal = assertInstanceOf(LiteralExpression.class, statement.expression());
    assertEquals(42, literal.value());
  }

  @Test
  void rejectsMissingClosingBrace() {
    assertThrows(UnexpectedTokenException.class, () -> new BlockStatementParser(cursor("{ 42;")).parse());
  }

  @Test
  void preservesSourceRange() {
    BlockStatement block = new BlockStatementParser(
        cursor("{ 42; }")
    ).parse();
    SourceRange range = block.range();
    assertEquals(0, range.start().offset());
    assertEquals(7, range.end().offset());
  }
}