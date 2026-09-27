package dev.kurai.i18n4j;

import dev.kurai.i18n4j.util.Keyed;
import java.util.Locale;

public sealed interface Translation extends Keyed<TranslationKey> permits TranslationImpl {

  static Translation translation(
      final TranslationKey key, final Locale locale, final String content) {
    return new TranslationImpl(key, locale, content);
  }

  @Override
  TranslationKey key();

  Locale locale();

  String content();

  String format(final TranslationArgument[] arguments);
}
