# Files Excluded from Git Repository

This document lists all files and folders that are **intentionally excluded** from the Git repository via `.gitignore`. These files are either generated during build, contain sensitive information, or are user-specific.

---

## Build Artifacts (Generated Files)

These are created during compilation and can be regenerated:

```
# APK files
*.apk
*.ap_
*.aab

# Compiled code
*.dex
*.class

# Build directories
build/
*/build/
.gradle/
out/
release/

# Proguard outputs
proguard/
```

**Reason**: Build outputs should not be versioned. They can be regenerated from source code.

---

## IDE-Specific Files (Android Studio)

These are user-specific or IDE-generated:

```
# IntelliJ IDEA
*.iml
.idea/workspace.xml
.idea/tasks.xml
.idea/gradle.xml
.idea/assetWizardSettings.xml
.idea/dictionaries
.idea/libraries
.idea/caches
.idea/modules.xml
.idea/navEditor.xml

# Kept in repository:
.idea/compiler.xml       ← Project compiler settings
.idea/misc.xml           ← Project SDK version
.idea/vcs.xml           ← Git integration
```

**Reason**: These contain user-specific paths and preferences. Each developer has their own.

---

## Local Configuration

These contain machine-specific or sensitive data:

```
# Local SDK path
local.properties

# Keystore files (sensitive!)
*.jks
*.keystore

# Temporary files
BUILD_FIX_APPLIED.md
BUILD_NOW.bat
FixRunButton.bat
ACTIVATE_RUN_BUTTON_NOW.txt
RUN_APP_NOW.md
LaunchApp.bat
QuickLaunch.bat
SystemCheck.bat
```

**Reason**: 
- `local.properties` contains absolute SDK paths unique to each machine
- Keystore files contain signing keys (NEVER commit these!)
- Temporary batch files are for local development only

---

## Sensitive Data

**NEVER commit these:**

```
google-services.json      ← Contains Firebase project IDs
gradle.properties         ← May contain API keys
```

**Status**: 
- `google-services.json` - **Included** (sample, but reviewers should replace)
- `gradle.properties` - **Included** (with placeholder API keys)

⚠️ **WARNING**: Before going to production, regenerate Firebase config and use real API keys!

---

## What IS Included in Git

### Source Code
✅ All `.kt` (Kotlin) files  
✅ All `.xml` (resource) files  
✅ `AndroidManifest.xml`  

### Configuration
✅ `build.gradle.kts` (root & app)  
✅ `settings.gradle.kts`  
✅ `gradle.properties` (with placeholders)  
✅ `proguard-rules.pro`  

### Documentation
✅ `README.md`  
✅ `SETUP_GUIDE.md`  
✅ `ARCHITECTURE.md`  
✅ `CONTRIBUTING.md`  
✅ `CHANGELOG.md`  
✅ `PROJECT_SUMMARY.md`  
✅ `LICENSE`  

### Build Tools
✅ `gradlew` & `gradlew.bat`  
✅ `gradle/wrapper/`  

### Resources
✅ `res/` directory (layouts, strings, drawables)  
✅ `assets/` directory (if present)  

---

## File Count Summary

| Category | Included | Excluded |
|----------|----------|----------|
| **Source Files (.kt)** | ✅ 100+ files | ❌ None |
| **Resource Files (.xml)** | ✅ All | ❌ None |
| **Build Files** | ✅ Config only | ❌ Artifacts |
| **Documentation** | ✅ All 8 files | ❌ Temp docs |
| **IDE Files** | ✅ Essential 3 | ❌ User-specific |
| **Generated Files** | ❌ None | ✅ All build outputs |

---

## Repository Size

With proper `.gitignore`:

| Metric | Size |
|--------|------|
| **With build artifacts** | ~150 MB ❌ |
| **Without build artifacts** | ~5 MB ✅ |
| **Reduction** | 97% smaller! |

---

## Security Notes

### API Keys
The repository includes **placeholder** API keys in `gradle.properties`:
```properties
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY_HERE
```

**Action Required**: Replace with real API keys after cloning.

### Firebase Configuration
The `google-services.json` is a **sample** configuration. 

**Action Required**: 
1. Create your own Firebase project
2. Download your `google-services.json`
3. Replace the sample file

### Signing Keys
**NEVER commit**:
- `debug.keystore` (local development key)
- `release.keystore` (production signing key)

These should be stored securely and never in version control.

---

## For Third-Party Reviewers

### What You'll See in Repository
✅ Complete source code  
✅ All documentation  
✅ Build configuration  
✅ Resource files  

### What You Won't See
❌ Build outputs (APK, AAB)  
❌ Generated files (.dex, .class)  
❌ User-specific IDE settings  
❌ Compiled libraries  
❌ Temporary files  

### To Build the Project
You'll need to:
1. Clone the repository
2. Add your `local.properties` with SDK path
3. Add your `google-services.json` from Firebase
4. Add your Google Maps API key to `gradle.properties`
5. Run: `./gradlew assembleDebug`

See **SETUP_GUIDE.md** for detailed instructions.

---

## Verification Commands

To verify what's included/excluded:

```bash
# See all tracked files
git ls-files

# See ignored files
git status --ignored

# Check repository size
git count-objects -vH

# See what would be added
git add --dry-run .
```

---

## Best Practices Followed

✅ **Build artifacts excluded** - Can be regenerated  
✅ **User-specific files excluded** - Unique per developer  
✅ **Sensitive data excluded** - API keys, keystores  
✅ **Essential config included** - Needed to build  
✅ **Documentation included** - Helps reviewers  
✅ **Source code included** - The actual project  

---

## If You Need to Include Something

To track a previously ignored file:

```bash
git add -f path/to/file
```

**Use sparingly!** Most ignored files should stay ignored.

---

## Repository Cleanliness

This repository follows Git best practices:
- ✅ Small repository size
- ✅ Fast clone times
- ✅ No unnecessary files
- ✅ Clear history
- ✅ Professional structure

---

**Last Updated**: October 26, 2025  
**Git Version**: 2.x recommended

