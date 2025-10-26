# Setup Guide for PharmaTech Morocco

This guide will help you set up the development environment and get PharmaTech Morocco running on your machine.

## Prerequisites

### Required Software

1. **Android Studio** (Arctic Fox or later)
   - Download from: https://developer.android.com/studio
   - Version: 2021.3.1 or later recommended

2. **Java Development Kit (JDK) 17**
   - Included with Android Studio
   - Or download from: https://adoptium.net/

3. **Git**
   - Download from: https://git-scm.com/downloads
   - For Windows: Git Bash is recommended

### Optional but Recommended

- **GitHub Account** - For contributing
- **Firebase Account** - For backend features
- **Google Cloud Account** - For Maps API key

## Step 1: Clone the Repository

```bash
# Clone the repository
git clone https://github.com/yourusername/pharmatech-morocco.git

# Navigate to project directory
cd pharmatech-morocco
```

## Step 2: Open in Android Studio

1. Launch Android Studio
2. Click **File → Open**
3. Navigate to the cloned `pharmatech-morocco` folder
4. Click **OK**
5. Wait for Gradle sync to complete (2-5 minutes)

### If Gradle Sync Fails:

```bash
# In Android Studio terminal or project root:
./gradlew --refresh-dependencies
```

Or manually:
1. **File → Invalidate Caches / Restart**
2. Click **Invalidate and Restart**
3. Wait for Android Studio to restart
4. **File → Sync Project with Gradle Files**

## Step 3: Configure Firebase

### Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com)
2. Click **Add Project**
3. Enter project name: `pharmatech-morocco`
4. Enable Google Analytics (optional)
5. Click **Create Project**

### Add Android App to Firebase

1. In Firebase Console, click **Add app** → **Android**
2. Enter package name: `com.pharmatech.morocco`
3. Download `google-services.json`
4. Move it to `app/` directory in your project

### Enable Firebase Services

#### Authentication
1. In Firebase Console → **Authentication**
2. Click **Get Started**
3. Enable **Email/Password**
4. Enable **Google Sign-In**
5. For Google Sign-In, add your SHA-1 certificate:

```bash
# Get SHA-1 from Android Studio
./gradlew signingReport

# Or use keytool
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

Copy the SHA-1 and add it in Firebase Console → Project Settings → Your Apps → Add fingerprint

#### Firestore Database
1. **Firestore Database** → **Create database**
2. Choose **Start in production mode**
3. Select location: **europe-west3** (closest to Morocco)
4. Click **Enable**

Set security rules:
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    match /pharmacies/{document=**} {
      allow read: if true;
      allow write: if false;
    }
    
    match /medications/{document=**} {
      allow read: if true;
      allow write: if false;
    }
  }
}
```

#### Cloud Messaging
1. **Cloud Messaging** → No additional setup needed
2. Server key will be auto-generated

#### Storage
1. **Storage** → **Get Started**
2. Choose **Start in production mode**
3. Click **Done**

