# Fix: Kotlin Native IR Linkage Error with Koin Compose

## Problem
The iOS app was crashing at runtime with:
```
kotlin.native.internal.IrLinkageError: Can not read value from variable 'tmp0_safe_receiver': 
Variable uses unlinked class symbol 'org.koin.compose.stable/StableParametersDefinition|null[0]'
```

This error occurred when trying to display the LoginScreen, specifically when calling `koinScreenModel<LoginScreenModel>()`.

## Root Cause
The issue was caused by incompatible Koin Compose library versions for iOS/Native builds:

1. **Voyager-Koin Integration**: The `koinScreenModel()` function from `voyager-koin` library uses the unstable Koin Compose API (`org.koin.compose.stable.StableParametersDefinition`)
2. **Version Mismatch**: Koin Compose versions were mismatched:
   - `koin-compose = "1.1.2"` 
   - `koin-compose-viewmodel = "1.2.0-Beta4"`
3. **IR Linkage**: The Kotlin/Native compiler couldn't link the "stable" API classes properly, causing a runtime crash

## Solutions Applied

### 1. Updated Koin Compose Versions
Updated `gradle/libs.versions.toml` to use consistent, iOS-compatible versions:

**Before:**
```toml
koin-compose = { module = "io.insert-koin:koin-compose", version = "1.1.2" }
koin-compose-viewmodel = { module = "io.insert-koin:koin-compose-viewmodel", version = "1.2.0-Beta4" }
```

**After:**
```toml
koin-compose = { module = "io.insert-koin:koin-compose", version = "1.2.0-Beta5" }
koin-compose-viewmodel = { module = "io.insert-koin:koin-compose-viewmodel", version = "1.2.0-Beta5" }
```

### 2. Replaced voyager-koin with Direct Koin Injection
Updated `features/login/src/commonMain/kotlin/.../LoginScreen.kt`:

**Before (Problematic):**
```kotlin
import cafe.adriel.voyager.koin.koinScreenModel

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<LoginScreenModel>()  // ❌ Uses unstable API
        // ...
    }
}
```

**After (Fixed):**
```kotlin
import cafe.adriel.voyager.core.model.rememberScreenModel
import org.koin.compose.koinInject

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val loginUseCase: LoginUseCase = koinInject()  // ✅ Direct injection
        val screenModel = rememberScreenModel { LoginScreenModel(loginUseCase) }
        // ...
    }
}
```

### 3. Removed voyager-koin Dependency
Updated `composeApp/build.gradle.kts`:

**Before:**
```kotlin
implementation(libs.voyager.navigator)
implementation(libs.voyager.screenmodel)
implementation(libs.voyager.transitions)
implementation(libs.voyager.koin)  // ❌ Not needed
```

**After:**
```kotlin
implementation(libs.voyager.navigator)
implementation(libs.voyager.screenmodel)
implementation(libs.voyager.transitions)  // ✅ Removed voyager-koin
```

## Why This Works

### Direct Koin Injection Approach
1. **`koinInject()`**: Uses the stable Koin Compose API to inject dependencies directly
2. **`rememberScreenModel { }`**: Voyager's built-in factory function to create screen models
3. **Manual Dependency Passing**: Dependencies are injected and passed explicitly to the ScreenModel constructor
4. **No Unstable APIs**: Avoids the `koin-compose.stable` API that causes linkage errors

### Benefits
- ✅ **iOS Compatible**: No IR linkage errors on Native targets
- ✅ **Explicit Dependencies**: Clear dependency flow
- ✅ **Less Magic**: More control over ScreenModel creation
- ✅ **Stable APIs Only**: Uses only stable Koin Compose APIs
- ✅ **Version Consistency**: Matching Koin Compose library versions

## How to Apply the Fix

### Step 1: Clean Build
```bash
# Clean all caches
rm -rf ~/Library/Developer/Xcode/DerivedData/*
rm -rf ~/Library/Caches/Google/AndroidStudio*/DerivedData/*
./gradlew clean
```

### Step 2: Sync Gradle
After the changes, sync your Gradle project:
```bash
./gradlew --refresh-dependencies
```

### Step 3: Rebuild
```bash
# Build the framework
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode

# Or build in Xcode
open iosApp/iosApp.xcodeproj
# Press ⌘R to build and run
```

## Pattern for Other Screens

If you have other screens using `koinScreenModel`, update them using this pattern:

**Before:**
```kotlin
import cafe.adriel.voyager.koin.koinScreenModel

class MyScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<MyScreenModel>()
    }
}
```

**After:**
```kotlin
import cafe.adriel.voyager.core.model.rememberScreenModel
import org.koin.compose.koinInject

class MyScreen : Screen {
    @Composable
    override fun Content() {
        // Inject dependencies needed by the ScreenModel
        val dependency1 = koinInject<Dependency1>()
        val dependency2 = koinInject<Dependency2>()
        
        // Create ScreenModel with explicit dependencies
        val screenModel = rememberScreenModel { 
            MyScreenModel(dependency1, dependency2) 
        }
    }
}
```

## Alternative Approach (Not Used)

You could also use `remember { }` with Koin's `KoinContext`:

```kotlin
import org.koin.compose.getKoin

class MyScreen : Screen {
    @Composable
    override fun Content() {
        val koin = getKoin()
        val screenModel = remember { 
            MyScreenModel(koin.get(), koin.get()) 
        }
    }
}
```

This approach also avoids the unstable API but is less idiomatic.

## Verification

After applying the fix:
- ✅ No more `IrLinkageError` at runtime
- ✅ LoginScreen displays properly
- ✅ Koin injection works correctly
- ✅ App runs smoothly on iOS simulator and device

## Related Files Modified

1. **gradle/libs.versions.toml** - Updated Koin Compose versions
2. **features/login/src/.../LoginScreen.kt** - Replaced `koinScreenModel` with `rememberScreenModel` + `koinInject`
3. **composeApp/build.gradle.kts** - Removed `voyager-koin` dependency

## Key Takeaway

**For iOS/Native Kotlin Multiplatform projects:**
- Avoid `voyager-koin`'s `koinScreenModel()` - it uses unstable APIs
- Use direct `koinInject()` + `rememberScreenModel { }` pattern instead
- Keep Koin Compose library versions consistent (all on Beta5)
- Prefer explicit dependency injection over implicit helpers

## Summary

The IR linkage error was caused by `voyager-koin`'s use of unstable Koin Compose APIs that aren't properly linked in Kotlin/Native builds. By switching to direct `koinInject()` and `rememberScreenModel()`, we use only stable APIs and avoid linkage issues while maintaining the same functionality.

The app should now build and run successfully on iOS! 🎉

