package dev.kurai.i18n4j;

import dev.kurai.i18n4j.util.Keyed;

public sealed interface TranslationKey extends Keyed<String> permits TranslationKeyImpl {

  static TranslationKey translationKey(final String key) {
    return new TranslationKeyImpl(key);
  }

  @Override
  String key();

  TranslationKey append(final String key);
}