Set security rules:
```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /users/{userId}/{allPaths=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

## Step 4: Get Google Maps API Key

1. Go to [Google Cloud Console](https://console.cloud.google.com)
2. Create a new project or select existing
3. Enable **Maps SDK for Android**
4. Go to **APIs & Services → Credentials**
5. Click **Create Credentials → API Key**
6. Copy the API key
7. Restrict the key (recommended):
   - Application restrictions: Android apps
   - Add package: `com.pharmatech.morocco`
   - Add SHA-1 certificate
   - API restrictions: Maps SDK for Android

### Add API Key to Project

1. Open `gradle.properties` in project root
2. Add:
```properties
MAPS_API_KEY=your_actual_api_key_here
```

3. The key will be automatically injected into AndroidManifest.xml

**Important**: Never commit `gradle.properties` with real API keys to Git!

## Step 5: Configure Local SDK Path

Android Studio should automatically create `local.properties`, but if not:

Create `local.properties` in project root:
```properties
sdk.dir=C\:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
```

Replace with your actual Android SDK path:
- Windows: `C:\Users\[Username]\AppData\Local\Android\Sdk`
- macOS: `/Users/[Username]/Library/Android/sdk`
- Linux: `/home/[Username]/Android/Sdk`

## Step 6: Build the Project

### From Android Studio

1. Click **Build → Clean Project**
2. Wait for completion
3. Click **Build → Rebuild Project**
4. Wait for build to finish (3-5 minutes first time)

### From Command Line

```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build and run tests
./gradlew build
```

## Step 7: Run on Emulator

### Create AVD (Android Virtual Device)

1. Click **Device Manager** icon (phone icon in toolbar)
2. Click **Create Device**
3. Select device: **Pixel 7** or **Pixel 8**
4. Click **Next**
5. Select system image:
   - API Level: 33 (Tiramisu) or higher
   - Click **Download** if needed
6. Click **Next**
7. AVD Name: `PharmaTech_Test`
8. Click **Finish**

### Run the App

1. Select your AVD from device dropdown
2. Click green **Run ▶** button
3. Wait for emulator to start (1-2 minutes)
4. App will install and launch automatically

## Step 8: Run on Physical Device

1. Enable Developer Options on your device:
   - Settings → About Phone
   - Tap **Build Number** 7 times

2. Enable USB Debugging:
   - Settings → Developer Options
   - Toggle **USB Debugging**

3. Connect device via USB

4. When prompted on device, allow USB debugging

5. In Android Studio, select your device from dropdown

6. Click **Run ▶**

## Troubleshooting

### Gradle Sync Failed

**Error**: "Failed to resolve dependencies"

**Solution**:
```bash
./gradlew clean --refresh-dependencies
```

Or check internet connection and VPN settings.

### Build Failed - Firebase

**Error**: "google-services.json not found"

**Solution**: Ensure `google-services.json` is in `app/` directory, not root.

### Build Failed - API Key

**Error**: "MAPS_API_KEY not found"

**Solution**: Add MAPS_API_KEY to `gradle.properties`:
```properties
MAPS_API_KEY=your_key_here
```

### Emulator Won't Start

**Solution**:
1. Enable virtualization in BIOS
2. Install Intel HAXM or AMD equivalent
3. Allocate more RAM to emulator (2048 MB minimum)

### App Crashes on Launch

**Check Logcat**:
1. Press **Alt+6** (Windows/Linux) or **Cmd+6** (Mac)
2. Filter by: `package:com.pharmatech.morocco`
3. Look for red error lines

Common causes:
- Missing Firebase configuration
- Network permissions not granted
- API key issues

### Run Button Disabled

**Solution**:
1. **File → Invalidate Caches / Restart**
2. Click **Invalidate and Restart**
3. Wait for restart
4. **File → Sync Project with Gradle Files**

## Verify Installation

After successful build and run, verify:

- [ ] Splash screen appears
- [ ] Login screen loads
- [ ] Can register new account
- [ ] Can login successfully
- [ ] Home screen displays
- [ ] Navigation works (bottom bar)
- [ ] No crashes in normal use

## Next Steps

1. **Read Documentation**:
   - `ARCHITECTURE.md` - Understand app structure
   - `CONTRIBUTING.md` - Learn how to contribute
   - `README.md` - Feature overview

2. **Explore Code**:
   - Start with `MainActivity.kt`
   - Check `PharmaTechNavigation.kt` for app flow
   - Examine feature modules

3. **Run Tests**:
   ```bash
   ./gradlew test
   ```

4. **Make Changes**:
   - Create a new branch
   - Follow coding standards
   - Submit a pull request

## Additional Resources

- [Android Developer Guide](https://developer.android.com/guide)
- [Jetpack Compose Tutorial](https://developer.android.com/jetpack/compose/tutorial)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)

## Support

If you encounter issues not covered here:

1. Check [existing issues](https://github.com/yourusername/pharmatech-morocco/issues)
2. Open a new issue with:
   - Android Studio version
   - Android SDK version
   - Error messages
   - Steps to reproduce
   - Screenshots if applicable

## Success!

If you see the PharmaTech Morocco login screen, **congratulations!** 🎉

You're ready to start developing. Happy coding! 💻

---

**Last Updated**: October 26, 2025

