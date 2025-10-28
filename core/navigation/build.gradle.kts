plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    sourceSets {
        commonMain.dependencies {
            api(project(":features:home"))
            api(project(":features:login"))
            api(libs.voyager.navigator)
            api(libs.koin.core)
        }
    }
}

android {
    namespace = "com.dhimas.pengeluaranapp.core.navigation"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildTypes {
        getByName("debug") {}
        getByName("release") {}
        create("staging") {
            initWith(getByName("debug"))
        }
    }
}
