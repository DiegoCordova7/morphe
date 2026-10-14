package dev.morphe.parser.statement.match;

import dev.morphe.ast.expression.VariableExpression;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.ExpressionStatement;
import dev.morphe.ast.statement.MatchStatement;
import dev.morphe.ast.statement.ReturnStatement;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchStatementParserTest extends AParserTest {

  @Test
  void parsesMatchStatement() {
    MatchStatement match = assertInstanceOf(
        MatchStatement.class,
        new MatchStatementParser(
            cursor("""
                match (value) {
                    1 => print(value);
                    2 => {
                        return 20;
                    }
                }
                """)
        ).parse()
    );
    VariableExpression value = assertInstanceOf(VariableExpression.class, match.value());
    assertEquals("value", value.name());
    assertEquals(2, match.cases().size());
    MatchStatement.MatchCase first = match.cases().get(0);
    MatchStatement.MatchCase second = match.cases().get(1);
    assertInstanceOf(ExpressionStatement.class, first.body());
    BlockStatement secondBody = assertInstanceOf(BlockStatement.class, second.body());
    assertEquals(1, secondBody.statements().size());
    assertInstanceOf(ReturnStatement.class, secondBody.statements().get(0));
  }
}