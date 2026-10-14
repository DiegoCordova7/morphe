# Morphe AST

## Overview

The Morphe AST (Abstract Syntax Tree) is the structural representation of a Morphe source file produced by the parser.

It models the syntactic structure of the language through immutable nodes representing declarations, expressions, patterns, statements, types, operators, and source locations. The AST is rooted at a `CompilationUnit`, which represents the complete source file and contains its top-level declarations.

Each node describes the structure of the source program without performing semantic analysis, execution, code generation, or runtime evaluation.

The AST acts as the boundary between parsing and the later stages of the language implementation. The parser produces AST nodes from the token stream, while subsequent stages can traverse and analyze those nodes to determine semantic validity and eventually generate bytecode.

The AST is intentionally independent from the Morphe runtime and bytecode representations. Runtime values, execution behavior, and bytecode instructions belong to their respective layers and are resolved only after the AST has been analyzed.

---

## Architecture

The Morphe AST is designed as a structural and immutable intermediate representation between parsing and the later stages of the language implementation.

The parser constructs a `CompilationUnit` as the root of the AST and populates it with the top-level declarations found in the source file. These declarations contain the statements, expressions, patterns, and types that make up the structure of the program.

The AST does not perform semantic analysis, execution, or code generation. Instead, it provides a stable representation that can be traversed by later stages such as semantic analysis and compilation.

The AST remains independent from the runtime and bytecode layers. Semantic analysis operates on AST nodes, while the compiler later transforms the validated AST into bytecode.

---

### Responsibilities

The AST is responsible for representing the syntactic structure of a Morphe source file in a form that can be traversed, inspected, and analyzed independently from the source tokens.

Its responsibilities include:

* Representing the `CompilationUnit` and its top-level declarations.
* Representing declarations, expressions, patterns, statements, and types.
* Preserving source locations through `SourceRange`.
* Preserving the structural relationships between syntactic constructs.
* Providing a uniform traversal mechanism through visitors.
* Enforcing structural invariants required by AST nodes.
* Providing an immutable representation that can safely be shared between compiler stages.

The AST does not perform semantic validation, execute programs, evaluate runtime values, resolve runtime representations, or emit bytecode. Those responsibilities belong to later stages of the language implementation.

Compilation Pipeline

The AST sits between parsing and semantic processing in the Morphe compilation pipeline:

```text
Source Code
↓
Lexer
↓
Tokens
↓
Parser
↓
CompilationUnit / AST
↓
Semantic Analysis
↓
Compiler
↓
Bytecode
↓
Morphe VM
```

The lexer converts source text into tokens, and the parser uses those tokens to construct the corresponding AST, rooted at a CompilationUnit.

Once constructed, the AST can be traversed by semantic analysis to resolve names, validate types, verify scopes, and enforce language rules that cannot be determined from syntax alone.

After semantic analysis, the validated AST can be transformed into bytecode by the compiler. The resulting bytecode is then executed by the Morphe VM, which provides the runtime state and execution environment required by the program.

The AST therefore acts as a boundary between the syntactic representation of a source file and its semantic and executable representations.

---

### Design Principles

The AST follows several principles that guide its structure:

* **Immutability** — AST nodes are immutable after construction. Collections exposed by nodes are immutable and cannot be modified by consumers.
* **Structural responsibility** — AST nodes enforce structural invariants such as required arguments, valid structural relationships, and non-null child nodes where required. Semantic validity remains outside the AST.
* **Semantic separation** — Semantic rules such as name resolution, type compatibility, scope validation, and symbol resolution are handled outside the AST.
* **Layer independence** — AST types and nodes are independent from runtime values and bytecode representations.
* **Composition** — Complex language constructs are represented through smaller reusable nodes. Declarations, statements, patterns, expressions, and other AST elements can be recursively composed where the language structure requires it.
* **Source awareness** — Nodes preserve their corresponding source ranges to support diagnostics and later compiler stages.
* **Visitor-based traversal** — Visitors provide extensible operations over AST nodes without coupling those operations directly to the node implementations.
* **Explicit representation** — Constructs that have distinct syntactic or semantic meaning are represented by dedicated AST nodes rather than being encoded indirectly through unrelated nodes.

---

## Package Structure

The AST module is organized by the syntactic and structural role of each node:

```text
dev.morphe.ast
├── IASTNode
├── CompilationUnit
│
├── declaration
├── exception
│   └── argument
├── expression
├── operator
├── pattern
├── source
├── statement
├── type
└── visitor
```

### `declaration`

Contains nodes that represent top-level declarations in a Morphe source file.

```text
declaration
├── IDeclaration
├── FunctionDeclaration
├── CompositeDeclaration
└── Parameter
```

`FunctionDeclaration` represents a function declared in the source program, while `CompositeDeclaration` represents a composite type declaration.

`Parameter` represents a function parameter and is associated with function declarations.

### `expression`

Contains expressions that produce values or represent value-producing operations.

```text
expression
├── IExpression
├── LiteralExpression
├── VariableExpression
├── BinaryExpression
├── UnaryExpression
├── UpdateExpression
├── ArrayAccessExpression
├── ArrayCreationExpression
├── ArrayLiteralExpression
├── FunctionCallExpression
├── LambdaExpression
├── CompositeInstanceExpression
├── AttemptExpression
├── IfExpression
└── MatchExpression
```

