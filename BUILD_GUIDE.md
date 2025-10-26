# 🚀 PharmaTech Morocco - Build & Deploy Guide

## 📋 Table of Contents
1. [Prerequisites](#prerequisites)
2. [Initial Setup](#initial-setup)
3. [Building the App](#building-the-app)
4. [Running the App](#running-the-app)
5. [Testing](#testing)
6. [Deployment](#deployment)
7. [Troubleshooting](#troubleshooting)

---

## Prerequisites

### Required Software
- ✅ **Android Studio Hedgehog (2023.1.1)** or newer
- ✅ **JDK 17** (comes with Android Studio)
- ✅ **Git** (for version control)
- ✅ **Android SDK** (API 24-34)

### Required Accounts (for full functionality)
- ✅ **Google Account** (for Firebase)
- ✅ **Google Cloud Account** (for Maps API)
- ⚠️ Backend API credentials (if available)

---

## Initial Setup

### 1. Clone/Open Project

```bash
# If cloning from repository
git clone <repository-url>
cd PharmaTech Morocco

# Or open existing project
# File → Open → Navigate to: C:\Users\LENOVO\Desktop\Pharmacie
```

### 2. Configure Android Studio

1. **Open Android Studio**
2. **Import Project**
   - Select project folder
   - Wait for Gradle sync
   - Accept any SDK updates

3. **Configure SDK**
   - Tools → SDK Manager
   - Install Android SDK 34 (if not present)
   - Install Android SDK Build-Tools 34.0.0

### 3. Firebase Setup (Critical!)

#### Option A: Use Your Firebase Project (Recommended)

1. **Create Firebase Project**
   ```
   1. Go to: https://console.firebase.google.com
   2. Click "Add project"
   3. Project name: pharmatech-morocco
   4. Enable Google Analytics (optional)
   ```

2. **Add Android App**
   ```
   Package name: com.pharmatech.morocco
   App nickname: PharmaTech Morocco
   SHA-1: (optional for development)
   ```

3. **Download Configuration**
   ```
   1. Download google-services.json
   2. Replace: app/google-services.json
   ```

4. **Enable Services**
   - Authentication → Enable Email/Password & Google
   - Firestore Database → Create database (Start in test mode)
   - Storage → Get started
   - Cloud Messaging → Enable
   - Crashlytics → Enable

#### Option B: Disable Firebase (Limited Features)

Edit `PharmaTechApp.kt`:
```kotlin
// Comment these lines:
// FirebaseApp.initializeApp(this)
// FirebaseCrashlytics.getInstance()...
```

### 4. Configure API Keys

Edit `gradle.properties`:
```properties
MAPS_API_KEY=your_google_maps_api_key_here
```

To get Maps API key:
1. Go to: https://console.cloud.google.com
2. Enable Maps SDK for Android
3. Create API Key
4. Restrict to Android apps

---

## Building the App

### Using Android Studio (Recommended)

#### Debug Build
```
1. Build → Make Project (Ctrl+F9 / Cmd+F9)
2. Wait for build completion
3. Check Build tab for errors
```

#### Release Build
```
1. Build → Generate Signed Bundle / APK
2. Select APK
3. Create/Select keystore
4. Fill in details
5. Build → Release
```

### Using Command Line

#### Windows
```cmd
cd C:\Users\LENOVO\Desktop\Pharmacie

# Clean build
gradlew clean

# Build debug APK
gradlew assembleDebug

# Build release APK
gradlew assembleRelease

# Install on device
gradlew installDebug
```

#### Linux/Mac
```bash
cd /path/to/PharmaTech Morocco

# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on device
./gradlew installDebug
```

### Build Outputs

Debug APK location:
```
app/build/outputs/apk/debug/app-debug.apk
```

Release APK location:
```
app/build/outputs/apk/release/app-release.apk
```

---

## Running the App

### Using Emulator

1. **Create Emulator**
   ```
   Tools → Device Manager → Create Device
   Device: Pixel 6 or similar
   System Image: Android 14 (API 34)
   Click Finish
   ```

2. **Start Emulator**
   ```
   Click Play button in Device Manager
   Wait for emulator to start
   ```

3. **Run App**
   ```
   Click Run button (Shift+F10)
   Or: Run → Run 'app'
   ```

### Using Physical Device

1. **Enable Developer Options**
   ```
   Settings → About Phone → Tap Build Number 7 times
   ```

2. **Enable USB Debugging**
   ```
   Settings → Developer Options → USB Debugging (ON)
   ```

3. **Connect Device**
   ```
   Connect via USB
   Accept USB debugging prompt on device
   ```

4. **Run App**
   ```
   Select device from dropdown
   Click Run button
   ```

---

## Testing

### Unit Tests
```bash
# Run all unit tests
./gradlew test

# Run specific test
./gradlew test --tests "AuthRepositoryTest"

# With coverage
./gradlew testDebugUnitTest jacocoTestReport
```

### Instrumented Tests
```bash
# Run on connected device/emulator
./gradlew connectedAndroidTest

# Run specific test
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.pharmatech.morocco.ExampleInstrumentedTest
```

### Manual Testing Checklist

- [ ] Splash screen loads with animation
- [ ] Login screen accepts input
- [ ] Registration form validates correctly
- [ ] Navigation works between all screens
- [ ] Home screen displays properly
- [ ] Pharmacy list renders
- [ ] Medication list renders
- [ ] Tracker shows statistics
- [ ] Profile loads user info
- [ ] Dark/Light theme switches correctly

---

## Deployment

### Internal Testing

#### Google Play Console

1. **Create App**
   ```
   1. Go to: https://play.google.com/console
   2. Create app
   3. Fill in details
   ```

2. **Upload APK/Bundle**
   ```
   Release → Testing → Internal testing
   Upload app-release.aab
   ```

3. **Add Testers**
   ```
   Testers → Add testers by email
   ```

### Production Release

#### Pre-Release Checklist

- [ ] All features tested
- [ ] No critical bugs
- [ ] Firebase configured properly
- [ ] ProGuard rules verified
- [ ] App icons created (all densities)
- [ ] Screenshots prepared
- [ ] Store listing ready
- [ ] Privacy policy created
- [ ] Terms of service ready

#### Build Release Bundle
```bash
./gradlew bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`

#### Upload to Play Store
```
1. Play Console → Production
2. Create new release
3. Upload app-release.aab
4. Fill release notes
5. Review and rollout
```

---

## Troubleshooting

### Build Errors

#### Error: "SDK not found"
```
Solution:
1. Tools → SDK Manager
2. Install missing SDKs
3. Sync Gradle
```

#### Error: "Plugin not found"
```
Solution:
1. File → Invalidate Caches → Invalidate and Restart
2. Delete .gradle folder
3. Sync Gradle
```

#### Error: "Dependency resolution failed"
```
Solution:
1. Check internet connection
2. Update gradle.properties with proper proxy (if needed)
3. Try gradlew clean build
```

### Runtime Errors

#### Firebase Error
```
Error: "Default FirebaseApp is not initialized"

Solution:
1. Check google-services.json exists in app/
2. Verify package name matches
3. Sync Gradle after adding file
```

#### Maps Not Loading
```
Error: Grey screen or blank map

Solution:
1. Verify API key in gradle.properties
2. Enable Maps SDK in Google Cloud Console
3. Check key restrictions
```

#### App Crashes on Launch
```
Solution:
1. Check Logcat for errors
2. Verify all dependencies are compatible
3. Clean and rebuild project
4. Check Firebase initialization
```

### Performance Issues

#### Slow Build Times
```
Solutions:
1. Enable Gradle daemon (already enabled)
2. Increase memory in gradle.properties:
   org.gradle.jvmargs=-Xmx4096m
3. Enable parallel builds (already enabled)
4. Use SSD for project files
```

#### App Lag
```
Solutions:
1. Enable ProGuard for release builds (already enabled)
2. Optimize images (use WebP)
3. Check for memory leaks
4. Profile with Android Profiler
```

---

## Build Variants

### Debug
- Debugging enabled
- Logging verbose
- Firebase Crashlytics disabled
- App suffix: .debug

### Release
- ProGuard enabled
- Logging minimal
- Firebase Crashlytics enabled
- Optimized and minified

### Custom Build Types

Add to `app/build.gradle.kts`:
```kotlin
buildTypes {
    create("staging") {
        initWith(getByName("debug"))
        applicationIdSuffix = ".staging"
        buildConfigField("String", "API_BASE_URL", "\"https://staging-api.pharmatech.ma/v1/\"")
    }
}
```

---

## CI/CD Setup (Optional)

### GitHub Actions

Create `.github/workflows/android.yml`:
```yaml
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        java-version: '17'
    - name: Build with Gradle
      run: ./gradlew build
```

---

## Performance Optimization

### Build Performance
- ✅ Gradle daemon enabled
- ✅ Parallel builds enabled
- ✅ Build cache enabled
- ✅ Configuration on demand

### App Performance
- ✅ ProGuard for release
- ✅ Resource shrinking
- ✅ Code minification
- ✅ Image optimization (use Coil)

---

## Version Management

### Updating Version

Edit `app/build.gradle.kts`:
```kotlin
defaultConfig {
    versionCode = 2 // Increment for each release
    versionName = "1.0.1" // Semantic versioning
}
```

### Versioning Strategy
```
Major.Minor.Patch
1.0.0 - Initial release
1.0.1 - Bug fixes
1.1.0 - New features
2.0.0 - Major changes
```

---

## Additional Resources

- [Android Developer Guide](https://developer.android.com/guide)
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose)
- [Firebase Android Setup](https://firebase.google.com/docs/android/setup)
- [Play Store Publishing](https://support.google.com/googleplay/android-developer)

---

## Quick Commands Reference

```bash
# Clean build
./gradlew clean

# Build debug
./gradlew assembleDebug

# Build release
./gradlew assembleRelease

# Install debug
./gradlew installDebug

# Run tests
./gradlew test

# Run lint
./gradlew lint

# Check dependencies
./gradlew dependencies

# Create bundle
./gradlew bundleRelease
```

---

**Happy Building! 🚀**

*For additional help, see SETUP_GUIDE.md or README.md*

