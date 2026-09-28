package dev.kurai.i18n4j.tolgee;

import static dev.kurai.i18n4j.Translation.translation;
import static dev.kurai.i18n4j.TranslationKey.translationKey;
import static java.net.http.HttpClient.newHttpClient;

import com.google.common.collect.Lists;
import dev.kurai.i18n4j.Translation;
import dev.kurai.i18n4j.TranslationProvider;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jspecify.annotations.Nullable;

public final class TolgeeTranslationProvider implements TranslationProvider<TolgeeClient> {

  private static final HttpClient HTTP_CLIENT = newHttpClient();
  private static final int PAGE_SIZE = 1000;

  private final @Nullable String namespace;

  public TolgeeTranslationProvider() {
    this(null);
  }

  public TolgeeTranslationProvider(@Nullable final String namespace) {
    this.namespace = namespace;
  }

  @Override
  public Collection<Translation> provideTranslations(final TolgeeClient source) {
    final var languages = fetchLanguageTags(source);
    if (languages.isEmpty()) {
      return List.of();
    }

    String url =
        source.projectPath()
            + "/translations/"
            + String.join(",", languages.stream().map(TolgeeTranslationProvider::encode).toList())
            + "?structureDelimiter=";
    if (this.namespace != null) {
      url += "&ns=" + encode(this.namespace);
    }

    final JSONObject root = get(source, url);
    final var translations = Lists.<Translation>newArrayList();
    for (final String languageTag : root.keySet()) {
      final JSONObject values = root.optJSONObject(languageTag);
      if (values == null) {
        continue;
      }

      final Locale locale = Locale.forLanguageTag(languageTag);
      for (final String key : values.keySet()) {
        if (values.get(key) instanceof final String text) {
          translations.add(translation(translationKey(key), locale, text));
        }
      }
    }
    return translations;
  }

  private static List<String> fetchLanguageTags(final TolgeeClient client) {
    final var tags = Lists.<String>newArrayList();
    int page = 0;
    int totalPages;
    do {
      final JSONObject body =
          get(client, client.projectPath() + "/languages?size=" + PAGE_SIZE + "&page=" + page);
      final JSONObject embedded = body.optJSONObject("_embedded");
      final JSONArray languages = embedded == null ? null : embedded.optJSONArray("languages");
      if (languages != null) {
        for (int i = 0; i < languages.length(); i++) {
          tags.add(languages.getJSONObject(i).getString("tag"));
        }
      }
      final JSONObject pageInfo = body.optJSONObject("page");
      totalPages = pageInfo == null ? 1 : pageInfo.optInt("totalPages", 1);
      page++;
    } while (page < totalPages);
    return tags;
  }

  private static JSONObject get(final TolgeeClient client, final String url) {
    final HttpRequest request =
        HttpRequest.newBuilder(URI.create(url))
            .header("X-API-Key", client.apiKey())
            .header("Accept", "application/json")
            .GET()
            .build();
    try {
      final HttpResponse<String> response =
          HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() / 100 != 2) {
        throw new IllegalStateException("Tolgee response: " + response.body());
      }
      return new JSONObject(response.body());
    } catch (final JSONException e) {
      throw new IllegalStateException("Invalid JSON response from Tolgee (" + url + ")", e);
    } catch (final IOException e) {
      throw new UncheckedIOException("No response from Tolgee (" + url + ")", e);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Request interruption", e);
    }
  }

  private static String encode(final String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
  }
}
