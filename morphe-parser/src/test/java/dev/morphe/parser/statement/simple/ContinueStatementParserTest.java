package dev.morphe.parser.statement.simple;

import dev.morphe.ast.statement.ContinueStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContinueStatementParserTest extends AParserTest {

  @Test
  void parsesContinueStatement() {
    IStatement statement = new ContinueStatementParser(
        cursor("continue;")
    ).parse();
    assertInstanceOf(ContinueStatement.class, statement);
  }

  @Test
  void requiresSemicolon() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new ContinueStatementParser(
            cursor("continue")
        ).parse()
    );
  }

  @Test
  void preservesSourceRange() {
    ContinueStatement statement = assertInstanceOf(
        ContinueStatement.class, new ContinueStatementParser(cursor("continue;")).parse()
    );
    assertEquals(0, statement.range().start().offset());
    assertEquals(9, statement.range().end().offset());
  }
}