package dev.kurai.i18n4j;

import static java.util.List.copyOf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import org.jspecify.annotations.Nullable;

final class TranslationStoreImpl implements TranslationStore {

  private final Collection<Translation> translations;

  TranslationStoreImpl() {
    this.translations = new ArrayList<>();
  }

  @Override
  public Collection<Translation> findAll() {
    return copyOf(this.translations);
  }

  @Override
  public Collection<Translation> findAllByKey(final TranslationKey key) {
    return copyOf(
        this.translations.stream()
            .filter(translation -> translation.key().key().equals(key.key()))
            .toList());
  }

  @Override
  public Collection<Translation> findAllByLocale(final Locale locale) {
    return copyOf(
        this.translations.stream().filter(translation -> translation.locale() == locale).toList());
  }

  @Override
  public void insertOne(final Translation translation) {
    if (this.findByKeyAndLocale(translation.key(), translation.locale()) != null) {
      throw new IllegalArgumentException("Translation already exists");
    }

    this.translations.add(translation);
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
    final var existing = this.findByKeyAndLocale(translation.key(), translation.locale());
    if (existing == null) {
      throw new IllegalArgumentException("Translation does not exist");
    }

    this.translations.remove(existing);
    this.translations.add(translation);
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
    this.translations.removeIf(translation -> translation.key().key().equals(key.key()));
  }

  @Override
  public void deleteByLocale(final Locale locale) {
    this.translations.removeIf(translation -> translation.locale() == locale);
  }

  @Override
  public void deleteByKeyAndLocale(final TranslationKey key, final Locale locale) {
    this.translations.removeIf(
        translation -> translation.key().key().equals(key.key()) && translation.locale() == locale);
  }

  @Override
  public @Nullable Translation findByKeyAndLocale(final TranslationKey key, final Locale locale) {
    return this.translations.stream()
        .filter(
            translation ->
                translation.key().key().equals(key.key()) && translation.locale() == locale)
        .findFirst()
        .orElse(null);
  }
}
