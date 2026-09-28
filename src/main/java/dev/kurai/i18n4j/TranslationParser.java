package dev.kurai.i18n4j;

/**
 * Renders a {@link Translation} into a concrete output type.
 *
 * @param <R> the type of the rendered result
 */
public interface TranslationParser<R> {

  /**
   * Renders the given translation.
   *
   * @param translation the translation to render
   * @param arguments the arguments to substitute into the translation's content
   * @return the rendered result
   */
  R parse(final Translation translation, final TranslationArgument... arguments);
}
