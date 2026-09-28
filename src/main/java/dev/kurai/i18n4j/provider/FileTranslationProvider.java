package dev.kurai.i18n4j.provider;

import static com.google.common.collect.Lists.newArrayList;

import dev.kurai.i18n4j.Translation;
import dev.kurai.i18n4j.TranslationProvider;
import java.io.File;
import java.util.Collection;

/**
 * A {@link TranslationProvider} that reads translations from files with a specific extension,
 * skipping any file whose name does not match.
 */
public abstract class FileTranslationProvider implements TranslationProvider<File> {

  /** The file extension, without a leading dot, this provider reads. */
  protected final String fileExtension;

  /**
   * Creates a new file translation provider.
   *
   * @param fileExtension the file extension, without a leading dot, this provider reads
   */
  protected FileTranslationProvider(final String fileExtension) {
    this.fileExtension = fileExtension;
  }

  /**
   * Reads the translations from the given file, or returns an empty collection if it is not a
   * regular file with this provider's {@linkplain #fileExtension extension}.
   *
   * @param source the file to read
   * @return the translations found in the file, or an empty collection if the file is skipped
   */
  @Override
  public final Collection<Translation> provideTranslations(final File source) {
    final String fileName = source.getName();
    if (!source.isFile() || !fileName.endsWith('.' + this.fileExtension)) {
      return newArrayList();
    }

    return this.readFile(source);
  }

  /**
   * Parses the translations contained in the given file.
   *
   * @param file the file to parse
   * @return the translations found in the file
   */
  public abstract Collection<Translation> readFile(final File file);
}
