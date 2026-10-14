package dev.morphe.parser.statement.simple;

import dev.morphe.ast.statement.BreakStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.cursor.TokenCursor;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BreakStatementParserTest extends AParserTest {

  @Test
  void parsesBreakStatement() {
    IStatement statement = new BreakStatementParser(
        cursor("break;")
    ).parse();
    assertInstanceOf(BreakStatement.class, statement);
  }

  @Test
  void requiresSemicolon() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new BreakStatementParser(
            cursor("break")
        ).parse()
    );
  }

  @Test
  void preservesSourceRange() {
    BreakStatement statement = assertInstanceOf(
        BreakStatement.class, new BreakStatementParser(cursor("break;")).parse()
    );
    assertEquals(0, statement.range().start().offset());
    assertEquals(6, statement.range().end().offset());
  }

  @Test
  void leavesFollowingTokenUnconsumed() {
    TokenCursor cursor = cursor("break; 42;");
    new BreakStatementParser(cursor).parse();
    assertEquals("42", cursor.current().lexeme());
  }
}