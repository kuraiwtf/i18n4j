package dev.kurai.i18n4j.tolgee;

import static java.util.Objects.requireNonNull;

import org.jspecify.annotations.Nullable;

public final class TolgeeClient {

  public static Builder builder() {
    return new Builder();
  }

  private final String baseUrl, apiKey;

  private final @Nullable Long projectId;

  private TolgeeClient(final String baseUrl, final String apiKey, final @Nullable Long projectId) {
    this.baseUrl = requireNonNull(baseUrl, "baseUrl must not be null");
    this.apiKey = requireNonNull(apiKey, "apiKey must not be null");

    this.projectId = projectId;
  }

  public String baseUrl() {
    return this.baseUrl;
  }

  public String apiKey() {
    return this.apiKey;
  }

  public @Nullable Long projectId() {
    return this.projectId;
  }

  String projectPath() {
    final String base = this.baseUrl.replaceAll("/+$", "");
    return this.projectId == null ? base + "/v2/projects" : base + "/v2/projects/" + this.projectId;
  }

  public static final class Builder {

    private static final String DEFAULT_BASE_URL = "https://app.tolgee.io";

    private @Nullable String baseUrl = DEFAULT_BASE_URL, apiKey;

    private @Nullable Long projectId;

    private Builder() {}

    public Builder baseUrl(final String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    public Builder apiKey(final String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    public Builder projectId(final Long projectId) {
      this.projectId = projectId;
      return this;
    }

    public TolgeeClient build() {
      return new TolgeeClient(
          requireNonNull(this.baseUrl, "Base URL cannot be null"),
          requireNonNull(this.apiKey, "API Key cannot be null"),
          this.projectId);
    }
  }
}
