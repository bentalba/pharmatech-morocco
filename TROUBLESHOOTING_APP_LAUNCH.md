# 🚨 URGENT: App Not Launching on Emulator - Debug Guide

## Issue Summary

**Symptoms:**
- Run button spins endlessly
- App doesn't appear on emulator
- "APP UI is unresponsive" error
- Nothing happens on home screen

**Your Configuration:**
- Emulator: Android API 36.1 (Android 16.0 Preview)
- App Target: API 34 (Android 14)
- Status: **INCOMPATIBLE** ❌

---

## IMMEDIATE SOLUTION (Recommended)

### Option 1: Use API 34 Emulator (FASTEST FIX)

**This is the recommended solution - API 36 is too new and unstable.**

#### Step 1: Create New Emulator

In Android Studio:
1. **Tools > Device Manager**
2. Click **"+"** to create new device
3. Select **Pixel 6** or **Pixel 7**
4. Click **Next**
5. **IMPORTANT:** Select **API 34 (Android 14.0)** with **Google APIs**
   - If not downloaded, click the download icon next to it
6. Click **Next** > **Finish**
7. Name it: `Pixel_6_API_34`

#### Step 2: Run on New Emulator

1. In toolbar, select the new **Pixel_6_API_34** device
2. Click **Run** ▶️
3. Wait for app to launch

**Expected:** App should launch successfully in 10-30 seconds.

---

## Option 2: Debug Current API 36 Issue

**Only try this if you MUST use API 36 (not recommended)**

### Step 1: Check Logcat Output

In Android Studio:

1. Open **Logcat** tab (bottom of screen)
2. Click **Run** ▶️ again
3. In Logcat, set filter to **"No Filters"**
4. Look for RED error messages

**Copy the entire error stack trace and share it for further diagnosis.**

### Step 2: Check Build Output

1. Open **Build** tab (bottom of screen)
2. Look for any **Gradle errors** or **warnings**
3. Copy any error messages

### Step 3: Manual Logcat Check

Open **Terminal** in Android Studio (bottom) and run:

```bash
# Clear previous logs
adb logcat -c

# Start app and monitor logs
adb logcat *:E

# This shows only ERROR level logs
# Look for lines containing "PharmaTech" or "FATAL EXCEPTION"
```

---

## Common Errors & Solutions

### Error 1: "Installation failed: INSTALL_FAILED_UPDATE_INCOMPATIBLE"

**Solution:**
```bash
# Uninstall old version
adb uninstall com.pharmatech.morocco

# Then run again from Android Studio
```

### Error 2: "Waiting for target device to come online"

**Solution:**
```bash
# Check emulator is running
adb devices

# Should show:
# emulator-5554    device

# If shows "offline", restart emulator
```

### Error 3: "Error: Hilt classes not generated"

**Solution:**
1. **Build > Clean Project**
2. **Build > Rebuild Project**
3. Wait for build to complete
4. Run again

### Error 4: "Default FirebaseApp is not initialized"

**This should be fixed in our latest code, but if you see it:**

**Solution:**
- This is expected if you haven't configured Firebase yet
- App should continue in OFFLINE MODE
- Check Logcat for: "App will run in OFFLINE MODE"

### Error 5: "Unable to instantiate application PharmaTechApp"

**Solution:**
```bash
# This is a Hilt issue - rebuild project
./gradlew clean
./gradlew assembleDebug
```

---

## Advanced Debugging

### Check if APK is Installing

In Terminal:
```bash
# Watch installation process
adb logcat | grep -i "install"

# Or check installed packages
adb shell pm list packages | grep pharmatech
```

### Check Activity Launch

```bash
# Monitor activity launch
adb logcat | grep -i "ActivityManager"

# Should see:
# ActivityManager: Start proc ... for activity {com.pharmatech.morocco/.MainActivity}
```

### Get Crash Report

```bash
# Get last crash
adb logcat -d | grep -i "AndroidRuntime"

# Or specific to our app
adb logcat -d | grep -i "pharmatech"
```

---

## If App Still Won't Launch

### Nuclear Option: Complete Reset

