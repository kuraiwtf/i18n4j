package dev.kurai.i18n4j.adventure;

import static net.kyori.adventure.text.minimessage.tag.resolver.TagResolver.resolver;

import com.google.common.collect.Lists;
import dev.kurai.i18n4j.Translation;
import dev.kurai.i18n4j.TranslationArgument;
import dev.kurai.i18n4j.TranslationParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class MiniMessageTranslationParser implements TranslationParser<Component> {

  @Override
  public Component parse(final Translation translation, final TranslationArgument... arguments) {
    final var resolvers = Lists.<TagResolver>newArrayList();

    for (final TranslationArgument argument : arguments) {
      resolvers.add(resolver(argument.key(), Tag.inserting(Component.text(argument.value()))));
    }

    return MiniMessage.miniMessage()
        .deserialize(translation.content(), resolvers.toArray(TagResolver[]::new));
  }
}
