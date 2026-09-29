package dev.kurai.i18n4j.adventure;

import dev.kurai.i18n4j.TranslationParser;
import net.kyori.adventure.text.Component;

public final class AdventureTranslationParsers {

  public static final TranslationParser<Component> COMPONENT = new ComponentTranslationParser();

  private AdventureTranslationParsers() {}
}
