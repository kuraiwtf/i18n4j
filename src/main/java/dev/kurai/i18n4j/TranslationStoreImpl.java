package dev.kurai.i18n4j;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import org.jspecify.annotations.Nullable;

final class TranslationStoreImpl implements TranslationStore {

  private static final String TRANSLATION_CANNOT_BE_NULL = "Translation cannot be null",
      TRANSLATIONS_CANNOT_BE_NULL = "Translations cannot be null",
      TRANSLATION_KEY_CANNOT_BE_NULL = "Translation key cannot be null",
      LOCALE_CANNOT_BE_NULL = "Locale cannot be null";

  private final Table<TranslationKey, Locale, Translation> translations;
  private final Collection<Translation> translationsView =
      Collections.unmodifiableCollection((this.translations = HashBasedTable.create()).values());

  @Override
  public Collection<Translation> findAll() {
    return this.translationsView;
  }

  @Override
  public Collection<Translation> findAllByKey(final TranslationKey translationKey) {
    return this.translations
        .row(requireNonNull(translationKey, TRANSLATION_KEY_CANNOT_BE_NULL))
        .values();
  }

  @Override
  public Collection<Translation> findAllByLocale(final Locale locale) {
    return this.translations.column(locale).values();
  }

  @Override
  public void insertOne(final Translation translation) {
    requireNonNull(translation, TRANSLATION_CANNOT_BE_NULL);

    final TranslationKey translationKey = translation.key();
    final Locale locale = translation.locale();

    if (this.exists(translationKey, locale)) {
      throw new IllegalArgumentException("Translation already exists");
    }

    this.translations.put(translationKey, locale, translation);
  }

  @Override
  public void insertMany(final Translation... translations) {
    this.insertMany(asList(translations));
  }

  @Override
  public void insertMany(final Iterable<Translation> translations) {
    requireNonNull(translations, TRANSLATIONS_CANNOT_BE_NULL);

    for (final Translation translation : translations) {
      this.insertOne(translation);
    }
  }

  @Override
  public void updateOne(final Translation translation) {
    requireNonNull(translation, TRANSLATION_CANNOT_BE_NULL);

    final TranslationKey translationKey = translation.key();
    final Locale locale = translation.locale();

    if (!this.exists(translationKey, locale)) {
      throw new IllegalArgumentException("Translation does not exist");
    }

    this.translations.put(translationKey, locale, translation);
  }

  @Override
  public void updateMany(final Translation... translations) {
    this.updateMany(asList(translations));
  }

  @Override
  public void updateMany(final Iterable<Translation> translations) {
    requireNonNull(translations, TRANSLATIONS_CANNOT_BE_NULL);

    for (final Translation translation : translations) {
      this.updateOne(translation);
    }
  }

  @Override
  public void deleteByKey(final TranslationKey translationKey) {
    this.translations.row(requireNonNull(translationKey, TRANSLATION_KEY_CANNOT_BE_NULL)).clear();
  }

  @Override
  public void deleteByLocale(final Locale locale) {
    this.translations.column(requireNonNull(locale, LOCALE_CANNOT_BE_NULL)).clear();
  }

  @Override
  public void deleteByKeyAndLocale(final TranslationKey translationKey, final Locale locale) {
    this.translations.remove(
        requireNonNull(translationKey, TRANSLATION_KEY_CANNOT_BE_NULL),
        requireNonNull(locale, LOCALE_CANNOT_BE_NULL));
  }

  private boolean exists(final TranslationKey translationKey, final Locale locale) {
    return this.translations.contains(
        requireNonNull(translationKey, TRANSLATION_KEY_CANNOT_BE_NULL),
        requireNonNull(locale, LOCALE_CANNOT_BE_NULL));
  }

  @Override
  public @Nullable Translation findByKeyAndLocale(
      final TranslationKey translationKey, final Locale locale) {
    return this.translations.get(
        requireNonNull(translationKey, TRANSLATION_KEY_CANNOT_BE_NULL),
        requireNonNull(locale, LOCALE_CANNOT_BE_NULL));
  }
}
