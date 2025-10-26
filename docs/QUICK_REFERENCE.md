# 📝 Quick Reference: Feature Development

## 🚀 Quick Start Checklist

### Langkah Cepat Membuat Feature Baru

```bash
# 1. Nama feature (contoh: expense, profile, statistics)
FEATURE_NAME="expense"

# 2. Update settings.gradle.kts
# Tambahkan: include(":features:$FEATURE_NAME")

# 3. Buat folder structure
mkdir -p features/$FEATURE_NAME/src/commonMain/kotlin/com/dhimas/pengeluaranapp/features/$FEATURE_NAME/{domain/usecase,ui/{screens,components,viewmodel},di}

# 4. Copy template build.gradle.kts dari feature lain
cp features/login/build.gradle.kts features/$FEATURE_NAME/build.gradle.kts

# 5. Update namespace di build.gradle.kts
# namespace = "com.dhimas.pengeluaranapp.features.$FEATURE_NAME"
```

---

## 📁 File Template

### build.gradle.kts
```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget()
    listOf(iosX64(), iosArm64(), iosSimulatorArm64())
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.koin)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            
            // Core modules
            implementation(projects.core.domain)
            implementation(projects.core.model)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
        }
    }
}

android {
    namespace = "com.dhimas.pengeluaranapp.features.FEATURE_NAME"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
```

### Screen Template
```kotlin
package com.dhimas.pengeluaranapp.features.FEATURE.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator

class MyScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<MyScreenModel>()
        val uiState by screenModel.uiState.collectAsState()
        
        MyScreenContent(
            uiState = uiState,
            onAction = screenModel::handleAction
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyScreenContent(
    uiState: MyUiState,
    onAction: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("My Screen") }) }
    ) { paddingValues ->
        // Your UI here
    }
}
```

### ScreenModel Template
```kotlin
package com.dhimas.pengeluaranapp.features.FEATURE.ui.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MyUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: String = ""
)

class MyScreenModel(
    private val useCase: MyUseCase
) : ScreenModel {
    
    private val _uiState = MutableStateFlow(MyUiState())
    val uiState: StateFlow<MyUiState> = _uiState.asStateFlow()
    
    fun handleAction() {
        screenModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val result = useCase()
            
            result.fold(
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        data = data.toString()
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }
}
```

### UseCase Template
```kotlin
package com.dhimas.pengeluaranapp.features.FEATURE.domain.usecase

import com.dhimas.pengeluaranapp.core.domain.repository.MyRepository

class MyUseCase(
    private val repository: MyRepository
) {
    suspend operator fun invoke(param: String): Result<String> {
        // Validation
        if (param.isBlank()) {
            return Result.failure(Exception("Parameter cannot be empty"))
        }
        
        // Business logic
        return repository.doSomething(param)
    }
}
```

### DI Module Template
```kotlin
package com.dhimas.pengeluaranapp.features.FEATURE.di

import com.dhimas.pengeluaranapp.features.FEATURE.domain.usecase.*
import com.dhimas.pengeluaranapp.features.FEATURE.ui.viewmodel.*
import org.koin.dsl.module

val featureModule = module {
    // Use Cases
    factory { MyUseCase(get()) }
    
    // ViewModels
    factory { MyScreenModel(get()) }
}
```

---

## 🎯 Core Layer Templates

### Model (core:model)
```kotlin
package com.dhimas.pengeluaranapp.core.model

import kotlinx.serialization.Serializable

@Serializable
data class MyEntity(
    val id: String,
    val name: String,
    val createdAt: Long
)
```

### Repository Interface (core:domain)
```kotlin
package com.dhimas.pengeluaranapp.core.domain.repository

import com.dhimas.pengeluaranapp.core.model.MyEntity
import kotlinx.coroutines.flow.Flow

interface MyRepository {
    suspend fun getById(id: String): Result<MyEntity>
    suspend fun getAll(): Flow<List<MyEntity>>
    suspend fun create(entity: MyEntity): Result<MyEntity>
    suspend fun update(entity: MyEntity): Result<MyEntity>
    suspend fun delete(id: String): Result<Unit>
}
```

### Repository Implementation (core:data)
```kotlin
package com.dhimas.pengeluaranapp.core.data.repository

import com.dhimas.pengeluaranapp.core.domain.repository.MyRepository
import com.dhimas.pengeluaranapp.core.model.MyEntity
import com.dhimas.pengeluaranapp.core.data.remote.api.MyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MyRepositoryImpl(
    private val api: MyApi
) : MyRepository {
    
    override suspend fun getById(id: String): Result<MyEntity> {
        return try {
            val response = api.getById(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAll(): Flow<List<MyEntity>> = flow {
        try {
            val response = api.getAll()
            emit(response)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    
    override suspend fun create(entity: MyEntity): Result<MyEntity> {
        return try {
            val response = api.create(entity)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun update(entity: MyEntity): Result<MyEntity> {
        return try {
            val response = api.update(entity.id, entity)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun delete(id: String): Result<Unit> {
        return try {
            api.delete(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### API Interface (core:data)
```kotlin
package com.dhimas.pengeluaranapp.core.data.remote.api

import com.dhimas.pengeluaranapp.core.model.MyEntity
import de.jensklingenberg.ktorfit.http.*

interface MyApi {
    @GET("entities/{id}")
    suspend fun getById(@Path("id") id: String): MyEntity
    
    @GET("entities")
    suspend fun getAll(): List<MyEntity>
    
    @POST("entities")
    suspend fun create(@Body entity: MyEntity): MyEntity
    
    @PUT("entities/{id}")
    suspend fun update(
        @Path("id") id: String,
        @Body entity: MyEntity
    ): MyEntity
    
    @DELETE("entities/{id}")
    suspend fun delete(@Path("id") id: String)
}
```

---

## 🔗 Common Patterns

### Navigation
```kotlin
// Push new screen
navigator.push(NewScreen())

// Pop current screen
navigator.pop()

// Replace current screen
navigator.replace(NewScreen())

// Pop to root
navigator.popUntilRoot()
```

### State Updates
```kotlin
// Update state immutably
_uiState.value = _uiState.value.copy(
    isLoading = false,
    data = newData
)

// Collect StateFlow
val uiState by screenModel.uiState.collectAsState()
```

### Error Handling
```kotlin
result.fold(
    onSuccess = { data ->
        // Handle success
    },
    onFailure = { error ->
        // Handle error
        _uiState.value = _uiState.value.copy(
            error = error.message ?: "Unknown error"
        )
    }
)
```

---

## 📋 Pre-Commit Checklist

- [ ] Build berhasil: `./gradlew build`
- [ ] Tidak ada compilation error
- [ ] Tidak ada unused imports
- [ ] Code formatting consistent
- [ ] Dependency injection registered
- [ ] Navigation working
- [ ] Error handling implemented
- [ ] Loading states handled

---

## 🆘 Common Issues & Solutions

### Issue: "Unresolved reference"
**Solution**: Pastikan dependency sudah ditambahkan di `build.gradle.kts`

### Issue: "Cannot find ScreenModel"
**Solution**: Pastikan Koin module sudah di-register di `AppModule`

### Issue: "Type mismatch"
**Solution**: Cek package import, pastikan menggunakan class dari `core:model`

### Issue: Build gagal
**Solution**: Run `./gradlew clean build` atau restart Gradle daemon

---

**Last Updated**: October 26, 2025

