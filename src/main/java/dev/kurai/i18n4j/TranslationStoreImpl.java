package dev.kurai.i18n4j;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

final class TranslationStoreImpl implements TranslationStore {

  private final Table<TranslationKey, Locale, Translation> translations;
  private final Collection<Translation> translationView;

  TranslationStoreImpl() {
    this.translations = HashBasedTable.create();
    this.translationView = Collections.unmodifiableCollection(this.translations.values());
  }

  @Override
  public Collection<Translation> findAll() {
    return this.translationView;
  }

  @Override
  public Collection<Translation> findAllByKey(final TranslationKey key) {
    return this.translations.row(key).values();
  }

  @Override
  public Collection<Translation> findAllByLocale(final Locale locale) {
    return this.translations.column(locale).values();
  }

  @Override
  public void insertOne(final Translation translation) {
    if (this.exists(translation.key(), translation.locale())) {
      throw new IllegalArgumentException("Translation already exists");
    }

    this.translations.put(translation.key(), translation.locale(), translation);
  }

  @Override
  public void insertMany(final Translation... translations) {
    for (final Translation translation : translations) {
      this.insertOne(translation);
    }
  }

  @Override
  public void insertMany(final Collection<Translation> translations) {
    translations.forEach(this::insertOne);
  }

  @Override
  public void updateOne(final Translation translation) {
    if (!this.exists(translation.key(), translation.locale())) {
      throw new IllegalArgumentException("Translation does not exist");
    }

    this.translations.put(translation.key(), translation.locale(), translation);
  }

  @Override
  public void updateMany(final Translation... translations) {
    for (final Translation translation : translations) {
      this.updateOne(translation);
    }
  }

  @Override
  public void updateMany(final Collection<Translation> translations) {
    translations.forEach(this::updateOne);
  }

  @Override
  public void deleteByKey(final TranslationKey key) {
    this.translations.row(key).clear();
  }

  @Override
  public void deleteByLocale(final Locale locale) {
    this.translations.column(locale).clear();
  }

  @Override
  public void deleteByKeyAndLocale(final TranslationKey key, final Locale locale) {
    this.translations.remove(key, locale);
  }

  private boolean exists(final TranslationKey key, final Locale locale) {
    return this.translations.contains(key, locale);
  }

  @Override
  public Translation findByKeyAndLocale(final TranslationKey key, final Locale locale) {
    final Translation found = this.translations.get(key, locale);
    if (found == null) {
      return new TranslationImpl(key, locale, key.key());
    }

    return found;
  }
}
