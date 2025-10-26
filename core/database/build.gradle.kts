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
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            // Room could be added later when migrating implementation
            // implementation(libs.androidx.room.runtime)
            // implementation(libs.androidx.room.ktx)
        }
        iosMain.dependencies {
        }
    }
}

android {
    namespace = "com.dhimas.pengeluaranapp.core.database"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
