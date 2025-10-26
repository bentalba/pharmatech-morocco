# PharmaTech Morocco - Emulator Testing Guide

## Prerequisites

### 1. System Requirements
- Android Studio Hedgehog (2023.1.1) or later
- Minimum 16GB RAM (recommended 32GB for smooth emulation)
- 20GB free disk space
- Intel VT-x or AMD-V virtualization enabled in BIOS

### 2. Required Tools
- Android SDK Platform Tools
- Android Emulator
- Google Play Services
- Google APIs

## Emulator Setup

### Recommended Emulator Configurations

#### Configuration 1: Modern Device (Primary Testing)
- **Device:** Pixel 6
- **API Level:** 34 (Android 14)
- **Target:** Google APIs (with Play Store)
- **RAM:** 4096 MB
- **Storage:** 8 GB
- **Features:**
  - ✅ Google Play Services
  - ✅ Camera support
  - ✅ GPS/Location
  - ✅ Biometric sensors

#### Configuration 2: Compatibility Testing
- **Device:** Pixel 4
- **API Level:** 29 (Android 10)
- **Target:** Google APIs
- **RAM:** 3072 MB
- **Storage:** 8 GB

#### Configuration 3: Tablet Testing
- **Device:** Pixel Tablet
- **API Level:** 34
- **Target:** Google APIs
- **RAM:** 4096 MB

### Creating the Emulator

```bash
# Via Android Studio Device Manager
# 1. Tools > Device Manager
# 2. Create Device > Pixel 6
# 3. Select System Image: API 34 Google APIs
# 4. Verify configuration
# 5. Finish

# Via Command Line
avdmanager create avd -n "Pixel_6_API_34" -k "system-images;android-34;google_apis;x86_64" -d "pixel_6"
```

## Pre-Test Configuration

### Step 1: Firebase Setup

1. **Create Firebase Project:**
   ```
   https://console.firebase.google.com/
   ```

2. **Download `google-services.json`:**
   - Go to Project Settings > Your Apps > Android App
   - Download the config file
   - Replace `/app/google-services.json` with your downloaded file

3. **Enable Firebase Services:**
   - ✅ Authentication (Email/Password)
   - ✅ Firestore Database
   - ✅ Storage
   - ✅ Crashlytics
   - ✅ Cloud Messaging (optional)

### Step 2: Google Maps API Key

1. **Get API Key:**
   ```
   https://console.cloud.google.com/google/maps-apis
   ```

2. **Enable APIs:**
   - Maps SDK for Android
   - Places API
   - Geolocation API

3. **Configure API Key:**
   ```bash
   # Edit gradle.properties
   MAPS_API_KEY=AIza...your_actual_key_here
   ```

4. **Restrict API Key (Recommended):**
   - Application restrictions: Android apps
   - Add package name: `com.pharmatech.morocco`
   - Add SHA-1 fingerprint:
     ```bash
     keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
     ```

### Step 3: Configure Emulator Features

#### Enable Location Services
```bash
# Via Extended Controls in Emulator
# ... > Location > Set custom location
# Example: Casablanca, Morocco
Latitude: 33.5731
Longitude: -7.5898
```

Or use GPX file:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<gpx version="1.0">
  <wpt lat="33.5731" lon="-7.5898">
    <name>Casablanca</name>
  </wpt>
</gpx>
```

#### Configure Camera
```bash
# Via AVD Manager > Edit > Show Advanced Settings
# Camera:
#   - Front: Emulated
#   - Back: VirtualScene or Webcam0 (if available)
```

#### Enable Biometric Authentication
```bash
# Via adb
adb -e emu finger touch 1
```

Or in Emulator Extended Controls:
```
... > Fingerprint > Touch the sensor
```

## Building and Installing

### Debug Build

```bash
# Clean and build
./gradlew clean assembleDebug

# Install on emulator
./gradlew installDebug