Expressions can be recursively composed to represent nested operations and other value-producing constructs.

### `pattern`

Contains patterns used by pattern matching constructs.

```text
pattern
├── IPattern
├── DefaultPattern
├── LiteralPattern
├── VariablePattern
├── OrPattern
├── RangePattern
├── ArrayPattern
└── CompositePattern
```

Patterns are compositional. `ArrayPattern` can contain arbitrary `IPattern` instances, while `CompositePattern` contains field patterns whose values are themselves represented by `IPattern` instances.

### `statement`

Contains statements that represent executable actions and control flow within statement blocks.

```text
statement
├── IStatement
├── ExpressionStatement
├── VariableDeclarationStatement
├── AssignmentStatement
├── ArrayAssignmentStatement
├── BlockStatement
├── IfStatement
├── MatchStatement
├── WhileStatement
├── DoWhileStatement
├── ForStatement
├── BreakStatement
├── ContinueStatement
└── ReturnStatement
```

Statements are contained within `BlockStatement` nodes and form the executable structure of function bodies and other block-based constructs.

### `type`

Contains the type representations used by the AST.

```text
type
├── IASTType
├── ASTTypeKind
├── PrimitiveType
├── FunctionType
└── CompositeType
```

AST types describe the types present in the source program and are independent from runtime and bytecode type representations.

### `operator`

Contains the operator definitions referenced by expression and statement nodes.

```text
operator
├── AssignmentOperator
├── BinaryOperator
├── UnaryOperator
└── UpdateOperator
```

Operators describe the syntactic operations represented by the corresponding AST nodes.

### `visitor`

Contains visitor interfaces used to traverse different AST node categories.

```text
visitor
├── IASTVisitor
├── IDeclarationVisitor
├── IExpressionVisitor
├── IPatternVisitor
└── IStatementVisitor
```

Visitors provide extensible operations over AST nodes without requiring those operations to be implemented directly by every node.

### `source`

Contains source-location structures shared by AST nodes.

```text
source
├── SourcePosition
└── SourceRange
```

`SourcePosition` identifies a location within the source, while `SourceRange` represents the portion of source code occupied by an AST node.

### `exception`

Contains exceptions related to AST construction and argument validation.

```text
exception
├── ASTException
└── argument
    ├── ArgumentException
    ├── InvalidArgumentException
    └── NullArgumentException
```

These exceptions are primarily used to enforce structural invariants when constructing AST nodes.

---

## Compilation Unit

`CompilationUnit` represents the root node of a Morphe abstract syntax tree
and corresponds to a complete Morphe source file.

A compilation unit contains the top-level declarations found in the source
file. These declarations are represented through the `IDeclaration` interface
and may be function or composite declarations.

```text
CompilationUnit
└── declarations
    ├── FunctionDeclaration
    ├── CompositeDeclaration
    └── ...
```

The compilation unit also preserves the complete `SourceRange` occupied by
the source file represented by the AST.

```text
CompilationUnit
├── declarations
└── range
```

The `CompilationUnit` does not contain statements directly. Executable
statements are contained within `BlockStatement` nodes, which are typically
associated with function bodies.

For example:

```text
CompilationUnit
└── FunctionDeclaration
    ├── name
    ├── parameters
    ├── returnType
    └── body
        └── BlockStatement
            └── statements
                ├── VariableDeclarationStatement
                └── ReturnStatement
```

This structure separates top-level declarations from executable statements
and provides a clear boundary between the source-file structure and the
contents of individual declarations.

The compilation unit is produced by the parser and serves as the root input
for subsequent AST processing, including semantic analysis and compilation.


---

## Declarations

Declarations represent top-level program constructs that introduce named
entities into the structure of a Morphe source file.

The AST currently supports function and composite declarations:

```text
IDeclaration
├── FunctionDeclaration
└── CompositeDeclaration
```

Declarations are contained by the `CompilationUnit` and form the top-level
structure of a Morphe source file.

### Function Declarations

`FunctionDeclaration` represents the declaration of a function, including
its name, parameters, return type, and body.

```text
FunctionDeclaration
├── name
├── parameters
├── returnType
└── body
```

Function parameters are represented by the shared `Parameter` node. The
function return type is represented through `IASTType`, while the body is a
`BlockStatement` containing the statements executed by the function.

For example:

```text
func Integer add(Integer a, Integer b) {
    return a + b;
}
```

The declaration describes the structure of the function but does not
represent its execution. Function resolution, type validation, scope
handling, and code generation are performed by later compiler stages.

### Composite Declarations

`CompositeDeclaration` represents the declaration of a composite type and
the fields that make up its structure.

```text
CompositeDeclaration
├── name
└── fields
```

Each field is represented by `CompositeType.CompositeField`, associating a
field name with an `IASTType`.

For example:

```text
composite Person {
    String name;
    Integer age;
}
```

The declaration introduces the composite structure at the source level.
`CompositeType` represents the corresponding AST type and remains separate
from the declaration itself.

Composite declarations do not impose object-oriented visibility modifiers
such as `public`, `protected`, or `private`. Access and composition rules
are determined by Morphe's language semantics rather than by Java-style
object encapsulation.

---

## Expressions

