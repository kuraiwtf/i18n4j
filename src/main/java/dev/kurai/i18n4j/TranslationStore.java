package dev.kurai.i18n4j;

import java.util.Collection;
import java.util.Locale;
import org.jspecify.annotations.Nullable;

/**
 * An in-memory store of {@link Translation Translations}, indexed by {@link TranslationKey} and
 * {@link Locale}.
 *
 * <p>At most one translation may exist for a given key/locale pair.
 */
public sealed interface TranslationStore permits TranslationStoreImpl {

  /**
   * Creates a new, empty translation store.
   *
   * @return a new translation store
   */
  static TranslationStore translationStore() {
    return new TranslationStoreImpl();
  }

  /**
   * Returns every translation held by this store.
   *
   * @return all stored translations
   */
  Collection<Translation> findAll();

  /**
   * Returns every translation held by this store for the given translationKey, regardless of locale.
   *
   * @param translationKey the translationKey to look up
   * @return the translations stored under the given translationKey
   */
  Collection<Translation> findAllByKey(final TranslationKey translationKey);

  Collection<Translation> findAllByKey(final String translationKey);

  /**
   * Returns every translation held by this store for the given locale, regardless of key.
   *
   * @param locale the locale to look up
   * @return the translations stored under the given locale
   */
  Collection<Translation> findAllByLocale(final Locale locale);

  /**
   * Inserts a translation into this store.
   *
   * @param translation the translation to insert
   * @throws IllegalArgumentException if a translation already exists for the same key and locale
   */
  void insertOne(final Translation translation);

  /**
   * Inserts several translations into this store.
   *
   * @param translations the translations to insert
   * @throws IllegalArgumentException if a translation already exists for the same key and locale
   *     as one of the given translations
   */
  void insertMany(final Translation... translations);

  /**
   * Inserts several translations into this store.
   *
   * @param translations the translations to insert
   * @throws IllegalArgumentException if a translation already exists for the same key and locale
   *     as one of the given translations
   */
  void insertMany(final Iterable<Translation> translations);

  /**
   * Replaces an existing translation in this store.
   *
   * @param translation the translation to update
   * @throws IllegalArgumentException if no translation exists for the same key and locale
   */
  void updateOne(final Translation translation);

  /**
   * Replaces several existing translations in this store.
   *
   * @param translations the translations to update
   * @throws IllegalArgumentException if no translation exists for the same key and locale as one
   *     of the given translations
   */
  void updateMany(final Translation... translations);

  /**
   * Replaces several existing translations in this store.
   *
   * @param translations the translations to update
   * @throws IllegalArgumentException if no translation exists for the same key and locale as one
   *     of the given translations
   */
  void updateMany(final Iterable<Translation> translations);

  /**
   * Removes every translation stored under the given translationKey, regardless of locale.
   *
   * @param translationKey the translationKey to remove
   */
  void deleteByKey(final TranslationKey translationKey);

  void deleteByKey(final String translationKey);

  /**
   * Removes every translation stored under the given locale, regardless of key.
   *
   * @param locale the locale to remove
   */
  void deleteByLocale(final Locale locale);

  /**
   * Removes the translation stored under the given translationKey and locale, if any.
   *
   * @param translationKey the translationKey to remove
   * @param locale the locale to remove
   */
  void deleteByKeyAndLocale(final TranslationKey translationKey, final Locale locale);

  void deleteByKeyAndLocale(final String translationKey, final Locale locale);

  /**
   * Returns the translation stored under the given translationKey and locale, or a fallback translation
   * whose content is the translationKey itself if none exists.
   *
   * @param translationKey the translationKey to look up
   * @param locale the locale to look up
   * @return the matching translation, or a fallback translation if none is stored
   */
  @Nullable Translation findByKeyAndLocale(final TranslationKey translationKey, final Locale locale);

  /**
   * Returns the translation stored under the given translationKey and locale.
   *
   * @param translationKey the translationKey to look up
   * @param locale the locale to look up
   * @return the matching translation if present, or {@code null} if none is stored
   */
  @Nullable Translation findByKeyAndLocale(final String translationKey, final Locale locale);
}
