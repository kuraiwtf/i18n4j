plugins {
  `java-library`
  `maven-publish`
}

java {
  toolchain { languageVersion = JavaLanguageVersion.of(25) }

  withSourcesJar()
  withJavadocJar()
}

repositories {
  mavenCentral()
  maven("https://jitpack.io")
}

dependencies {
  compileOnlyApi(project(":"))
  api("com.github.crowdin:crowdin-api-client-java:1.36.0")
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])

      groupId = project.group.toString()
      artifactId = "extra-" + project.name
      version = project.version.toString()
    }
  }

  repositories {
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/kuraiwtf/i18n4j")
      credentials {
        username = (project.findProperty("githubActor") ?: System.getenv("GITHUB_ACTOR")) as String?
        password = (project.findProperty("githubPassword") ?: System.getenv("GITHUB_TOKEN")) as String?
      }
    }
  }
}
