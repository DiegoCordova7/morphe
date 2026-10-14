package dev.morphe.parser.statement.compound;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.DoWhileStatement;
import dev.morphe.ast.statement.ReturnStatement;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoWhileStatementParserTest extends AParserTest {

  @Test
  void parsesDoWhileStatement() {
    DoWhileStatement doWhile =
        new DoWhileStatementParser(
            cursor("""
                    do {
                        return value;
                    } while (value < 10);
                    """)
        ).parse();

    IExpression condition = doWhile.condition();
    assertInstanceOf(BinaryExpression.class, condition);

    BlockStatement body = doWhile.body();
    assertEquals(1, body.statements().size());
    assertInstanceOf(ReturnStatement.class, body.statements().get(0));
  }

  @Test
  void parsesEmptyBody() {
    DoWhileStatement doWhile =
        new DoWhileStatementParser(
            cursor("do { } while (value < 10);")
        ).parse();

    assertTrue(doWhile.body().statements().isEmpty());
  }

  @Test
  void preservesSourceRange() {
    String source = "do { return value; } while (value < 10);";

    DoWhileStatement doWhile =
        new DoWhileStatementParser(cursor(source)).parse();

    assertEquals(0, doWhile.range().start().offset());
    assertEquals(source.length(), doWhile.range().end().offset());
  }

  @Test
  void requiresDoKeyword() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new DoWhileStatementParser(
            cursor("{ } while (value < 10);")
        ).parse()
    );
  }

  @Test
  void requiresWhileKeyword() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new DoWhileStatementParser(
            cursor("do { } (value < 10);")
        ).parse()
    );
  }

  @Test
  void requiresOpeningParenthesis() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new DoWhileStatementParser(
            cursor("do { } while value < 10);")
        ).parse()
    );
  }

  @Test
  void requiresClosingParenthesis() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new DoWhileStatementParser(
            cursor("do { } while (value < 10;")
        ).parse()
    );
  }

  @Test
  void requiresTerminatingSemicolon() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new DoWhileStatementParser(
            cursor("do { } while (value < 10)")
        ).parse()
    );
  }

  @Test
  void requiresBodyBlock() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new DoWhileStatementParser(
            cursor("do while (value < 10);")
        ).parse()
    );
  }
}