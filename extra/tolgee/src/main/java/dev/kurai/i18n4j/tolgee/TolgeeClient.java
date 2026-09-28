package dev.kurai.i18n4j.tolgee;

import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public final class TolgeeClient {

  private final String baseUrl;
  private final String apiKey;

  private final @Nullable Long projectId;

  private TolgeeClient(final Builder builder) {
    this.baseUrl = requireNonNull(builder.baseUrl, "baseUrl must not be null");
    this.apiKey = requireNonNull(builder.apiKey, "apiKey must not be null");

    this.projectId = builder.projectId;
  }

  public static Builder builder() {
    return new Builder();
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

    private String baseUrl = DEFAULT_BASE_URL;
    private String apiKey;

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
      return new TolgeeClient(this);
    }
  }
}
