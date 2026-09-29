package dev.kurai.i18n4j.adventure;

import static dev.kurai.i18n4j.TranslationKey.translationKey;
import static java.util.Objects.requireNonNull;

import dev.kurai.i18n4j.TranslationStore;
import java.util.Locale;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslator;

public final class BuiltinMiniMessageTranslator extends MiniMessageTranslator {

  private final TranslationStore translationStore;

  public BuiltinMiniMessageTranslator(final TranslationStore translationStore) {
    this.translationStore = requireNonNull(translationStore, "Translation store cannot be null");
  }

  @Override
  protected String getMiniMessageString(final String key, final Locale locale) {
    return this.translationStore.findByKeyAndLocale(translationKey(key), locale).content();
  }

  @Override
  public Key name() {
    return Key.key("kurai", "translation_store");
  }
}