Expressions represent value-producing constructs in the Morphe AST. An
expression can be used as part of another expression or within AST constructs
that accept expressions, allowing complex operations to be composed
recursively.

The expression hierarchy is rooted at `IExpression`:

```text
IExpression
├── LiteralExpression
├── VariableExpression
├── BinaryExpression
├── UnaryExpression
├── UpdateExpression
├── ArrayAccessExpression
├── ArrayCreationExpression
├── ArrayLiteralExpression
├── FunctionCallExpression
├── LambdaExpression
├── CompositeInstanceExpression
├── AttemptExpression
├── IfExpression
└── MatchExpression
```

### Values and References

`LiteralExpression` represents literal values written directly in the source
program, while `VariableExpression` represents references to named values.

```text
42
```

```text
value
```

These expressions form the basic building blocks for more complex
expressions.

### Operators

`BinaryExpression`, `UnaryExpression`, and `UpdateExpression` represent
operations over expressions.

```text
a + b
```

```text
-a
```

```text
counter++
```

Operands and nested expressions are represented through child
`IExpression` nodes, allowing operations to be composed recursively.

### Arrays

Array-related expressions represent array creation, literal construction,
and element access.

```text
[1, 2, 3]
```

```text
new Integer[10]
```

```text
values[index]
```

The AST represents these constructs structurally without resolving their
runtime representation.

### Functions and Lambdas

`FunctionCallExpression` represents invocation of a callable expression,
while `LambdaExpression` represents an anonymous function value.

```text
add(10, 20)
```

```text
(Integer x) => x * 2
```

Because functions are values in Morphe, callable expressions can be passed
through and composed with other expressions rather than being restricted to
direct references to declared functions.

### Composite Instances

`CompositeInstanceExpression` represents the construction of a composite
value using a declared composite type and its field values.

```text
Person {
    name: "Diego",
    age: 25
}
```

The AST represents the construction syntactically. Resolution of the
composite type and validation of its fields are handled during semantic
analysis.

### Conditional and Matching Expressions

`IfExpression` and `MatchExpression` provide value-producing control-flow
constructs.

An `IfExpression` selects a value based on a condition, while a
`MatchExpression` selects a result based on pattern matching.

```text
if condition {
    valueA
} else {
    valueB
}
```

```text
match value {
    0 => "zero"
    _ => "other"
}
```

Both constructs are expressions because their evaluation produces a value
that can participate in a larger expression.

### Attempt Expressions

`AttemptExpression` represents Morphe's expression-level error-handling
construct.

It represents an operation that provides a fallback result when the primary
operation fails and may optionally perform additional error handling.

```text
attempt {
    operation()
} => fallback
```

Because `AttemptExpression` produces a value, it can be embedded directly
into other expressions and used as part of assignments or other
value-producing constructs.

### Expression Composition

Expressions are designed to compose recursively. For example:

```text
attempt {
    process(
        [1, 2, 3][index]
    )
} => defaultValue
```

The individual constructs remain independent AST nodes while their
relationships are represented through child expressions.

This compositional structure keeps expression nodes focused on individual
language constructs while allowing complex expressions to be represented by
combining existing nodes.

---

## Patterns

Patterns represent structural matching rules in the Morphe AST. They are
used by matching constructs to describe which values can be matched and,
when applicable, which values are bound to names.

The pattern hierarchy is rooted at `IPattern`:

```text id="1j8d0m"
IPattern
├── DefaultPattern
├── LiteralPattern
├── VariablePattern
├── OrPattern
├── RangePattern
├── ArrayPattern
└── CompositePattern
```

Patterns are intentionally separate from expressions. An expression produces
or references a value, while a pattern describes the structure or constraints
that a value must satisfy during matching.

### Basic Patterns

`DefaultPattern` represents the wildcard pattern and matches without
introducing a binding.

`LiteralPattern` matches a value against a literal.

```text
0
```

```text
"hello"
```

`VariablePattern` introduces a binding for the value being matched.

```text
value
```

Unlike a literal pattern, a variable pattern does not compare the matched
value against a predefined value; it captures the matched value under its
name.

### Pattern Composition

Patterns can be recursively composed through other pattern nodes.

`OrPattern` allows multiple alternative patterns:

```text
1 | 2 | 3
```

`RangePattern` represents a range of accepted values:

```text
1..10
```

The patterns contained by these constructs remain `IPattern` instances,
allowing different pattern types to be combined without coupling them to a
specific enclosing construct.

### Array Patterns

`ArrayPattern` matches the structure of an array by applying patterns to its
individual elements.

```text
[first, second, third]
```

Element patterns are stored as a list of `IPattern` instances, allowing
array patterns to contain nested patterns:

```text
[
    first,
    1..10,
    [x, y]
]
```

An array pattern may also contain an optional rest pattern for matching
remaining elements:

```text
[first, second, ...rest]
```

The rest-pattern representation is kept as part of the array pattern
structure rather than being represented as a separate top-level pattern
kind.

### Composite Patterns

`CompositePattern` matches a composite value by its type name and a
collection of field patterns.

```text
Person {
    name,
    age: 18..30
}
```

Its structure is:

```text
CompositePattern
├── typeName
└── fields
    └── FieldPattern
        ├── name
        └── pattern
```

