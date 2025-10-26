# 🚀 Feature Development Guide

## 📋 Checklist Sebelum Mulai

Sebelum menambahkan feature baru, pastikan Anda memahami:
- ✅ Arsitektur Clean Architecture yang digunakan
- ✅ Struktur modular project
- ✅ Dependency flow antar modules
- ✅ Naming conventions
- ✅ Package structure

## 🎯 Step-by-Step: Menambahkan Feature Baru

### Step 1: Perencanaan Feature

**Pertanyaan yang harus dijawab:**
1. Apa nama feature? (contoh: `expense`, `profile`, `statistics`)
2. Apa saja screen yang dibutuhkan?
3. Data models apa yang diperlukan?
4. Apakah perlu API baru?
5. Apakah perlu database?
6. Dependency dengan feature lain?

**Contoh: Feature "Add Expense"**
- **Nama**: `expense`
- **Screens**: AddExpenseScreen, ExpenseDetailScreen
- **Models**: Expense, Category
- **API**: POST /expenses, GET /expenses/{id}
- **Database**: Expense table
- **Dependencies**: Depends on `core:model`, `core:domain`, `core:data`

---

### Step 2: Membuat Module Feature Baru

#### 2.1 Update `settings.gradle.kts`

```kotlin
// settings.gradle.kts

// Feature modules
include(":features:login")
include(":features:home")
include(":features:expense")  // ← Tambahkan ini
```

#### 2.2 Buat Struktur Folder

```bash
mkdir -p features/expense/src/commonMain/kotlin/com/dhimas/pengeluaranapp/features/expense
```

Struktur lengkap:
```
features/expense/
├── build.gradle.kts
└── src/
    ├── commonMain/
    │   └── kotlin/
    │       └── com/dhimas/pengeluaranapp/features/expense/
    │           ├── domain/
    │           │   └── usecase/
    │           │       └── AddExpenseUseCase.kt
    │           ├── ui/
    │           │   ├── screens/
    │           │   │   └── AddExpenseScreen.kt
    │           │   ├── components/
    │           │   │   └── ExpenseFormComponent.kt
    │           │   └── viewmodel/
    │           │       └── AddExpenseScreenModel.kt
    │           └── di/
    │               └── ExpenseFeatureModule.kt
    ├── androidMain/
    └── iosMain/
```

#### 2.3 Buat `build.gradle.kts`

```kotlin
// features/expense/build.gradle.kts

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
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
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            
            // Navigation
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)
            
            // DI
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
    namespace = "com.dhimas.pengeluaranapp.features.expense"
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

---

### Step 3: Tambahkan Domain Models

#### 3.1 Buat Model di `core:model`

```kotlin
// core/model/src/commonMain/kotlin/.../core/model/Expense.kt

package com.dhimas.pengeluaranapp.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Expense(
    val id: String,
    val userId: String,
    val categoryId: String,
    val amount: Double,
    val description: String,
    val date: Long,
    val createdAt: Long
)

@Serializable
data class Category(
    val id: String,
    val name: String,
    val icon: String,
    val color: String
)
```

#### 3.2 Buat Repository Interface di `core:domain`

```kotlin
// core/domain/src/commonMain/kotlin/.../core/domain/repository/ExpenseRepository.kt

package com.dhimas.pengeluaranapp.core.domain.repository

import com.dhimas.pengeluaranapp.core.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun addExpense(expense: Expense): Result<Expense>
    suspend fun getExpenseById(id: String): Result<Expense>
    suspend fun getAllExpenses(): Flow<List<Expense>>
    suspend fun updateExpense(expense: Expense): Result<Expense>
    suspend fun deleteExpense(id: String): Result<Unit>
}
```

---

### Step 4: Implementasi Data Layer

#### 4.1 Buat Network DTOs (jika perlu)

```kotlin
// core/network/src/commonMain/kotlin/.../core/network/dto/ExpenseRequest.kt

package com.dhimas.pengeluaranapp.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddExpenseRequest(
    val categoryId: String,
    val amount: Double,
    val description: String,
    val date: Long
)

@Serializable
data class ExpenseResponse(
    val id: String,
    val userId: String,
    val categoryId: String,
    val amount: Double,
    val description: String,
    val date: Long,
    val createdAt: Long
)
```

#### 4.2 Buat API Interface

```kotlin
// core/data/src/commonMain/kotlin/.../core/data/remote/api/ExpenseApi.kt

