# 📱 PengeluaranApp - Project Summary

## 🎯 Overview
**PengeluaranApp** adalah aplikasi manajemen pengeluaran yang dibangun dengan **Kotlin Multiplatform (KMP)** menggunakan **Compose Multiplatform**, menargetkan platform **Android** dan **iOS**.

## 🏗️ Arsitektur

Project ini menggunakan **Clean Architecture** dengan **modularisasi berbasis feature** untuk memastikan:
- ✅ Separation of Concerns
- ✅ Scalability & Maintainability
- ✅ Testability
- ✅ Reusability
- ✅ Independent Development

### Struktur Module

```
PengeluaranApp/
│
├── composeApp/                    # Main application module
│   ├── androidMain/               # Android-specific code
│   ├── iosMain/                   # iOS-specific code
│   └── commonMain/                # Shared code
│
├── core/                          # Core modules (shared infrastructure)
│   ├── model/                     # Domain models & entities
│   ├── domain/                    # Business logic interfaces
│   ├── network/                   # Network layer (DTOs, API)
│   ├── data/                      # Data layer implementation
│   ├── database/                  # Database configuration
│   ├── ui/                        # Shared UI components
│   ├── designsystem/              # Design system (theme, colors)
│   └── common/                    # Common utilities
│
├── features/                      # Feature modules
│   ├── login/                     # Login feature
│   └── home/                      # Home feature
│
├── iosApp/                        # iOS app entry point
└── docs/                          # Documentation
```

## 📦 Module Dependencies

### Dependency Flow
```
features → core/data → core/domain → core/model
              ↓
         core/network
              ↓
         core/database
```

### Core Modules

| Module | Tujuan | Dependencies |
|--------|--------|--------------|
| **core:model** | Domain models dengan @Serializable | kotlinx-serialization |
| **core:domain** | Business logic interfaces (Repository, UseCase) | core:model, kotlinx-coroutines |
| **core:network** | Network DTOs & API interfaces | core:model, Ktor |
| **core:data** | Repository implementations | core:domain, core:network, core:database |
| **core:database** | Database setup (Room, SQLDelight) | - |
| **core:ui** | Shared UI components | Compose |
| **core:designsystem** | Theme, Colors, Typography | Compose |
| **core:common** | Utilities & extensions | - |

### Feature Modules

| Feature | Deskripsi | Status |
|---------|-----------|--------|
| **features:login** | Authentication (Login/Register) | ✅ Implemented |
| **features:home** | Dashboard & expense overview | ✅ Implemented |

## 🎨 Tech Stack

### Core Technologies
- **Kotlin Multiplatform** - Cross-platform development
- **Compose Multiplatform** - Declarative UI framework
- **Kotlin Coroutines** - Asynchronous programming
- **Kotlinx Serialization** - JSON serialization

### Networking
- **Ktor Client** - HTTP client
- **Ktorfit** - Type-safe HTTP client (Retrofit-like)

### Dependency Injection
- **Koin** - Lightweight DI framework

### Navigation
- **Voyager** - Type-safe navigation for Compose

### Database
- **Room** (Android) - Local database
- **SQLDelight** (potential for iOS) - Cross-platform database

### Image Loading
- **Kamel** - Compose Image Loading

### Other Libraries
- **Moko Permissions** - Runtime permissions
- **Moko Resources** - Resource management
- **BuildKonfig** - Build configuration

## 📂 Package Structure

### Core Packages
```
com.dhimas.pengeluaranapp
├── core
│   ├── model/                  # User, Expense, etc.
│   ├── domain/
│   │   ├── repository/         # UserRepository, ExpenseRepository
│   │   └── usecase/            # LoginUseCase, GetExpensesUseCase
│   ├── network/
│   │   └── dto/                # LoginRequest, LoginResponse, etc.
│   └── data/
│       ├── repository/         # Repository implementations
│       ├── local/              # Local data sources
│       └── remote/             # Remote data sources (API)
```

### Feature Package Structure
```
features/[feature-name]/
├── data/                       # Feature-specific DTOs (optional)
├── domain/                     # Feature-specific use cases
├── ui/
│   ├── screens/                # Screen composables
│   ├── components/             # Reusable components
│   └── viewmodel/              # ViewModels/ScreenModels
└── di/                         # Koin modules
```

## 🔄 Data Flow