`FieldPattern` is an internal structural component of `CompositePattern`,
rather than an independent `IPattern`. Each field associates a source-level
field name with another `IPattern`.

This allows composite patterns to be recursively nested:

```text
Person {
    name,
    address: Address {
        city,
        country
    }
}
```

The AST does not resolve the type name or verify that the referenced fields
exist. Those checks belong to semantic analysis.

### Pattern Matching and Composition

Because patterns are recursively composable, a single match arm can combine
multiple pattern concepts:

```text
match value {
    [first, 0..10, ...rest] => ...
    Person {
        name,
        age: 18..30
    } => ...
    _ => ...
}
```

The AST only represents the structure of these patterns. Matching behavior,
binding rules, exhaustiveness, type compatibility, and other semantic
constraints are determined by later compiler stages.

---

### Visitor Integration

`IPatternVisitor` provides traversal and dispatch for top-level pattern
nodes:

```text
IPatternVisitor
├── visit(DefaultPattern)
├── visit(LiteralPattern)
├── visit(VariablePattern)
├── visit(OrPattern)
├── visit(RangePattern)
├── visit(ArrayPattern)
└── visit(CompositePattern)
```

Structural components such as `CompositePattern.FieldPattern` are traversed
through their containing `CompositePattern` and are not visitor nodes of
their own.

---

## Statements

Statements represent executable program actions and control-flow constructs
in the Morphe AST. Unlike expressions, statements are not inherently
value-producing.

Statements are contained within `BlockStatement` nodes and therefore do not
appear directly as children of the `CompilationUnit`.

The statement hierarchy is rooted at `IStatement`:

```text
IStatement
├── ExpressionStatement
├── VariableDeclarationStatement
├── AssignmentStatement
├── ArrayAssignmentStatement
├── BlockStatement
├── IfStatement
├── MatchStatement
├── WhileStatement
├── DoWhileStatement
├── ForStatement
├── BreakStatement
├── ContinueStatement
└── ReturnStatement
```

### Expression Statements

`ExpressionStatement` allows an expression to be evaluated in statement
context when its resulting value is not the primary purpose of the
construct.

This is useful for expressions whose evaluation produces observable effects,
such as function calls:

```text
print("Hello");
```

The expression remains an `IExpression` in the AST;
`ExpressionStatement` provides the statement-level context in which it is
evaluated.

### Variable Declarations

`VariableDeclarationStatement` introduces a local variable together with
its associated type and initialization state.

```text
Integer value = 10;
```

The declaration can represent whether the variable is mutable or constant
according to the language's declaration semantics. Type compatibility and
scope rules are handled during semantic analysis.

### Assignment

`AssignmentStatement` represents assignment to an existing variable.

```text
value = 20;
```

Assignment operators are represented independently through
`AssignmentOperator`, allowing simple and compound assignments to share the
same operator representation.

### Array Assignment

`ArrayAssignmentStatement` represents assignment to an element of an array.

```text
values[index] = 10;
```

The target and assigned value are represented structurally in the AST, while
index validity and type compatibility are determined during semantic
analysis.

### Blocks

`BlockStatement` represents a sequence of statements enclosed by braces.

```text
{
    Integer value = 10;
    value = value + 1;
}
```

Blocks provide the structural container for statements and are used as
bodies for constructs such as functions, conditionals, and loops.

Whether a particular block introduces a new semantic scope is determined by
the language's semantic and execution rules rather than by the AST itself.

### Conditional Statements

`IfStatement` represents conditional control flow:

```text
if condition {
    statement();
} else {
    otherStatement();
}
```

Unlike `IfExpression`, an `IfStatement` does not produce a value.

### Match Statements

`MatchStatement` represents statement-level pattern matching.

```text
match value {
    0 => return;
    _ => continue;
}
```

Match arms associate patterns with executable statement bodies. The patterns
themselves are represented through `IPattern`.

`MatchStatement` is structurally independent from `MatchExpression`, even
though both use the same pattern system.

### Loops

The AST provides three loop constructs:

```text
WhileStatement
DoWhileStatement
ForStatement
```

A `WhileStatement` evaluates its condition before each iteration:

```text
while condition {
    statement();
}
```

A `DoWhileStatement` executes its body before evaluating the condition:

```text
do {
    statement();
} while condition;
```

A `ForStatement` represents the initialization, condition, update, and body
components of a `for` loop:

```text
for initialization; condition; update {
    statement();
}
```

The semantic behavior and validity of these constructs are determined during
later compiler stages.

### Break and Continue

`BreakStatement` and `ContinueStatement` represent explicit changes to loop
control flow.

```text
break;
```

```text
continue;
```

Their validity depends on the surrounding control-flow context and is
therefore checked during semantic analysis.

### Return Statements

`ReturnStatement` exits the current function and may optionally provide a
return expression.

```text
return value;
```

or:

```text
return;
```

The compatibility between the returned expression and the function's
declared return type is validated during semantic analysis.

### Statements and Expressions

Morphe intentionally keeps statements and expressions as separate AST
categories.

An expression may be embedded inside another expression:

```text
result = calculate(a + b);
```

while an expression can be placed in statement context through
`ExpressionStatement`:

```text
calculate(a + b);
```

This separation keeps value-producing constructs independent from executable
control-flow constructs while still allowing expressions to be used for
their side effects.


---

## Types

