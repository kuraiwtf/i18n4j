package dev.kurai.i18n4j;

import dev.kurai.i18n4j.util.Keyed;
import java.util.Locale;

/**
 * A single piece of translated content for a given {@link TranslationKey} and {@link Locale}.
 *
 * <p>Instances are immutable and are typically obtained from a {@link TranslationProvider} or a
 * {@link TranslationStore}.
 */
public sealed interface Translation extends Keyed<TranslationKey> permits TranslationImpl {

  /**
   * Creates a new translation.
   *
   * @param key the key identifying the translated string
   * @param locale the locale this translation is written in
   * @param content the raw, unformatted translated content
   * @return a new translation
   */
  static Translation translation(
      final TranslationKey key, final Locale locale, final String content) {
    return new TranslationImpl(key, locale, content);
  }

  @Override
  TranslationKey key();

  /**
   * Returns the locale this translation is written in.
   *
   * @return the locale of this translation
   */
  Locale locale();

  /**
   * Returns the raw, unformatted content of this translation.
   *
   * @return the translation content
   */
  String content();

  /**
   * Formats this translation's {@linkplain #content() content}, substituting each {@code
   * {key}} placeholder with the value of the matching argument.
   *
   * @param arguments the arguments to substitute into the content
   * @return the formatted content
   */
  String format(final TranslationArgument[] arguments);
}
