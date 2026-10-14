# Morphe Parser

The **Morphe Parser** transforms the token stream produced by the Lexer into a structured **Abstract Syntax Tree (AST)**.

It is responsible for recognizing Morphe's grammar, validating syntactic structure, preserving source ranges, and constructing AST nodes without performing semantic analysis.

```text
Source
  │
  ▼
Lexer
  │
  ▼
Tokens
  │
  ▼
Parser
  │
  ▼
AST
  │
  ▼
Semantic Analysis
```

The parser is intentionally independent from semantic concerns. It determines whether a sequence of tokens forms a valid syntactic structure; type compatibility, name resolution, and other language rules are handled by the Semantic module.

---

## Responsibilities

The Parser is responsible for:

- Parsing expressions and operators according to precedence.
- Parsing statements and control-flow constructs.
- Parsing declarations and assignments.
- Parsing function calls, lambdas, postfix operations, and other expression forms.
- Parsing pattern matching syntax.
- Parsing types used by declarations and other language constructs.
- Constructing the corresponding AST nodes.
- Preserving source ranges for parsed AST structures.
- Reporting syntactic errors when the token sequence does not match the grammar.

The Parser does **not**:

- Resolve identifiers.
- Infer or validate types.
- Validate variable or function declarations semantically.
- Check whether operations are type-compatible.
- Resolve composite types.
- Generate bytecode.

Those responsibilities belong to later compilation stages.

---

## Architecture

The parser is organized around small, composable parser components.

```text
Parser
│
├── Expression
│   ├── Logical Or
│   ├── Logical And
│   ├── Equality
│   ├── Relational
│   ├── Additive
│   ├── Multiplicative
│   ├── Unary
│   ├── Primary
│   └── Postfix
│   └── Attempt
│
├── Statement
│   ├── Simple
│   │   ├── Variable Declaration
│   │   ├── Assignment
│   │   ├── Expression
│   │   ├── Return
│   │   ├── Break
│   │   └── Continue
│   │
│   ├── Compound
│   │   ├── Block
│   │   ├── If
│   │   ├── While
│   │   ├── Do-While
│   │   └── For
│   │
│   └── Match
│       ├── Match Statement
│       └── Match Case
│
├── Pattern
│   ├── Literal
│   ├── Variable
│   ├── Default
│   ├── Range
│   ├── Or
│   ├── Array
│   ├── Composite
│   └── Field
│
└── Type
    ├── Primitive
    ├── Named
    ├── Error
    └── Array
```

Most parser components follow a common structure based on `AbstractParser` and `TokenCursor`.

This keeps each grammar rule isolated and allows larger constructs to be composed from smaller parsers.

---

## Token Cursor

`TokenCursor` provides controlled traversal over the token stream.

Parser components use it to:

- Inspect the current token.
- Look ahead without consuming tokens.
- Advance through the token stream.
- Match optional tokens.
- Detect the end of the source.
- Access the previous token when constructing source ranges.

The cursor also guarantees that the parser operates over a stable token sequence and that the token stream contains an EOF marker.

---

## Expressions

Expression parsing is implemented as a precedence hierarchy.

```text
LogicalOr
    ↓
LogicalAnd
    ↓
Equality
    ↓
Relational
    ↓
Additive
    ↓
Multiplicative
    ↓
Unary
    ↓
Primary
    ↓
Postfix
```

This structure ensures that operators are grouped according to Morphe's precedence rules.

For example:

```morphe
a + b * c
```

is parsed so that multiplication binds more strongly than addition.

The expression parser is intentionally independent from statement terminators. `ExpressionParser` parses an expression but does not consume a trailing semicolon. Statement-level parsers consume semicolons when required by their grammar.

---

## Statements

Statements are dispatched through `StatementParser` and divided into simple, compound, and match-related constructs.

### Simple Statements

Morphe supports:

```morphe
Integer value;

Integer result = calculate();

value = 10;
value += 5;

print(value);

return value;

break;

continue;
```

Assignment operators include:

```text
=
+=
-=
*=
/=
%=
```

Assignment is represented as a statement rather than an expression.

### Compound Statements

The parser supports block-based control flow:

```morphe
if (condition) {
    statement();
} else {
    statement();
}
```

```morphe
while (condition) {
    statement();
}
```

```morphe
do {
    statement();
} while (condition);
```

```morphe
for (initialization; condition; update) {
    statement();
}
```

Blocks are represented by BlockStatement and contain an ordered list of statements.

### Attempt

Morphe provides `attempt` as a structured error-handling construct.

An attempt consists of:

- An attempt body.
- A fallback expression.
- An error handler.
- An optional side-effect statement executed when the handler is triggered.

Example:

```morphe
attempt {
    divide(a, b)
} => 0
onError(error) => print(error);
```

The parser represents the construct as an AttemptExpression, preserving the attempt body, fallback expression, and error handler for later semantic analysis and execution.

---

## Match Statements

Morphe supports structural pattern matching through `match`.

A match case may contain either a single statement or a block:

```morphe
match (value) {
    1 => print(value);
    _ => print("other");
}
```

A block can also be used:

```morphe
match (value) {
    1 => {
        print(value);
        return;
    }
}
```

The parser separates match statement parsing from pattern parsing, allowing the pattern grammar to evolve independently.

---

## Patterns

Pattern parsing is organized as a hierarchy:

```text
Pattern
  ↓
Or
  ↓
Range
  ↓
Primary
```

Primary patterns include:

- Literal patterns.
- Variable patterns.
- Default (`_`) patterns.
- Array patterns.
- Composite patterns.

### Literal Patterns

```morphe
1
3.14
true
"hello"
```

### Variable Patterns