AST types represent type information expressed by Morphe source code. They
describe the identity and structure of types present in the source program
without representing runtime values or bytecode-level types.

The type hierarchy is rooted at `IASTType`:

```text
IASTType
├── PrimitiveType
├── FunctionType
└── CompositeType
```

`ASTTypeKind` identifies the general category of an AST type.

AST types represent source-level type information. Their semantic meaning,
compatibility, and resolution are determined during semantic analysis.

### Primitive Types

`PrimitiveType` represents the primitive types available directly in the
Morphe language.

For example:

```text
Integer
Double
Boolean
String
```

Primitive types describe the declared or inferred type of a value without
containing its runtime representation.

The AST records the type information expressed by the source program.
Runtime values and their behavior are handled by the runtime layer.

### Function Types

`FunctionType` represents the type of a callable value.

A function type describes the parameter types and return type of the callable:

```text
(Integer, Integer) -> Integer
```

`FunctionType` is independent from `FunctionDeclaration`.

A `FunctionDeclaration` represents a named function declaration, including
its parameters, return type, and body:

```text
FunctionDeclaration
├── name
├── parameters
├── returnType
└── body
```

In contrast, `FunctionType` represents the type of the callable value itself.

This distinction is important because functions are values in Morphe and can
therefore be represented by expressions such as `LambdaExpression` and
referenced by constructs such as `FunctionCallExpression`.

### Composite Types

`CompositeType` represents the structural type of a composite value.

A composite type contains a name and its declared fields:

```text
Person
├── name: String
└── age: Integer
```

Fields are represented by `CompositeType.CompositeField`, which associates a
field name with an `IASTType`.

A composite declaration and its corresponding type are intentionally
represented by separate AST structures:

```text
CompositeDeclaration
        ↓
declares

CompositeType
        ↓
describes
the composite type
```

`CompositeDeclaration` represents the source-level declaration construct,
while `CompositeType` represents the structural type information associated
with that construct.

This separation allows the same type representation to be referenced by
expressions and other AST structures without making those structures depend
directly on the declaration node.

### Type Independence

AST types are deliberately independent from both bytecode types and runtime
representations.

```text
Source Code
    ↓
AST Type
    ↓
Semantic Analysis
    ↓
Compiler
    ↓
Bytecode Type
    ↓
VM / Runtime Representation
```

An `IASTType` therefore does not contain runtime values, VM-specific
behavior, or bytecode instructions.

This separation allows the language-level type system to evolve without
coupling the AST directly to the execution engine.

---

## Operators

Operators represent the different operator categories available in the
Morphe language. They are modeled independently from expression and
statement nodes so that AST nodes describe program structure while operator
definitions describe the operation represented by that structure.

The operator package contains:

```text
operator
├── AssignmentOperator
├── BinaryOperator
├── UnaryOperator
└── UpdateOperator
```

Operators do not represent AST nodes themselves. They are structural
components referenced by expression and statement nodes.

### Binary Operators

`BinaryOperator` represents operators that combine two expressions.

```text
left + right
left == right
left && right
```

They are used by `BinaryExpression`, which stores the operator together with
its left and right operands.

### Unary Operators

`UnaryOperator` represents operators applied to a single expression.

```text
-x
!value
```

These operators are represented by `UnaryExpression`.

### Update Operators

`UpdateOperator` represents increment and decrement operations applied to an
expression.

```text
value++
value--
```

`UpdateExpression` uses these operators to represent the corresponding
update operation.

### Assignment Operators

`AssignmentOperator` represents assignment operations.

```text
value = expression
value += expression
value -= expression
```

`AssignmentStatement` uses these operators to distinguish simple assignment
from compound assignment.

### Operator Independence

Operators do not perform evaluation themselves. They only identify the
operation represented by an AST node.

Their meaning, operand compatibility, and resulting type are determined
during semantic analysis. Their executable behavior is implemented by later
compiler and runtime stages.

---

## Visitors

The AST uses the Visitor pattern to provide extensible operations over its
nodes without coupling those operations to the node implementations.

The visitor hierarchy is organized by AST category:

```text
IASTVisitor
├── IDeclarationVisitor<R>
├── IExpressionVisitor<R>
├── IPatternVisitor<R>
└── IStatementVisitor<R>
```

`IASTVisitor` provides the common visitor abstraction, while the specialized
visitors define dispatch operations for their respective AST node
categories.

Each specialized visitor is generic over its result type. This allows
different AST-processing stages to return different kinds of results without
changing the AST nodes themselves.

For example, a compiler visitor may use `Void` when processing nodes without
requiring a meaningful result, while semantic analysis may use a result type
appropriate for the information it produces.

### Declaration Visitor

`IDeclarationVisitor<R>` provides visitor operations for declaration nodes:

```text
IDeclarationVisitor<R>
├── visit(FunctionDeclaration)
└── visit(CompositeDeclaration)
```

It allows compiler stages and other AST-processing operations to handle
declarations without placing those operations directly inside declaration
nodes.

### Expression Visitor

`IExpressionVisitor<R>` provides dispatch operations for expression nodes:

