package dev.morphe.lexer.exception;

/**
 * Base exception for errors produced by the Morphe lexer.
 *
 * <p>Lexer exceptions represent failures encountered while processing
 * source code or while operating on lexer components.</p>
 */
public abstract class LexerException extends RuntimeException {

  /**
   * Creates a lexer exception.
   *
   * @param message the exception message format
   * @param args the arguments used to format the message
   */
  protected LexerException(String message, Object... args) {
    super(String.format(message, args));
  }

  /**
   * Creates a lexer exception with an underlying cause.
   *
   * @param cause the underlying cause of this exception
   * @param message the exception message format
   * @param args the arguments used to format the message
   */
  protected LexerException(Throwable cause, String message, Object... args) {
    super(String.format(message, args), cause);
  }
}

