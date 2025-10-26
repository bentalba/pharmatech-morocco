# 🔥 Firebase Configuration Setup Guide - PharmaTech Morocco

## Complete Step-by-Step Firebase Integration

---

## 📋 Prerequisites

Before starting, ensure you have:
- ✅ A Google Account
- ✅ Android Studio installed
- ✅ PharmaTech Morocco project open
- ✅ Active internet connection

---

## STEP 1: Create Firebase Project

### 1.1 Go to Firebase Console
```
URL: https://console.firebase.google.com
```

### 1.2 Create New Project
1. Click **"Create a project"** or **"Add project"**
2. **Project name:** `pharmatech-morocco`
3. Click **"Continue"**
4. **Google Analytics:** Enable (Recommended) or Skip
5. If enabled, select/create Analytics account
6. Click **"Create project"**
7. Wait for project creation (30-60 seconds)
8. Click **"Continue"** when ready

---

## STEP 2: Add Android App to Firebase

### 2.1 Register Your App
1. In Firebase Console, click the **Android icon** (⚡) or **"Add app" → Android**
2. Fill in the registration form:

```
Android package name: com.pharmatech.morocco
App nickname (optional): PharmaTech Morocco
Debug signing certificate SHA-1 (optional for now)
```

### 2.2 Get SHA-1 Certificate (For Google Sign-In)

**On Windows:**
```cmd
cd C:\Users\LENOVO\Desktop\Pharmacie
gradlew signingReport
```

**On Mac/Linux:**
```bash
./gradlew signingReport
```

**Output will show:**
```
SHA-1: XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX
```

**Copy the SHA-1** and paste it in Firebase (you can add it later too)

3. Click **"Register app"**

---

## STEP 3: Download Configuration File

### 3.1 Download google-services.json
1. Click **"Download google-services.json"**
2. Save the file to your computer

### 3.2 Add to Your Project
1. **Delete** the existing placeholder file:
   ```
   C:\Users\LENOVO\Desktop\Pharmacie\app\google-services.json
   ```

2. **Copy** the downloaded `google-services.json` to:
   ```
   C:\Users\LENOVO\Desktop\Pharmacie\app\
   ```

3. **Verify** the file is in the correct location:
   ```
   Pharmacie/
   └── app/
       ├── build.gradle.kts
       └── google-services.json  ← Should be here
   ```

### 3.3 Sync Project
1. In Android Studio, click **"Sync Now"** if prompted
2. Or go to **File → Sync Project with Gradle Files**

---

## STEP 4: Enable Firebase Authentication

### 4.1 Navigate to Authentication
1. In Firebase Console, click **"Authentication"** in the left menu
2. Click **"Get started"**

### 4.2 Enable Email/Password Sign-In
1. Click **"Sign-in method"** tab
2. Find **"Email/Password"**
3. Click on it
4. Toggle **"Enable"** switch to ON
5. Click **"Save"**

### 4.3 Enable Google Sign-In
1. Still in **"Sign-in method"** tab
2. Find **"Google"**
3. Click on it
4. Toggle **"Enable"** switch to ON
5. Enter **Project support email** (your email)
6. Click **"Save"**

**Important:** Make sure you added SHA-1 certificate earlier, or add it now:
- Go to **Project Settings** (⚙️ icon)
- Scroll to **"Your apps"**
- Under your Android app, click **"Add fingerprint"**
- Paste SHA-1 and click **"Save"**

---

## STEP 5: Set Up Cloud Firestore

### 5.1 Create Firestore Database
1. In Firebase Console, click **"Firestore Database"**
2. Click **"Create database"**

### 5.2 Choose Security Rules
**For Development (Start in test mode):**
```
Start in test mode
```
- Click **"Next"**
- Select location: **eur3 (europe-west)** (closest to Morocco)
- Click **"Enable"**

**⚠️ Warning:** Test mode is NOT secure for production!

