pluginManagement {
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
    versionCatalogs {
        create("libs") {
            val agp = "8.4.0"
            val kotlin = "2.0.0"
            val ksp = "2.0.0-1.0.21"
            val androidxComposeBom = "2024.05.00"
            val androidxCore = "1.13.1"
            val androidxLifecycle = "2.8.0"
            val androidxActivity = "1.9.0"
            val androidxNavigation = "2.7.7"
            val room = "2.6.1"

            plugin("android-application", "com.android.application").version(agp)
            plugin("kotlin-android", "org.jetbrains.kotlin.android").version(kotlin)
            plugin("kotlin-compose", "org.jetbrains.kotlin.plugin.compose").version(kotlin)
            plugin("devtools-ksp", "com.google.devtools.ksp").version(ksp)

            library("androidx-core-ktx", "androidx.core:core-ktx").version(androidxCore)
            library("androidx-lifecycle-runtime-ktx", "androidx.lifecycle:lifecycle-runtime-ktx").version(androidxLifecycle)
            library("androidx-lifecycle-viewmodel-compose", "androidx.lifecycle:lifecycle-viewmodel-compose").version(androidxLifecycle)
            library("androidx-activity-compose", "androidx.activity:activity-compose").version(androidxActivity)
            
            library("androidx-compose-bom", "androidx.compose:compose-bom").version(androidxComposeBom)
            library("androidx-compose-ui", "androidx.compose.ui", "ui").withoutVersion()
            library("androidx-compose-ui-graphics", "androidx.compose.ui", "ui-graphics").withoutVersion()
            library("androidx-compose-ui-tooling-preview", "androidx.compose.ui", "ui-tooling-preview").withoutVersion()
            library("androidx-compose-material3", "androidx.compose.material3", "material3").withoutVersion()
            library("androidx-navigation-compose", "androidx.navigation:navigation-compose").version(androidxNavigation)

            library("room-runtime", "androidx.room:room-runtime").version(room)
            library("room-ktx", "androidx.room:room-ktx").version(room)
            library("room-compiler", "androidx.room:room-compiler").version(room)
        }
    }
}

rootProject.name = "WattTrack"
include(":app")
