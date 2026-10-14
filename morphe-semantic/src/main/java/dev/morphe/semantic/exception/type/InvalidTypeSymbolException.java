package dev.morphe.semantic.exception.type;

public final class InvalidTypeSymbolException extends TypeResolutionException {

  public InvalidTypeSymbolException(String name) {
    super("Symbol '" + name + "' is not a type");
  }
}