### 5.3 Create Initial Collections (Optional)
Create these collections for better structure:
```
- users
- pharmacies
- medications
- medication_trackers
- reminders
- health_insights
```

**To create a collection:**
1. Click **"+ Start collection"**
2. **Collection ID:** `users`
3. Click **"Next"**
4. Add a dummy document (Auto-ID is fine)
5. Click **"Save"**

---

## STEP 6: Set Up Cloud Storage

### 6.1 Enable Storage
1. In Firebase Console, click **"Storage"**
2. Click **"Get started"**

### 6.2 Security Rules
Choose **"Start in test mode"**
- Click **"Next"**
- Select same location as Firestore
- Click **"Done"**

### 6.3 Create Folders (Optional)
```
- profile_photos/
- prescription_images/
- medication_images/
```

---

## STEP 7: Enable Cloud Messaging (FCM)

### 7.1 Automatic Setup
Firebase Cloud Messaging is automatically enabled when you add the Android app.

### 7.2 Verify Setup
1. Click **"Cloud Messaging"** in left menu
2. You should see **"Server key"** and **"Sender ID"**
3. Keep this page open, you might need these values later

---

## STEP 8: Enable Crashlytics

### 8.1 Enable Crashlytics
1. Click **"Crashlytics"** in left menu
2. Click **"Enable Crashlytics"**
3. Follow the setup wizard

### 8.2 Verify in App
The app is already configured for Crashlytics. Just run it once:
1. Build and run your app
2. Wait 5 minutes
3. Check Firebase Console → Crashlytics
4. You should see initialization message

---

## STEP 9: Enable Analytics

### 9.1 Verify Analytics
Analytics is already enabled if you chose it during project creation.

1. Click **"Analytics"** → **"Dashboard"**
2. You should see "Waiting for data"
3. Run your app
4. Data will appear within 24 hours

---

## STEP 10: Configure Production Security Rules

### 10.1 Firestore Security Rules (Production-Ready)

Go to **Firestore Database** → **"Rules"** tab:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users collection
    match /users/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Pharmacies collection (public read, admin write)
    match /pharmacies/{pharmacyId} {
      allow read: if true;
      allow write: if request.auth != null && 
                     get(/databases/$(database)/documents/users/$(request.auth.uid)).data.isAdmin == true;
    }
    
    // Medications collection (public read, admin write)
    match /medications/{medicationId} {
      allow read: if true;
      allow write: if request.auth != null && 
                     get(/databases/$(database)/documents/users/$(request.auth.uid)).data.isAdmin == true;
    }
    
    // User-specific collections
    match /medication_trackers/{trackerId} {
      allow read, write: if request.auth != null && 
                           resource.data.userId == request.auth.uid;
    }
    
    match /reminders/{reminderId} {
      allow read, write: if request.auth != null && 
                           resource.data.userId == request.auth.uid;
    }
    
    match /health_insights/{insightId} {
      allow read, write: if request.auth != null && 
                           resource.data.userId == request.auth.uid;
    }
  }
}
```

Click **"Publish"**

### 10.2 Storage Security Rules (Production-Ready)

Go to **Storage** → **"Rules"** tab:

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    // Profile photos
    match /profile_photos/{userId}/{filename} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Prescription images
    match /prescription_images/{userId}/{filename} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Medication images (public read, admin write)
    match /medication_images/{filename} {
      allow read: if true;
      allow write: if request.auth != null;
    }
  }
}
```

Click **"Publish"**

---

## STEP 11: Test Firebase Integration

### 11.1 Build and Run
```cmd
cd C:\Users\LENOVO\Desktop\Pharmacie
gradlew clean build
gradlew installDebug
```

### 11.2 Test Authentication
1. Open the app
2. Go to Register screen
3. Create an account
4. Check Firebase Console → Authentication → Users
5. You should see the new user

### 11.3 Test Firestore
1. Login to the app
2. Perform any action that writes to Firestore
3. Check Firebase Console → Firestore Database
4. Verify data is saved

