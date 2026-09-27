package dev.kurai.i18n4j.adventure;

import dev.kurai.i18n4j.Translation;
import dev.kurai.i18n4j.TranslationArgument;
import dev.kurai.i18n4j.TranslationParser;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

final class ComponentTranslationParser implements TranslationParser<Audience, Component> {

  @Override
  public Component parse(
      final Translation translation,
      final Audience viewer,
      final TranslationArgument... arguments) {
    return Component.text(translation.format(arguments));
  }
}