# Or combined
./gradlew clean installDebug
```

### Running from Android Studio

```
Run > Run 'app'
```

Select your emulator from the device dropdown.

## Testing Scenarios

### 1. Initial Launch Testing

#### Test 1.1: First Launch
- [ ] App installs successfully
- [ ] Splash screen displays
- [ ] No crashes on launch
- [ ] Firebase initializes (check Logcat for "Firebase initialized successfully" or "OFFLINE MODE")
- [ ] Navigation to login/home screen works

**Expected Logs:**
```
D/PharmaTechApp: Firebase initialized successfully
I/PharmaTechApp: PharmaTech Morocco App initialized successfully in ONLINE mode
```

Or if Firebase not configured:
```
E/PharmaTechApp: Firebase initialization failed - likely missing or invalid google-services.json
W/PharmaTechApp: App will run in OFFLINE MODE with limited functionality
I/PharmaTechApp: PharmaTech Morocco App initialized successfully in OFFLINE mode
```

#### Test 1.2: Permissions
- [ ] App requests necessary permissions on first use
- [ ] Location permission dialog appears when accessing pharmacies
- [ ] Camera permission dialog appears when scanning barcodes
- [ ] Notification permission requested appropriately

### 2. Authentication Testing

#### Test 2.1: User Registration
```
Test Case: Register new user
1. Navigate to registration screen
2. Enter test email: test@example.com
3. Enter password: Test123456!
4. Enter display name: Test User
5. Submit registration

Expected: User registered successfully, navigated to home screen
```

#### Test 2.2: User Login
```
Test Case: Login with existing user
1. Navigate to login screen
2. Enter credentials
3. Submit login

Expected: Login successful, home screen loads
```

#### Test 2.3: Offline Auth Handling
```
Test Case: Login without network
1. Disable network on emulator (F8 or Extended Controls > Cellular > Data OFF)
2. Attempt login

Expected: Graceful error message about network connectivity
```

### 3. Location & Pharmacy Features

#### Test 3.1: Nearby Pharmacies
```
Test Case: Load nearby pharmacies
Prerequisites: Location permission granted, mock location set

1. Open pharmacy tab
2. Grant location permission if prompted
3. Wait for pharmacy list to load

Expected:
- Loading indicator shows
- Mock pharmacies from Morocco displayed (if mock data loaded)
- OR empty state with helpful message
- Map shows user location and pharmacy markers
```

#### Test 3.2: Pharmacy Search
```
Test Case: Search pharmacies
1. Open pharmacy screen
2. Enter search query: "Centrale"
3. View results

Expected: Filtered list of matching pharmacies
```

#### Test 3.3: Mock Location Testing
```
# Set different Moroccan cities
Casablanca: 33.5731, -7.5898
Rabat: 34.0209, -6.8416
Marrakech: 31.6295, -7.9811
Fes: 34.0181, -5.0078

Test pharmacy results update based on location
```

### 4. Medication Features

#### Test 4.1: Medication Search
```
Test Case: Search medications
1. Open medication screen
2. Search for: "Paracetamol"
3. View results

Expected: List of matching medications (from mock data or backend)
```

#### Test 4.2: Barcode Scanning
```
Test Case: Scan medication barcode
Prerequisites: Camera permission granted

1. Click scan barcode button
2. Allow camera access
3. Point camera at barcode or use mock barcode: 6111000001234

Expected:
- Camera opens (or shows "Camera not available on emulator" message)
- If supported, barcode detected and medication details shown
- If not supported, helpful message about emulator limitations
```

### 5. Network Scenarios

#### Test 5.1: Offline Mode
```
Test Case: App behavior offline
1. Disable emulator network (F8)
2. Navigate through app
3. Try pharmacy search
4. Try medication search

Expected:
- App remains functional with cached data
- Error messages indicate offline status
- UI shows "No internet connection" appropriately
```

#### Test 5.2: Network Reconnection
```
Test Case: Network recovery
1. Start with network disabled
2. Navigate app (offline)
3. Enable network
4. Trigger refresh

Expected:
- Network status updates automatically
- Data syncs from backend
- UI updates to show online status
```

#### Test 5.3: Slow Network Simulation
```
# Via Extended Controls > Cellular
Set network speed to "EDGE" or "GPRS"

