package dev.kurai.i18n4j;

/**
 * A named value substituted into a {@link Translation}'s content in place of a {@code {key}}
 * placeholder.
 */
public sealed interface TranslationArgument permits TranslationArgumentImpl {

  /**
   * Creates a new translation argument.
   *
   * @param key the placeholder name, without surrounding braces
   * @param value the value to substitute in place of the placeholder
   * @return a new translation argument
   */
  static TranslationArgument translationArgument(final String key, final String value) {
    return new TranslationArgumentImpl(key, value);
  }

  /**
   * Returns the placeholder name, without surrounding braces.
   *
   * @return the argument key
   */
  String key();

  /**
   * Returns the value substituted in place of the placeholder.
   *
   * @return the argument value
   */
  String value();
}
