package dev.kurai.i18n4j;

import java.util.Locale;

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
