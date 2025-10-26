# PharmaTech Morocco - Launch Guide for Android Studio

## Quick Start (3 Steps)

### Step 1: Start Your Emulator
1. Open Android Studio
2. Look for the Device Manager icon in the toolbar (looks like a phone)
3. Click the green Play ▶ button next to "Medium Phone API 36" (or your device name)
4. Wait 1-2 minutes for the device to fully boot
5. You should see the Android home screen

### Step 2: Run the Build Script
1. Go to your project folder: `C:\Users\LENOVO\Desktop\Pharmacie`
2. Double-click `QuickLaunch.bat`
3. Wait for the build to complete (2-5 minutes first time)
4. The app will automatically install and launch

### Step 3: Verify App is Running
- Check the emulator screen
- You should see the PharmaTech Morocco splash screen or login screen

---

## If QuickLaunch.bat Doesn't Work

### Manual Method (Using Android Studio)

#### A. Sync Gradle
1. Open Android Studio
2. Open your project: `C:\Users\LENOVO\Desktop\Pharmacie`
3. Click the "Sync Project with Gradle Files" button (looks like an elephant with an arrow)
4. Wait for sync to complete (check bottom status bar)
5. If errors appear in the "Build" tab at bottom, read them carefully

#### B. Run the App
1. Make sure your emulator is running (see Step 1 above)
2. At the top of Android Studio, find the device selector dropdown
   - It's next to the green Run ▶ button
   - Should show your emulator name or "Medium Phone API 36"
3. Click the green Run ▶ button (or press Shift+F10)
4. Android Studio will build, install, and launch the app

---

## Common Issues and Fixes

### Issue 1: "Run button is disabled/grayed out"

**Cause:** Gradle sync hasn't completed or failed

**Fix:**
1. Wait for Gradle sync to finish (check bottom status bar)
2. If sync failed:
   - Click "Build" → "Clean Project"
   - Then click "Build" → "Rebuild Project"
   - Wait for completion
3. If still disabled:
   - Click "File" → "Sync Project with Gradle Files"
   - Wait again

### Issue 2: "No devices found"

**Cause:** Emulator not running or not detected

**Fix:**
1. Start emulator from Device Manager (see Step 1)
2. Wait for device to fully boot
3. Try running `adb devices` in cmd to verify:
   ```cmd
   C:\Users\LENOVO\AppData\Local\Android\Sdk\platform-tools\adb.exe devices
   ```
4. Should show your device as "device" (not "offline")

### Issue 3: "Build failed with errors"

**Cause:** Various compilation issues

**Fix:**
1. Look at the "Build" tab at bottom of Android Studio
2. Read the error message carefully
3. Common fixes:
   - Missing dependencies: Click "Sync Project with Gradle Files"
   - Internet issues: Check your connection, retry
   - Java version: Ensure JDK 17 is installed

### Issue 4: "App crashes immediately"

**Cause:** Missing Firebase configuration or runtime errors

**Fix:**
1. Check Logcat (Alt+6 in Android Studio)
2. Look for red error messages
3. Common issues:
   - Firebase: Ensure `google-services.json` exists in `app/` folder
   - Missing permissions: Already configured, should work

### Issue 5: QuickLaunch.bat shows "ERROR"

**Fix based on error message:**

- **"ADB not found"**
  - Your SDK is at: `C:\Users\LENOVO\AppData\Local\Android\Sdk`
  - Verify `platform-tools\adb.exe` exists there
  - If not, install Platform-Tools from SDK Manager

- **"No device detected"**
  - Start emulator manually (see Step 1)
  - Run script again

- **"Build failed"**
  - Open Android Studio
  - Click "Build" → "Make Project" to see detailed errors
  - Fix errors, then retry script

- **"Installation failed"**
  - Run these commands in cmd:
    ```cmd
    C:\Users\LENOVO\AppData\Local\Android\Sdk\platform-tools\adb.exe uninstall com.pharmatech.morocco.debug
    C:\Users\LENOVO\AppData\Local\Android\Sdk\platform-tools\adb.exe uninstall com.pharmatech.morocco
    ```
  - Then run QuickLaunch.bat again

---

## Step-by-Step Manual Build (If All Else Fails)

Open cmd and run these commands one by one:

```cmd
cd C:\Users\LENOVO\Desktop\Pharmacie

gradlew.bat clean

gradlew.bat assembleDebug

C:\Users\LENOVO\AppData\Local\Android\Sdk\platform-tools\adb.exe install -r app\build\outputs\apk\debug\app-debug.apk

C:\Users\LENOVO\AppData\Local\Android\Sdk\platform-tools\adb.exe shell am start -n com.pharmatech.morocco.debug/.MainActivity
```

If any command fails, copy the error message.

---

## Verifying Success

When the app launches successfully, you should see:

1. **Splash Screen** (brief)
2. **Login Screen** with:
   - PharmaTech Morocco logo/title
   - Email field
   - Password field
   - Login button
   - Sign in with Google button
   - Sign Up link

If you see this screen, **SUCCESS!** The app is running.

---

## Next Steps After Successful Launch

### Test Basic Navigation
1. Try clicking "Sign Up" to see the registration screen
2. Register a test account
3. Login with your test credentials
4. Explore the home screen

### Expected Features on Home Screen
- Welcome message with your name
- Today's medications count
- Adherence rate
- Quick action buttons
- Nearby pharmacies

---

## Android Studio Tips

### Useful Shortcuts
- **Run app**: Shift+F10
- **Stop app**: Ctrl+F2
- **Open Logcat**: Alt+6 (to see logs/errors)
- **Open Device Manager**: Click phone icon in toolbar
- **Sync Gradle**: Ctrl+Shift+O (or click elephant button)

### Logcat Filters (for debugging)
1. Open Logcat (Alt+6)
2. In the search box, type: `package:com.pharmatech.morocco`
3. Set log level to "Error" or "Warn" to see problems
4. Red lines = errors to investigate

---

## Getting Help

If you encounter an error:

1. **Take a screenshot** of the error message
2. **Check Logcat** (Alt+6) for detailed errors
3. **Copy the error text** from the Build tab or Logcat
4. Provide:
   - What you were doing when error occurred
   - The complete error message
   - Screenshot if possible

---

## Summary Checklist

- [ ] Emulator is running and shows Android home screen
- [ ] Android Studio is open with your project
- [ ] Gradle sync completed successfully (no errors in Build tab)
- [ ] Device selector shows your emulator name
- [ ] Run ▶ button is green (not grayed out)
- [ ] Clicked Run ▶ or ran QuickLaunch.bat
- [ ] App installed without errors
- [ ] App launched and shows login screen

If all checked, you're ready to develop and test!

