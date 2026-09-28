package dev.kurai.i18n4j;

import java.util.Locale;

/**
 * Default {@link Translation} implementation, see {@link Translation#translation}.
 *
 * @param key the key identifying the translated string
 * @param locale the locale this translation is written in
 * @param content the raw, unformatted translated content
 */
public record TranslationImpl(TranslationKey key, Locale locale, String content)
    implements Translation {

  @Override
  public String format(final TranslationArgument[] arguments) {
    final String result = this.content;

    for (final TranslationArgument argument : arguments) {
      return result.replace('{' + argument.key() + '}', argument.value());
    }

    return result;
  }
}