```bash
# 1. Close Android Studio
# 2. Delete build caches
./gradlew clean
rm -rf .gradle
rm -rf app/build

# 3. Wipe emulator data
# In Device Manager > Right-click emulator > Wipe Data

# 4. Restart Android Studio

# 5. File > Invalidate Caches / Restart > Invalidate and Restart

# 6. After restart:
./gradlew clean assembleDebug

# 7. Run again
```

---

## Why API 36 is Problematic

### Compatibility Issues

| Component | API 34 Support | API 36 Support |
|-----------|---------------|---------------|
| Compose BOM 2024.02 | ✅ Tested | ⚠️ Untested |
| Hilt 2.48 | ✅ Stable | ⚠️ May break |
| Firebase BOM 32.7 | ✅ Stable | ❌ Likely incompatible |
| Google Play Services | ✅ Full | ⚠️ Partial |
| ML Kit | ✅ Full | ❌ May crash |
| ARCore | ✅ Full | ❌ Incompatible |

**Android 16 Preview Issues:**
- Preview APIs change frequently
- Dependencies not updated yet
- Emulator itself may be buggy
- Not recommended for development

---

## Recommended Emulator Setup

### Production Testing Stack

Use these emulators for reliable testing:

1. **Primary:** Pixel 6 / API 34 (Android 14) - Latest stable
2. **Compatibility:** Pixel 4 / API 29 (Android 10) - Common older device
3. **Tablet:** Pixel Tablet / API 34 - Large screen testing

### How to Download System Images

If API 34 isn't available:

1. **Tools > SDK Manager**
2. Go to **SDK Platforms** tab
3. Check **Android 14.0 (API 34)**
4. Under "API 34", check **Google APIs Intel x86_64 System Image**
5. Click **Apply** to download
6. Wait for download to complete
7. Create emulator using this image

---

## Quick Diagnostic Checklist

Run through this checklist:

- [ ] Emulator is running (shows Android home screen)
- [ ] Android Studio sees emulator (appears in device dropdown)
- [ ] Gradle build succeeds (no red errors in Build tab)
- [ ] Using API 34 or lower (NOT API 35/36)
- [ ] Sufficient disk space (>10GB free)
- [ ] Sufficient RAM (>8GB available)
- [ ] No antivirus blocking Android Studio
- [ ] Internet connection available (for first build)

---

## Expected Behavior

### Successful Launch Sequence

1. Click Run ▶️
2. Gradle build runs (10-30 seconds first time)
3. "Installing APK..." appears
4. Emulator screen shows app icon briefly
5. Splash screen appears
6. Either:
   - Login screen (if Firebase configured)
   - Home screen (if offline mode)

**Total time:** 30-60 seconds on first run

### What You Should See in Logcat

```
D/PharmaTechApp: Running on EMULATOR: Google sdk_gphone64_x86_64
I/PharmaTechApp: Firebase initialized successfully
  OR
W/PharmaTechApp: App will run in OFFLINE MODE with limited functionality
I/PharmaTechApp: PharmaTech Morocco App initialized successfully in [ONLINE/OFFLINE] mode
```

---

## Still Not Working?

### Get Detailed Diagnostics

Run in Terminal:
```bash
# Get full system info
adb shell getprop | grep -i "ro.build"

# Check available memory
adb shell dumpsys meminfo com.pharmatech.morocco

# Get app install status
adb shell pm path com.pharmatech.morocco
```

Share the output of these commands for further help.

---

## Emergency Workaround: Minimal Build

If nothing works, try this minimal configuration:

### Temporarily Disable Features

In `app/build.gradle.kts`, comment out problematic dependencies:

```kotlin
dependencies {
    // Core - KEEP THESE
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))

    // Hilt - KEEP
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")

    // TEMPORARILY COMMENT OUT:
    // implementation("com.google.ar:core:1.41.0")  // ARCore may break on API 36
    // ML Kit - may be incompatible

    // Firebase - if causing issues, comment temporarily
}
```

Then:
```bash
./gradlew clean assembleDebug
```

---

## Summary

**BEST SOLUTION:**
👉 **Switch to API 34 emulator** - This will solve 99% of issues.

**IF YOU MUST USE API 36:**
1. Get detailed logcat output
2. Share exact error messages
3. Prepare for potential library incompatibilities

**Next Steps:**
1. Create API 34 emulator (5 minutes)
2. Run app on API 34 (should work)
3. If issues persist, check logcat and share errors

---

*Last Updated: 2025-10-26*