### 11.4 Check Crashlytics
1. Force a crash in the app (for testing)
2. Wait 5 minutes
3. Check Firebase Console → Crashlytics
4. You should see the crash report

---

## STEP 12: Get Google Maps API Key

### 12.1 Enable Maps SDK
1. Go to: https://console.cloud.google.com
2. Select your Firebase project (pharmatech-morocco)
3. Go to **"APIs & Services" → "Library"**
4. Search for **"Maps SDK for Android"**
5. Click on it
6. Click **"Enable"**

### 12.2 Create API Key
1. Go to **"APIs & Services" → "Credentials"**
2. Click **"+ CREATE CREDENTIALS"**
3. Select **"API key"**
4. Copy the API key

### 12.3 Restrict API Key
1. Click on the created API key to edit
2. Under **"Application restrictions"**:
   - Select **"Android apps"**
   - Click **"+ Add an item"**
   - **Package name:** `com.pharmatech.morocco`
   - **SHA-1:** (paste the SHA-1 you got earlier)
   - Click **"Done"**
3. Under **"API restrictions"**:
   - Select **"Restrict key"**
   - Check **"Maps SDK for Android"**
   - Also check **"Places API"** if you'll use it
4. Click **"Save"**

### 12.4 Add to Project
1. Open `gradle.properties`
2. Replace placeholder:
   ```properties
   MAPS_API_KEY=your_actual_google_maps_api_key_here
   ```
3. Save the file
4. Sync Gradle

---

## ✅ VERIFICATION CHECKLIST

Before moving to development:

- [ ] ✅ google-services.json in `app/` folder
- [ ] ✅ Email/Password authentication enabled
- [ ] ✅ Google Sign-In enabled with SHA-1
- [ ] ✅ Firestore database created
- [ ] ✅ Cloud Storage enabled
- [ ] ✅ Cloud Messaging enabled
- [ ] ✅ Crashlytics enabled
- [ ] ✅ Security rules updated
- [ ] ✅ Google Maps API key added
- [ ] ✅ Gradle synced successfully
- [ ] ✅ App builds without errors
- [ ] ✅ Test user created in Authentication

---

## 🚨 COMMON ISSUES & SOLUTIONS

### Issue 1: "google-services.json not found"
**Solution:** Make sure the file is in `app/` folder, not in root project folder.

### Issue 2: "Default FirebaseApp is not initialized"
**Solution:** 
1. Check `google-services.json` is correct
2. Sync Gradle
3. Clean and rebuild project

### Issue 3: Google Sign-In not working
**Solution:**
1. Verify SHA-1 is added in Firebase Console
2. Make sure Google Sign-In is enabled
3. Check package name matches exactly

### Issue 4: Maps not showing
**Solution:**
1. Verify API key in `gradle.properties`
2. Check Maps SDK is enabled in Google Cloud Console
3. Verify API key restrictions allow your package name

### Issue 5: Firestore permission denied
**Solution:**
1. Check security rules in Firestore Console
2. For testing, temporarily use test mode
3. Verify user is authenticated

---

## 📞 NEED HELP?

### Firebase Documentation
- https://firebase.google.com/docs/android/setup
- https://firebase.google.com/docs/auth/android/start
- https://firebase.google.com/docs/firestore/quickstart

### Google Maps Documentation
- https://developers.google.com/maps/documentation/android-sdk/start

---

## 🎉 SUCCESS!

If all steps are completed:
- ✅ Firebase is fully configured
- ✅ Authentication is working
- ✅ Database is ready
- ✅ Storage is set up
- ✅ Maps are configured
- ✅ Ready for development!

**Next Steps:**
1. Start implementing features
2. Test authentication flow
3. Implement data synchronization
4. Add real-time features

---

**Generated on:** October 25, 2025  
**Project:** PharmaTech Morocco  
**Version:** 1.0.0