package com.dhimas.pengeluaranapp.core.data.remote.api

import com.dhimas.pengeluaranapp.core.network.dto.AddExpenseRequest
import com.dhimas.pengeluaranapp.core.network.dto.ExpenseResponse
import de.jensklingenberg.ktorfit.http.*

interface ExpenseApi {
    @POST("expenses")
    suspend fun addExpense(@Body request: AddExpenseRequest): ExpenseResponse
    
    @GET("expenses/{id}")
    suspend fun getExpense(@Path("id") id: String): ExpenseResponse
    
    @GET("expenses")
    suspend fun getAllExpenses(): List<ExpenseResponse>
    
    @PUT("expenses/{id}")
    suspend fun updateExpense(
        @Path("id") id: String,
        @Body request: AddExpenseRequest
    ): ExpenseResponse
    
    @DELETE("expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: String)
}
```

#### 4.3 Implementasi Repository

```kotlin
// core/data/src/commonMain/kotlin/.../core/data/repository/ExpenseRepositoryImpl.kt

package com.dhimas.pengeluaranapp.core.data.repository

import com.dhimas.pengeluaranapp.core.domain.repository.ExpenseRepository
import com.dhimas.pengeluaranapp.core.model.Expense
import com.dhimas.pengeluaranapp.core.data.remote.api.ExpenseApi
import com.dhimas.pengeluaranapp.core.network.dto.AddExpenseRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExpenseRepositoryImpl(
    private val expenseApi: ExpenseApi
) : ExpenseRepository {
    
    override suspend fun addExpense(expense: Expense): Result<Expense> {
        return try {
            val request = AddExpenseRequest(
                categoryId = expense.categoryId,
                amount = expense.amount,
                description = expense.description,
                date = expense.date
            )
            val response = expenseApi.addExpense(request)
            
            val newExpense = Expense(
                id = response.id,
                userId = response.userId,
                categoryId = response.categoryId,
                amount = response.amount,
                description = response.description,
                date = response.date,
                createdAt = response.createdAt
            )
            
            Result.success(newExpense)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getExpenseById(id: String): Result<Expense> {
        return try {
            val response = expenseApi.getExpense(id)
            val expense = Expense(
                id = response.id,
                userId = response.userId,
                categoryId = response.categoryId,
                amount = response.amount,
                description = response.description,
                date = response.date,
                createdAt = response.createdAt
            )
            Result.success(expense)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAllExpenses(): Flow<List<Expense>> = flow {
        try {
            val response = expenseApi.getAllExpenses()
            val expenses = response.map { dto ->
                Expense(
                    id = dto.id,
                    userId = dto.userId,
                    categoryId = dto.categoryId,
                    amount = dto.amount,
                    description = dto.description,
                    date = dto.date,
                    createdAt = dto.createdAt
                )
            }
            emit(expenses)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    
    override suspend fun updateExpense(expense: Expense): Result<Expense> {
        return try {
            val request = AddExpenseRequest(
                categoryId = expense.categoryId,
                amount = expense.amount,
                description = expense.description,
                date = expense.date
            )
            val response = expenseApi.updateExpense(expense.id, request)
            
            val updatedExpense = Expense(
                id = response.id,
                userId = response.userId,
                categoryId = response.categoryId,
                amount = response.amount,
                description = response.description,
                date = response.date,
                createdAt = response.createdAt
            )
            
            Result.success(updatedExpense)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteExpense(id: String): Result<Unit> {
        return try {
            expenseApi.deleteExpense(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

---

### Step 5: Buat Use Case

```kotlin
// features/expense/src/commonMain/kotlin/.../features/expense/domain/usecase/AddExpenseUseCase.kt

package com.dhimas.pengeluaranapp.features.expense.domain.usecase

import com.dhimas.pengeluaranapp.core.domain.repository.ExpenseRepository
import com.dhimas.pengeluaranapp.core.model.Expense

class AddExpenseUseCase(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(
        categoryId: String,
        amount: Double,
        description: String,
        date: Long
    ): Result<Expense> {
        // Validation
        if (amount <= 0) {
            return Result.failure(Exception("Amount must be greater than 0"))
        }
        
        if (categoryId.isBlank()) {
            return Result.failure(Exception("Category is required"))
        }
        
        if (description.isBlank()) {
            return Result.failure(Exception("Description is required"))
        }
        
        // Create expense object
        val expense = Expense(
            id = "", // Will be generated by backend
            userId = "", // Will be set by backend from token
            categoryId = categoryId,
            amount = amount,
            description = description,
            date = date,
            createdAt = System.currentTimeMillis()
        )
        
        return expenseRepository.addExpense(expense)
    }
}
```

---

### Step 6: Buat UI Layer

#### 6.1 UI State & Events

```kotlin
// features/expense/src/commonMain/kotlin/.../features/expense/ui/viewmodel/AddExpenseScreenModel.kt

package com.dhimas.pengeluaranapp.features.expense.ui.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.dhimas.pengeluaranapp.features.expense.domain.usecase.AddExpenseUseCase
import com.dhimas.pengeluaranapp.core.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AddExpenseUiState(
    val categoryId: String = "",
    val amount: String = "",
    val description: String = "",
    val date: Long = System.currentTimeMillis(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successExpense: Expense? = null
)

class AddExpenseScreenModel(
    private val addExpenseUseCase: AddExpenseUseCase
) : ScreenModel {
    
    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()
    
    fun updateCategory(categoryId: String) {
        _uiState.value = _uiState.value.copy(categoryId = categoryId)
    }
    
    fun updateAmount(amount: String) {
        _uiState.value = _uiState.value.copy(amount = amount)
    }
    
    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }
    
    fun updateDate(date: Long) {
        _uiState.value = _uiState.value.copy(date = date)
    }
    
    fun addExpense() {
        val currentState = _uiState.value
        
        screenModelScope.launch {
            _uiState.value = currentState.copy(
                isLoading = true,
                error = null
            )
            
            val amountDouble = currentState.amount.toDoubleOrNull() ?: 0.0
            
            val result = addExpenseUseCase(
                categoryId = currentState.categoryId,
                amount = amountDouble,
                description = currentState.description,
                date = currentState.date
            )
            
            result.fold(
                onSuccess = { expense ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        successExpense = expense,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error occurred"
                    )
                }
            )
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
```

#### 6.2 Screen Composable

```kotlin
// features/expense/src/commonMain/kotlin/.../features/expense/ui/screens/AddExpenseScreen.kt

package com.dhimas.pengeluaranapp.features.expense.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dhimas.pengeluaranapp.features.expense.ui.viewmodel.AddExpenseScreenModel

class AddExpenseScreen : Screen {
    
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<AddExpenseScreenModel>()
        val uiState by screenModel.uiState.collectAsState()
        
        // Navigate back on success
        LaunchedEffect(uiState.successExpense) {
            if (uiState.successExpense != null) {
                navigator.pop()
            }
        }
        
        AddExpenseScreenContent(
            uiState = uiState,
            onCategoryChange = screenModel::updateCategory,
            onAmountChange = screenModel::updateAmount,
            onDescriptionChange = screenModel::updateDescription,
            onDateChange = screenModel::updateDate,
            onAddClick = screenModel::addExpense,
            onBackClick = { navigator.pop() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddExpenseScreenContent(
    uiState: AddExpenseUiState,
    onCategoryChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Category dropdown (simplified)
            OutlinedTextField(
                value = uiState.categoryId,
                onValueChange = onCategoryChange,
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Amount input
            OutlinedTextField(
                value = uiState.amount,
                onValueChange = onAmountChange,
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Description input
            OutlinedTextField(
                value = uiState.description,
                onValueChange = onDescriptionChange,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            // Error message
            if (uiState.error != null) {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Add button
            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Add Expense")
                }
            }
        }
    }
}
```

---

### Step 7: Setup Dependency Injection

```kotlin
// features/expense/src/commonMain/kotlin/.../features/expense/di/ExpenseFeatureModule.kt

package com.dhimas.pengeluaranapp.features.expense.di

import com.dhimas.pengeluaranapp.features.expense.domain.usecase.AddExpenseUseCase
import com.dhimas.pengeluaranapp.features.expense.ui.viewmodel.AddExpenseScreenModel
import org.koin.dsl.module

val expenseFeatureModule = module {
    // Use Cases
    factory { AddExpenseUseCase(get()) }
    
    // ViewModels
    factory { AddExpenseScreenModel(get()) }
}
```

#### Register di Application

```kotlin
// composeApp/src/commonMain/kotlin/.../di/AppModule.kt

import com.dhimas.pengeluaranapp.features.expense.di.expenseFeatureModule

val appModules = listOf(
    // ... existing modules
    expenseFeatureModule  // ← Tambahkan ini
)
```

---

### Step 8: Integrasikan ke App

#### Update Navigation

```kotlin
// Dari screen lain, navigate ke AddExpenseScreen
import com.dhimas.pengeluaranapp.features.expense.ui.screens.AddExpenseScreen

Button(onClick = { 
    navigator.push(AddExpenseScreen()) 
}) {
    Text("Add Expense")
}
```

#### Update Dependencies di `composeApp`

```kotlin
// composeApp/build.gradle.kts

commonMain.dependencies {
    // ... existing dependencies
    
    // Feature modules
    implementation(projects.features.login)
    implementation(projects.features.home)
    implementation(projects.features.expense)  // ← Tambahkan ini
}
```

---

## ⚠️ Yang Harus Diperhatikan

### 1. **Dependency Rules**
- ❌ Feature modules **TIDAK BOLEH** depend pada feature lain
- ✅ Feature modules **HANYA** depend pada core modules
- ✅ Core modules depend pada library external

### 2. **Package Naming**
```
com.dhimas.pengeluaranapp
├── core.[module-name]           # Core modules
└── features.[feature-name]      # Feature modules
```

### 3. **File Naming**
- **Screen**: `[Name]Screen.kt` (contoh: `AddExpenseScreen.kt`)
- **ViewModel**: `[Name]ScreenModel.kt` (contoh: `AddExpenseScreenModel.kt`)
- **Use Case**: `[Action][Entity]UseCase.kt` (contoh: `AddExpenseUseCase.kt`)
- **Repository**: `[Entity]Repository.kt` (contoh: `ExpenseRepository.kt`)
- **Model**: `[Entity].kt` (contoh: `Expense.kt`)

### 4. **State Management**
- Gunakan `StateFlow` untuk state yang observable
- Gunakan `MutableStateFlow` internal, expose `StateFlow` public
- Update state dengan `copy()` untuk immutability

### 5. **Error Handling**
- Gunakan `Result<T>` untuk operation yang bisa fail
- Wrap exceptions dalam try-catch
- Tampilkan user-friendly error messages di UI

### 6. **Testing**
Buat test untuk setiap layer:
```
features/expense/src/
├── commonTest/
│   └── kotlin/.../features/expense/
│       ├── domain/usecase/AddExpenseUseCaseTest.kt
│       └── ui/viewmodel/AddExpenseScreenModelTest.kt
```

---

## ✅ Checklist Sebelum Commit

- [ ] Module baru sudah terdaftar di `settings.gradle.kts`
- [ ] `build.gradle.kts` sudah dikonfigurasi dengan benar
- [ ] Domain models sudah dibuat di `core:model`
- [ ] Repository interface sudah dibuat di `core:domain`
- [ ] Repository implementation sudah dibuat di `core:data`
- [ ] Use case sudah dibuat dengan validation
- [ ] ViewModel/ScreenModel sudah handle loading & error state
- [ ] UI Screen sudah menggunakan Material3 components
- [ ] Dependency injection sudah di-setup
- [ ] Feature sudah diintegrasikan ke main app
- [ ] Code sudah di-test (minimal manual testing)
- [ ] Tidak ada error compilation
- [ ] Build berhasil: `./gradlew :composeApp:assembleDebug`

---

## 📚 Referensi

- **Clean Architecture**: [Blog Uncle Bob](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- **Compose Multiplatform**: [Official Docs](https://www.jetbrains.com/lp/compose-multiplatform/)
- **Voyager Navigation**: [GitHub](https://github.com/adrielcafe/voyager)
- **Koin DI**: [Documentation](https://insert-koin.io/)
- **Project Summary**: [PROJECT_SUMMARY.md](./PROJECT_SUMMARY.md)

---

**Happy Coding! 🚀**

*Jika ada pertanyaan, silakan refer ke dokumentasi atau bertanya ke team lead.*

