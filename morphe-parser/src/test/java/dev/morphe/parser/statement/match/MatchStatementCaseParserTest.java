package dev.morphe.parser.statement.match;

import dev.morphe.ast.pattern.IPattern;
import dev.morphe.ast.pattern.LiteralPattern;
import dev.morphe.ast.statement.BlockStatement;
import dev.morphe.ast.statement.ExpressionStatement;
import dev.morphe.ast.statement.MatchStatement;
import dev.morphe.ast.statement.ReturnStatement;
import dev.morphe.parser.AParserTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchStatementCaseParserTest extends AParserTest {

  @Test
  void parsesLiteralPatternWithStatement() {
    MatchStatement.MatchCase matchCase =
        new MatchStatementCaseParser(
            cursor("""
                1 => print(value);
                """)
        ).parse();
    IPattern pattern = matchCase.pattern();
    assertInstanceOf(LiteralPattern.class, pattern);
    assertInstanceOf(ExpressionStatement.class, matchCase.body());
  }

  @Test
  void parsesLiteralPatternWithBlock() {
    MatchStatement.MatchCase matchCase =
        new MatchStatementCaseParser(
            cursor("""
                1 => {
                    return 10;
                }
                """)
        ).parse();
    assertInstanceOf(LiteralPattern.class, matchCase.pattern());
    BlockStatement body = assertInstanceOf(BlockStatement.class, matchCase.body());
    assertEquals(1, body.statements().size());
    assertInstanceOf(ReturnStatement.class, body.statements().get(0));
  }

  @Test
  void parsesOrPatternWithStatement() {
    MatchStatement.MatchCase matchCase =
        new MatchStatementCaseParser(
            cursor("""
                1 | 2 => print(value);
                """)
        ).parse();
    assertNotNull(matchCase.pattern());
    assertInstanceOf(ExpressionStatement.class, matchCase.body());
  }

  @Test
  void parsesOrPatternWithBlock() {
    MatchStatement.MatchCase matchCase =
        new MatchStatementCaseParser(
            cursor("""
                1 | 2 => {
                    return 10;
                }
                """)
        ).parse();
    assertNotNull(matchCase.pattern());
    BlockStatement body = assertInstanceOf(BlockStatement.class, matchCase.body());
    assertEquals(1, body.statements().size());
    assertInstanceOf(ReturnStatement.class, body.statements().get(0));
  }
}