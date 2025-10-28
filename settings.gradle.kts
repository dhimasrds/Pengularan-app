rootProject.name = "PengeluaranApp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")

// Core modules
include(":core:model")
include(":core:domain")
include(":core:common")
include(":core:network")
include(":core:database")
include(":core:data")
include(":core:ui")
include(":core:designsystem")
include(":core:navigation")

// Feature modules
include(":features:login")
include(":features:home")