```morphe
value
```

### Default Patterns

```morphe
_
```

### Range Patterns

```morphe
1..10
```

### Alternative Patterns

```morphe
1 | 2 | 3
```

Patterns can be combined:

```morphe
1..10 | 20 | 30..40
```

### Array Patterns

```morphe
[]
```

```morphe
[1, 2, 3]
```

```morphe
[head, ...tail]
```

Nested array patterns are also supported:

```morphe
[[1, 2], [3, 4]]
```

### Composite Patterns

Composite patterns identify a composite type and optionally provide patterns for selected fields:

```morphe
Person {
    name: "Diego",
    age: 25
}
```

Fields can contain complete patterns:

```morphe
Person {
    age: 18..64
}
```

This syntax is parsed structurally. Resolution of the referenced composite type and validation of its fields are performed during semantic analysis.

---

## Types

`TypeParser` handles the syntactic representation of Morphe types.

Supported forms include:

```text
Integer
Double
Boolean
String
Void
```

Named types:

```text
Person
City
```

Arrays:

```text
Integer[]
String[]
Person[][]
```

Error types used by the language's error-handling syntax are also recognized by the parser.

The parser only constructs the corresponding AST type representation. Type compatibility and resolution are handled by Semantic Analysis.

---

## Source Ranges

Parsed AST nodes preserve their corresponding source locations through `SourceRange`.

For example:

```morphe
Person {
    name: "Diego"
}
```

produces a composite pattern whose range covers the complete syntactic construct.

Source ranges are used by later stages for diagnostics and source-level tooling.

---

## Syntax Errors

The parser reports malformed token sequences through parser-specific exceptions.

Examples include:

```morphe
Person {
    name: "Diego"
```

Missing closing delimiters, incomplete expressions, invalid separators, and other malformed constructs are rejected during parsing.

Parser errors are concerned with **syntax**, not semantic validity.

For example, whether:

```morphe
Person {
    age: "invalid"
}
```

is type-compatible is not determined by the Parser.

---

## Parser Composition

Parser components are intentionally small and composable.

For example, parsing a composite pattern can be represented as:

```text
CompositePatternParser
        │
        ▼
FieldPatternParser
        │
        ▼
PatternParser
        │
        ▼
OrPatternParser
        │
        ▼
RangePatternParser
        │
        ▼
PrimaryPatternParser
```

This design allows individual grammar rules to be tested independently while still supporting complete language constructs.

Functional parser interfaces are used where appropriate, allowing parser components to be treated as composable parsing strategies.

---

## Testing

The Parser module is tested at the parser-component level as well as through higher-level grammar constructs.

Tests cover:

- Valid syntax.
- Invalid syntax.
- Operator precedence.
- Nested constructs.
- Source ranges.
- Token consumption.
- Alternative and range patterns.
- Array and composite patterns.
- Required delimiters and separators.
- Optional grammar components.

The test suite is organized to mirror the parser architecture, making failures easier to associate with a specific grammar rule.

Representative parser tests include:

```text
ExpressionParserTest
StatementParserTest
PatternParserTest
ArrayPatternParserTest
CompositePatternParserTest
...
```

The goal is to ensure that each parser validates the syntax for which it is responsible without duplicating validation performed by delegated parsers.

---

## Error Boundary

The Parser is one stage in the larger Morphe pipeline:

```text
Source
  │
  ▼
Lexer
  │
  │ lexical errors
  ▼
Tokens
  │
  ▼
Parser
  │
  │ syntax errors
  ▼
AST
  │
  ▼
Semantic
  │
  │ semantic errors
  ▼
Compiler
  │
  ▼
Bytecode
```

This separation keeps errors associated with the stage that can correctly identify their cause.

---

## Module Structure

The Parser module is organized approximately as follows:

```text
parser/
├── cursor/
├── expression/
├── pattern/
├── source/
├── statement/
├── type/
└── exception/
```

The exact internal organization may evolve as the grammar grows, while the parser's external responsibility remains the same: transform valid token sequences into AST structures.

---

## Design Principles

The Parser follows several design principles:

### Single Responsibility

Each parser handles a specific grammar construct instead of implementing the entire language grammar in one class.

### Composition

Higher-level parsers delegate nested syntax to lower-level parsers.

### AST Independence

Parser implementations construct AST nodes but do not embed execution or semantic behavior into them.

### Explicit Error Boundaries

Syntax errors are reported during parsing, while semantic errors are deferred to the Semantic stage.

### Source Preservation

Parsed structures retain their source ranges so later stages can provide meaningful diagnostics.

### Testability

Small parser components allow grammar rules to be tested independently and composed into higher-level integration tests.

---

## Status

The Parser module is currently in the **stable implementation and verification stage**.

The parser grammar has been implemented across expressions, statements, patterns, and types, with individual parser components being documented and tested before moving to semantic analysis.

The next major stage in the Morphe pipeline is:

```text
Parser
  ↓
Semantic
  ↓
Compiler
  ↓
Bytecode
  ↓
VM
```

---

## Related Modules

| Module | Responsibility |
|---|---|
| **Lexer** | Converts source text into tokens. |
| **AST** | Defines the abstract syntax tree structures. |
| **Parser** | Converts tokens into AST nodes. |
| **Semantic** | Validates and resolves semantic rules. |
| **Compiler** | Converts validated AST structures into bytecode. |
| **Bytecode** | Defines the bytecode representation. |
| **Runtime** | Provides runtime values and execution data. |
| **VM** | Executes Morphe bytecode. |
| **Engine** | Coordinates the language execution pipeline. |
| **API** | Exposes Morphe execution to external consumers. |

---

## License

Morphe is licensed under the Apache License 2.0.