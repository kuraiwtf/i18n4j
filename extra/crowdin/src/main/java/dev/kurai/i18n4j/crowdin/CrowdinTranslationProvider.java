package dev.kurai.i18n4j.crowdin;

import static dev.kurai.i18n4j.Translation.translation;
import static dev.kurai.i18n4j.TranslationKey.translationKey;
import static java.util.Objects.requireNonNull;

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

/**
 * Reads translations from a <a href="https://crowdin.com/">Crowdin</a> project, using each source
 * string's identifier as its {@link dev.kurai.i18n4j.TranslationKey} and pulling every target
 * language's approved translations alongside the source language.
 *
 * <p>For plural strings, only the {@code other} plural form is used.
 */
@NullMarked
public final class CrowdinTranslationProvider implements TranslationProvider<Client> {

  private static <T> List<T> paginate(final IntFunction<ResponseList<T>> fetcher) {
    final List<T> result = new ArrayList<>();

    int offset = 0;
    while (true) {
      final List<ResponseObject<T>> page = fetcher.apply(offset).getData();

      for (final ResponseObject<T> responseObject : page) {
        result.add(responseObject.getData());
      }

      if (page.size() < PAGE_SIZE) {
        return result;
      }

      offset += PAGE_SIZE;
    }
  }

  private static @Nullable Long stringIdOf(final LanguageTranslations languageTranslations) {
    return switch (languageTranslations) {
      case final PlainLanguageTranslations plain -> plain.getStringId();
      case final ICULanguageTranslations icu -> icu.getStringId();
      case final PluralLanguageTranslations plural -> plural.getStringId();
      default ->
          throw new IllegalStateException(
              "Unknown traduction type: " + languageTranslations.getClass());
    };
  }

  private static @Nullable String extractText(final LanguageTranslations languageTranslations) {
    return switch (languageTranslations) {
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

  private static final int PAGE_SIZE = 500;
  private static final String DEFAULT_PLURAL_FORM = "other";

  private final long projectId;

  /**
   * Creates a new Crowdin translation provider for the given project.
   *
   * @param projectId the id of the Crowdin project to read translations from
   */
  public CrowdinTranslationProvider(final long projectId) {
    this.projectId = projectId;
  }

  /**
   * Reads the source strings and every target language's translations from the configured Crowdin
   * project.
   *
   * @param client the Crowdin client to read translations with
   * @return the translations found in the project
   */
  @Override
  public Collection<Translation> provideTranslations(final Client client) {
    requireNonNull(client, "Crowdin client cannot be null");

    final Project project = client.getProjectsGroupsApi().getProject(this.projectId).getData();
    final Locale sourceLocale = Locale.forLanguageTag(project.getSourceLanguageId());

    final var translations = Lists.<Translation>newArrayList();
    final var identifiers = Maps.<Long, String>newHashMap();

    for (final SourceString sourceString : this.fetchSourceStrings(client)) {
      final String identifier = sourceString.getIdentifier();
      if (identifier != null) {
        identifiers.put(sourceString.getId(), identifier);

        if (sourceString.getText() instanceof final String stringText) {
          translations.add(translation(translationKey(identifier), sourceLocale, stringText));
        }
      }
    }

    for (final String targetLanguageId : project.getTargetLanguageIds()) {
      final Locale targetLocale = Locale.forLanguageTag(targetLanguageId);
      for (final LanguageTranslations languageTranslation :
          this.fetchLanguageTranslations(client, targetLanguageId)) {
        final String text = extractText(languageTranslation),
            identifier = identifiers.get(stringIdOf(languageTranslation));

        if (identifier != null && text != null) {
          translations.add(translation(translationKey(identifier), targetLocale, text));
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
}
