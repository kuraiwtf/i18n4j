package dev.kurai.i18n4j;

public final class TranslationParsers {

  public static final TranslationParser<Object, String> STRING_PARSER =
      (translation, _, arguments) -> translation.format(arguments);
}