```text
IExpressionVisitor<R>
├── visit(LiteralExpression)
├── visit(VariableExpression)
├── visit(BinaryExpression)
├── visit(UnaryExpression)
├── visit(UpdateExpression)
├── visit(ArrayAccessExpression)
├── visit(ArrayCreationExpression)
├── visit(ArrayLiteralExpression)
├── visit(FunctionCallExpression)
├── visit(LambdaExpression)
├── visit(CompositeInstanceExpression)
├── visit(AttemptExpression)
├── visit(IfExpression)
└── visit(MatchExpression)
```

Expressions dispatch to the corresponding visitor method through
`IExpression.accept(...)`.

### Pattern Visitor

`IPatternVisitor<R>` provides dispatch operations for pattern nodes:

```text
IPatternVisitor<R>
├── visit(DefaultPattern)
├── visit(LiteralPattern)
├── visit(VariablePattern)
├── visit(OrPattern)
├── visit(RangePattern)
├── visit(ArrayPattern)
└── visit(CompositePattern)
```

Structural components such as `CompositePattern.FieldPattern` are not
visitor nodes. They are traversed through their containing
`CompositePattern`.

### Statement Visitor

`IStatementVisitor<R>` provides dispatch operations for executable statement
nodes:

```text
IStatementVisitor<R>
├── visit(ExpressionStatement)
├── visit(VariableDeclarationStatement)
├── visit(AssignmentStatement)
├── visit(ArrayAssignmentStatement)
├── visit(BlockStatement)
├── visit(IfStatement)
├── visit(MatchStatement)
├── visit(WhileStatement)
├── visit(DoWhileStatement)
├── visit(ForStatement)
├── visit(BreakStatement)
├── visit(ContinueStatement)
└── visit(ReturnStatement)
```

Statements dispatch through `IStatement.accept(...)`.

### Double Dispatch

Each AST node exposes an `accept(...)` method that receives the corresponding
visitor and delegates to the visitor operation associated with its concrete
node type.

For example:

```java
return visitor.visit(this);
```

This provides double dispatch: the node selects the appropriate visitor
operation based on its concrete type, while the visitor determines what the
operation does and what result it produces.

### Visitor Results

The generic result type allows the same AST to be processed by visitors with
different purposes.

For example:

```text
Compiler
    → visits AST
    → produces bytecode-related results

Semantic Analysis
    → visits AST
    → produces semantic information or validation results
```

The AST nodes themselves do not depend on any of these processing stages.

A visitor may also intentionally return `null` when its operation does not
produce a meaningful result for a particular node.

### Why Visitors Are Used

Visitors allow operations such as semantic analysis, validation,
pretty-printing, AST inspection, and compilation to be implemented
independently from the AST node classes.

For example, a compiler visitor can process a `BinaryExpression` without
requiring `BinaryExpression` to contain bytecode-generation logic.

This keeps AST nodes focused on representing program structure while
allowing new AST-processing operations to be introduced without modifying
the existing node implementations.

The Visitor pattern does, however, introduce a deliberate trade-off:
adding a new AST node type requires the corresponding visitor interfaces to
be updated with a new dispatch operation and their implementations to handle
that node.

---

## Source Tracking

The AST preserves source-location information through `SourcePosition` and
`SourceRange`. Every AST node exposes its associated `SourceRange` through
`IASTNode`.

This allows later compiler stages to associate AST constructs with their
original source locations for diagnostics, error reporting, and source-aware
analysis.

### Source Position

`SourcePosition` represents a specific location within the source:

```text
SourcePosition
├── offset
├── line
└── column
```

* `offset` identifies the zero-based character position within the source.
* `line` identifies the one-based source line.
* `column` identifies the one-based position within that line.

A position is immutable and identifies a single location in the source.

### Source Range

`SourceRange` represents a contiguous range within the source through a
starting and ending `SourcePosition`.

```text
SourceRange
├── start
└── end
```

The start position is inclusive and the end position is exclusive.

For example, a function declaration may have a range covering its complete
source representation:

```text
func Integer add(Integer a, Integer b) {
    return a + b;
}
^---------------------------------^
            SourceRange
```

Child nodes maintain their own ranges, allowing diagnostics to identify the
specific syntactic construct involved in an error rather than only the
containing declaration or statement.

`SourceRange` may also represent an empty range when its start and end
positions have the same offset.

### AST Integration

`IASTNode` defines the common source-location contract:

```java
SourceRange range();
```

All AST nodes therefore expose source information regardless of whether they
represent a declaration, expression, pattern, statement, or another AST
construct.

Source tracking is intentionally independent from semantic information.
`SourceRange` identifies where a construct appears in the source; it does not
determine what that construct means or whether it is semantically valid.

### Use in Diagnostics

Source tracking enables later compiler stages to produce source-aware
diagnostics.

For example:

```text
error: incompatible types
  --> example.morphe:12:18
   |
12 |     result = "text";
   |              ^^^^^^
   |
   = expected Integer
   = found String
```

Semantic analysis can use the range associated with the offending AST node to
report the relevant source location without requiring AST nodes to implement
diagnostic or semantic-analysis logic themselves.

Because source information is preserved directly by AST nodes, diagnostics
can be precise while remaining independent from the parser and source-token
representation.

---

## Validation and Semantics

The AST performs structural validation during node construction, while
semantic validation is delegated to later compiler stages.

This separation keeps AST nodes responsible for representing structurally
valid syntax without coupling them to symbol resolution, type checking, scope
analysis, or other language semantics.