Expected:
- Loading indicators appear
- No ANR (Application Not Responding) errors
- Requests timeout gracefully (within 15-30 seconds)
```

### 6. Notifications & Reminders

#### Test 6.1: Notification Channels
```
Test Case: Verify notification channels created
1. Launch app
2. Go to Android Settings > Apps > PharmaTech > Notifications

Expected: Three channels visible
- Medication Reminders (High importance)
- Pharmacy Updates (Default)
- Health Insights (Low)
```

#### Test 6.2: Medication Reminder Scheduling
```
Test Case: Schedule medication reminder
Prerequisites: POST_NOTIFICATIONS permission granted (Android 13+)

1. Add medication to tracker
2. Set reminder time
3. Save

Expected:
- Reminder scheduled
- Notification appears at scheduled time (test with immediate time for quick verification)
```

#### Test 6.3: Exact Alarm Permission (Android 12+)
```
Test Case: Exact alarm permission handling
Prerequisites: Android 12+ (API 31+) emulator

1. Attempt to schedule exact reminder
2. Check if permission requested

Expected:
- On Android 12+: Permission prompt or settings redirect
- Permission status visible in app
- Fallback to inexact alarms if permission denied
```

### 7. Database & Data Persistence

#### Test 7.1: Data Persistence
```
Test Case: Verify data persists across app restarts
1. Add favorite pharmacy
2. Add medication to tracker
3. Close app completely (swipe away from recents)
4. Reopen app

Expected: All data still present
```

#### Test 7.2: Mock Data Generation
```
Test Case: Generate mock data (DEBUG builds only, emulator only)
Prerequisites: Debug build running on emulator

1. Access developer menu (if implemented)
2. Trigger "Generate Mock Data"
3. Navigate to pharmacy/medication screens

Expected:
- Sample pharmacies visible
- Sample medications searchable
- Realistic Moroccan pharmacy/medication data
```

### 8. Performance Testing

#### Test 8.1: App Launch Time
```
Measure: Time from tap to usable home screen
Target: < 3 seconds on Pixel 6 emulator

Use: adb shell am start -W com.pharmatech.morocco/.MainActivity
Check TotalTime in output
```

#### Test 8.2: Memory Usage
```
Monitor in Android Studio Profiler
Target:
- Idle: < 150 MB
- Active use: < 300 MB
- No memory leaks (GC events should free memory)
```

#### Test 8.3: Network Request Performance
```
Monitor in Network Profiler
Target:
- API requests complete within 5 seconds
- Pharmacy search: < 2 seconds
- Medication search: < 2 seconds
```

### 9. UI/UX Testing

#### Test 9.1: Screen Orientations
```
Test Case: Landscape mode
1. Rotate emulator to landscape (Ctrl+F11 / Cmd+←)
2. Navigate through all screens
3. Rotate back to portrait

Expected: All screens adapt correctly, no crashes
```

#### Test 9.2: Dark Mode
```
Test Case: Dark mode support
1. Enable dark mode in emulator: Settings > Display > Dark theme
2. Navigate through app

Expected: UI adapts to dark theme appropriately
```

#### Test 9.3: Different Screen Sizes
```
Test on:
- Phone: Pixel 6 (411 x 915 dp)
- Tablet: Pixel Tablet (900 x 1200 dp)
- Small phone: Pixel 4 (393 x 830 dp)

Expected: Responsive layouts, no overflow/clipping
```

### 10. Error Handling

#### Test 10.1: Invalid Firebase Config
```
Test Case: App behavior with placeholder Firebase config
Prerequisites: Default google-services.json (placeholder)

1. Launch app
2. Check logs

Expected:
- App doesn't crash
- "Running in OFFLINE MODE" message in logs
- Auth features show appropriate errors
```

#### Test 10.2: Invalid Maps API Key
```
Test Case: Maps with invalid API key
Prerequisites: Placeholder MAPS_API_KEY in gradle.properties

1. Navigate to pharmacy map view

