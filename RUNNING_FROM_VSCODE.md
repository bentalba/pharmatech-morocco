# Running PharmaTech Morocco from VS Code

## Current Status
✅ **Build in progress** - Dependencies are being downloaded

## Setup Completed

### 1. Environment Configuration
- ✅ Java (OpenJDK 21) configured from Android Studio JBR
- ✅ Android SDK located at `%LOCALAPPDATA%\Android\Sdk`
- ✅ Android Emulator available: `Medium_Phone_API_36.1`
- ✅ Emulator is currently running (`emulator-5554`)

### 2. Gradle Setup
- ✅ Fixed corrupted `gradlew.bat` file
- ✅ Downloaded `gradle-wrapper.jar`
- ✅ Configured Gradle 8.2
- ✅ Cleared corrupted Gradle cache

### 3. Build Process
**Current Command:**
```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"
.\gradlew.bat assembleDebug
```

**Status:** Downloading dependencies (normal for first build)
**Expected Time:** 5-10 minutes depending on internet speed

## Next Steps (After Build Completes)

### Option 1: Install to Emulator
```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"
.\gradlew.bat installDebug
```

### Option 2: Use the Automated Script
```powershell
powershell.exe -ExecutionPolicy Bypass -File .\RunApp.ps1
```

The script will:
1. Check Java and Android SDK
2. Detect running emulators
3. Build and install the app
4. Launch the app automatically

### Option 3: Manual Installation
After build completes, the APK will be at:
```
app\build\outputs\apk\debug\app-debug.apk
```

Install manually:
```powershell
$env:ANDROID_HOME\platform-tools\adb.exe install -r app\build\outputs\apk\debug\app-debug.apk
```

## Troubleshooting

### If Build Fails with Firebase Error
The app will run in **OFFLINE MODE** even without Firebase configuration. However, for full functionality:

1. See `FIREBASE_SETUP_GUIDE.md` for Firebase setup
2. Or continue testing without Firebase (limited features)

### If Build Fails with API Key Error
Edit `gradle.properties` and add:
```properties
MAPS_API_KEY=YOUR_ACTUAL_API_KEY_HERE
```

### Clear Gradle Cache (if needed)
```powershell
Remove-Item -Path "$env:USERPROFILE\.gradle\caches" -Recurse -Force
.\gradlew.bat clean
```

## Quick Commands Reference

### Start Emulator
```powershell
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"
& "$env:ANDROID_HOME\emulator\emulator.exe" -avd Medium_Phone_API_36.1
```

### List Emulators
```powershell
& "$env:ANDROID_HOME\emulator\emulator.exe" -list-avds
```

### Check Connected Devices
```powershell
& "$env:ANDROID_HOME\platform-tools\adb.exe" devices
```

### View Logs
```powershell
& "$env:ANDROID_HOME\platform-tools\adb.exe" logcat
```

### Launch App
```powershell
& "$env:ANDROID_HOME\platform-tools\adb.exe" shell am start -n com.pharmatech.morocco/.MainActivity
```

## Files Created
- `RunApp.ps1` - Automated build and run script
- `gradle/wrapper/gradle-wrapper.jar` - Gradle wrapper (downloaded)
- `gradlew.bat` - Fixed Gradle wrapper script

## Documentation
- `EMULATOR_TESTING_GUIDE.md` - Comprehensive emulator testing guide
- `FIREBASE_SETUP_GUIDE.md` - Firebase configuration steps
- `ISSUES_RESOLVED.md` - All fixed issues
- `QUICK_START_GUIDE.md` - General quickstart

## Build Progress
Monitor the terminal for:
1. "CONFIGURING" - Downloading and configuring dependencies
2. "EXECUTING" - Compiling code
3. "BUILD SUCCESSFUL" - Ready to install

Typical first build: **5-10 minutes**
Subsequent builds: **30-60 seconds**
