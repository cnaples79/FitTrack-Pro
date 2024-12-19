pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")
        maven("https://nodejs.org/dist/") {
            content {
                includeModule("org.nodejs", "node")
            }
        }
    }
}

rootProject.name = "FitTrack-Pro"
include(":androidApp")
include(":shared")
include(":webApp")
