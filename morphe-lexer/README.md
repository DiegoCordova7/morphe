# Morphe Lexer

The **Morphe Lexer** transforms Morphe source code into a sequence of lexical tokens.

It is the first stage of the language front-end and is responsible for recognizing the lexical structure of the source without assigning syntactic meaning to it.

```text
Morphe Source
      │
      ▼
    Lexer
      │
      ▼
   Tokens
      │
      ▼
    Parser
```

The lexer operates independently from the parser and does not construct an AST.

---

## Responsibilities

The lexer is responsible for:

* Traversing Morphe source code.
* Recognizing identifiers and keywords.
* Recognizing primitive literals.
* Recognizing operators and punctuation.
* Recognizing pattern-matching symbols.
* Tracking source positions.
* Producing source spans for tokens.
* Reporting invalid characters.
* Reporting unterminated strings.
* Distinguishing integer and floating-point numeric literals.
* Resolving lexemes into their corresponding token kinds.

The lexer does **not**:

* Validate grammatical structure.
* Construct an AST.
* Perform semantic analysis.
* Resolve variables or types.
* Execute code.

These responsibilities belong to later stages of the language pipeline.

---

## Lexical Pipeline

The lexer processes source code incrementally using a cursor.

```text
Source
  │
  ▼
LexerCursor
  │
  ├── Position tracking
  ├── Character inspection
  ├── Source traversal
  └── Whitespace skipping
  │
  ▼
Lexer
  │
  ├── Identifier scanning
  ├── Number scanning
  ├── String scanning
  └── Symbol scanning
  │
  ▼
TokenKindResolver
  │
  ▼
Token
```

The resulting tokens are returned as an immutable `List<Token>`.

---

## Tokenization

The lexer exposes a single public entry point:

```java
List<Token> tokens = new Lexer(source).tokenize();
```

Tokenization continues until an explicit `EOF` token is produced.

For example:

```text
Integer value = 42;
```

is conceptually transformed into:

```text
TYPE        "Integer"
IDENTIFIER  "value"
ASSIGN      "="
INTEGER     "42"
SEMICOLON   ";"
EOF         ""
```

The lexer preserves the original lexeme associated with each token.

---

## Token Kinds

Token kinds are grouped according to their lexical role.

```text
ITokenKind
│
├── IdentifierTokenKind
├── KeywordTokenKind
├── LiteralTokenKind
├── MatchTokenKind
├── OperatorTokenKind
├── PunctuationTokenKind
├── TypeTokenKind
└── SpecialTokenKind
```

### Identifiers

Identifiers represent user-defined names.

```text
value
counter
myFunction
result_1
```

Identifiers may begin with a letter and may contain letters, digits, and underscores.

---

### Keywords

Keywords are reserved words recognized directly by the lexer.

Examples include language constructs such as:

```text
if
else
while
return
function
```

The exact set of keywords is defined by `KeywordTokenKind`.

---

### Literals

Literal token kinds represent literal values appearing directly in source code.

The current lexical categories include:

```text
INTEGER
DOUBLE
STRING
BOOLEAN
NULL
```

Numeric literals are distinguished lexically:

```text
42      → INTEGER
42.5    → DOUBLE
```

Strings are delimited by double quotes:

```text
"hello"
```

Unterminated strings produce an `UnterminatedStringException`.

---

### Operators

Operators represent symbolic operations and language constructs.

Examples include:

```text
+
-
*
/
%
=
==
!=
<
>
<=
>=
&&
||
!
++
--
+=
-=
*=
/=
|>
->
#
```

The complete set is defined by `OperatorTokenKind`.

---

### Pattern Matching Tokens

Morphe pattern matching has its own lexical symbols:

```text
|
=>
..
...
_
```

These are represented by `MatchTokenKind`.

For example:

```text
1 => "one"
2 => "two"
_ => "other"
```

The lexer treats these symbols as lexical tokens; their syntactic meaning is determined later by the parser.

---

### Punctuation

Structural punctuation includes:

```text
( )
{ }
[ ]
;
,
```

These delimit expressions, blocks, collections, and statements.

---

## Symbol Resolution

Symbols can have overlapping prefixes.

For example:

```text
=
==
```

or:

```text
>
>=
```

The lexer therefore uses **longest-match resolution**.

```text
Source
  │
  ▼
Current position
  │
  ├── "="
  └── "=="
        │
        ▼
   longest match
        │
        ▼
       "=="
```

`TokenKindResolver` maintains a dedicated symbol table and selects the longest registered symbol beginning at the current cursor position.

This prevents shorter symbols from prematurely consuming the beginning of a longer symbol.

---

## Source Positions

Every token contains a `SourceSpan` describing its location in the original source.

A source position contains:

```text
SourcePosition
├── offset
├── line
└── column
```

The offset is zero-based, while line and column numbers are one-based.

For example:

