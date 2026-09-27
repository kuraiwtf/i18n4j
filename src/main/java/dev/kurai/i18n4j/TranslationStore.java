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

  void insertOne(final Translation translation);

  void insertMany(final Translation... translations);

  void insertMany(final Collection<Translation> translations);

  void updateOne(final Translation translation);

  void updateMany(final Translation... translations);

  void updateMany(final Collection<Translation> translations);

  void deleteByKey(final TranslationKey key);

  void deleteByLocale(final Locale locale);

  void deleteByKeyAndLocale(final TranslationKey key, final Locale locale);

  @Nullable Translation findByKeyAndLocale(final TranslationKey key, final Locale locale);
}
