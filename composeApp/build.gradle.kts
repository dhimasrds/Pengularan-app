import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Networking
            implementation(libs.ktorfit)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.logging)

            // Database (Android uses Room via androidMain); no common DB impl here

            // Navigation
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.transitions)

            // DI
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Image Loading
            implementation(libs.kamel)

            // DateTime
            implementation(libs.kotlinx.datetime)

            // Permissions
            implementation(libs.moko.permissions)

            // Resources
            implementation(libs.moko.resources)

            // Feature modules
            implementation(projects.features.login)
            implementation(projects.features.home)

            // Core modules
            implementation(projects.core.domain)
            implementation(projects.core.model)
            implementation(projects.core.network)
            implementation(projects.core.data)
            implementation(projects.core.navigation)
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            // Room (Android only)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.room.ktx)
            // Koin Android
            implementation(libs.koin.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.dhimas.pengeluaranapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.dhimas.pengeluaranapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        create("staging") {
            initWith(getByName("debug"))
            isDebuggable = true
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

buildkonfig {
    packageName = "com.dhimas.pengeluaranapp.config"

    defaultConfigs {
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "API_BASE_URL", "")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "API_KEY", "")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN, "IS_DEBUG", "false")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "DATABASE_NAME", "\"pengeluaran.db\"")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT, "DATABASE_VERSION", "1")
    }

    defaultConfigs("debug") {
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "API_BASE_URL", "\"https://api-dev.pengeluaran.com\"")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "API_KEY", "\"debug_key_12345\"")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN, "IS_DEBUG", "true")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "DATABASE_NAME", "\"pengeluaran_debug.db\"")
    }

    defaultConfigs("staging") {
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "API_BASE_URL", "\"https://api-staging.pengeluaran.com\"")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "API_KEY", "\"staging_key_67890\"")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN, "IS_DEBUG", "true")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "DATABASE_NAME", "\"pengeluaran_staging.db\"")
    }

    defaultConfigs("release") {
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "API_BASE_URL", "\"https://api.pengeluaran.com\"")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "API_KEY", "\"prod_key_placeholder\"")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN, "IS_DEBUG", "false")
        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "DATABASE_NAME", "\"pengeluaran.db\"")
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}




