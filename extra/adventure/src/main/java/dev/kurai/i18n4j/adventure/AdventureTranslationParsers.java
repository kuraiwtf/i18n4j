package dev.kurai.i18n4j.adventure;

import dev.kurai.i18n4j.TranslationParser;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public final class AdventureTranslationParsers {

  public static final TranslationParser<Audience, Component> COMPONENT =
      new ComponentTranslationParser();

  public static final TranslationParser<Audience, Component> MINI_MESSAGE =
      new MiniMessageTranslationParser();

  private AdventureTranslationParsers() {}
}
