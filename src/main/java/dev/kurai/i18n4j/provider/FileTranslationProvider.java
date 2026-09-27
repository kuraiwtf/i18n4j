package dev.kurai.i18n4j.provider;

import static com.google.common.collect.Lists.newArrayList;

import dev.kurai.i18n4j.Translation;
import dev.kurai.i18n4j.TranslationProvider;
import java.io.File;
import java.util.Collection;

public abstract class FileTranslationProvider implements TranslationProvider<File> {

  protected final String fileExtension;

  protected FileTranslationProvider(final String fileExtension) {
    this.fileExtension = fileExtension;
  }

  @Override
  public final Collection<Translation> provideTranslations(final File source) {
    final String fileName = source.getName();
    if (!source.isFile() || !fileName.endsWith('.' + this.fileExtension)) {
      return newArrayList();
    }

    return this.readFile(source);
  }

  public abstract Collection<Translation> readFile(final File file);
}
