package dev.kurai.i18n4j;

public sealed interface TranslationArgument permits TranslationArgumentImpl {

  static TranslationArgument translationArgument(final String key, final String value) {
    return new TranslationArgumentImpl(key, value);
  }

  String key();

  String value();
}
