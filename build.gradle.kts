plugins {
    id("java")
    id("maven-publish")
    id("io.papermc.paperweight.userdev") version "1.5.5"
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

allprojects {
    group = "de.oliver"
    version = "0.0.1"
    description = "Simple, lightweight and fast Morph plugin using packets"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.fancyplugins.de/releases")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

val mcVersion = "1.20.1"

dependencies {
    paperweight.paperDevBundle("$mcVersion-R0.1-SNAPSHOT")

    implementation("de.oliver:FancyLib:1.0.4")
}

tasks {
    runServer {
        minecraftVersion(mcVersion)
    }

    shadowJar {
        archiveClassifier.set("")
    }

    publishing {
        repositories {
            maven {
                name = "fancypluginsReleases"
                url = uri("https://repo.fancyplugins.de/releases")
                credentials(PasswordCredentials::class)
                authentication {
                    isAllowInsecureProtocol = true
                    create<BasicAuthentication>("basic")
                }
            }

            maven {
                name = "fancypluginsSnapshots"
                url = uri("https://repo.fancyplugins.de/snapshots")
                credentials(PasswordCredentials::class)
                authentication {
                    isAllowInsecureProtocol = true
                    create<BasicAuthentication>("basic")
                }
            }
        }
        publications {
            create<MavenPublication>("maven") {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
                from(project.components["java"])
            }
        }
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(17)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
        val props = mapOf(
            "version" to project.version,
            "description" to project.description,
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}