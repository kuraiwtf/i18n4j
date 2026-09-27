package dev.kurai.i18n4j;

public interface TranslationParser<V, R> {

  R parse(final Translation translation, final V viewer, final TranslationArgument... arguments);
}
