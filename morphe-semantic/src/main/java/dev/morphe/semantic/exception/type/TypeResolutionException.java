package dev.morphe.semantic.exception.type;

import dev.morphe.semantic.exception.SemanticException;

public abstract class TypeResolutionException extends SemanticException {

  protected TypeResolutionException(String message) {
    super(message);
  }
}