# 🚀 PharmaTech Morocco - Visual Launch Guide

## 📱 EASIEST METHOD (Recommended)

### Method 1: Using Scripts (Fastest)

```
Step 1: Start Emulator
┌────────────────────────────────────┐
│  Android Studio                    │
│  ┌──────────────────────────────┐ │
│  │ [📱] Device Manager          │ │
│  └──────────────────────────────┘ │
│                                    │
│  Your Devices:                     │
│  Medium Phone API 36  [▶ Play]   │
│  ↑                     ↑           │
│  Click this           Then this    │
└────────────────────────────────────┘

Step 2: Run Script
┌────────────────────────────────────┐
│  File Explorer                     │
│  C:\Users\LENOVO\Desktop\Pharmacie │
│                                    │
│  📄 QuickLaunch.bat                │
│     ↑                              │
│     Double-click this              │
└────────────────────────────────────┘

Step 3: Wait & Watch
┌────────────────────────────────────┐
│  Command Prompt Window             │
│  --------------------------------- │
│  [1/5] Checking devices... ✓       │
│  [2/5] Cleaning build... ✓         │
│  [3/5] Building APK... ⏳          │
│  [4/5] Installing... (wait)        │
│  [5/5] Launching... (wait)         │
│  --------------------------------- │
│  SUCCESS! App is running.          │
└────────────────────────────────────┘
```

---

## 🏃 Method 2: Using Android Studio (Traditional)

```
Step 1: Open Project
┌────────────────────────────────────────────────┐
│  Android Studio                                │
│  File → Open → Navigate to Pharmacie folder   │
│  Click OK                                      │
└────────────────────────────────────────────────┘

Step 2: Wait for Gradle Sync
┌────────────────────────────────────────────────┐
│  Android Studio - Bottom Status Bar           │
│  ├─────────────────────────────────────────┐  │
│  │ Gradle: Syncing...  [Progress bar] ⏳   │  │
│  └─────────────────────────────────────────┘  │
│                                                │
│  Wait until it shows:                          │
│  ├─────────────────────────────────────────┐  │
│  │ Gradle: Build Successful ✓              │  │
│  └─────────────────────────────────────────┘  │
└────────────────────────────────────────────────┘

Step 3: Select Device & Run
┌────────────────────────────────────────────────┐
│  Android Studio - Top Toolbar                  │
│  ┌──────────┐  ┌────────────────┐  ┌────────┐│
│  │   app   ▼│  │ Medium Phone  ▼│  │ ▶ Run  ││
│  └──────────┘  └────────────────┘  └────────┘│
│       ↑               ↑                 ↑      │
│    Module        Device Selector   Click here │
└────────────────────────────────────────────────┘
```

---

## 🔧 Troubleshooting Visual Guide

### Problem: Run Button Grayed Out ❌

```
Issue:
┌────────────────────────────────────┐
│  Toolbar                           │
│  [app ▼] [Device ▼] [⏸ Run]      │
│                        ↑           │
│                   Button is gray   │
└────────────────────────────────────┘

Fix: Sync Gradle
┌────────────────────────────────────┐
│  1. Click: File → Sync Project    │
│     with Gradle Files              │
│                                    │
│  2. Or click toolbar icon:         │
│     [🐘] ← This elephant icon     │
│                                    │
│  3. Wait for sync to complete      │
└────────────────────────────────────┘
```

### Problem: No Device Available ❌

```
Issue:
┌────────────────────────────────────┐
│  Device Selector shows:            │
│  [No Devices Available ▼]          │
└────────────────────────────────────┘

Fix: Start Emulator
┌────────────────────────────────────┐
│  1. Click Device Manager icon:     │
│     [📱] in toolbar                │
│                                    │
│  2. In Device Manager panel:       │
│     ┌──────────────────────────┐  │
│     │ Available Devices        │  │
│     │ Medium Phone API 36 [▶]  │  │
│     └──────────────────────────┘  │
│          Click Play ▶              │
│                                    │
│  3. Wait 1-2 minutes               │
│                                    │
│  4. Check device selector again    │
└────────────────────────────────────┘
```

### Problem: Build Fails ❌

