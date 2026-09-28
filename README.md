# i18n4j

A small, modern internationalization library for Java, built around immutable value types,
sealed interfaces and pluggable translation sources.

```java
final TranslationStore store = TranslationStore.translationStore();

store.insertOne(translation(translationKey("menu.title"), Locale.US, "Menu"));
store.insertOne(translation(translationKey("menu.title"), Locale.FRANCE, "Menu"));

final Translation translation = store.findByKeyAndLocale(translationKey("menu.title"), Locale.US);
System.out.println(translation.content()); // "Menu"
```

## Features

- **Immutable core types** — `Translation`, `TranslationKey` and `TranslationArgument` are
  sealed, thread-safe value types.
- **Pluggable sources** — implement `TranslationProvider<S>` to load translations from anything;
  a JSON `FileTranslationProvider` ships out of the box.
- **Pluggable rendering** — implement `TranslationParser<V, R>` to render a translation for a
  given viewer type and output type.
- **In-memory store** — `TranslationStore` indexes translations by key and locale, with a
  built-in fallback to the key itself when a translation is missing.
- **Optional integrations** — first-party modules for
  [Adventure](https://docs.advntr.dev/) components/MiniMessage and
  [Crowdin](https://crowdin.com/).

## Requirements

Java 25 or later.

## Installation

Artifacts are published to GitHub Packages.

<details>
<summary>Gradle (Kotlin DSL)</summary>

```kotlin
repositories {
  maven {
    name = "i18n4j"
    url = uri("https://maven.pkg.github.com/kuraiwtf/i18n4j")
    credentials {
      username = providers.gradleProperty("githubActor").orNull ?: System.getenv("GITHUB_ACTOR")
      password = providers.gradleProperty("githubPassword").orNull ?: System.getenv("GITHUB_TOKEN")
    }
  }
}

dependencies {
  implementation("dev.kurai.i18n4j:i18n4j:VERSION")

  // Optional modules
  implementation("dev.kurai.i18n4j:extra-adventure:VERSION")
  implementation("dev.kurai.i18n4j:extra-crowdin:VERSION")
}
```

</details>

<details>
<summary>Maven</summary>

```xml
<repositories>
  <repository>
    <id>i18n4j</id>
    <url>https://maven.pkg.github.com/kuraiwtf/i18n4j</url>
  </repository>
</repositories>

<dependency>
  <groupId>dev.kurai.i18n4j</groupId>
  <artifactId>i18n4j</artifactId>
  <version>VERSION</version>
</dependency>
```

</details>

GitHub Packages requires authentication even for reads — see
[GitHub's documentation](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry)
for setting up credentials.

## Core concepts

| Type | Role |
|---|---|
| `TranslationKey` | A dot-separated identifier, e.g. `"menu.title"`. |
| `Translation` | A key + locale + raw content triple, with `{placeholder}` formatting. |
| `TranslationArgument` | A named value substituted into a translation's placeholders. |
| `TranslationProvider<S>` | Reads translations from a source of type `S` (a file, an API client, ...). |
| `TranslationStore` | An in-memory index of translations by key and locale. |
| `TranslationParser<V, R>` | Renders a translation into an output type `R` for a viewer type `V`. |

## Loading translations from files

```java
final Collection<Translation> translations =
    FileTranslationProviders.JSON.provideTranslations(new File("menu.json"));

store.insertMany(translations);
```

```json
[
  { "key": "menu.title", "locale": "en_US", "content": "Menu" },
  { "key": "menu.title", "locale": "fr_FR", "content": "Menu" }
]
```

## Rendering translations

```java
final Translation translation = store.findByKeyAndLocale(translationKey("menu.title"), Locale.US);
final String rendered = TranslationParsers.STRING_PARSER.parse(translation, null);
```

## Extra modules

### Adventure

Render translations directly as Adventure `Component`s, either as plain text or as
[MiniMessage](https://docs.advntr.dev/minimessage/):

```java
final Component component = AdventureTranslationParsers.MINI_MESSAGE.parse(
    translation, audience, translationArgument("player", "Kurai"));

audience.sendMessage(component);
```

### Crowdin

Pull source strings and their translations straight from a
[Crowdin](https://crowdin.com/) project:

```java
final TranslationProvider<Client> provider = new CrowdinTranslationProvider(projectId);
store.insertMany(provider.provideTranslations(crowdinClient));
```

## Building

```shell
./gradlew build
```

## Contributing

Issues and pull requests are welcome.
