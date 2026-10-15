package dev.morphe.engine;

import dev.morphe.ast.CompilationUnit;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.compiler.Compiler;
import dev.morphe.engine.listener.ICompilationListener;
import dev.morphe.lexer.Lexer;
import dev.morphe.lexer.exception.LexerException;
import dev.morphe.lexer.token.Token;
import dev.morphe.parser.Parser;
import dev.morphe.parser.exception.ParserException;
import dev.morphe.semantic.SemanticAnalysis;
import dev.morphe.semantic.exception.SemanticException;
import dev.morphe.semantic.visitors.context.SemanticContext;
import dev.morphe.vm.VirtualMachine;
import dev.morphe.vm.exception.VMException;
import dev.morphe.vm.state.VMExecutionState;

import java.util.List;

/**
 * Provides the high-level entry point for compiling and executing
 * Morphe source code.
 *
 * <p>The engine coordinates the complete Morphe pipeline:
 *
 * <pre>
 * Source
 *   → Lexer
 *   → Parser
 *   → Semantic Analysis
 *   → Compiler
 *   → Bytecode
 *   → Virtual Machine
 * </pre>
 *
 * <p>The engine does not implement lexical, syntactic, semantic,
 * compilation, or execution rules itself. It only coordinates the
 * corresponding components.
 */
public final class Engine {

  private static ICompilationListener compilationListener;

  public static void setCompilationListener(ICompilationListener listener) {
    compilationListener = listener;
  }

  private final String code;
  private BytecodeProgram program;
  private final VirtualMachine vm;

  /**
   * Creates an engine for the specified Morphe source code.
   *
   * @param code the source code to process
   * @param outputHandler the handler used to produce execution output
   */
  public Engine(String code, IOutputHandler outputHandler) {
    if (code == null) throw new IllegalArgumentException("code cannot be null");
    if (outputHandler == null) throw new IllegalArgumentException("outputHandler cannot be null");
    this.code = code;
    this.vm = new VirtualMachine(new VMIOAdapter(outputHandler));
  }

  /**
   * Compiles the source code into a bytecode program.
   *
   * @return the result of the compilation
   */
  public EngineResult<BytecodeProgram> compile() {
    try {
      Lexer lexer = new Lexer(code);
      List<Token> tokens = lexer.tokenize();
      Parser parser = new Parser(tokens);
      CompilationUnit compilationUnit = parser.parse();
      SemanticContext semanticContext = new SemanticAnalysis().analyze(compilationUnit);
      Compiler compiler = new Compiler(compilationUnit, semanticContext);
      this.program = compiler.compile();
      if (compilationListener != null) try {
        compilationListener.onCompilationSuccess(program);
      } catch (Exception exception) {
        System.err.println("[Metrics Warning] Error registering metric: " + exception.getMessage());
      }
      return EngineResult.success(program);
    } catch (LexerException exception) {
      return EngineResult.error(EnginePhase.LEXING, exception);
    } catch (ParserException exception) {
      return EngineResult.error(EnginePhase.PARSING, exception);
    } catch (SemanticException exception) {
      return EngineResult.error(EnginePhase.SEMANTIC_ANALYSIS, exception);
    }
  }

  public EngineResult<Void> execute() {
    if (program == null)
      return EngineResult.error(EnginePhase.EXECUTION, new IllegalStateException("No compiled program available"));
    try {
      vm.execute(program);
      return EngineResult.success(null);
    } catch (VMException exception) {
      return EngineResult.error(EnginePhase.EXECUTION, exception);
    }
  }

  public EngineResult<Void> provideInput(String input) {
    try {
      vm.provideInput(input);
      return EngineResult.success(null);
    } catch (VMException exception) {
      return EngineResult.error(EnginePhase.EXECUTION, exception);
    }
  }

  public VMExecutionState state() {
    return vm.state();
  }
}