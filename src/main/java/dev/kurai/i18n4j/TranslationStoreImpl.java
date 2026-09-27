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
  public void insert(final Collection<Translation> translations) {
    this.translations.addAll(translations);
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
