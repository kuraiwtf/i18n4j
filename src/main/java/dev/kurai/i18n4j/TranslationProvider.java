package dev.kurai.i18n4j;

import java.util.Collection;

public interface TranslationProvider<S> {

  Collection<Translation> provideTranslations(final S source);
}
