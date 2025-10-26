# Panduan Membuat Feature Baru (KMP + Compose + Koin + Voyager)

Dokumen ini menjelaskan langkah demi langkah cara menambahkan modul feature baru di proyek ini agar konsisten dengan struktur modular dan Clean Architecture yang sudah diadopsi. Ikuti panduan ini untuk menghindari implementasi yang keliru atau ketergantungan silang yang tidak diinginkan.

Fokus: Kotlin Multiplatform + Compose Multiplatform + Koin (DI) + Voyager (Navigasi)

---

## 1) Prasyarat & Prinsip

- Struktur proyek saat ini membedakan antara:
  - core: model, domain, common, network, database, data, ui, designsystem
  - features: login, home, dan fitur lain (dibuat serupa)
- Aturan dependensi berarah (Clean Architecture):
  - features → core:domain, core:model, core:ui, core:designsystem, core:common
  - features TIDAK bergantung langsung pada core:data, core:network, core:database
  - core:data bergantung pada domain + network + database
  - composeApp (app) mengagregasi modul Koin dan mengatur navigasi
- Navigasi antar feature dilakukan via kontrak kecil FeatureApi (loose-coupled entry point per feature).

---

## 2) Penamaan & Lokasi

- Nama modul fitur: `:features:<nama>` (huruf kecil, tanpa spasi), contoh: `:features:expense`
- Paket Kotlin: `com.dhimas.pengeluaranapp.features.<nama>`
  - Pisahkan `api/` (kontrak publik) dan `impl/` (implementasi internal)
- Struktur direktori minimal:
  - `features/<nama>/build.gradle.kts`
  - `features/<nama>/src/commonMain/kotlin/com/dhimas/pengeluaranapp/features/<nama>/api/...`
  - `features/<nama>/src/commonMain/kotlin/com/dhimas/pengeluaranapp/features/<nama>/impl/...`

---

## 3) Langkah Cepat (Checklist)

1. Tambah include di `settings.gradle.kts`:
   - `include(":features:<nama>")`
2. Buat folder `features/<nama>` dan file `build.gradle.kts` (lihat template di bawah).
3. Buat kontrak Feature API: `<Nama>FeatureApi` dengan fungsi `entryScreen(): Screen`.
4. Buat modul Koin: `<nama>FeatureModule` yang `single { <Nama>FeatureApiImpl() }`.
5. Buat `Screen` utama (`<Nama>Screen`) yang mengimplementasikan `cafe.adriel.voyager.core.screen.Screen`.
6. (Opsional) Buat ViewModel (Voyager ScreenModel atau KMP ViewModel) dan inject via Koin.
7. Registrasi modul fitur di agregator Koin app (`composeApp/.../di/Modules.kt`).
8. (Opsional) Ganti bootstrap navigator di `App.kt` untuk menggunakan `FeatureApi.entryScreen()`.
9. Build & run. Pastikan tidak ada dependensi langsung dari `features:*` ke `core:data|network|database`.

---

## 4) Template Gradle untuk Modul Feature

Contoh `features/expense/build.gradle.kts` (salin dari `features/login` dan ganti nama):

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget()
    listOf(iosX64(), iosArm64(), iosSimulatorArm64())

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)

                implementation(libs.voyager.navigator)
                implementation(libs.voyager.screenmodel)
                implementation(libs.voyager.transitions)

                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                implementation(projects.core.model)
                implementation(projects.core.domain)
                implementation(projects.core.ui)
                implementation(projects.core.designsystem)
                implementation(projects.core.common)
            }
        }
        val androidMain by getting {
            dependencies { implementation(libs.androidx.activity.compose) }
        }
        val iosMain by getting { }
    }
}
```

Catatan:
- Gunakan versi library dari `gradle/libs.versions.toml` (jangan menambahkan/merubah versi di modul).
- Hindari menambahkan dependensi ke `projects.core.data`, `projects.core.network`, atau `projects.core.database` dalam modul fitur.

---

## 5) Kontrak Feature API

Letakkan di `features/<nama>/src/commonMain/.../api/<Nama>FeatureApi.kt`:

```kotlin
interface ExpenseFeatureApi {
    fun entryScreen(): cafe.adriel.voyager.core.screen.Screen
}
```

---

## 6) Implementasi + Koin Module + Screen

Letakkan di `features/<nama>/src/commonMain/.../impl/ExpenseFeatureModule.kt`:

```kotlin
val expenseFeatureModule = org.koin.dsl.module {
    single<com.dhimas.pengeluaranapp.features.expense.api.ExpenseFeatureApi> { ExpenseFeatureApiImpl() }
    // single { ExpenseViewModel(get(), ...) } // contoh jika butuh VM/use case
}