Expected:
- Map shows error overlay
- Helpful message about API key configuration
- App doesn't crash
```

#### Test 10.3: Backend API Unavailable
```
Test Case: Backend 404/500 errors
Prerequisites: Network enabled, backend URL unreachable

1. Trigger pharmacy search (network call)
2. Check behavior

Expected:
- Error message displayed
- Cached data shown if available
- Option to retry
```

## Debugging Tips

### Viewing Logs

```bash
# All app logs
adb logcat -s "PharmaTech*"

# Firebase logs
adb logcat -s "Firebase*"

# Network logs
adb logcat -s "OkHttp"

# Database logs
adb logcat -s "Room"

# Clear logs and start fresh
adb logcat -c && adb logcat -s "PharmaTech*"
```

### Inspecting Database

```bash
# Pull database from emulator
adb -e pull /data/data/com.pharmatech.morocco/databases/pharmatech_database ./

# Inspect with sqlite3
sqlite3 pharmatech_database
.tables
SELECT * FROM pharmacies;
SELECT * FROM medications;
```

### Network Debugging

```bash
# Use Charles Proxy or Proxyman
# Configure emulator proxy:
adb shell settings put global http_proxy <your_ip>:8888

# Reset proxy:
adb shell settings put global http_proxy :0
```

### Resetting App State

```bash
# Clear all app data
adb shell pm clear com.pharmatech.morocco

# Uninstall and reinstall
adb uninstall com.pharmatech.morocco
./gradlew installDebug
```

## Common Issues & Solutions

### Issue: "Default FirebaseApp is not initialized"
**Solution:** Replace `app/google-services.json` with valid config from Firebase Console

### Issue: Maps not loading / blank map
**Solutions:**
1. Check `gradle.properties` has valid `MAPS_API_KEY`
2. Verify API key is enabled for "Maps SDK for Android"
3. Check API key restrictions (package name + SHA-1)

### Issue: Location not working
**Solutions:**
1. Grant location permission in app
2. Set mock location in emulator (Extended Controls > Location)
3. Verify `ACCESS_FINE_LOCATION` permission in manifest

### Issue: Camera/Barcode scanning not working
**Solution:** Emulator camera support is limited
- Use webcam as camera source if available
- Test on physical device for production validation
- Emulator detection should show appropriate message

### Issue: App crashes on launch
**Solutions:**
1. Check Logcat for stack trace
2. Verify all dependencies in sync
3. Clear build: `./gradlew clean`
4. Invalidate caches: File > Invalidate Caches / Restart

### Issue: Slow emulator performance
**Solutions:**
1. Reduce emulator RAM if system RAM is limited
2. Enable Hardware Acceleration (HAXM/WHPX)
3. Close other running emulators
4. Use x86_64 system images (faster than ARM)

## Release Build Testing

### Create Release Build

```bash
# Generate release APK
./gradlew assembleRelease

# APK location:
# app/build/outputs/apk/release/app-release-unsigned.apk
```

### Test ProGuard

```bash
# Ensure ProGuard rules are working
./gradlew assembleRelease

# Install release build
adb install app/build/outputs/apk/release/app-release-unsigned.apk

# Test all features
# Check for crashes due to over-obfuscation
```

## Continuous Testing

### Automated Emulator Tests

```bash
# Run instrumented tests
./gradlew connectedAndroidTest

# Run specific test
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.pharmatech.morocco.ExampleTest
```

## Checklist: Pre-Deployment Validation

- [ ] All tests pass on Pixel 6 API 34 emulator
- [ ] All tests pass on Pixel 4 API 29 emulator
- [ ] Firebase configuration working
- [ ] Maps API key valid and restricted
- [ ] Authentication flows work
- [ ] Location features functional
- [ ] Medication search/tracking works
- [ ] Notifications delivered
- [ ] Offline mode tested
- [ ] Network error handling verified
- [ ] Release build tested with ProGuard
- [ ] No memory leaks detected
- [ ] Performance benchmarks met
- [ ] UI tested on phone and tablet
- [ ] Dark mode tested
- [ ] Landscape orientation tested

---

**Last Updated:** 2025-10-26
**Version:** 1.0.0
