plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget()
    jvm("desktop")
    listOf(iosX64(), iosArm64(), iosSimulatorArm64())

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(compose.components.uiToolingPreview)


            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")


            implementation(projects.core.model)
            implementation(projects.core.domain)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.core.common)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(compose.preview)
            implementation(compose.uiTooling)
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        iosMain.dependencies { }
    }
}

android {
    namespace = "com.dhimas.pengeluaranapp.features.home"
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