A structurally valid AST is not necessarily a semantically valid Morphe
program.

### Structural Validation

AST nodes validate invariants that can be determined from the node itself and
its immediate structural components.

Common structural checks include:

* Required arguments must not be `null`.
* Child AST nodes must not be `null`.
* Collections of AST nodes must not contain `null` elements.
* Required source information must be present.
* Nodes must preserve their required internal structure.
* Values must satisfy constraints that are intrinsic to the node itself.

For example, a declaration may require a non-null source range and name:

```java
if (range == null) throw new NullArgumentException("range");
if (name == null) throw new NullArgumentException("name");
```

These checks ensure that once an AST node has been constructed, its basic
structural invariants are satisfied.

Structural validation does not require symbol tables, type environments,
runtime values, or information from other parts of the program.

### Semantic Validation

Semantic validation determines whether a structurally valid AST represents a
valid Morphe program.

Examples include:

* Resolving variable and function names.
* Validating scopes and bindings.
* Checking type compatibility.
* Verifying function return types.
* Resolving composite types.
* Validating composite fields.
* Checking array access and assignment compatibility.
* Validating pattern bindings.
* Checking pattern exhaustiveness and compatibility.
* Verifying that `break` and `continue` occur inside valid loop contexts.
* Verifying that `return` occurs inside a function.
* Validating function calls and their arguments.

These rules require information that is not available from an individual AST
node alone and therefore belong to semantic analysis.

### Separation of Responsibilities

Structural validation occurs as part of AST construction:

```text
Parser
   ↓
AST Node Construction
   ↓
Structurally Valid AST
   ↓
Semantic Analysis
   ↓
Semantically Valid AST / Program
   ↓
Compiler
```

Structural validation answers:

> Is this AST node structurally well-formed?

Semantic validation answers:

> Does this structurally valid AST represent a meaningful and valid Morphe
> program?

The two forms of validation therefore operate at different levels of
responsibility.

### Example

The following composite pattern can be structurally valid:

```text
Person {
    name,
    age: 18..30
}
```

The AST can verify that the pattern contains the required structural
components, such as a type name and valid field-pattern objects.

However, the AST does not determine:

* Whether `Person` exists.
* Whether `name` and `age` are fields of `Person`.
* Whether the range is compatible with the type of `age`.
* Whether the bindings introduced by the pattern are valid in the current
  scope.
* Whether the pattern is exhaustive or otherwise valid in its matching
  context.

Those questions require semantic information and are therefore resolved
during semantic analysis.

### AST Independence

AST nodes do not perform semantic resolution or execution. They do not access
symbol tables, runtime values, VM state, or bytecode instructions.

This keeps the AST independent from later compiler stages and allows the same
representation to be used by different AST-processing operations through
the visitor interfaces.

---

## Immutability

AST nodes are immutable after construction. Once a node has been created,
its source range, properties, and child nodes cannot be replaced or modified
through the node's public API.

Immutability provides a stable representation of the program that can be
safely traversed by multiple compiler stages and visitors without requiring
AST nodes to manage mutable state.

### Immutable State

AST nodes store their state in `final` fields:

```java
private final SourceRange range;
private final String name;
private final List<IExpression> arguments;
````

Required values are validated during construction and assigned exactly once.

### Defensive Collection Copies

Nodes that contain collections create immutable copies during construction
using `List.copyOf(...)`:

```java
this.arguments = List.copyOf(arguments);
```

This prevents callers from modifying the original collection after the node
has been constructed.

The returned collection is also immutable:

```java
List<IExpression> arguments = expression.arguments();
```

Attempting to modify it results in an exception rather than changing the AST.

### Immutable Child Structure

AST nodes do not expose setters or other mutation-oriented operations for
their child nodes.

For example, once an expression has been constructed:

```text
BinaryExpression
├── left
├── operator
└── right
```

its operands and operator remain fixed for the lifetime of the node.

If a different structure is required, a new AST node must be constructed.

### Why Immutability Matters

An immutable AST provides several benefits:

* **Predictable traversal** — visitors can assume that the tree does not
  change while they process it.
* **Safe sharing** — multiple compiler stages can reference the same nodes
  without coordinating mutations.
* **Simpler reasoning** — compiler operations do not need to account for
  unexpected changes to previously processed nodes.
* **Stable diagnostics** — source ranges and structural information remain
  consistent throughout compilation.
* **Clear ownership** — the AST represents a fixed program structure rather
  than a mutable intermediate state.

Immutability is therefore a fundamental property of the AST rather than an
implementation detail.

---

## Examples

The following examples illustrate how common Morphe constructs are
represented at the language level and how the AST organizes their structure.

### Function

A function declaration combines a name, parameters, return type, and body:

```morphe
func Integer add(Integer a, Integer b) {
    return a + b;
}
````

Its AST structure can be viewed conceptually as:

```text
FunctionDeclaration
├── name: "add"
├── parameters
│   ├── Parameter("a", Integer)
│   └── Parameter("b", Integer)
├── returnType: Integer
└── body
    └── ReturnStatement
        └── BinaryExpression
            ├── left: VariableExpression("a")
            ├── operator: ADD
            └── right: VariableExpression("b")
```

The declaration describes the structure of the function. Function resolution,
type checking, and compilation are handled by later stages.

