# DTO Architecture - Simplified Version

## 📐 Struktur Baru (Simplified)

Setelah refactoring, kita tidak lagi menggunakan pola DTO terpisah. Semua menggunakan satu class `User` dengan `@Serializable`.

### Lokasi File

```
core/
  model/
    └── User.kt                 ✅ Domain model dengan @Serializable
      
core/
  network/
    dto/
      ├── LoginRequest.kt       ✅ Request DTO
      └── LoginResponse.kt      ✅ Response DTO (menggunakan User langsung)
```

### 🔄 Data Flow (Simplified)

```
API Response (JSON)
      ↓
LoginResponse {
    token: String,
    user: User  ← Langsung domain model!
}
      ↓
Repository → UseCase → UI
```

### 📦 Package Organization

**Network DTOs**: `com.dhimas.pengeluaranapp.core.network.dto`
- LoginRequest.kt
- LoginResponse.kt

**Domain Models**: `com.dhimas.pengeluaranapp.core.model`
- User.kt (dengan @Serializable)

### ✅ Keuntungan Struktur Simplified

1. **Lebih Sederhana**: Tidak perlu mapper dan class duplikat
2. **Less Boilerplate**: Tidak perlu konversi DTO → Domain
3. **Easier Maintenance**: Hanya satu class yang perlu diupdate
4. **Suitable**: Cocok untuk aplikasi dengan struktur API yang stabil

### 📝 Contoh Penggunaan

#### User.kt (Domain Model dengan Serialization):
```kotlin
@Serializable
data class User(
    val id: String,
    val username: String,
    val email: String,
    val createdAt: Long,
    val updatedAt: Long
)
```

#### LoginResponse.kt:
```kotlin
@Serializable
data class LoginResponse(
    val token: String,
    val user: User  // Langsung gunakan domain model
)
```

#### Di Repository:
```kotlin
override suspend fun loginUser(email: String, password: String): Result<User> {
    return try {
        val response = authApi.login(LoginRequest(email, password))
        val user = response.user  // ✨ Langsung ambil tanpa konversi!
        saveUser(user)
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

### 🎯 Perubahan dari Struktur Sebelumnya

| Sebelumnya | Sekarang |
|------------|----------|
| `UserDto` + `User` | Hanya `User` |
| `UserDtoMapper.kt` | ❌ Dihapus |
| `response.user.toDomain()` | `response.user` |
| 2 class untuk satu entity | 1 class |

### ⚠️ Trade-offs

**Keuntungan:**
- ✅ Lebih sederhana dan mudah dipahami
- ✅ Tidak perlu mapping code
- ✅ Cocok untuk aplikasi kecil-menengah

**Kekurangan:**
- ⚠️ Domain model coupled dengan kotlinx.serialization
- ⚠️ Jika API berubah, domain model ikut berubah
- ⚠️ Tidak bisa melakukan transformasi data dengan mudah

### 🚀 Kapan Menggunakan Pola Simplified Ini?

✅ **Gunakan jika:**
- API struktur stabil dan jarang berubah
- Tim kecil dan aplikasi tidak terlalu kompleks
- Prioritas pada kesederhanaan code

❌ **Jangan gunakan jika:**
- API sering berubah dan tidak stabil
- Butuh transformasi kompleks dari API ke domain
- Aplikasi besar dengan banyak layer

### 📊 Summary

Dengan struktur simplified ini, kita mengorbankan sedikit flexibility untuk mendapatkan kesederhanaan. Cocok untuk aplikasi Pengeluaran yang fokusnya adalah MVP dan iterasi cepat.

