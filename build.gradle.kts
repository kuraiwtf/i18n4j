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
}

dependencies {
  api("org.json:json:20260814")
  api("org.jspecify:jspecify:1.0.1")
  api("com.google.guava:guava:33.7.1-jre")
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])

      groupId = project.group.toString()
      artifactId = project.name
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