### Pattern Matching

Morphe patterns can be composed recursively inside a match construct:

```morphe
match value {
    0 => "zero"
    1 | 2 | 3 => "small"
    4..10 => "medium"
    [first, second, ...rest] => "array"
    _ => "other"
}
```

The corresponding AST can be viewed conceptually as:

```text
MatchExpression
├── value: VariableExpression("value")
└── cases
    ├── LiteralPattern(0)
    ├── OrPattern
    │   ├── LiteralPattern(1)
    │   ├── LiteralPattern(2)
    │   └── LiteralPattern(3)
    ├── RangePattern(4, 10)
    ├── ArrayPattern
    │   ├── VariablePattern("first")
    │   ├── VariablePattern("second")
    │   └── rest: VariablePattern("rest")
    └── DefaultPattern
```

The same pattern hierarchy can be used by both `MatchExpression` and
`MatchStatement`, while the surrounding construct determines whether the
match produces a value or executes statements.

### Composite Value

A composite value can be constructed by providing values for its declared
fields:

```morphe
Person {
    name: "Diego",
    age: 25
}
```

Conceptually, the AST contains:

```text
CompositeInstanceExpression
├── typeName: "Person"
└── fields
    ├── name
    │   └── LiteralExpression("Diego")
    └── age
        └── LiteralExpression(25)
```

Composite values can also participate in patterns:

```morphe
match person {
    Person {
        name,
        age: 18..30
    } => name
    _ => "unknown"
}
```

The corresponding pattern structure is:

```text
CompositePattern
├── typeName: "Person"
└── fields
    ├── FieldPattern
    │   ├── name: "name"
    │   └── pattern: VariablePattern("name")
    └── FieldPattern
        ├── name: "age"
        └── pattern: RangePattern(18, 30)
```

These examples demonstrate the compositional nature of the AST: complex
constructs are represented by combining smaller, independently defined
nodes.

---

## Module Relationships

The AST module is part of the larger Morphe compiler architecture. It acts as
the language-level structural representation between parsing and the later
semantic analysis and code-generation stages.

The primary relationship between the compiler modules is:

```text
Lexer
  │
  │ tokens
  ▼
Parser
  │
  │ AST
  ▼
Morphe AST
  │
  │ consumed by
  ▼
Semantic Analysis
  │
  │ semantic information / validation
  ▼
Compiler
  │
  │ bytecode
  ▼
Bytecode
  │
  │ executed by
  ▼
VM
  │
  │ uses
  ▼
Runtime
```

### Parser → AST

The parser is responsible for constructing AST nodes from the token stream.

The AST module therefore provides the structures required to represent
parsed Morphe programs, but does not contain parsing logic itself.

```text
Parser
   └── produces → AST
```

The parser constructs a `CompilationUnit` as the root of the parsed program.
The compilation unit contains the top-level declarations represented by
`IDeclaration` implementations.

### AST → Semantic Analysis

Semantic analysis consumes the AST to determine whether the represented
program satisfies Morphe's language rules.

This stage may resolve:

* names and scopes
* types
* function calls
* composite types and fields
* pattern bindings
* control-flow constraints
* function signatures
* other semantic relationships

```text
AST
   └── consumed by → Semantic Analysis
```

Semantic analysis may maintain symbol tables, type environments, resolved
references, diagnostics, and other semantic information independently from
the AST structure.

The AST itself remains unaware of symbol tables, semantic environments, or
resolution state.

### Semantic Analysis → Compiler

Once the program has passed the required semantic checks, the compiler can
translate the language constructs represented by the AST into bytecode.

```text
Semantic Analysis
   │
   │ validated program
   ▼
Compiler
```

The exact representation of semantic information passed to the compiler is
an implementation detail of the semantic and compiler stages. The AST remains
the structural representation of the source program.

### Compiler → Bytecode

The compiler depends on the bytecode module as its target representation.

```text
Compiler
   └── produces → Bytecode
```

The compiler translates AST constructs into the corresponding bytecode
instructions.

The AST does not contain bytecode instructions and does not depend on the
bytecode instruction set.

This separation allows the bytecode representation to remain independent
from the AST implementation.

### Bytecode → VM / Runtime

The VM consumes bytecode and executes it using the runtime system.

```text
Bytecode
   └── executed by → VM
                       │
                       └── uses → Runtime
```

The VM interprets bytecode instructions and uses runtime representations for
values, types, memory, and execution state.

The runtime therefore belongs to the execution layer rather than the
language-level AST representation.

### Dependency Direction

The intended dependency direction is:

```text
Parser
   ↓
AST
   ↓
Semantic Analysis
   ↓
Compiler
   ↓
Bytecode
   ↓
VM
   ↓
Runtime
```

Each layer is responsible for a different representation or stage of the
language implementation.

The AST should not depend on the VM, runtime, or bytecode execution layers.

In particular:

* AST nodes do not execute operations.
* AST types do not represent runtime values.
* AST nodes do not contain bytecode instructions.
* AST visitors do not require VM state.
* Source tracking remains independent from execution.
* AST nodes do not perform semantic resolution.
* Semantic environments and symbol tables remain outside the AST.

This separation keeps the AST reusable as a language-level representation
and prevents implementation details from the execution engine from leaking
into the syntax tree.