package dev.kurai.i18n4j;

import static java.util.Objects.requireNonNull;

import java.util.Locale;

/**
 * Default {@link Translation} implementation, see {@link Translation#translation}.
 *
 * @param key the key identifying the translated string
 * @param locale the locale this translation is written in
 * @param content the raw, unformatted translated content
 */
record TranslationImpl(TranslationKey key, Locale locale, String content) implements Translation {

  TranslationImpl {
    requireNonNull(key, "Translation key cannot be null");
    requireNonNull(locale, "Translation locale cannot be null");
    requireNonNull(content, "Translation content cannot be null");
  }

  @Override
  public String format(final TranslationArgument... arguments) {
    String result = this.content;

    for (final TranslationArgument argument :
        requireNonNull(arguments, "Translation arguments cannot be null")) {
      result = result.replace('{' + argument.key() + '}', argument.value());
    }

    return result;
  }
}
