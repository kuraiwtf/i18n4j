package dev.kurai.i18n4j;

/** Ready-to-use {@link TranslationParser} implementations. */
public final class TranslationParsers {

  /** Parses a translation into its formatted {@link String} content, ignoring the viewer. */
  public static final TranslationParser<Object, String> STRING_PARSER =
      (translation, _, arguments) -> translation.format(arguments);

  private TranslationParsers() {}
}
