You are an expert Kotlin Multiplatform (KMP) developer tasked with creating a production-ready template for Android and iOS platforms with shared UI using Compose Multiplatform. The template should follow clean architecture principles with MVVM pattern.

## Project Requirements

### Core Setup
- Kotlin version: 2.2.0
- Target platforms: Android and iOS with shared UI
- Architecture: Clean Architecture with MVVM pattern
- Build tool: Gradle with Kotlin DSL (consider Amper for future migration)

### Initial Project Generation
Use Compose Multiplatform Wizard (https://kmp.jetbrains.com) for initial setup with:
- Kotlin Multiplatform Mobile template
- Compose Multiplatform UI
- Shared resources configuration
- Platform-specific configurations

### Required Tech Stack

1. **Build & Configuration**:
    - **BuildKonfig Plugin**:
```kotlin
     // In build.gradle.kts
     plugins {
         id("com.codingfeline.buildkonfig") version "0.15.1"
     }
     
     buildkonfig {
         packageName = "com.yourpackage.config"
         
         defaultConfigs {
             buildConfigField(STRING, "API_BASE_URL", "https://api.production.com")
             buildConfigField(STRING, "API_KEY", "")
         }
         
         defaultConfigs("debug") {
             buildConfigField(STRING, "API_BASE_URL", "https://api.staging.com")
             buildConfigField(STRING, "API_KEY", "debug_api_key")
         }
         
         defaultConfigs("release") {
             buildConfigField(STRING, "API_BASE_URL", "https://api.production.com")
             buildConfigField(STRING, "API_KEY", getEnvironmentVariable("PROD_API_KEY"))
         }
     }
```

- **Gradle Version Catalog** (libs.versions.toml):
```toml
     [versions]
     kotlin = "2.2.0"
     compose-multiplatform = "1.7.0"
     ktorfit = "2.1.0"
     realm = "2.0.0"
     voyager = "1.1.0-beta02"
     ktor = "3.0.0"
     koin = "4.0.0"
     buildkonfig = "0.15.1"
     moko-permissions = "0.18.0"
     moko-resources = "0.24.0"
     kamel = "1.0.0"
     kotlinx-datetime = "0.6.1"
     ksp = "2.2.0-1.0.21"
     
     [libraries]
     # Networking
     ktorfit = { module = "de.jensklingenberg.ktorfit:ktorfit-lib", version.ref = "ktorfit" }
     ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
     ktor-client-okhttp = { module = "io.ktor:ktor-client-okhttp", version.ref = "ktor" }
     ktor-client-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" }
     ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
     ktor-serialization-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }
     
     # Database
     realm-base = { module = "io.realm.kotlin:library-base", version.ref = "realm" }
     
     # Navigation
     voyager-navigator = { module = "cafe.adriel.voyager:voyager-navigator", version.ref = "voyager" }
     voyager-screenmodel = { module = "cafe.adriel.voyager:voyager-screenmodel", version.ref = "voyager" }
     voyager-transitions = { module = "cafe.adriel.voyager:voyager-transitions", version.ref = "voyager" }
     voyager-koin = { module = "cafe.adriel.voyager:voyager-koin", version.ref = "voyager" }
     
     # DI
     koin-core = { module = "io.insert-koin:koin-core", version.ref = "koin" }
     koin-compose = { module = "io.insert-koin:koin-compose", version.ref = "koin" }
     koin-compose-viewmodel = { module = "io.insert-koin:koin-compose-viewmodel", version.ref = "koin" }
     
     # Permissions & Resources
     moko-permissions = { module = "dev.icerock.moko:permissions", version.ref = "moko-permissions" }
     moko-resources = { module = "dev.icerock.moko:resources", version.ref = "moko-resources" }
     
     # Image Loading
     kamel = { module = "media.kamel:kamel-image", version.ref = "kamel" }
     
     # DateTime
     kotlinx-datetime = { module = "org.jetbrains.kotlinx:kotlinx-datetime", version.ref = "kotlinx-datetime" }
     
     [plugins]
     kotlin-multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
     compose-multiplatform = { id = "org.jetbrains.compose", version.ref = "compose-multiplatform" }
     ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
     buildkonfig = { id = "com.codingfeline.buildkonfig", version.ref = "buildkonfig" }
     realm = { id = "io.realm.kotlin", version.ref = "realm" }
```

2. **Dependency Injection**: Koin (recommended for KMP) or Kodein
    - Set up multi-module DI configuration
    - Implement scope management for ViewModels

3. **Networking**: Ktorfit with Platform-Specific Engines
```kotlin
   // In commonMain
   expect fun httpClient(): HttpClient
   
   // In androidMain
   actual fun httpClient(): HttpClient = HttpClient(OkHttp) {
       install(ContentNegotiation) {
           json(Json { 
               isLenient = true
               ignoreUnknownKeys = true 
           })
       }
       install(HttpTimeout) {
           requestTimeoutMillis = 15000
       }
       install(Logging) {
           logger = Logger.DEFAULT
           level = LogLevel.ALL
       }
   }
   
   // In iosMain
   actual fun httpClient(): HttpClient = HttpClient(Darwin) {
       install(ContentNegotiation) {
           json(Json { 
               isLenient = true
               ignoreUnknownKeys = true 
           })
       }
       install(HttpTimeout) {
           requestTimeoutMillis = 15000
       }
       engine {
           configureRequest {
               setAllowsCellularAccess(true)
               setTimeoutIntervalForRequest(15.0)
               setTimeoutIntervalForResource(15.0)
           }
       }
   }
```

4. **Local Database**: Realm Kotlin
    - Version: Latest stable (2.0.0+)
    - Configure for both Android and iOS
    - Set up schema versioning and migration strategy
    - Implement repository pattern for data access

5. **Navigation**: Voyager
    - Version: 1.1.0-beta02 or latest
    - Configure with ScreenModel integration
    - Set up bottom navigation and tab navigation
    - Implement deep linking support
    - Add transition animations

6. **Resource Management**:
    - Compose Multiplatform Resources for basic resources
    - Moko Resources for advanced localization

7. **Permission Handling**: moko-permissions
    - Configure for both platforms
    - Create permission wrapper for common permissions

8. **Image Loading**: Kamel
    - Configure with caching strategy
    - Add placeholder and error handling
    - Set up memory and disk cache

9. **Date/Time**: kotlinx-datetime
    - Configure timezone handling
    - Create utility extensions

### Environment Configuration with BuildKonfig

Create multiple build configurations for different environments:
```kotlin
// shared/build.gradle.kts
buildkonfig {
    packageName = "com.yourapp.shared.config"
    
    defaultConfigs {
        buildConfigField(STRING, "API_BASE_URL", "")
        buildConfigField(STRING, "API_KEY", "")
        buildConfigField(BOOLEAN, "IS_DEBUG", "false")
        buildConfigField(STRING, "DATABASE_NAME", "app.realm")
        buildConfigField(INT, "DATABASE_VERSION", "1")
    }
    
    defaultConfigs("debug") {
        buildConfigField(STRING, "API_BASE_URL", "https://api-dev.yourapp.com")
        buildConfigField(STRING, "API_KEY", "debug_key_12345")
        buildConfigField(BOOLEAN, "IS_DEBUG", "true")
        buildConfigField(STRING, "DATABASE_NAME", "app_debug.realm")
    }
    
    defaultConfigs("staging") {
        buildConfigField(STRING, "API_BASE_URL", "https://api-staging.yourapp.com")
        buildConfigField(STRING, "API_KEY", getEnvironmentVariable("STAGING_API_KEY"))
        buildConfigField(BOOLEAN, "IS_DEBUG", "true")
        buildConfigField(STRING, "DATABASE_NAME", "app_staging.realm")
    }
    
    defaultConfigs("release") {
        buildConfigField(STRING, "API_BASE_URL", "https://api.yourapp.com")
        buildConfigField(STRING, "API_KEY", getEnvironmentVariable("PROD_API_KEY"))
        buildConfigField(BOOLEAN, "IS_DEBUG", "false")
        buildConfigField(STRING, "DATABASE_NAME", "app.realm")
    }
}
```

### Project Structure
```
project-root/
├── gradle/
│   └── libs.versions.toml
├── buildSrc/
│   ├── build.gradle.kts
│   └── src/main/kotlin/
│       ├── Dependencies.kt
│       └── BuildConfig.kt
├── shared/
│   ├── build.gradle.kts
│   ├── commonMain/
│   │   ├── kotlin/
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   │   ├── database/
│   │   │   │   │   └── preferences/
│   │   │   │   ├── remote/
│   │   │   │   │   ├── api/
│   │   │   │   │   ├── dto/
│   │   │   │   │   └── client/
│   │   │   │   │       └── HttpClientFactory.kt
│   │   │   │   └── repository/
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   └── usecase/
│   │   │   ├── presentation/
│   │   │   │   ├── navigation/
│   │   │   │   ├── screens/
│   │   │   │   ├── viewmodel/
│   │   │   │   └── components/
│   │   │   ├── di/
│   │   │   │   ├── Modules.kt
│   │   │   │   ├── NetworkModule.kt
│   │   │   │   ├── DatabaseModule.kt
│   │   │   │   └── ViewModelModule.kt
│   │   │   └── config/
│   │   │       └── AppConfig.kt
│   │   └── resources/
│   ├── androidMain/
│   │   └── kotlin/
│   │       └── client/
│   │           └── HttpClientFactory.android.kt
│   └── iosMain/
│       └── kotlin/
│           └── client/
│               └── HttpClientFactory.ios.kt
├── androidApp/
│   ├── build.gradle.kts
│   └── src/main/
│       └── kotlin/
│           └── MainActivity.kt
└── iosApp/
    └── iosApp/
        └── ContentView.swift
```

### Gradle Configuration Requirements

1. Root build.gradle.kts:
```kotlin
plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.buildkonfig) apply false
    alias(libs.plugins.realm) apply false
}
```

2. Shared module build.gradle.kts with platform-specific configurations:
```kotlin
kotlin {
    androidTarget()
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            // Add all common dependencies
            implementation(libs.ktorfit)
            implementation(libs.ktor.client.core)
            implementation(libs.realm.base)
            // ... other dependencies
        }
        
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}
```

### Future Considerations: Amper Build System

Include a note about potential migration to Amper (JetBrains' experimental build system):
```yaml
# module.yaml (Amper configuration example)
product:
  type: kmp-app
  platforms: [android, iosArm64, iosSimulatorArm64, iosX64]

dependencies:
  - io.ktor:ktor-client-core:3.0.0
  - de.jensklingenberg.ktorfit:ktorfit-lib:2.1.0
  - cafe.adriel.voyager:voyager-navigator:1.1.0-beta02
  
settings:
  kotlin:
    version: 2.2.0
  compose:
    enabled: true
```

### Implementation Tasks

1. Create base classes with BuildKonfig integration:
    - BaseViewModel with environment-aware configuration
    - BaseRepository with dynamic API endpoints
    - NetworkClient with BuildKonfig values

2. Set up platform-specific HTTP clients:
    - OkHttp for Android with interceptors
    - Darwin for iOS with native configuration

3. Configure build variants:
    - Debug/Staging/Release with BuildKonfig
    - Environment-specific API keys and endpoints
    - Platform-specific ProGuard/R8 rules

4. Create sample implementation:
    - Login screen using BuildKonfig API URL
    - List screen with proper error handling
    - Environment indicator for debug builds

5. Set up CI/CD with environment variables:
    - GitHub Actions with secrets management
    - Build matrix for different environments
    - Automated deployment configuration

### Quality Checks

Ensure the template includes:
- Environment-specific configurations via BuildKonfig
- Centralized dependency management via Version Catalog
- Platform-optimized HTTP clients (OkHttp for Android, Darwin for iOS)
- Proper separation of debug/staging/production configurations
- Security best practices for API key management

The final template should be production-ready, scalable, and follow Kotlin/KMP best practices with proper environment management and platform-specific optimizations.