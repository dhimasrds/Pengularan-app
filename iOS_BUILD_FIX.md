# iOS Build Fix - Multiple Commands Produce Error

## Problem
The iOS app build was failing with the error:
```
Multiple commands produce '/Users/dhimas.saputra/Library/Caches/Google/AndroidStudio.../Build/Products/Debug-iphonesimulator/.app'
```

The `.app` file had no name (literally `.app` instead of `PengeluaranApp.app`), which caused duplicate command detection in Xcode's build system.

## Root Cause
The `project.pbxproj` file was using variable references like `${APP_NAME}`, `${BUNDLE_ID}`, and `${TEAM_ID}` from `Config.xcconfig`, but these variables were not properly defined or weren't being read correctly during the build process.

Additionally, the `FRAMEWORK_SEARCH_PATHS` contained:
1. A reference to a non-existent `shared/build/xcode-frameworks/` path
2. A newline character `\n` that was causing parsing issues

## Solution Applied

### 1. Fixed Config.xcconfig
Updated `/iosApp/Configuration/Config.xcconfig`:
```xcconfig
TEAM_ID=

APP_NAME=PengeluaranApp
BUNDLE_ID=com.dhimas.pengeluaran_app.
```

### 2. Fixed project.pbxproj Build Settings
Replaced variable references with hardcoded values in both Debug and Release configurations:

**Before:**
```
PRODUCT_NAME = "${APP_NAME}";
PRODUCT_BUNDLE_IDENTIFIER = "${BUNDLE_ID}${TEAM_ID}";
DEVELOPMENT_TEAM = "${TEAM_ID}";
FRAMEWORK_SEARCH_PATHS = (
    "$(inherited)",
    "$(SRCROOT)/../shared/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)\n$(SRCROOT)/../composeApp/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)",
);
```

**After:**
```
PRODUCT_NAME = "PengeluaranApp";
PRODUCT_BUNDLE_IDENTIFIER = "com.dhimas.pengeluaran-app.PengeluaranApp";
DEVELOPMENT_TEAM = "";
FRAMEWORK_SEARCH_PATHS = (
    "$(inherited)",
    "$(SRCROOT)/../composeApp/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)",
);
```

### 3. Clean Build Caches
To ensure the changes take effect:
```bash
# Clean Xcode derived data
rm -rf ~/Library/Developer/Xcode/DerivedData/*
rm -rf ~/Library/Caches/Google/AndroidStudio*/DerivedData/*

# Clean Gradle build
./gradlew clean
```

## How to Build the iOS App

### Option 1: Via Xcode (Recommended)
1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select your target device/simulator
3. Press ⌘R or click the Run button
4. The Gradle build will run automatically via the "Compile Kotlin Framework" build phase

### Option 2: Via Command Line
```bash
# Build the Kotlin framework first
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode

# Then build the iOS app
xcodebuild -project iosApp/iosApp.xcodeproj \
  -scheme iosApp \
  -configuration Debug \
  -sdk iphonesimulator \
  -destination 'platform=iOS Simulator,name=iPhone 15'
```

### Option 3: Via Android Studio with KMM Plugin
1. Open the project in Android Studio
2. Select the iOS run configuration from the toolbar
3. Click Run

## Verification
After applying these changes and cleaning the caches:
- The product name should be `PengeluaranApp.app` (not `.app`)
- The build should complete without the "Multiple commands produce" error
- The app should be installable on the iOS simulator

## Notes

### If You Need to Change Team ID for Code Signing
Edit the `DEVELOPMENT_TEAM` value in the `project.pbxproj` file or set it in Xcode:
1. Open the project in Xcode
2. Select the iosApp target
3. Go to Signing & Capabilities
4. Select your team

### Framework Search Path
The framework is now correctly pointing to:
- `composeApp/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)`

This is where the Kotlin Multiplatform build outputs the `ComposeApp.framework`.

### Future Recommendations
1. **Consider using xcconfig properly**: If you want to use variables, ensure they're defined in a base configuration file that's properly loaded
2. **Add output paths to build script**: The warning about the "Compile Kotlin Framework" script phase can be addressed by adding output file lists
3. **Use stable dependency versions**: Continue using `kotlin.native.cacheKind=none` until KMP dependencies are more stable

## Related Files Modified
- `/iosApp/Configuration/Config.xcconfig`
- `/iosApp/iosApp.xcodeproj/project.pbxproj`

## Troubleshooting

### If the error persists:
1. Quit Xcode completely
2. Run the cache cleaning commands above
3. Reopen Xcode and try again

### If you see "Could not find ComposeApp.framework":
1. Build the framework first: `./gradlew :composeApp:embedAndSignAppleFrameworkForXcode`
2. Then build in Xcode

### If Gradle builds hang:
1. Check for stuck Java/Gradle processes: `ps aux | grep gradle`
2. Kill them: `killall -9 java`
3. Try again

## Summary
The issue was caused by undefined or improperly resolved Xcode variables leading to an empty product name. By hardcoding the values and fixing the framework search path, the build system can now correctly identify the output product and avoid duplicate command errors.