```
Issue:
┌────────────────────────────────────┐
│  Build tab shows:                  │
│  ❌ Build failed in 2s             │
│  See error details below...        │
└────────────────────────────────────┘

Fix: Clean & Rebuild
┌────────────────────────────────────┐
│  1. Click: Build → Clean Project  │
│     (Wait for completion)          │
│                                    │
│  2. Click: Build → Rebuild Project│
│     (Wait 2-5 minutes)             │
│                                    │
│  3. If still fails, check errors:  │
│     - Read error message in Build  │
│       tab at bottom                │
│     - Common: Internet connection  │
│       needed for dependencies      │
└────────────────────────────────────┘
```

---

## 📊 What You Should See When Successful

```
Emulator Screen (after launch):
┌──────────────────────────────┐
│ ┌────────────────────────┐   │
│ │                        │   │
│ │   PharmaTech Morocco   │   │
│ │                        │   │
│ │   [Logo/Animation]     │   │
│ │                        │   │
│ │   Loading...           │   │
│ │                        │   │
│ └────────────────────────┘   │
│         Splash Screen         │
└──────────────────────────────┘
           ⬇ (2 seconds)
┌──────────────────────────────┐
│ ┌────────────────────────┐   │
│ │  Welcome Back          │   │
│ │                        │   │
│ │  Email:                │   │
│ │  [_________________]   │   │
│ │                        │   │
│ │  Password:             │   │
│ │  [_________________]   │   │
│ │                        │   │
│ │  [    Login    ]       │   │
│ │                        │   │
│ │  Sign in with Google   │   │
│ │                        │   │
│ │  Don't have account?   │   │
│ │  Sign Up               │   │
│ └────────────────────────┘   │
│         Login Screen          │
└──────────────────────────────┘
```

---

## 🎯 Quick Reference Commands

### Check System Status
```
📂 Navigate to: C:\Users\LENOVO\Desktop\Pharmacie
🖱️ Double-click: SystemCheck.bat
✅ Verify all checks pass
```

### Launch App (Fastest)
```
📂 Navigate to: C:\Users\LENOVO\Desktop\Pharmacie
▶️ Double-click: QuickLaunch.bat
⏱️ Wait 2-5 minutes (first time)
✅ App launches automatically
```

### Manual Build (Terminal)
```cmd
cd C:\Users\LENOVO\Desktop\Pharmacie
gradlew.bat clean assembleDebug
```

### Check Logs (if app crashes)
```
In Android Studio:
1. Press Alt+6 (opens Logcat)
2. Set filter to: package:com.pharmatech.morocco
3. Look for red error lines
4. Copy error message for help
```

---

## 📋 Pre-Flight Checklist

Before running, verify:

```
[ ] Android Studio is installed
[ ] Emulator is created and working
[ ] Internet connection is active
[ ] Project folder: C:\Users\LENOVO\Desktop\Pharmacie
[ ] google-services.json exists in app/ folder
[ ] Emulator has enough storage (4GB+)
[ ] Your PC has at least 8GB RAM
```

---

## 🆘 Emergency Recovery

If everything fails, try this nuclear option:

```
1. Close Android Studio completely
2. Delete these folders in project:
   - .gradle
   - .idea
   - app/build
   - build
3. Open Android Studio
4. Open project (will re-sync automatically)
5. Wait for sync to complete
6. Try running again
```

---

## 📞 Getting Help

When asking for help, provide:

```
✅ DO include:
   - Screenshot of error
   - Text from error message
   - What step you're on
   - Output from SystemCheck.bat

❌ DON'T say:
   - "It doesn't work"
   - "I got an error"
   (Not specific enough!)
```

---

## 🎓 Learning Android Studio

### Essential Shortcuts
```
Shift + F10     = Run app
Ctrl + F2       = Stop app
Alt + 6         = Open Logcat
Alt + 1         = Project structure
Ctrl + Shift+F  = Find in project
```

### Important Panels
```
- Logcat (Alt+6): See app logs and errors
- Build (Alt+4): See build progress and errors
- Device Manager: Manage emulators
- Project (Alt+1): Browse project files
```

---

## ✨ Success Indicators

You'll know it worked when:

```
✅ QuickLaunch.bat shows "SUCCESS!"
✅ Emulator screen shows app UI
✅ No red error messages in console
✅ App responds to touches/clicks
✅ Can navigate between screens
```

---

**Ready? Start with `SystemCheck.bat` to verify everything is ready!** 🚀

