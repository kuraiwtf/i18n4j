package dev.kurai.i18n4j.provider;

import static dev.kurai.i18n4j.Translation.translation;
import static dev.kurai.i18n4j.TranslationKey.translationKey;

import dev.kurai.i18n4j.Translation;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

final class JsonTranslationProvider extends FileTranslationProvider {

  JsonTranslationProvider() {
    super("json");
  }

  private static final String KEY_FIELD = "key";
  private static final String LOCALE_FIELD = "locale";
  private static final String CONTENT_FIELD = "content";

  @Override
  public Collection<Translation> readFile(final File source) {
    final JSONArray entries = read(source);
    final var translations = new ArrayList<Translation>(entries.length());
    for (int index = 0; index < entries.length(); index++) {
      translations.add(parseEntry(source, entries, index));
    }
    return List.copyOf(translations);
  }

  private static JSONArray read(final File source) {
    try (final Reader reader = Files.newBufferedReader(source.toPath(), StandardCharsets.UTF_8)) {
      return new JSONArray(new JSONTokener(reader));
    } catch (final IOException exception) {
      throw new UncheckedIOException("Unable to read translation file " + source, exception);
    } catch (final JSONException exception) {
      throw new IllegalArgumentException(
          "Malformed translation file " + source + " (root must be a JSON array)", exception);
    }
  }

  private static Translation parseEntry(
      final File source, final JSONArray entries, final int index) {
    try {
      final JSONObject entry = entries.getJSONObject(index);
      final String key = entry.getString(KEY_FIELD);
      final String rawLocale = entry.getString(LOCALE_FIELD);
      final Locale locale = Locale.forLanguageTag(rawLocale.replace('_', '-'));
      final String content = entry.getString(CONTENT_FIELD);
      return translation(translationKey(key), locale, content);
    } catch (final JSONException exception) {
      throw new IllegalArgumentException(
          "Invalid translation entry #" + index + " in " + source, exception);
    }
  }
}
