# Morphe

**A language built around shape, not just syntax.**

Morphe is a strongly-typed programming language with structural pattern matching. Source code is compiled to bytecode and executed by a custom virtual machine that can be explored end to end.

This repository contains the official website and documentation for the Morphe programming language.

## Website

**[Open Morphe Docs](https://diegocordova7.github.io/morphe-docs/)**

The website provides:

- **Syntax Guide** — The language structures and features currently supported by the compiler.
- **Manifesto** — The principles and ideas behind Morphe.
- **Playground** — An interactive environment for writing and executing Morphe code.
- **Documentation** — An overview of the language and its execution model.

## From Source to Execution

Morphe follows a complete language implementation pipeline:

```text
Source
  ↓
Lexer
  ↓
Parser
  ↓
Semantic Analysis
  ↓
Compiler
  ↓
Bytecode
  ↓
Virtual Machine
```

The goal is to make the entire process understandable and inspectable, from source code to execution.

## Language Highlights

### Structured types

Morphe uses strongly-typed structures where data shapes are explicitly declared and checked before execution.

### Structural matching

The `match` expression allows values to be matched against literals, ranges, alternatives, and structural patterns.

```morphe
match (city) {
    City { name: "Moctezuma" } => {
        print(city.name);
    }
}
```

### Functions as values

Functions can be treated as values, allowing behavior to be passed, stored, and composed.

### Custom bytecode and virtual machine

Morphe programs compile to bytecode designed specifically for the language and execute on its own virtual machine.

## Morphe Ecosystem

Morphe is developed as a collection of independent components that work together to form the language and its execution environment.

| Repository          | Description                                                                       |
| ------------------- | --------------------------------------------------------------------------------- |
| **Morphe Bytecode** | Bytecode representation and infrastructure.                                       |
| **Morphe Runtime**  | Runtime values and execution data structures.                                     |
| **Morphe VM**       | Virtual machine responsible for executing Morphe bytecode.                        |
| **Morphe Lexer**    | Source code tokenization.                                                         |
| **Morphe AST**      | Abstract syntax tree representation.                                              |
| **Morphe Parser**   | Parsing Morphe source code into an AST.                                           |
| **Morphe Semantic** | Type checking and semantic analysis.                                              |
| **Morphe Compiler** | Compilation from AST to bytecode.                                                 |
| **Morphe Engine**   | High-level execution pipeline that coordinates compilation and program execution. |
| **Morphe API**      | Public API for interacting with the Morphe execution ecosystem.                   |

### Repositories

- **Morphe Bytecode** — [GitHub repository](https://github.com/DiegoCordova7/morphe-bytecode?utm_source=chatgpt.com)
- **Morphe Runtime** — [GitHub repository](https://github.com/DiegoCordova7/morphe-runtime?utm_source=chatgpt.com)
- **Morphe VM** — [GitHub repository](https://github.com/DiegoCordova7/morphe-vm?utm_source=chatgpt.com)
- **Morphe Lexer** — [GitHub repository](https://github.com/DiegoCordova7/morphe-lexer?utm_source=chatgpt.com)
- **Morphe AST** — [GitHub repository](https://github.com/DiegoCordova7/morphe-ast?utm_source=chatgpt.com)
- **Morphe Parser** — [GitHub repository](https://github.com/DiegoCordova7/morphe-parser?utm_source=chatgpt.com)
- **Morphe Semantic** — [GitHub repository](https://github.com/DiegoCordova7/morphe-semantic?utm_source=chatgpt.com)
- **Morphe Compiler** — [GitHub repository](https://github.com/DiegoCordova7/morphe-compiler?utm_source=chatgpt.com)
- **Morphe Engine** — [GitHub repository](https://github.com/DiegoCordova7/morphe-engine?utm_source=chatgpt.com)
- **Morphe API** — [GitHub repository](https://github.com/DiegoCordova7/morphe-api?utm_source=chatgpt.com)

> Some repositories may still be under development as the language evolves.

## Project Philosophy

Morphe is a long-term exploration of programming language design.

It is not intended to replace existing languages. Instead, it explores how ideas such as strong typing, structural matching, composition, functional programming, and custom execution models can coexist within a consistent language.

> Once you understand how programming languages work beneath the surface, you also realize that they are not fixed ideas—they are design choices.

Read the **Morphe Manifesto** to learn more about the principles behind the project.

## Website

The documentation website is a static project built with:

- HTML
- CSS
- JavaScript
- SVG

It is designed to be hosted directly as a static site.

## License

Copyright 2026 DiegoCordova.

Licensed under the Apache License 2.0.
