package dev.morphe.semantic.visitors.statement;

import dev.morphe.ast.expression.LiteralExpression;
import dev.morphe.ast.source.SourcePosition;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.VariableDeclarationStatement;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;
import dev.morphe.semantic.visitors.SemanticExpressionVisitor;
import dev.morphe.semantic.visitors.context.SemanticContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VariableDeclarationStatementAnalyzerTest {

  @Test
  void definesVariableSymbol() {
    SemanticContext context = new SemanticContext();

    VariableDeclarationStatement statement =
        new VariableDeclarationStatement(
            new SourceRange(new SourcePosition(1, 1, 1), new SourcePosition(1, 1, 2)),
            "x",
            PrimitiveType.INTEGER,
            false,
            null
        );

    new VariableDeclarationStatementAnalyzer()
        .analyze(statement, context, new SemanticExpressionVisitor());

    Symbol symbol = context.symbol(statement);

    assertEquals("x", symbol.name());
    assertEquals(SymbolKind.VARIABLE, symbol.kind());
    assertEquals(PrimitiveType.INTEGER, symbol.type());
  }

}