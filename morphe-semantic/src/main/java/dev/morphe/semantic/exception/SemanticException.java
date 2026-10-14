package dev.morphe.semantic.exception;

/**
 * Base exception for errors detected during semantic analysis of a
 * Morphe program.
 *
 * <p>Semantic exceptions represent failures related to the meaning and
 * validity of an already parsed abstract syntax tree. They are distinct
 * from lexical, syntactic, and runtime errors.</p>
 */
public abstract class SemanticException extends RuntimeException {

  /**
   * Creates a semantic exception with the specified message.
   *
   * @param message the message describing the semantic error
   */
  public SemanticException(String message) {
    super(message);
  }
}