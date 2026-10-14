package morphe;

import dev.morphe.engine.IInputHandler;
import dev.morphe.engine.IOutputHandler;

import java.util.Scanner;

public final class ConsoleHandler implements IInputHandler, IOutputHandler {

  private final Scanner scanner;

  public ConsoleHandler() {
    this.scanner = new Scanner(System.in);
  }

  @Override
  public void print(String text) {
    System.out.println(text);
  }

  @Override
  public String input() {
    return scanner.nextLine();
  }
}