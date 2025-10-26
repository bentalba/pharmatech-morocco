# Add project specific ProGuard rules here.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep Hilt classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper

# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# ML Kit
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**
-keep class com.google.android.gms.vision.** { *; }

# ARCore
-keep class com.google.ar.core.** { *; }
-keep class com.google.ar.** { *; }
-dontwarn com.google.ar.**

# Keep all model classes (data classes used with Gson/Firebase)
-keep class com.pharmatech.morocco.core.network.models.** { *; }
-keep class com.pharmatech.morocco.core.database.entities.** { *; }

# Keep all DTOs and API models
-keepclassmembers class com.pharmatech.morocco.core.network.models.** {
    <fields>;
    <init>(...);
}
-keepclassmembers class com.pharmatech.morocco.core.database.entities.** {
    <fields>;
    <init>(...);
}

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-dontwarn kotlinx.coroutines.**

# Timber logging
-dontwarn timber.log.**
-keep class timber.log.** { *; }

# Lottie animations
-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** { *; }

# Biometric
-keep class androidx.biometric.** { *; }
-dontwarn androidx.biometric.**

# WorkManager
-keep class androidx.work.** { *; }
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.ListenableWorker {
    public <init>(android.content.Context,androidx.work.WorkerParameters);
}
-dontwarn androidx.work.**

# Compose
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

# Navigation Compose
-keepnames class androidx.navigation.fragment.NavHostFragment
-keep class * extends androidx.navigation.Navigator

# Accompanist
-keep class com.google.accompanist.** { *; }
-dontwarn com.google.accompanist.**

# DataStore
-keep class androidx.datastore.*.** { *; }
-dontwarn androidx.datastore.**

# Prevent obfuscation of ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}

# Keep @Parcelize classes
-keep @kotlinx.parcelize.Parcelize class * { *; }

# Keep custom view classes
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Keep BuildConfig
-keep class com.pharmatech.morocco.BuildConfig { *; }

