package dev.kurai.i18n4j;

import dev.kurai.i18n4j.util.Keyed;

/**
 * A dot-separated identifier used to look up {@link Translation Translations} in a {@link
 * TranslationStore}, e.g. {@code "menu.title"}.
 */
public sealed interface TranslationKey extends Keyed<String> permits TranslationKeyImpl {

  /**
   * Creates a new translation key.
   *
   * @param key the raw key string
   * @return a new translation key
   */
  static TranslationKey translationKey(final String key) {
    return new TranslationKeyImpl(key);
  }

  @Override
  String key();

  /**
   * Returns a new key formed by appending {@code key} as a dot-separated segment of this key,
   * e.g. {@code translationKey("menu").append("title")} yields {@code "menu.title"}.
   *
   * @param key the segment to append
   * @return the combined translation key
   */
  TranslationKey append(final String key);
}
