package dev.kurai.i18n4j.adventure;

import dev.kurai.i18n4j.*;
import net.kyori.adventure.text.Component;

final class ComponentTranslationParser implements TranslationParser<Component> {

  @Override
  public Component parse(final Translation translation, final TranslationArgument... arguments) {
    return Component.text(translation.format(arguments));
  }
}
