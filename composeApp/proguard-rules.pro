# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Keep Compose related classes
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Koin related classes
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# Keep Ktor related classes
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**


# Keep Voyager related classes
-keep class cafe.adriel.voyager.** { *; }
-dontwarn cafe.adriel.voyager.**

# Keep serialization classes
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep all serializable classes
-keep @kotlinx.serialization.Serializable class * {
    static **$Companion;
}

# Keep BuildKonfig generated classes
-keep class com.dhimas.pengeluaranapp.config.BuildKonfig { *; }
