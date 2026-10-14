package morphe;

import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.engine.Engine;
import dev.morphe.engine.EngineResult;
import dev.morphe.vm.state.VMExecutionState;

public final class Main {

  public static void main(String[] args) {

    ConsoleHandler handler = new ConsoleHandler();
    Engine engine = new Engine(
        """
        type Ciudad {
          nombre : String;
        }
        type Persona {
          nombre : String;
          edad : Integer;
          ciudad : Ciudad;
        }
        func main() : Void {
          print("Dame tu nombre: ");
          String nombre = readString();
          Persona persona = new Persona {
            nombre = nombre;
            edad = 21;
            ciudad = new Ciudad {
              nombre = "Moctezuma";
            };
          };
          print(persona.nombre + " de " + persona.ciudad.nombre);
        }        type Ciudad {
          nombre : String;
        }
        type Persona {
          nombre : String;
          edad : Integer;
          ciudad : Ciudad;
        }
        func main() : Void {
          print("Dame tu nombre: ");
          String nombre = readString();
          Persona persona = new Persona {
            nombre = nombre;
            edad = 21;
            ciudad = new Ciudad {
              nombre = "Moctezuma";
            };
          };
          print(persona.nombre + " de " + persona.ciudad.nombre);
        }
        """,
        handler
    );

    EngineResult<BytecodeProgram> compilation = engine.compile();

    if (!compilation.isSuccess()) {
      System.err.println(compilation.error().getMessage());
      return;
    }

    EngineResult<Void> execution = engine.execute();

    while (execution.isSuccess() && engine.state() == VMExecutionState.WAITING_FOR_INPUT) {
      String input = handler.input();
      execution = engine.provideInput(input);
    }

    if (!execution.isSuccess()) {
      System.err.println(execution.error().getMessage());
    }
  }
}