```
UI (Composables)
      ↓
ViewModel/ScreenModel
      ↓
UseCase (Business Logic)
      ↓
Repository (Interface)
      ↓
Repository Implementation
      ↓
Data Sources (Local/Remote)
      ↓
Database / API
```

## 🎯 Design Patterns

### 1. **Repository Pattern**
Memisahkan data layer dari business logic
```kotlin
interface UserRepository {
    suspend fun loginUser(email: String, password: String): Result<User>
}
```

### 2. **Use Case Pattern**
Enkapsulasi business logic dalam single responsibility classes
```kotlin
class LoginUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User>
}
```

### 3. **DTO Pattern** (Simplified)
Menggunakan single model dengan @Serializable untuk network dan domain
```kotlin
@Serializable
data class User(
    val id: String,
    val username: String,
    val email: String
)
```

### 4. **Dependency Injection**
Menggunakan Koin untuk constructor injection
```kotlin
val domainModule = module {
    factory { LoginUseCase(get()) }
}
```

## 📱 Platform-Specific Code

### Android
- **Room Database** untuk persistent storage
- **Android Material Components**
- **Activity & Fragment** entry points

### iOS
- **Swift wrapper** untuk Kotlin code
- **SwiftUI** untuk platform-specific UI (jika diperlukan)

## 🔐 Authentication Flow

```
Login Screen
    ↓
LoginViewModel
    ↓
LoginUseCase
    ↓
UserRepository
    ↓
AuthApi (Ktor)
    ↓
Backend API
    ↓
Save token & user data locally
    ↓
Navigate to Home
```

## 📊 State Management

Menggunakan **StateFlow** untuk reactive state management:
```kotlin
class LoginScreenModel : ScreenModel {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
}
```

## 🧪 Testing Strategy

### Unit Tests
- **Use Cases** - Business logic validation
- **ViewModels** - State management testing
- **Repositories** - Data layer testing

### Integration Tests
- **API Communication** - Mock server tests
- **Database Operations** - Room/SQLDelight tests

### UI Tests
- **Compose Tests** - UI component testing

## 🚀 Build & Run

### Android
```bash
./gradlew :composeApp:assembleDebug
```

### iOS
```bash
# Open iosApp in Xcode and run
```

## 📈 Project Status

- ✅ Project setup & architecture
- ✅ Core modules implemented
- ✅ Login feature complete
- ✅ Home feature complete
- ✅ Network layer configured
- ✅ Dependency injection setup
- 🔄 Database implementation (in progress)
- 📝 Add expense feature (planned)
- 📝 Expense history feature (planned)
- 📝 Reports & analytics (planned)

## 🔗 Key Files

| File | Purpose |
|------|---------|
| `settings.gradle.kts` | Module configuration |
| `gradle/libs.versions.toml` | Dependency versions |
| `composeApp/build.gradle.kts` | Main app configuration |
| `core/*/build.gradle.kts` | Core module configurations |
| `features/*/build.gradle.kts` | Feature module configurations |

## 📚 Documentation

- 📖 [Feature Development Guide](./FEATURE_DEVELOPMENT_GUIDE.md)
- 📖 [DTO Architecture](./DTO_ARCHITECTURE_SIMPLIFIED.md)
- 📖 [App Guide](../AppGuide.md)

## 👥 Development Team

Project ini menggunakan modular architecture yang memungkinkan:
- **Parallel Development** - Multiple developers can work on different features
- **Feature Ownership** - Clear responsibility per feature module
- **Easy Onboarding** - New developers can focus on specific modules

## 🔧 Development Tools

- **IDE**: Android Studio / IntelliJ IDEA
- **Build System**: Gradle with Kotlin DSL
- **Version Control**: Git
- **CI/CD**: (To be configured)

## 📝 Code Style

- **Kotlin Coding Conventions** - Follow official Kotlin style guide
- **Package Naming**: `com.dhimas.pengeluaranapp.[layer].[module]`
- **Class Naming**: PascalCase
- **Function Naming**: camelCase
- **Constants**: UPPER_SNAKE_CASE

## 🎓 Learning Resources

- [Kotlin Multiplatform Docs](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Koin Documentation](https://insert-koin.io/)
- [Ktor Documentation](https://ktor.io/)

---

**Last Updated**: October 26, 2025
**Version**: 1.0.0

