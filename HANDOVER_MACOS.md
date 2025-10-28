# 🍎 SHIFAA Premium - macOS Development Handover

**Last Updated**: October 28, 2024  
**Project**: SHIFAA Premium - Moroccan Pharmacy Management System  
**Target Platform**: Android (API 26+)  
**Development Environment**: macOS (Apple Silicon or Intel)  
**Current State**: ✅ Fully Functional, Production Ready

---

## 📋 Table of Contents

1. [Quick Start](#quick-start)
2. [System Requirements](#system-requirements)
3. [Initial Setup](#initial-setup)
4. [Development Tools](#development-tools)
5. [Emulator Configuration](#emulator-configuration)
6. [Build & Run](#build--run)
7. [Project Architecture](#project-architecture)
8. [Common Tasks](#common-tasks)
9. [Troubleshooting](#troubleshooting)
10. [Performance Optimization](#performance-optimization)

---

## 🚀 Quick Start

### First-Time Setup (30 minutes)

```bash
# 1. Clone repository
git clone https://github.com/bentalba/pharmatech-morocco.git
cd pharmatech-morocco

# 2. Switch to development branch
git checkout dev

# 3. Grant execute permissions
chmod +x gradlew

# 4. Verify build
./gradlew clean assembleDebug

# 5. Launch emulator and install
./gradlew installDebug
```

### Daily Development

```bash
# Start emulator
~/Library/Android/sdk/emulator/emulator -avd Medium_Phone_API_36 &

# Build and install
./gradlew installDebug

# View logs
~/Library/Android/sdk/platform-tools/adb logcat -s SHIFAA
```

---

## 💻 System Requirements

### Hardware

**Apple Silicon (M1/M2/M3) - Recommended:**
- RAM: 16 GB minimum, 32 GB recommended
- Storage: 50 GB free space
- macOS: Sonoma 14.0+ or Ventura 13.0+

**Intel Macs:**
- Processor: Core i5 or better
- RAM: 16 GB minimum
- Storage: 50 GB free space
- macOS: Ventura 13.0+ (Monterey 12.0 minimum)

### Software Prerequisites

| Tool | Version | Purpose |
|------|---------|---------|
| **macOS** | 13.0+ | Operating System |
| **Android Studio** | Ladybug 2024.2.1+ | IDE |
| **JDK** | 17 (jbr-17.0.14) | Java Development Kit |
| **Gradle** | 8.13 (wrapper) | Build System |
| **Git** | 2.30+ | Version Control |
| **Xcode Command Line Tools** | Latest | Build Tools |

---

## 🛠 Initial Setup

### 1. Install Xcode Command Line Tools

```bash
# Install tools
xcode-select --install

# Verify installation
xcode-select -p
# Should output: /Library/Developer/CommandLineTools
```

### 2. Install Homebrew (Optional but Recommended)

```bash
# Install Homebrew
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Add to PATH (Apple Silicon)
echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zshrc
source ~/.zshrc

# Install useful tools
brew install git
brew install --cask android-studio
```

### 3. Configure Git

```bash
# Set user information
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Set default editor (optional)
git config --global core.editor "code --wait"

# Enable credential caching
git config --global credential.helper osxkeychain
```

### 4. Clone Repository

```bash
# Navigate to projects directory
cd ~/Developer  # or your preferred location

# Clone repository
git clone https://github.com/bentalba/pharmatech-morocco.git

# Enter directory
cd pharmatech-morocco

# Check branches
git branch -a

# Switch to dev branch for development
git checkout dev
```

---

## 🔧 Development Tools

### Android Studio Installation

**Download & Install:**
1. Visit: https://developer.android.com/studio
2. Download: Android Studio Ladybug 2024.2.1 or later
3. Open DMG file and drag to Applications folder
4. Launch Android Studio from `/Applications/Android Studio.app`

**First Launch Configuration:**

1. **Welcome Screen** → Select "Standard" installation
2. **SDK Components** (Auto-installed):
   - Android SDK Platform 34 (Android 14)
   - Android SDK Build-Tools 34.0.0
   - Android Emulator
   - Android SDK Platform-Tools
   - Intel x86 Emulator Accelerator (HAXM) - Intel only

3. **SDK Location**:
   - Default: `~/Library/Android/sdk`
   - Custom: Set in Preferences → Appearance & Behavior → System Settings → Android SDK

**Install Additional SDK Platforms:**

```bash
# Open SDK Manager: Tools → SDK Manager
# OR use command line:
~/Library/Android/sdk/cmdline-tools/latest/bin/sdkmanager "platforms;android-26"
~/Library/Android/sdk/cmdline-tools/latest/bin/sdkmanager "platforms;android-34"
~/Library/Android/sdk/cmdline-tools/latest/bin/sdkmanager "build-tools;34.0.0"
~/Library/Android/sdk/cmdline-tools/latest/bin/sdkmanager "system-images;android-34;google_apis;arm64-v8a"
```

### JDK Configuration

**Android Studio Embedded JDK (Recommended):**
- Location: `/Applications/Android Studio.app/Contents/jbr/Contents/Home`
- Version: 17.0.14

**Set JAVA_HOME:**

```bash
# Add to ~/.zshrc (for zsh - default on macOS)
echo 'export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"' >> ~/.zshrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc

# Or add to ~/.bash_profile (for bash)
echo 'export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"' >> ~/.bash_profile
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.bash_profile

# Reload shell configuration
source ~/.zshrc  # or source ~/.bash_profile

# Verify
java -version
# Should output: openjdk version "17.0.14"
```

### Android SDK Tools Path

**Add to PATH:**

```bash
# Add these to ~/.zshrc
export ANDROID_HOME=~/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin

# Reload
source ~/.zshrc

# Verify
adb version
emulator -version
```

### Gradle Configuration

**Wrapper (Included in Project):**
- Version: 8.13
- Location: `pharmatech-morocco/gradle/wrapper/`
- No separate installation needed

**Gradle Properties:**
```properties
# File: gradle.properties
org.gradle.jvmargs=-Xmx4096m
org.gradle.parallel=true
org.gradle.caching=true
android.enableDexingArtifactTransform=true
android.useAndroidX=true
kotlin.code.style=official
```

---

## 📱 Emulator Configuration

### Create AVD (Android Virtual Device)

**Via Android Studio GUI:**

1. **Open AVD Manager**: Tools → Device Manager
2. **Create Virtual Device**:
   - Category: Phone
   - Device: Medium Phone (1080x2340, 420dpi)
   - Click "Next"

3. **Select System Image**:
   - Release Name: UpsideDownCake
   - API Level: 36 (or 34)
   - ABI: `arm64-v8a` (Apple Silicon) or `x86_64` (Intel)
   - Target: Google APIs
   - Download if necessary
   - Click "Next"

4. **Configure AVD**:
   - AVD Name: `Medium_Phone_API_36`
   - Startup Orientation: Portrait
   - **Advanced Settings**:
     - RAM: 2048 MB (minimum)
     - VM Heap: 512 MB
     - Internal Storage: 2048 MB
     - SD Card: 512 MB (optional)
     - Graphics: Hardware - GLES 2.0
     - Boot Option: Quick Boot
   - Click "Finish"

**Via Command Line:**

```bash
# List available system images
~/Library/Android/sdk/cmdline-tools/latest/bin/sdkmanager --list | grep system-images

# Download system image (Apple Silicon)
~/Library/Android/sdk/cmdline-tools/latest/bin/sdkmanager "system-images;android-34;google_apis;arm64-v8a"

# Create AVD
~/Library/Android/sdk/cmdline-tools/latest/bin/avdmanager create avd \
  -n Medium_Phone_API_36 \
  -k "system-images;android-34;google_apis;arm64-v8a" \
  -d "pixel_4"

# List AVDs
~/Library/Android/sdk/emulator/emulator -list-avds
```

### Emulator Performance Optimization

**For Apple Silicon (M1/M2/M3):**

1. **Use ARM64 System Images**: Native performance, no translation layer
2. **Enable Hardware Acceleration**: Automatic with Hypervisor.Framework
3. **Allocate Sufficient RAM**: 2-4 GB depending on available memory

**Configuration File** (`~/.android/avd/Medium_Phone_API_36.avd/config.ini`):

```ini
hw.ramSize=2048
hw.heapSize=512
hw.gpu.enabled=yes
hw.gpu.mode=host
hw.keyboard=yes
hw.audioInput=yes
fastboot.forceFastBoot=yes
disk.dataPartition.size=2048M
```

### Launch Emulator

**From Android Studio:**
- Device Manager → Play button on AVD

**From Command Line:**

```bash
# Launch in background
~/Library/Android/sdk/emulator/emulator -avd Medium_Phone_API_36 &

# Launch with specific GPU
~/Library/Android/sdk/emulator/emulator -avd Medium_Phone_API_36 -gpu host &

# Launch with network bridge
~/Library/Android/sdk/emulator/emulator -avd Medium_Phone_API_36 -netdelay none -netspeed full &

# Check if emulator is running
~/Library/Android/sdk/platform-tools/adb devices
```

---

## 🏗 Build & Run

### Project Structure

```
pharmatech-morocco/
├── app/
│   ├── build.gradle.kts           # App module build config
│   ├── src/
│   │   └── main/
│   │       ├── AndroidManifest.xml
│   │       ├── java/com/pharmatech/morocco/
│   │       │   ├── MainActivity.kt
│   │       │   ├── PharmaTechApp.kt
│   │       │   ├── core/          # Shared utilities
│   │       │   ├── features/      # Feature modules
│   │       │   └── ui/            # Theme, navigation
│   │       ├── res/               # Resources
│   │       └── assets/            # Excel files, fonts
│   └── google-services.json       # Firebase config
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle.kts               # Root build config
├── settings.gradle.kts            # Project settings
├── gradle.properties              # Gradle properties
├── gradlew                        # Gradle wrapper (Unix)
└── local.properties               # Local SDK path
```

### Build Configuration

**Key Settings (app/build.gradle.kts):**

```kotlin
android {
    namespace = "com.pharmatech.morocco"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.pharmatech.morocco"
        minSdk = 26  // Critical: Required for Apache POI
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        
        multiDexEnabled = true  // Critical: Required for 65K+ methods
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}
```

### Build Commands

**Clean Build:**

```bash
# Navigate to project directory
cd ~/Developer/pharmatech-morocco

# Clean previous builds
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK (requires signing)
./gradlew assembleRelease
```

**Build Outputs:**
- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release.apk`

### Install on Emulator

**Prerequisites:**
1. Emulator must be running
2. Check device connection: `adb devices`

**Installation:**

```bash
# Install debug build
./gradlew installDebug

# OR manually install APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Launch app
adb shell am start -n com.pharmatech.morocco/.MainActivity
```

### View Logs

```bash
# View all logs
adb logcat

# Filter by app tag
adb logcat -s SHIFAA

# Filter by log level (errors only)
adb logcat *:E

# Clear logs
adb logcat -c

# Save logs to file
adb logcat > ~/Desktop/shifaa_logs.txt
```

### Build from Android Studio

1. **Open Project**: File → Open → Select `pharmatech-morocco` directory
2. **Sync Gradle**: File → Sync Project with Gradle Files
3. **Select Run Configuration**: Top toolbar → app
4. **Select Device**: Top toolbar → Medium_Phone_API_36
5. **Run**: Click green play button or press `Shift + F10`

---

## 🏛 Project Architecture

### Technology Stack

| Layer | Technology |
|-------|-----------|
| **UI** | Jetpack Compose (Material 3) |
| **Architecture** | MVVM + Clean Architecture |
| **DI** | Hilt (Dagger 2) |
| **Database** | Room |
| **Networking** | Retrofit + OkHttp |
| **Async** | Kotlin Coroutines + Flow |
| **Maps** | Google Maps SDK |
| **Excel** | Apache POI 5.2.5 |
| **Image** | Coil |
| **Navigation** | Compose Navigation |

### Layer Breakdown

**Data Layer:**
- `data/local/`: Room entities, DAOs
- `data/remote/`: API services, DTOs
- `data/repository/`: Repository implementations

**Domain Layer:**
- `domain/model/`: Domain models
- `domain/repository/`: Repository interfaces
- `domain/usecase/`: Business logic

**Presentation Layer:**
- `presentation/`: Screens, ViewModels, Composables
- `ui/theme/`: Theme, colors, typography
- `ui/navigation/`: Navigation graph

### Key Dependencies

```kotlin
// build.gradle.kts (app module)
dependencies {
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    
    // Apache POI (Excel)
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")
    
    // MultiDex (Required for Apache POI)
    implementation("androidx.multidex:multidex:2.0.1")
}
```

---

## 📝 Common Tasks

### Running Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests (requires emulator)
./gradlew connectedAndroidTest

# Test specific module
./gradlew :app:test

# Generate test coverage report
./gradlew jacocoTestReport
```

### Code Quality

```bash
# Lint check
./gradlew lint

# Format code (if ktlint configured)
./gradlew ktlintFormat

# Detekt static analysis
./gradlew detekt
```

### Dependency Management

```bash
# Check for dependency updates
./gradlew dependencyUpdates

# View dependency tree
./gradlew :app:dependencies

# Find specific dependency
./gradlew :app:dependencies | grep "retrofit"
```

### Git Workflow

**Daily Development:**

```bash
# Pull latest changes
git checkout dev
git pull origin dev

# Create feature branch
git checkout -b feature/new-feature

# Work on feature...
# Stage changes
git add .

# Commit
git commit -m "feat: Add new feature description"

# Push to remote
git push -u origin feature/new-feature

# Create pull request on GitHub
```

**Commit Message Convention:**

```
feat: Add new feature
fix: Fix bug
refactor: Refactor code
docs: Update documentation
style: Format code
test: Add tests
chore: Update dependencies
perf: Improve performance
```

### APK Size Optimization

```bash
# Enable R8/ProGuard for release
# In app/build.gradle.kts:
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}

# Analyze APK
./gradlew assembleRelease
~/Library/Android/sdk/build-tools/34.0.0/aapt dump badging app/build/outputs/apk/release/app-release.apk
```

---

## 🔍 Troubleshooting

### Gradle Issues

**Issue**: "Could not find Android SDK"

```bash
# Solution: Create/update local.properties
echo "sdk.dir=/Users/$(whoami)/Library/Android/sdk" > local.properties

# Or set ANDROID_HOME
export ANDROID_HOME=~/Library/Android/sdk
```

**Issue**: "Out of memory error during build"

```bash
# Solution 1: Increase heap in gradle.properties
org.gradle.jvmargs=-Xmx6144m

# Solution 2: Disable parallel builds temporarily
org.gradle.parallel=false
```

**Issue**: "Gradle daemon locked"

```bash
# Solution: Kill Gradle daemon
./gradlew --stop

# Or manually
killall -9 java
```

### Emulator Issues

**Issue**: Emulator won't start

```bash
# Solution 1: Reset AVD
rm -rf ~/.android/avd/Medium_Phone_API_36.avd
# Recreate AVD via Android Studio

# Solution 2: Check virtualization
~/Library/Android/sdk/emulator/emulator -accel-check

# Solution 3: Kill existing emulator processes
pkill -9 qemu-system
```

**Issue**: Emulator is slow (Intel Mac)

```bash
# Solution 1: Use x86_64 system image, not ARM
# Solution 2: Enable Intel HAXM
~/Library/Android/sdk/emulator/emulator -accel-check

# Solution 3: Reduce AVD RAM to 2048 MB
# Edit: ~/.android/avd/Medium_Phone_API_36.avd/config.ini
hw.ramSize=2048
```

**Issue**: "Failed to install APK"

```bash
# Solution 1: Uninstall existing app
adb uninstall com.pharmatech.morocco

# Solution 2: Clear package manager data
adb shell pm clear com.pharmatech.morocco

# Solution 3: Restart ADB
adb kill-server
adb start-server
```

### Build Issues

**Issue**: "DEX build stuck at 51%"

**Status**: ✅ Already fixed with MultiDex

```kotlin
// Verification in app/build.gradle.kts:
android {
    defaultConfig {
        multiDexEnabled = true
    }
}

dependencies {
    implementation("androidx.multidex:multidex:2.0.1")
}
```

**Issue**: "Apache POI MethodHandle error"

**Status**: ✅ Already fixed with minSdk = 26

```kotlin
// Verification in app/build.gradle.kts:
android {
    defaultConfig {
        minSdk = 26  // Required for MethodHandle support
    }
}
```

**Issue**: "Compose compiler version mismatch"

```bash
# Solution: Use Compose BOM for version alignment
implementation(platform("androidx.compose:compose-bom:2024.04.01"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
# Do NOT specify versions for Compose dependencies
```

### Runtime Issues

**Issue**: App crashes on launch

```bash
# Check logs
adb logcat -s AndroidRuntime:E

# Common causes:
# 1. Missing Firebase configuration
# 2. Room migration failure
# 3. Missing permission in manifest
```

**Issue**: Memory leaks

```bash
# Use Android Studio Profiler:
# View → Tool Windows → Profiler
# Select app process
# Monitor Memory graph

# Check for:
# - Unclosed database connections
# - Retained activity references
# - Endless coroutines (use viewModelScope)
```

---

## ⚡️ Performance Optimization

### Build Performance

**Gradle Optimization:**

```properties
# gradle.properties
org.gradle.jvmargs=-Xmx4096m
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true
android.enableDexingArtifactTransform=true
```

**Configuration Cache:**

```bash
# Enable configuration cache (Gradle 8.1+)
./gradlew --configuration-cache assembleDebug
```

### App Performance

**Compose Optimization:**

```kotlin
// Use remember for expensive calculations
val expensiveValue by remember { derivedStateOf { compute() } }

// Use keys for lists
LazyColumn {
    items(items, key = { it.id }) { item ->
        ItemComposable(item)
    }
}

// Avoid unnecessary recompositions
@Composable
fun ExpensiveComposable() {
    // Stable parameters only
}
```

**Room Optimization:**

```kotlin
// Use suspend functions
@Query("SELECT * FROM table")
suspend fun getAll(): List<Entity>

// Use Flow for reactive queries
@Query("SELECT * FROM table")
fun observeAll(): Flow<List<Entity>>

// Use pagination for large datasets
@Query("SELECT * FROM table LIMIT :limit OFFSET :offset")
suspend fun getPaginated(limit: Int, offset: Int): List<Entity>
```

**Network Optimization:**

```kotlin
// Implement caching
OkHttpClient.Builder()
    .cache(Cache(context.cacheDir, 10 * 1024 * 1024)) // 10 MB
    .build()

// Set proper timeouts
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
```

---

## 📚 Additional Resources

### Documentation

- [PROJECT_STATUS.md](PROJECT_STATUS.md) - Current project state
- [ARCHITECTURE.md](ARCHITECTURE.md) - Detailed architecture
- [README.md](README.md) - Project overview
- [CONTRIBUTING.md](CONTRIBUTING.md) - Contribution guidelines
- [CHANGELOG.md](CHANGELOG.md) - Version history
- [.github/copilot-instructions.md](.github/copilot-instructions.md) - AI assistant guidelines

### External Resources

- [Android Developers](https://developer.android.com/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Material Design 3](https://m3.material.io/)

### Useful Commands Cheatsheet

```bash
# Gradle
./gradlew clean                    # Clean build
./gradlew assembleDebug            # Build debug APK
./gradlew installDebug             # Install on emulator
./gradlew :app:dependencies        # View dependencies

# ADB
adb devices                        # List connected devices
adb install -r app.apk             # Install APK
adb uninstall com.pharmatech.morocco  # Uninstall app
adb logcat                         # View logs
adb shell am start -n <package>/.MainActivity  # Launch app

# Emulator
~/Library/Android/sdk/emulator/emulator -list-avds  # List AVDs
~/Library/Android/sdk/emulator/emulator -avd <name> &  # Start emulator

# Git
git status                         # Check status
git checkout dev                   # Switch to dev branch
git pull origin dev                # Pull latest changes
git add .                          # Stage all changes
git commit -m "message"            # Commit
git push origin <branch>           # Push to remote
```

---

## 🎯 Next Steps

### Immediate Actions

1. ✅ Complete initial setup (JDK, Android Studio, SDK)
2. ✅ Create and configure emulator
3. ✅ Clone repository and checkout dev branch
4. ✅ Build and run app
5. ✅ Verify all 6 tabs are functional

### Development Workflow

1. Create feature branch from `dev`
2. Implement feature following MVVM pattern
3. Test on emulator
4. Commit with conventional message
5. Push and create pull request
6. Merge to `dev` after review
7. Periodically merge `dev` to `master`

### Recommended Improvements

- [ ] Add unit tests for ViewModels
- [ ] Add instrumented tests for UI
- [ ] Implement CI/CD pipeline (GitHub Actions)
- [ ] Add ProGuard rules for release builds
- [ ] Optimize APK size
- [ ] Add analytics tracking
- [ ] Implement crash reporting
- [ ] Add user feedback mechanism

---

## 📞 Support

**For issues or questions:**

1. Check [Troubleshooting](#troubleshooting) section
2. Review [PROJECT_STATUS.md](PROJECT_STATUS.md)
3. Search GitHub issues
4. Create new issue with:
   - macOS version
   - Android Studio version
   - Error logs
   - Steps to reproduce

**Repository**: https://github.com/bentalba/pharmatech-morocco

---

**Last Updated**: October 28, 2024  
**Version**: 1.0.0  
**Platform**: macOS (Apple Silicon & Intel)

🍎 **Happy Coding on macOS!** 🚀
