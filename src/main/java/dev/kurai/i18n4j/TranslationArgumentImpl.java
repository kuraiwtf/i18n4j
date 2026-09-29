package dev.kurai.i18n4j;

import static java.util.Objects.requireNonNull;

record TranslationArgumentImpl(String key, String value) implements TranslationArgument {

  TranslationArgumentImpl {
    requireNonNull(key, "Argument key cannot be null");
    requireNonNull(value, "Argument value cannot be null");
  }
}
