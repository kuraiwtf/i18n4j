package dev.kurai.i18n4j.crowdin;

import static dev.kurai.i18n4j.Translation.translation;
import static dev.kurai.i18n4j.TranslationKey.translationKey;

import com.crowdin.client.Client;
import com.crowdin.client.core.model.ResponseList;
import com.crowdin.client.core.model.ResponseObject;
import com.crowdin.client.projectsgroups.model.Project;
import com.crowdin.client.sourcestrings.model.ListSourceStringsParams;
import com.crowdin.client.sourcestrings.model.SourceString;
import com.crowdin.client.stringtranslations.model.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.kurai.i18n4j.Translation;
import dev.kurai.i18n4j.TranslationProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.IntFunction;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class CrowdinTranslationProvider implements TranslationProvider<Client> {

  private static final int PAGE_SIZE = 500;
  private static final String DEFAULT_PLURAL_FORM = "other";

  private final long projectId;

  public CrowdinTranslationProvider(final long projectId) {
    this.projectId = projectId;
  }

  @Override
  public Collection<Translation> provideTranslations(final Client source) {
    final Project project = source.getProjectsGroupsApi().getProject(this.projectId).getData();
    final var translations = Lists.<Translation>newArrayList();

    final var keys = Maps.<Long, String>newHashMap();
    final Locale sourceLocale = Locale.forLanguageTag(project.getSourceLanguageId());

    for (final SourceString string : this.fetchSourceStrings(source)) {
      final String key = string.getIdentifier();
      if (key != null) {
        keys.put(string.getId(), key);

        if (string.getText() instanceof final String text) {
          translations.add(translation(translationKey(key), sourceLocale, text));
        }
      }
    }

    for (final String targetLanguageId : project.getTargetLanguageIds()) {
      final Locale targetLocale = Locale.forLanguageTag(targetLanguageId);
      for (final LanguageTranslations languageTranslation :
          this.fetchLanguageTranslations(source, targetLanguageId)) {
        final String text = extractText(languageTranslation);
        final String key = keys.get(stringIdOf(languageTranslation));

        if (key != null && text != null) {
          translations.add(translation(translationKey(key), targetLocale, text));
        }
      }
    }

    return translations;
  }

  private List<SourceString> fetchSourceStrings(final Client client) {
    return paginate(
        offset ->
            client
                .getSourceStringsApi()
                .listSourceStrings(
                    this.projectId,
                    ListSourceStringsParams.builder().limit(PAGE_SIZE).offset(offset).build()));
  }

  private List<LanguageTranslations> fetchLanguageTranslations(
      final Client client, final String languageId) {
    return paginate(
        offset -> {
          final ListLanguageTranslationsOptions options = new ListLanguageTranslationsOptions();
          options.setLimit(PAGE_SIZE);
          options.setOffset(offset);
          return client
              .getStringTranslationsApi()
              .listLanguageTranslations(this.projectId, languageId, options);
        });
  }

  private static <T> List<T> paginate(final IntFunction<ResponseList<T>> fetcher) {
    final List<T> result = new ArrayList<>();
    int offset = 0;
    while (true) {
      final List<ResponseObject<T>> page = fetcher.apply(offset).getData();
      for (final ResponseObject<T> object : page) {
        result.add(object.getData());
      }
      if (page.size() < PAGE_SIZE) {
        return result;
      }
      offset += PAGE_SIZE;
    }
  }

  private static @Nullable Long stringIdOf(final LanguageTranslations entry) {
    return switch (entry) {
      case final PlainLanguageTranslations plain -> plain.getStringId();
      case final ICULanguageTranslations icu -> icu.getStringId();
      case final PluralLanguageTranslations plural -> plural.getStringId();
      default ->
          throw new IllegalStateException("Unknown traduction type: " + entry.getClass());
    };
  }

  private static @Nullable String extractText(final LanguageTranslations entry) {
    return switch (entry) {
      case final PlainLanguageTranslations plain -> plain.getText();
      case final ICULanguageTranslations icu -> icu.getText();
      case final PluralLanguageTranslations plural when plural.getPlurals() != null ->
          plural.getPlurals().stream()
              .filter(form -> DEFAULT_PLURAL_FORM.equals(form.getPluralForm()))
              .map(PluralLanguageTranslations.Plurals::getText)
              .findFirst()
              .orElse(null);
      default -> null;
    };
  }
}
