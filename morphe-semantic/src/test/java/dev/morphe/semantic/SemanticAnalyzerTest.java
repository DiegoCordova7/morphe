package dev.morphe.semantic;

import dev.morphe.ast.source.SourcePosition;
import dev.morphe.ast.source.SourceRange;
import dev.morphe.ast.statement.VariableDeclarationStatement;
import dev.morphe.ast.type.PrimitiveType;
import dev.morphe.semantic.symbol.Symbol;
import dev.morphe.semantic.symbol.SymbolKind;
import dev.morphe.semantic.visitors.context.SemanticContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SemanticAnalyzerTest {

  @Test
  void analyzesVariableDeclaration() {
    SemanticContext context = new SemanticContext();

    SourcePosition position = new SourcePosition(0, 1, 1);
    SourceRange range = new SourceRange(position, position);

    VariableDeclarationStatement statement =
        new VariableDeclarationStatement(
            range,
            "x",
            PrimitiveType.INTEGER,
            false,
            null
        );

    SemanticAnalyzer analyzer = new SemanticAnalyzer();

    analyzer.visit(statement, context);

    Symbol symbol = context.symbol(statement);

    assertEquals("x", symbol.name());
    assertEquals(SymbolKind.VARIABLE, symbol.kind());
    assertEquals(PrimitiveType.INTEGER, symbol.type());
  }
}