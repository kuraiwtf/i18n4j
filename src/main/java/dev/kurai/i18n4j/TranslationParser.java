package dev.kurai.i18n4j;

/**
 * Renders a {@link Translation} into a concrete output type, optionally tailoring the result to
 * a viewer.
 *
 * @param <V> the type of the viewer the translation is rendered for
 * @param <R> the type of the rendered result
 */
public interface TranslationParser<V, R> {

  /**
   * Renders the given translation for the given viewer.
   *
   * @param translation the translation to render
   * @param viewer the viewer the translation is rendered for
   * @param arguments the arguments to substitute into the translation's content
   * @return the rendered result
   */
  R parse(final Translation translation, final V viewer, final TranslationArgument... arguments);
}
