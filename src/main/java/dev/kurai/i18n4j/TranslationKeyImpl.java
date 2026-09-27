package dev.kurai.i18n4j;

record TranslationKeyImpl(String key) implements TranslationKey {

  @Override
  public TranslationKey append(final String key) {
    return new TranslationKeyImpl(this.key + '.' + key);
  }
}
