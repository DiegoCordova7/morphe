package dev.morphe.parser.statement.compound;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.ReturnStatement;
import dev.morphe.ast.statement.WhileStatement;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WhileStatementParserTest extends AParserTest {

  @Test
  void parsesWhileStatement() {
    WhileStatement whileStatement =
        new WhileStatementParser(
            cursor("""
                    while (value < 10) {
                        return value;
                    }
                    """)
        ).parse();

    IExpression condition = whileStatement.condition();
    assertInstanceOf(BinaryExpression.class, condition);

    BlockStatement body = whileStatement.body();
    assertEquals(1, body.statements().size());
    assertInstanceOf(ReturnStatement.class, body.statements().get(0));
  }

  @Test
  void parsesEmptyBody() {
    WhileStatement whileStatement =
        new WhileStatementParser(
            cursor("""
                    while (value < 10) {
                    }
                    """)
        ).parse();

    assertTrue(whileStatement.body().statements().isEmpty());
  }

  @Test
  void preservesSourceRange() {
    WhileStatement whileStatement =
        new WhileStatementParser(
            cursor("while (value < 10) { return value; }")
        ).parse();
    assertEquals(0, whileStatement.range().start().offset());
    assertEquals(36, whileStatement.range().end().offset());
  }

  @Test
  void requiresOpeningParenthesis() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new WhileStatementParser(
            cursor("while value < 10) { }")
        ).parse()
    );
  }

  @Test
  void requiresClosingParenthesis() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new WhileStatementParser(
            cursor("while (value < 10 { }")
        ).parse()
    );
  }

  @Test
  void requiresBodyBlock() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new WhileStatementParser(
            cursor("while (value < 10)")
        ).parse()
    );
  }
}