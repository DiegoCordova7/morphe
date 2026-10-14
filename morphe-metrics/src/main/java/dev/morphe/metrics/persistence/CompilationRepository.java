
package dev.morphe.metrics.persistence;

import dev.morphe.bytecode.constants.BooleanConstant;
import dev.morphe.bytecode.constants.DoubleConstant;
import dev.morphe.bytecode.constants.IConstant;
import dev.morphe.bytecode.constants.IntegerConstant;
import dev.morphe.bytecode.constants.StringConstant;
import dev.morphe.bytecode.instructions.Instruction;
import dev.morphe.bytecode.instructions.opcodes.IOpCode;
import dev.morphe.bytecode.program.AttemptRegion;
import dev.morphe.bytecode.program.BytecodeProgram;
import dev.morphe.bytecode.program.CaptureDefinition;
import dev.morphe.bytecode.program.ErrorHandler;
import dev.morphe.bytecode.program.FunctionDefinition;
import dev.morphe.bytecode.type.ArrayBytecodeType;
import dev.morphe.bytecode.type.CompositeBytecodeType;
import dev.morphe.bytecode.type.FunctionBytecodeType;
import dev.morphe.bytecode.type.IBytecodeType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

public class CompilationRepository {

  public long save(BytecodeProgram program) throws SQLException {
    Objects.requireNonNull(program, "program");

    try (Connection connection = DatabaseConnection.open()) {
      boolean previousAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try {
        long programId = insertProgram(connection, program);

        Map<IBytecodeType, Long> typeNodes =
            new IdentityHashMap<>();

        persistTypes(connection, programId, program, typeNodes);
        persistFunctions(connection, programId, program, typeNodes);
        persistConstants(connection, programId, program);
        persistInstructions(connection, programId, program);
        persistAttemptRegions(connection, programId, program);

        connection.commit();
        return programId;

      } catch (SQLException | RuntimeException exception) {
        try {
          connection.rollback();
        } catch (SQLException rollbackException) {
          exception.addSuppressed(rollbackException);
        }
        throw exception;

      } finally {
        connection.setAutoCommit(previousAutoCommit);
      }
    }
  }

  private long insertProgram(
      Connection connection,
      BytecodeProgram program
  ) throws SQLException {
    String sql = """
                INSERT INTO bytecode_program (entry_function_index)
                VALUES (?)
                RETURNING program_id
                """;

    try (PreparedStatement statement =
             connection.prepareStatement(sql)) {
      statement.setInt(1, program.entryFunction());

      try (ResultSet result = statement.executeQuery()) {
        if (!result.next()) {
          throw new SQLException(
              "PostgreSQL did not return the program ID."
          );
        }
        return result.getLong("program_id");
      }
    }
  }

  private void persistInstructions(
      Connection connection,
      long programId,
      BytecodeProgram program
  ) throws SQLException {
    String opcodeSql = """
                INSERT INTO dim_bytecode_opcode (
                    opcode_name, opcode_family, operand_count
                )
                VALUES (?, ?, ?)
                ON CONFLICT (opcode_name)
                DO UPDATE SET
                    opcode_family = EXCLUDED.opcode_family,
                    operand_count = EXCLUDED.operand_count
                RETURNING opcode_id
                """;

    String instructionSql = """
                INSERT INTO fact_bytecode_instruction (
                    program_id, instruction_index, opcode_id
                )
                VALUES (?, ?, ?)
                """;

    String operandSql = """
                INSERT INTO bytecode_instruction_operand (
                    program_id, instruction_index,
                    operand_position, operand_value
                )
                VALUES (?, ?, ?, ?)
                """;

    try (
        PreparedStatement opcodeStatement =
            connection.prepareStatement(opcodeSql);
        PreparedStatement instructionStatement =
            connection.prepareStatement(instructionSql);
        PreparedStatement operandStatement =
            connection.prepareStatement(operandSql)
    ) {
      for (int i = 0; i < program.instructionCount(); i++) {
        Instruction<? extends IOpCode> instruction =
            program.instructionAt(i);
        IOpCode opcode = instruction.opcode();

        opcodeStatement.setString(1, opcode.toString());
        opcodeStatement.setString(
            2, opcode.getClass().getSimpleName()
        );
        opcodeStatement.setInt(3, opcode.operandCount());

        long opcodeId;
        try (ResultSet result =
                 opcodeStatement.executeQuery()) {
          if (!result.next()) {
            throw new SQLException(
                "Could not retrieve opcode ID: " + opcode
            );
          }
          opcodeId = result.getLong("opcode_id");
        }

        instructionStatement.setLong(1, programId);
        instructionStatement.setInt(2, i);
        instructionStatement.setLong(3, opcodeId);
        instructionStatement.executeUpdate();

        int[] operands = instruction.operands();

        for (int j = 0; j < operands.length; j++) {
          operandStatement.setLong(1, programId);
          operandStatement.setInt(2, i);
          operandStatement.setInt(3, j);
          operandStatement.setInt(4, operands[j]);
          operandStatement.addBatch();
        }

        operandStatement.executeBatch();
      }
    }
  }

