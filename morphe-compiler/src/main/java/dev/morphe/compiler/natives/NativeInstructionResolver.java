package dev.morphe.compiler.natives;

import dev.morphe.bytecode.instructions.opcodes.IOpCode;

import static dev.morphe.bytecode.instructions.opcodes.IOOpCode.*;

public final class NativeInstructionResolver {

  public IOpCode resolve(String name) {
    return switch (name) {
      case "print" -> PRINT;
      case "readInteger" -> INPUT_INTEGER;
      case "readDouble" -> INPUT_DOUBLE;
      case "readString" -> INPUT_STRING;
      case "readBoolean" -> INPUT_BOOLEAN;
      default -> throw new IllegalArgumentException("Unknown native function: " + name);
    };
  }
}