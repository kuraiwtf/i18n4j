package dev.kurai.i18n4j;

import java.util.Collection;

/**
 * Reads {@link Translation Translations} from a source of a given type, e.g. a file or a
 * remote translation management platform.
 *
 * @param <S> the type of the source translations are read from
 */
public interface TranslationProvider<S> {

  /**
   * Reads and returns every translation found in the given source.
   *
   * @param source the source to read translations from
   * @return the translations found in the source
   */
  Collection<Translation> provideTranslations(final S source);
}
