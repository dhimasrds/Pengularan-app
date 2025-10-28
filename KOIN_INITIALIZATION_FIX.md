# Koin Initialization Fix for iOS

## Problem
The iOS app build was failing with the error:
```
Cannot find 'KoinKt' in scope
```

This occurred in `iOSApp.swift` where it was trying to call `KoinKt.doInitKoin()` which didn't exist.

## Root Cause
The Swift code was attempting to manually initialize Koin using a non-existent function. However, the app was already using `KoinApplication` composable in the `App.kt` file, which handles Koin initialization automatically when the Compose UI starts.

## Solution Applied

### 1. Removed Manual Koin Initialization from Swift
Updated `/iosApp/iosApp/iOSApp.swift`:

**Before:**
```swift
@main
struct iOSApp: App {
    init() {
        KoinKt.doInitKoin()  // ❌ This function doesn't exist
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

**After:**
```swift
@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

### 2. Koin Initialization is Handled by KoinApplication Composable
The `App.kt` file already has the proper Koin initialization:

```kotlin
@Composable
fun App() {
    KoinApplication(application = {
        modules(appModules)  // ✅ Koin is initialized here
    }) {
        MaterialTheme {
            val loginFeatureApi = koinInject<LoginFeatureApi>()
            Navigator(
                screen = loginFeatureApi.entryScreen()
            ) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}
```

### 3. Created KoinInitializer.kt (Optional, for future use)
If you ever need to initialize Koin manually (e.g., for preview purposes or testing), I've created:
`/composeApp/src/iosMain/kotlin/com/dhimas/pengeluaranapp/KoinInitializer.kt`

```kotlin
fun initKoin() {
    startKoin {
        modules(appModules)
    }
}
```

## How It Works

1. **iOS App Launches** → `iOSApp.swift` starts
2. **ContentView Loads** → Shows `ComposeView`
3. **ComposeView Creates** → Calls `MainViewController()`
4. **MainViewController** → Returns `ComposeUIViewController { App() }`
5. **App() Composable Runs** → `KoinApplication` initializes Koin with all modules
6. **Koin is Ready** → All dependencies can be injected using `koinInject()`

## Why This Approach is Better

1. **Single Initialization**: Koin is initialized once when the Compose UI starts
2. **Platform Agnostic**: Same code works for both Android and iOS
3. **Compose Native**: Uses the official Koin Compose integration
4. **No Duplication**: Avoids initializing Koin twice which would cause conflicts

## Building the iOS App

Now you can build the iOS app successfully:

### Via Xcode:
1. Open `iosApp/iosApp.xcodeproj`
2. Select a simulator/device
3. Press ⌘R to build and run

### Via Command Line:
```bash
# Build the framework first
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode

# Build the iOS app
xcodebuild -project iosApp/iosApp.xcodeproj \
  -scheme iosApp \
  -configuration Debug \
  -sdk iphonesimulator
```

## Verification

After this fix:
- ✅ No more "Cannot find 'KoinKt' in scope" error
- ✅ Koin initializes properly when the app starts
- ✅ All dependencies are available for injection
- ✅ The app should build and run successfully

## Alternative Approach (Not Used)

If you wanted to initialize Koin from Swift (not recommended for this project), you would:

1. Use the `initKoin()` function from `KoinInitializer.kt`
2. Call it from Swift: `KoinInitializerKt.initKoin()`
3. Remove `KoinApplication` from `App.kt` and use plain Compose
4. This approach requires more manual setup and is less idiomatic

## Related Files Modified
- `/iosApp/iosApp/iOSApp.swift` - Removed incorrect Koin initialization
- `/composeApp/src/commonMain/kotlin/com/dhimas/pengeluaranapp/App.kt` - Cleaned up unused imports

## Related Files Created
- `/composeApp/src/iosMain/kotlin/com/dhimas/pengeluaranapp/KoinInitializer.kt` - Optional manual initialization function

## Summary
The error was caused by trying to call a non-existent Koin initialization function from Swift. The fix was to simply remove that call and rely on the existing `KoinApplication` composable, which properly handles Koin initialization for both Android and iOS platforms.

## Related Fix
If you encounter a runtime error about `IrLinkageError` and `org.koin.compose.stable/StableParametersDefinition`, see `KOIN_IR_LINKAGE_FIX.md` for the solution.

