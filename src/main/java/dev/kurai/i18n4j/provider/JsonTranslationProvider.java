package dev.kurai.i18n4j.provider;

import dev.kurai.i18n4j.Translation;
import java.io.File;
import java.util.Collection;
import java.util.List;

public final class JsonTranslationProvider extends FileTranslationProvider {

  JsonTranslationProvider() {
    super("json");
  }

  @Override
  public Collection<Translation> readFile(final File file) {
    return List.of();
  }
}
