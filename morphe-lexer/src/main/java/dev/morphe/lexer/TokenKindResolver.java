package dev.morphe.lexer;

import dev.morphe.lexer.kind.*;
import dev.morphe.lexer.source.LexerCursor;

import java.util.*;

/**
 * Resolves source lexemes into their corresponding {@link ITokenKind token
 * kinds}.
 *
 * <p>
 * The resolver maintains the lexical vocabulary recognized by the Morphe
 * lexer. Token kinds with an explicit lexeme are registered and can later be
 * resolved from their source representation.
 * </p>
 *
 * <p>
 * Symbolic token kinds are tracked separately because symbols require
 * longest-match resolution during lexical scanning. For example, when both
 * {@code =} and {@code ==} are valid symbols, the resolver must select
 * {@code ==} when both characters are present in the source.
 * </p>
 *
 * <p>
 * The resolver also maintains the set of characters that can begin a symbol.
 * This allows the lexer to determine whether symbol scanning should be
 * attempted without iterating over the complete symbol table.
 * </p>
 *
 * <p>
 * This class is an internal lexer component and is not part of the public
 * lexer API.
 * </p>
 */
final class TokenKindResolver {

  private final Map<String, ITokenKind> kinds = new HashMap<>();
  private final Map<String, ITokenKind> symbolKinds = new HashMap<>();
  private final Set<Character> symbolStarts = new HashSet<>();

  /**
   * Creates a resolver containing all lexeme-bearing token kinds recognized
   * by the Morphe lexer.
   */
  TokenKindResolver() {
    register(KeywordTokenKind.values());
    register(LiteralTokenKind.values());
    registerLiteralAliases();
    registerSymbols(MatchTokenKind.values());
    registerSymbols(OperatorTokenKind.values());
    registerSymbols(PunctuationTokenKind.values());
    register(TypeTokenKind.values());
    register(ErrorTokenKind.values());
  }

  /**
   * Registers token kinds that are resolved directly by their lexeme.
   *
   * <p>
   * Token kinds without a source-level lexeme are ignored because they cannot
   * be resolved from source text.
   * </p>
   *
   * @param tokenKinds the token kinds to register
   */
  private void register(ITokenKind[] tokenKinds) {
    for (ITokenKind kind : tokenKinds) {
      if (!kind.hasLexeme()) continue;
      kinds.put(kind.lexeme(), kind);
    }
  }

  /**
   * Registers symbolic token kinds.
   *
   * <p>
   * Symbols are registered in the general token-kind table as well as in a
   * dedicated symbol table used for longest-match resolution. The first
   * character of each symbol is also recorded so the lexer can quickly
   * determine whether the current character may begin a symbol.
   * </p>
   *
   * @param tokenKinds the symbolic token kinds to register
   */
  private void registerSymbols(ITokenKind[] tokenKinds) {
    for (ITokenKind kind : tokenKinds) {
      if (!kind.hasLexeme()) continue;
      String lexeme = kind.lexeme();
      kinds.put(lexeme, kind);
      symbolKinds.put(lexeme, kind);
      symbolStarts.add(lexeme.charAt(0));
    }
  }

  /**
   * Returns whether the specified character can begin a symbolic token.
   *
   * @param character the character to check
   * @return {@code true} if the character can begin a registered symbol;
   * {@code false} otherwise
   */
  boolean canStartSymbol(char character) {
    return symbolStarts.contains(character);
  }

  /**
   * Resolves a source lexeme to its corresponding token kind.
   *
   * <p>
   * If the lexeme is not registered, it is treated as an identifier.
   * </p>
   *
   * @param lexeme the source lexeme to resolve
   * @return the corresponding token kind, or {@link IdentifierTokenKind#IDENTIFIER}
   * if no explicit kind exists
   */
  ITokenKind resolve(String lexeme) {
    return kinds.getOrDefault(lexeme, IdentifierTokenKind.IDENTIFIER);
  }

  private void registerLiteralAliases() {
    kinds.put("true", LiteralTokenKind.BOOLEAN);
    kinds.put("false", LiteralTokenKind.BOOLEAN);
  }

  /**
   * Resolves the longest registered symbol beginning at the cursor position.
   *
   * <p>
   * Longest-match resolution ensures that multi-character symbols take
   * precedence over shorter symbols sharing the same prefix. For example,
   * {@code ==} is selected instead of {@code =} when both are valid at the
   * current source position.
   * </p>
   *
   * @param cursor the lexer cursor positioned at the beginning of a symbol
   * @return the longest matching symbol, or {@code null} if no registered
   * symbol matches the current position
   */
  String resolveSymbol(LexerCursor cursor) {
    String bestMatch = null;
    for (String lexeme : symbolKinds.keySet()) {
      if (cursor.startsWith(lexeme) && (bestMatch == null || lexeme.length() > bestMatch.length())) {
        bestMatch = lexeme;
      }
    }
    return bestMatch;
  }
}