  private void persistConstants(
      Connection connection,
      long programId,
      BytecodeProgram program
  ) throws SQLException {
    String sql = """
                INSERT INTO bytecode_constant (
                    program_id, constant_index, constant_kind,
                    integer_value, double_value,
                    string_value, boolean_value
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

    try (PreparedStatement statement =
             connection.prepareStatement(sql)) {
      for (int i = 0; i < program.constantCount(); i++) {
        IConstant constant = program.constantAt(i);

        statement.setLong(1, programId);
        statement.setInt(2, i);
        statement.setNull(4, Types.INTEGER);
        statement.setNull(5, Types.DOUBLE);
        statement.setNull(6, Types.VARCHAR);
        statement.setNull(7, Types.BOOLEAN);

        if (constant instanceof IntegerConstant value) {
          statement.setString(3, "INTEGER");
          statement.setInt(4, value.value());

        } else if (constant instanceof DoubleConstant value) {
          statement.setString(3, "DOUBLE");
          statement.setDouble(5, value.value());

        } else if (constant instanceof StringConstant value) {
          statement.setString(3, "STRING");
          statement.setString(6, value.value());

        } else if (constant instanceof BooleanConstant value) {
          statement.setString(3, "BOOLEAN");
          statement.setBoolean(7, value.value());

        } else {
          throw new SQLException(
              "Unsupported bytecode constant: "
                  + constant.getClass().getName()
          );
        }

        statement.executeUpdate();
      }
    }
  }

  private void persistTypes(
      Connection connection,
      long programId,
      BytecodeProgram program,
      Map<IBytecodeType, Long> typeNodes
  ) throws SQLException {
    String sql = """
                INSERT INTO bytecode_type (
                    program_id, type_index, type_node_id
                )
                VALUES (?, ?, ?)
                """;

    try (PreparedStatement statement =
             connection.prepareStatement(sql)) {
      for (int i = 0; i < program.typeCount(); i++) {
        long nodeId = persistTypeNode(
            connection, program.typeAt(i), typeNodes
        );

        statement.setLong(1, programId);
        statement.setInt(2, i);
        statement.setLong(3, nodeId);
        statement.executeUpdate();
      }
    }
  }

  private long persistTypeNode(
      Connection connection,
      IBytecodeType type,
      Map<IBytecodeType, Long> typeNodes
  ) throws SQLException {
    Objects.requireNonNull(type, "type");

    Long existingId = typeNodes.get(type);
    if (existingId != null) {
      return existingId;
    }

    String typeName = type instanceof CompositeBytecodeType composite
        ? composite.name()
        : null;

    String sql = """
                INSERT INTO bytecode_type_node (type_kind, type_name)
                VALUES (?, ?)
                RETURNING type_node_id
                """;

    long nodeId;

    try (PreparedStatement statement =
             connection.prepareStatement(sql)) {
      statement.setString(1, type.kind().name());

      if (typeName == null) {
        statement.setNull(2, Types.VARCHAR);
      } else {
        statement.setString(2, typeName);
      }

      try (ResultSet result = statement.executeQuery()) {
        if (!result.next()) {
          throw new SQLException(
              "Could not retrieve type node ID."
          );
        }
        nodeId = result.getLong("type_node_id");
      }
    }

    // Register before traversing children to handle recursive graphs.
    typeNodes.put(type, nodeId);

    if (type instanceof ArrayBytecodeType array) {
      insertTypeComponent(
          connection, nodeId, 0, null, "ELEMENT",
          persistTypeNode(
              connection, array.elementType(), typeNodes
          )
      );

    } else if (type instanceof CompositeBytecodeType composite) {
      for (int i = 0; i < composite.fields().size(); i++) {
        CompositeBytecodeType.CompositeBytecodeField field =
            composite.fields().get(i);

        insertTypeComponent(
            connection, nodeId, i, field.name(), "FIELD",
            persistTypeNode(
                connection, field.type(), typeNodes
            )
        );
      }

    } else if (type instanceof FunctionBytecodeType function) {
      for (int i = 0; i < function.parameterTypes().size(); i++) {
        insertTypeComponent(
            connection, nodeId, i, null, "PARAMETER",
            persistTypeNode(
                connection,
                function.parameterTypes().get(i),
                typeNodes
            )
        );
      }

      insertTypeComponent(
          connection, nodeId, 0, null, "RETURN",
          persistTypeNode(
              connection, function.returnType(), typeNodes
          )
      );
    }

    return nodeId;
  }

  private void insertTypeComponent(
      Connection connection,
      long parentId,
      int componentIndex,
      String componentName,
      String componentRole,
      long childId
  ) throws SQLException {
    String sql = """
                INSERT INTO bytecode_type_component (
                    parent_type_node_id, component_index,
                    component_name, component_role, child_type_node_id
                )
                VALUES (?, ?, ?, ?, ?)
                """;

    try (PreparedStatement statement =
             connection.prepareStatement(sql)) {
      statement.setLong(1, parentId);
      statement.setInt(2, componentIndex);

      if (componentName == null) {
        statement.setNull(3, Types.VARCHAR);
      } else {
        statement.setString(3, componentName);
      }

      statement.setString(4, componentRole);
      statement.setLong(5, childId);
      statement.executeUpdate();
    }
  }

  private void persistFunctions(
      Connection connection,
      long programId,
      BytecodeProgram program,
      Map<IBytecodeType, Long> typeNodes
  ) throws SQLException {
    String functionSql = """
                INSERT INTO bytecode_function (
                    program_id, function_index, function_name,
                    entry_point, function_signature, function_type_node_id
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

    String captureSql = """
                INSERT INTO bytecode_function_capture (
                    program_id, function_index, capture_position,
                    source_kind, source_index
                )
                VALUES (?, ?, ?, ?, ?)
                """;

    try (
        PreparedStatement functionStatement =
            connection.prepareStatement(functionSql);
        PreparedStatement captureStatement =
            connection.prepareStatement(captureSql)
    ) {
      for (int i = 0; i < program.functionCount(); i++) {
        FunctionDefinition function = program.functionAt(i);

        long functionTypeId = persistTypeNode(
            connection, function.type(), typeNodes
        );

        functionStatement.setLong(1, programId);
        functionStatement.setInt(2, i);
        functionStatement.setString(3, function.name());
        functionStatement.setInt(4, function.entryPoint());
        functionStatement.setString(
            5, function.type().toString()
        );
        functionStatement.setLong(6, functionTypeId);
        functionStatement.executeUpdate();

        for (int j = 0; j < function.captureCount(); j++) {
          CaptureDefinition capture = function.captureAt(j);

          captureStatement.setLong(1, programId);
          captureStatement.setInt(2, i);
          captureStatement.setInt(3, j);
          captureStatement.setString(
              4, capture.sourceKind().name()
          );
          captureStatement.setInt(5, capture.index());
          captureStatement.executeUpdate();
        }
      }
    }
  }

  private void persistAttemptRegions(
      Connection connection,
      long programId,
      BytecodeProgram program
  ) throws SQLException {
    String regionSql = """
                INSERT INTO bytecode_attempt_region (
                    program_id, region_index, start_instruction,
                    end_instruction, fallback_instruction
                )
                VALUES (?, ?, ?, ?, ?)
                """;

    String handlerSql = """
                INSERT INTO bytecode_error_handler (
                    program_id, region_index, handler_position,
                    error_kind, entry_instruction
                )
                VALUES (?, ?, ?, ?, ?)
                """;

    try (
        PreparedStatement regionStatement =
            connection.prepareStatement(regionSql);
        PreparedStatement handlerStatement =
            connection.prepareStatement(handlerSql)
    ) {
      for (int i = 0; i < program.attemptRegionCount(); i++) {
        AttemptRegion region = program.attemptRegionAt(i);

        regionStatement.setLong(1, programId);
        regionStatement.setInt(2, i);
        regionStatement.setInt(3, region.start());
        regionStatement.setInt(4, region.end());
        regionStatement.setInt(5, region.fallback());
        regionStatement.executeUpdate();

        for (int j = 0; j < region.handlerCount(); j++) {
          ErrorHandler handler = region.handlerAt(j);

          handlerStatement.setLong(1, programId);
          handlerStatement.setInt(2, i);
          handlerStatement.setInt(3, j);
          handlerStatement.setString(
              4, handler.errorKind().name()
          );
          handlerStatement.setInt(5, handler.entryPoint());
          handlerStatement.executeUpdate();
        }
      }
    }
  }
}