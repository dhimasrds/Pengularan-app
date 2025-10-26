# DTO Architecture Documentation

## 📐 Struktur DTO Berdasarkan Opsi 2: Shared DTOs di Core

### Lokasi File DTO

```
core/
  network/
    dto/
      ├── LoginRequest.kt      → Request DTO untuk authentication
      ├── LoginResponse.kt     → Response DTO dari API login
      ├── UserDto.kt           → User data dari network layer
      └── UserDtoMapper.kt     → Mapper untuk konversi DTO ↔ Domain
      
core/
  model/
    └── User.kt                → Domain model (Pure business logic)
```

### 🔄 Data Flow

```
API Response (JSON)
      ↓
LoginResponse (DTO)
      ↓ 
  UserDto (DTO)
      ↓ [UserDtoMapper.toDomain()]
  User (Domain Model)
      ↓
Repository → UseCase → UI
```

### 📦 Package Organization

**Network DTOs**: `com.dhimas.pengeluaranapp.core.network.dto`
- LoginRequest.kt
- LoginResponse.kt  
- UserDto.kt
- UserDtoMapper.kt

**Domain Models**: `com.dhimas.pengeluaranapp.core.model`
- User.kt

### 🎯 Prinsip Arsitektur

1. **DTOs di Network Layer** (`core/network`)
   - Hanya untuk serialisasi/deserialisasi API
   - Annotated dengan `@Serializable`
   - Struktur mengikuti API contract

2. **Domain Models di Model Layer** (`core/model`)
   - Clean dari annotation serialization
   - Representasi business logic
   - Stable terhadap perubahan API

3. **Mapper Pattern**
   - Extension functions untuk konversi
   - `UserDto.toDomain()` → User
   - `User.toDto()` → UserDto

### 📝 Contoh Penggunaan

#### Di Repository:
```kotlin
override suspend fun loginUser(email: String, password: String): Result<User> {
    return try {
        val response = authApi.login(LoginRequest(email, password))
        val user = response.user.toDomain()  // ✨ Konversi DTO → Domain
        saveUser(user)
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

#### Di AuthApi:
```kotlin
@POST("auth/login")
suspend fun login(@Body request: LoginRequest): LoginResponse
```

### ✅ Keuntungan Struktur Ini

1. **Shared DTOs**: Bisa digunakan di semua feature yang butuh
2. **Separation of Concerns**: Network layer terpisah dari Domain
3. **Maintainability**: Perubahan API tidak affect domain model
4. **Testability**: Mudah mock DTO untuk testing
5. **Type Safety**: Compile-time checking dengan mapper

### 🔧 Dependencies

**core/network** memerlukan:
```kotlin
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
```

**Modules yang depend on core/network**:
- core/data
- composeApp

### 🚀 Future: Feature-Specific DTOs

Untuk DTOs yang spesifik hanya untuk 1 feature:
```
feature/
  auth/
    data/
      dto/
        RegisterRequest.kt    → Hanya untuk auth feature
        ResetPasswordDto.kt   → Hanya untuk auth feature
```

### 📊 Summary

| Layer | Location | Purpose | Example |
|-------|----------|---------|---------|
| DTO | `core/network/dto` | API serialization | `LoginRequest`, `UserDto` |
| Domain | `core/model` | Business logic | `User` |
| Mapper | `core/network/dto` | Conversion | `UserDtoMapper` |

