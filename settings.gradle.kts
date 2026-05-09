pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "StopReminder"

include(":app")

include(":core")
include(":core:designsystem")
include(":core:common")
include(":core:data")
include(":core:domain")
include(":core:network")
include(":core:base")
include(":core:navigation")
include(":core:database")

include(":feature")
include(":feature:home")
