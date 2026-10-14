package dev.morphe.parser.statement.compound;

import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.IStatement;
import dev.morphe.ast.statement.IfStatement;
import dev.morphe.ast.statement.ReturnStatement;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IfStatementParserTest extends AParserTest {

  @Test
  void parsesIfStatement() {
    IStatement statement = new IfStatementParser(
        cursor("if (true) { return 1; }")
    ).parse();
    IfStatement ifStatement = assertInstanceOf(IfStatement.class, statement);
    LiteralExpression condition = assertInstanceOf(LiteralExpression.class, ifStatement.condition());
    assertEquals(true, condition.value());
    assertInstanceOf(BlockStatement.class, ifStatement.thenBranch());
    assertNull(ifStatement.elseBranch());
  }

  @Test
  void parsesIfStatementWithElse() {
    IStatement statement = new IfStatementParser(
        cursor("""
            if (true) {
                return 1;
            } else {
                return 2;
            }
            """)
    ).parse();
    IfStatement ifStatement = assertInstanceOf(IfStatement.class, statement);
    assertNotNull(ifStatement.thenBranch());
    assertNotNull(ifStatement.elseBranch());
  }

  @Test
  void parsesComplexCondition() {
    IStatement statement = new IfStatementParser(
        cursor("if (value > 10) { return value; }")
    ).parse();
    IfStatement ifStatement = assertInstanceOf(IfStatement.class, statement);
    assertNotNull(ifStatement.condition());
  }

  @Test
  void requiresOpeningParenthesis() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new IfStatementParser(
            cursor("if true { return 1; }")
        ).parse()
    );
  }

  @Test
  void requiresClosingParenthesis() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new IfStatementParser(
            cursor("if (true { return 1; }")
        ).parse()
    );
  }

  @Test
  void requiresThenBlock() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new IfStatementParser(
            cursor("if (true) return 1;")
        ).parse()
    );
  }

  @Test
  void requiresElseBlock() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new IfStatementParser(
            cursor("""
                if (true) {
                    return 1;
                } else return 2;
                """)
        ).parse()
    );
  }

  @Test
  void parsesIfStatementWithThenBlockStatements() {
    IStatement statement = new IfStatementParser(
        cursor("""
          if (true) {
              return 1;
          }
          """)
    ).parse();
    IfStatement ifStatement = assertInstanceOf(IfStatement.class, statement);
    BlockStatement thenBranch = ifStatement.thenBranch();
    assertEquals(1, thenBranch.statements().size());
    assertInstanceOf(ReturnStatement.class, thenBranch.statements().get(0));
  }

}