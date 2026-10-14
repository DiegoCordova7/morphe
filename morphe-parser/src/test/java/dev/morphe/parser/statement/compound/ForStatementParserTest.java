package dev.morphe.parser.statement.compound;

import dev.morphe.ast.expression.BinaryExpression;
import dev.morphe.ast.expression.IExpression;
import dev.morphe.ast.expression.UpdateExpression;
import dev.morphe.ast.statement.*;
import dev.morphe.parser.AParserTest;
import dev.morphe.parser.exception.syntax.UnexpectedTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForStatementParserTest extends AParserTest {

  @Test
  void parsesCompleteForStatement() {
    ForStatement forStatement = new ForStatementParser(
        cursor("""
                for (Integer i = 0; i < 10; i++) {
                    return i;
                }
                """)
        ).parse();
    IStatement initialization = forStatement.initialization();
    assertInstanceOf(VariableDeclarationStatement.class, initialization);
    IExpression condition = forStatement.condition();
    assertInstanceOf(BinaryExpression.class, condition);
    IStatement update = forStatement.update();
    assertInstanceOf(UpdateExpression.class, ((ExpressionStatement) update).expression());
    BlockStatement body = forStatement.body();
    assertEquals(1, body.statements().size());
    assertInstanceOf(ReturnStatement.class, body.statements().get(0));
  }

  @Test
  void parsesForWithoutInitialization() {
    ForStatement forStatement = new ForStatementParser(
        cursor("""
                for (; i < 10; i++) {
                    return i;
                }
                """)
        ).parse();
    assertNull(forStatement.initialization());
    assertNotNull(forStatement.condition());
    assertNotNull(forStatement.update());
  }

  @Test
  void parsesForWithoutCondition() {
    ForStatement forStatement = new ForStatementParser(
        cursor("""
                for (Integer i = 0; ; i++) {
                    return i;
                }
                """)
        ).parse();
    assertNotNull(forStatement.initialization());
    assertNull(forStatement.condition());
    assertNotNull(forStatement.update());
  }

  @Test
  void parsesForWithoutUpdate() {
    ForStatement forStatement = new ForStatementParser(
        cursor("""
                for (Integer i = 0; i < 10; ) {
                    return i;
                }
                """)
        ).parse();
    assertNotNull(forStatement.initialization());
    assertNotNull(forStatement.condition());
    assertNull(forStatement.update());
  }

  @Test
  void parsesEmptyForHeader() {
    ForStatement forStatement = new ForStatementParser(
        cursor("""
                for (;;) {
                    return 0;
                }
                """)
        ).parse();
    assertNull(forStatement.initialization());
    assertNull(forStatement.condition());
    assertNull(forStatement.update());
    assertEquals(1, forStatement.body().statements().size());
  }

  @Test
  void parsesAssignmentInitialization() {
    ForStatement forStatement = new ForStatementParser(
        cursor("for (i = 0; i < 10; i++) { }")
    ).parse();
    assertInstanceOf(AssignmentStatement.class, forStatement.initialization());
  }

  @Test
  void parsesExpressionInitialization() {
    ForStatement forStatement = new ForStatementParser(
        cursor("for (initialize(); i < 10; i++) { }")
    ).parse();
    assertInstanceOf(
        ExpressionStatement.class, forStatement.initialization()
    );
  }

  @Test
  void parsesAssignmentUpdate() {
    ForStatement forStatement = new ForStatementParser(
        cursor("for (Integer i = 0; i < 10; i++) { }")
    ).parse();
    ExpressionStatement update = assertInstanceOf(
        ExpressionStatement.class, forStatement.update()
    );
    assertInstanceOf(UpdateExpression.class, update.expression());
  }

  @Test
  void parsesEmptyBody() {
    ForStatement forStatement = new ForStatementParser(
        cursor("for (;;) { }")
    ).parse();
    assertTrue(forStatement.body().statements().isEmpty());
  }

  @Test
  void preservesSourceRange() {
    String source = "for (Integer i = 0; i < 10; i++) { }";
    ForStatement forStatement = new ForStatementParser(cursor(source)).parse();
    assertEquals(0, forStatement.range().start().offset());
    assertEquals(source.length(), forStatement.range().end().offset());
  }

  @Test
  void requiresOpeningParenthesis() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new ForStatementParser(
            cursor("for Integer i = 0; i < 10; i++) { }")
        ).parse()
    );
  }

  @Test
  void requiresClosingParenthesis() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new ForStatementParser(
            cursor("for (Integer i = 0; i < 10; i++ { }")
        ).parse()
    );
  }

  @Test
  void requiresBodyBlock() {
    assertThrows(
        UnexpectedTokenException.class,
        () -> new ForStatementParser(
            cursor("for (;;) ")
        ).parse()
    );
  }
}