private class ExpenseFeatureApiImpl : com.dhimas.pengeluaranapp.features.expense.api.ExpenseFeatureApi {
    override fun entryScreen(): cafe.adriel.voyager.core.screen.Screen = ExpenseScreen()
}
```

Screen utama `ExpenseScreen.kt`:

```kotlin
class ExpenseScreen : cafe.adriel.voyager.core.screen.Screen {
    @androidx.compose.runtime.Composable
    override fun Content() {
        androidx.compose.material3.Scaffold { _ ->
            androidx.compose.material3.Text("Expense Feature")
        }
    }
}
```

---

## 7) Registrasi di App (Koin + Navigasi)

- Tambahkan modul fitur ke agregator Koin di `composeApp/src/commonMain/kotlin/.../di/Modules.kt`:

```kotlin
// tambahkan expenseFeatureModule ke daftar modul aplikasi

val appModules = listOf(
    // ... existing core modules
    // ... existing feature modules
    expenseFeatureModule,
)
```

- Opsi navigasi via API (loose-coupled) di `App.kt`:

```kotlin
@androidx.compose.runtime.Composable
fun App() {
    org.koin.compose.KoinApplication({ modules(appModules) }) {
        val api = org.koin.compose.getKoin().get<com.dhimas.pengeluaranapp.features.expense.api.ExpenseFeatureApi>()
        androidx.compose.material3.MaterialTheme {
            cafe.adriel.voyager.navigator.Navigator(api.entryScreen()) { nav ->
                cafe.adriel.voyager.transitions.SlideTransition(nav)
            }
        }
    }
}
```

Jika belum ingin mengganti entry screen, Anda tetap bisa memulai dari `LoginScreen()` dan melakukan navigasi ke feature lain dengan memanggil `getKoin().get<ExpenseFeatureApi>().entryScreen()` dari tombol/aksi tertentu.

---

## 8) Batasan DI (Clean Architecture)

- Modul fitur hanya mengonsumsi kontrak domain (use case / repository interface) dari `:core:domain` dan model dari `:core:model`.
- Binding implementasi repository (misal `UserRepositoryImpl`) dilakukan di `:core:data` dan dimuat oleh app melalui Koin.
- Modul fitur tidak boleh tahu cara data didapatkan (network/database) — hanya bergantung pada kontrak domain.

---

## 9) Contoh Nyata: Menambah :features:expense

1. settings.gradle.kts
   ```kotlin
   include(":features:expense")
   ```
2. Folder & Gradle module `features/expense/build.gradle.kts` (pakai template di atas).
3. Buat API + impl + screen seperti pada bagian 5 & 6.
4. Registrasikan `expenseFeatureModule` ke `appModules`.
5. (Opsional) Tambah tombol di `HomeScreen` untuk `Navigator.push(getKoin().get<ExpenseFeatureApi>().entryScreen())`.
6. Sync Gradle → Build → Jalankan.

---

## 10) Pitfall Umum & Cara Menghindarinya

- Lupa menambahkan `include(":features:<nama>")` di settings.gradle.kts → modul tidak dikenali Gradle.
- Menambahkan dependensi langsung ke `:core:data` di modul fitur → melanggar boundary; pindahkan ketergantungan ke `:core:domain`.
- Tidak menambahkan `androidx.activity:activity-compose` di `androidMain` → crash saat integrasi lifecycle/Compose di Android.
- Salah paket atau nama kelas (mis. `FeatureApiImpl` private tidak berada di paket yang sama) → perbaiki paket sesuai contoh.
- Tidak mendaftarkan modul Koin fitur di `appModules` → `NoBeanDefFoundException` saat resolve `FeatureApi`.
- Mengubah versi library di modul fitur → versi harus terkelola terpusat di `libs.versions.toml`.

---

## 11) Checklist Validasi (Sebelum Commit)

- [ ] settings.gradle.kts sudah men-include modul fitur baru.
- [ ] build.gradle.kts modul fitur mengikuti template plugin & dependencies.
- [ ] Ada `FeatureApi` pada paket `...features.<nama>.api` dan mengembalikan `Screen`.
- [ ] Ada `FeatureModule` Koin yang mendaftarkan `FeatureApi` + VM (jika perlu).
- [ ] Tidak ada referensi ke `core:data`, `core:network`, atau `core:database` dari modul fitur.
- [ ] `appModules` memasukkan modul fitur baru.
- [ ] Build sukses; aplikasi bisa launch.

---

## 12) Testing Singkat

- Unit test untuk ViewModel/Presenter fitur bisa berada di `features/<nama>/src/commonTest` (KMP) atau `androidTest` (khusus Android).
- Mock kontrak domain via Koin test module.
- UI test (Android) gunakan Compose UI Test pada target Android.

---

## 13) Referensi & Contoh Lain

- Lihat contoh yang sudah ada: `:features:login`, `:features:home`.
- Rujuk `AppGuide.md` untuk gambaran arsitektur lengkap dan alasan pemilihan pola modular ini.

---

Selamat berkarya! Dengan mengikuti panduan ini, penambahan fitur baru akan konsisten, terukur, dan meminimalkan risiko regresi antar modul.