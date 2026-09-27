package dev.kurai.i18n4j;

import java.util.Collection;
import java.util.Locale;
import org.jspecify.annotations.Nullable;

public sealed interface TranslationStore permits TranslationStoreImpl {

  static TranslationStore translationStore() {
    return new TranslationStoreImpl();
  }

  Collection<Translation> findAll();

  Collection<Translation> findAllByKey(final TranslationKey key);

  Collection<Translation> findAllByLocale(final Locale locale);

  void insert(final Collection<Translation> translations);

  @Nullable Translation findByKeyAndLocale(final TranslationKey key, final Locale locale);
}
