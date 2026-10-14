package dev.morphe.vm.exception.internal.composite;

import dev.morphe.vm.exception.internal.VMInternalException;

public final class InvalidCompositeReferenceException extends VMInternalException {

  public InvalidCompositeReferenceException(int address) {
    super("Reference at heap address " + address + " does not point to a composite value");
  }
}