```text
hello
     ^
     offset = 5
     line   = 1
     column = 6
```

A token span represents the interval occupied by its lexeme:

```text
Source:   Integer value = 42;
          └────────────┘

Token:
start ────────────────► end
```

Source locations are used by lexical exceptions and will also provide the foundation for parser diagnostics.

---

## Lexer Cursor

`LexerCursor` encapsulates traversal through the source text.

It provides:

* Current character access.
* End-of-source detection.
* Single-character advancement.
* Multi-character advancement.
* Whitespace skipping.
* Prefix matching.
* Source substring extraction.
* Source position tracking.

The cursor deliberately does not perform token recognition.

```text
LexerCursor
     │
     ├── offset
     ├── line
     └── column
```

This keeps source traversal separate from lexical classification.

---

## Token Kind Resolution

`TokenKindResolver` maps source lexemes to token kinds.

```text
"if"
 │
 ▼
TokenKindResolver
 │
 ▼
KeywordTokenKind.IF
```

Symbols are resolved separately because they require longest-match behavior.

```text
Source
  │
  ▼
TokenKindResolver
  │
  ├── General lexeme resolution
  │
  └── Symbol longest-match resolution
```

Unknown identifier lexemes are resolved as:

```text
IdentifierTokenKind.IDENTIFIER
```

This allows identifiers to remain open-ended while reserved words and symbolic constructs are recognized explicitly.

---

## Error Handling

Lexical errors are represented by `LexerException` subclasses.

Examples include:

### Unexpected Character

An unsupported character produces an `UnexpectedCharacterException`.

```text
abc @ xyz
    ^
    unexpected character
```

### Unterminated String

A string that reaches the end of the source without a closing quote produces an `UnterminatedStringException`.

```text
"hello world
             ^
             unterminated string
```

Source-related errors retain the corresponding source position so diagnostics can be reported accurately.

---

## Architecture

The lexer is intentionally divided into small components:

```text
lexer
│
├── Lexer
│
├── TokenKindResolver
│
├── kind
│   ├── ITokenKind
│   ├── IdentifierTokenKind
│   ├── KeywordTokenKind
│   ├── LiteralTokenKind
│   ├── MatchTokenKind
│   ├── OperatorTokenKind
│   ├── PunctuationTokenKind
│   ├── TypeTokenKind
│   └── SpecialTokenKind
│
├── token
│   └── Token
│
├── source
│   ├── LexerCursor
│   ├── SourcePosition
│   └── SourceSpan
│
└── exception
    ├── argument
    └── source
```

The primary responsibilities are:

| Component           | Responsibility                        |
| ------------------- | ------------------------------------- |
| `Lexer`             | Coordinates lexical analysis          |
| `LexerCursor`       | Traverses source and tracks positions |
| `TokenKindResolver` | Resolves lexemes and symbols          |
| `ITokenKind`        | Defines token-kind behavior           |
| `Token`             | Represents a lexical token            |
| `SourcePosition`    | Represents a point in source code     |
| `SourceSpan`        | Represents a token's source range     |
| Lexer exceptions    | Report lexical failures               |

---

## Separation from the Parser

The lexer deliberately stops at lexical analysis.

For example, given:

```text
value = 10 + 20;
```

the lexer produces tokens:

```text
IDENTIFIER  "value"
ASSIGN      "="
INTEGER     "10"
PLUS        "+"
INTEGER     "20"
SEMICOLON   ";"
EOF         ""
```

It does not determine whether this sequence forms a valid assignment expression.

That responsibility belongs to the parser:

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
```

This separation allows the lexer to remain focused on **what characters and lexemes exist**, while the parser determines **how those tokens form language constructs**.

---

## Design Goals

The lexer is designed around a few principles:

### Small Components

Source traversal, token resolution, and lexical analysis are separated into dedicated components rather than being implemented as one large class.

### Explicit Token Categories

Token kinds are represented by dedicated enums instead of relying on raw strings throughout the lexer.

### Accurate Source Locations

Every token carries a source span, allowing later compiler stages to provide meaningful diagnostics.

### Longest-Match Symbols

Overlapping symbolic tokens are resolved using longest-match semantics.

### Immutable Results

`tokenize()` returns an immutable token list, preventing callers from modifying the result of lexical analysis.

### Parser Independence

The lexer has no knowledge of AST structures or parser rules.

---

## Current Lexical Flow

The complete lexical pipeline is:

```text
Morphe Source
      │
      ▼
LexerCursor
      │
      ▼
Whitespace Skipping
      │
      ▼
Character Classification
      │
      ├── Identifier / Keyword
      ├── Number
      ├── String
      └── Symbol
              │
              ▼
       TokenKindResolver
              │
              ▼
            Token
              │
              ▼
         Token Sequence
              │
              ▼
            Parser
```

The lexer therefore forms the boundary between raw Morphe source text and the structured token stream consumed by the parser.