package dev.morphe.semantic.exception.type;

public final class UnknownTypeException extends TypeResolutionException {

  public UnknownTypeException(String name) {
    super("Unknown type '" + name + "'");
  }
}