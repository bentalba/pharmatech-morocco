# GitHub Copilot Instructions - SHIFAA Premium System

## Project Context
**CODEWORD**: When you see "start conversion", this indicates the development environment has changed to **macOS** and you should adjust all commands, paths, and references accordingly.

**Project**: SHIFAA Premium - Moroccan Pharmacy Management System  
**Platform**: Android (Kotlin + Jetpack Compose)  
**Architecture**: MVVM + Clean Architecture  
**Current State**: Fully functional, deployed on emulator

---

## Core Principles

### 1. Code Quality & Architecture
- **ALWAYS** follow MVVM + Clean Architecture patterns
- **MAINTAIN** separation of concerns: data, domain, presentation layers
- **USE** Jetpack Compose for all UI components
- **APPLY** Material 3 design guidelines consistently
- **FOLLOW** Kotlin coding conventions and idiomatic patterns
- **IMPLEMENT** dependency injection with Hilt
- **ENSURE** proper error handling with sealed classes (NetworkResult)

### 2. UI/UX Excellence
- **PRESERVE** SHIFAA Premium theme:
  - Primary: Gold #D4AF37 (luxury, premium)
  - Secondary: Pharmacy Green #2D5F3F (medical credibility)
  - Tertiary: Teal #1B4D52 (modern accent)
- **MAINTAIN** consistent spacing (8dp grid system)
- **USE** proper elevation and shadows for depth
- **ENSURE** accessible contrast ratios (WCAG AA minimum)
- **IMPLEMENT** smooth animations and transitions
- **FOLLOW** touch target sizes (minimum 48dp)
- **PROVIDE** clear visual feedback for all interactions
- **SUPPORT** both light and dark themes

### 3. Performance & Optimization
- **OPTIMIZE** for first launch time (<3 seconds)
- **IMPLEMENT** lazy loading for lists and images
- **USE** proper caching strategies (Room + in-memory)
- **AVOID** unnecessary recompositions in Compose
- **MONITOR** memory usage (especially with Apache POI)
- **HANDLE** large datasets efficiently (pagination)
- **MINIMIZE** APK size (ProGuard/R8 optimization)

### 4. Code Organization
```
features/
  └── [feature]/
      ├── data/
      │   ├── local/      # Room entities, DAOs
      │   ├── remote/     # API services, DTOs
      │   └── repository/ # Repository implementations
      ├── domain/
      │   ├── model/      # Domain models
      │   ├── repository/ # Repository interfaces
      │   └── usecase/    # Business logic
      └── presentation/
          ├── [FeatureName]Screen.kt
          ├── [FeatureName]ViewModel.kt
          └── components/  # Reusable composables
```

---

## Development Guidelines

### When Adding New Features

1. **Planning Phase**
   - Review existing architecture
   - Identify affected layers (data/domain/presentation)
   - Check for existing similar implementations
   - Consider backward compatibility

2. **Implementation Phase**
   ```kotlin
   // 1. Create domain model (if needed)
   data class YourModel(...)
   
   // 2. Create repository interface
   interface YourRepository {
       suspend fun operation(): NetworkResult<YourModel>
   }
   
   // 3. Implement repository
   class YourRepositoryImpl @Inject constructor(...) : YourRepository
   
   // 4. Create ViewModel
   @HiltViewModel
   class YourViewModel @Inject constructor(...) : ViewModel()
   
   // 5. Create Composable Screen
   @Composable
   fun YourScreen(navController: NavController, viewModel: YourViewModel = hiltViewModel())
   ```

3. **Testing Phase**
   - Test on emulator (API 26+)
   - Verify theme consistency
   - Check memory usage
   - Test error scenarios

### When Modifying Existing Code

1. **ALWAYS READ** the entire file before making changes
2. **PRESERVE** existing patterns and conventions
3. **MAINTAIN** backward compatibility
4. **UPDATE** documentation if behavior changes
5. **TEST** thoroughly after modifications
6. **COMMIT** with descriptive messages following conventions

### When Fixing Bugs

1. **IDENTIFY** root cause before proposing solutions
2. **CHECK** for similar issues in codebase
3. **PREFER** fixing the cause over treating symptoms
4. **ADD** comments explaining non-obvious fixes
5. **CONSIDER** adding tests to prevent regression

---

## Technology Stack Specifics

### Jetpack Compose
```kotlin
// ALWAYS use remember for state
var state by remember { mutableStateOf(initialValue) }

// USE derivedStateOf for computed state
val computed by remember { derivedStateOf { computation() } }

// PREFER keys for lists
items(items, key = { it.id }) { item ->
    ItemComposable(item)
}

// USE proper modifiers order
Modifier
    .size(...)        // Size first
    .padding(...)     // Then padding
    .background(...)  // Then appearance
    .clickable(...)   // Then interaction
```

### Room Database
```kotlin
// ALWAYS use suspend for queries
@Query("SELECT * FROM table")
suspend fun getAll(): List<Entity>

// USE Flow for reactive queries
@Query("SELECT * FROM table WHERE id = :id")
fun observeById(id: String): Flow<Entity?>

// HANDLE migrations properly
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Migration logic
    }
}
```

### Retrofit + OkHttp
```kotlin
// ALWAYS handle errors with sealed class
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

// USE proper timeout configuration
OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
```

### Apache POI (Excel Import)
```kotlin
// ALWAYS close workbook after use
val workbook = WorkbookFactory.create(inputStream)
try {
    // Process Excel data
} finally {
    workbook.close()
}

// HANDLE large files in coroutines
withContext(Dispatchers.IO) {
    processExcelFile()
}

// MONITOR memory usage
// POI requires ~15K methods, ensure MultiDex is enabled
```

---

## Environment-Specific Adjustments

### Windows Environment (Current)
```powershell
# Build command
.\gradlew assembleDebug

# Install on emulator
.\gradlew installDebug

# ADB path
C:\Users\LENOVO\AppData\Local\Android\Sdk\platform-tools\adb.exe
```

### macOS Environment (After "start conversion")
```bash
# Build command
./gradlew assembleDebug

# Install on emulator
./gradlew installDebug

# ADB path
~/Library/Android/sdk/platform-tools/adb

# Android Studio path
/Applications/Android\ Studio.app

# SDK path (typical)
~/Library/Android/sdk
```

### Linux Environment
```bash
# Build command
./gradlew assembleDebug

# Install on emulator  
./gradlew installDebug

# ADB path
~/Android/Sdk/platform-tools/adb

# SDK path
~/Android/Sdk
```

---

## Critical Build Configuration

### Gradle Settings (DO NOT MODIFY without reason)
```kotlin
// app/build.gradle.kts
minSdk = 26  // Required for Apache POI MethodHandle support
multiDexEnabled = true  // Required for 65K+ methods
jvmTarget = "17"  // Required for Kotlin coroutines

// gradle.properties
org.gradle.jvmargs=-Xmx4096m  // 4GB heap for Apache POI
org.gradle.parallel=true
android.enableDexingArtifactTransform=true
```

### Dependencies Version Constraints
```kotlin
// CRITICAL: These versions are tested and working
Apache POI: 5.2.5 (do not upgrade without testing)
Compose BOM: 2024.04.01
Kotlin: 1.9.0
Hilt: 2.51.1
Room: 2.6.1
Retrofit: 2.11.0
```

---

## Git Workflow

### Commit Message Conventions
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

### Branch Strategy
- `master`: Production-ready code (stable)
- `dev`: Active development (integration branch)
- `feature/*`: New features (create from dev)
- `fix/*`: Bug fixes (create from dev)
- `claude/*`: AI assistant branches

### Before Committing
1. Run build: `./gradlew assembleDebug`
2. Check git status: `git status`
3. Review changes: `git diff`
4. Stage files: `git add <files>`
5. Commit: `git commit -m "type: message"`
6. Push: `git push origin <branch>`

---

## Testing Guidelines

### Manual Testing Checklist
- [ ] App launches without crashes
- [ ] All 6 tabs accessible and functional
- [ ] Theme colors applied consistently
- [ ] Navigation works correctly
- [ ] Data loads and displays properly
- [ ] Error states handled gracefully
- [ ] Loading states shown appropriately
- [ ] Memory usage acceptable (<200MB)
- [ ] No ANR (Application Not Responding)
- [ ] Rotation handled properly

### Emulator Configuration
- **Device**: Medium Phone (1080x2340, 420dpi)
- **System Image**: API 36 (Android 14) x86_64
- **RAM**: 2048 MB minimum
- **Storage**: 2048 MB minimum
- **Graphics**: Hardware - GLES 2.0

---

## Common Issues & Solutions

### Build Issues

**Issue**: DEX build stuck at 51%  
**Solution**: Already fixed with MultiDex. If occurs again:
```kotlin
android {
    defaultConfig {
        multiDexEnabled = true
    }
}
dependencies {
    implementation("androidx.multidex:multidex:2.0.1")
}
```

**Issue**: Out of memory during build  
**Solution**: Increase heap in gradle.properties:
```
org.gradle.jvmargs=-Xmx6144m  # Increase to 6GB if needed
```

**Issue**: Apache POI MethodHandle error  
**Solution**: Ensure minSdk = 26 or higher

### Runtime Issues

**Issue**: Compose recomposition too frequent  
**Solution**: Use remember and derivedStateOf properly
```kotlin
val expensiveValue by remember { derivedStateOf { compute() } }
```

**Issue**: Memory leak with coroutines  
**Solution**: Use viewModelScope or lifecycleScope
```kotlin
viewModelScope.launch {
    // Automatically cancelled when ViewModel cleared
}
```

**Issue**: Navigation back stack issues  
**Solution**: Use proper navigation setup
```kotlin
navController.navigate(route) {
    popUpTo(Screen.Home.route) { inclusive = false }
    launchSingleTop = true
}
```

---

## Code Review Checklist

Before suggesting code changes, verify:

### Architecture
- [ ] Follows MVVM pattern
- [ ] Data, Domain, Presentation layers separate
- [ ] Repository pattern used correctly
- [ ] Dependency injection with Hilt
- [ ] ViewModels handle business logic

### Code Quality
- [ ] No hard-coded strings (use strings.xml)
- [ ] Proper error handling
- [ ] No memory leaks (check coroutine scopes)
- [ ] Efficient algorithms (no nested loops with large data)
- [ ] Proper nullable handling

### UI/UX
- [ ] SHIFAA theme colors used
- [ ] Consistent spacing (8dp grid)
- [ ] Loading states shown
- [ ] Error states handled
- [ ] Accessibility considered

### Performance
- [ ] No blocking operations on main thread
- [ ] Images optimized and cached
- [ ] Lists use lazy loading
- [ ] Database queries optimized

---

## Documentation Standards

### Code Comments
```kotlin
/**
 * Imports hospital data from Excel file to Room database.
 * 
 * @param file Excel file containing hospital data
 * @return NetworkResult with import status
 * @throws IOException if file cannot be read
 */
suspend fun importHospitals(file: File): NetworkResult<Unit>
```

### File Headers
```kotlin
/*
 * FeatureName.kt
 * SHIFAA Premium System
 * 
 * Description: Brief description of file purpose
 * Created: Date
 * Modified: Date
 * 
 * Dependencies:
 * - Dependency 1
 * - Dependency 2
 */
```

---

## Security Guidelines

### Sensitive Data
- **NEVER** commit API keys, tokens, or passwords
- **USE** BuildConfig for API keys
- **STORE** sensitive data in encrypted SharedPreferences
- **USE** HTTPS only for network calls
- **VALIDATE** all user inputs
- **SANITIZE** data before database insertion

### ProGuard Rules
```proguard
# Keep model classes for Gson/Retrofit
-keep class com.pharmatech.morocco.**.model.** { *; }

# Keep Apache POI classes
-keep class org.apache.poi.** { *; }
-dontwarn org.apache.poi.**
```

---

## Continuous Improvement

### When to Refactor
- Code duplication (DRY principle violated)
- Complex functions (>50 lines)
- Deep nesting (>3 levels)
- Poor naming or unclear logic
- Performance bottlenecks identified

### When to Add Tests
- Critical business logic
- Complex algorithms
- Data transformations
- Repository implementations
- ViewModel state management

### When to Update Documentation
- Public API changes
- Breaking changes
- New features added
- Architectural decisions
- Complex implementations

---

## Quick Reference

### Essential Files
```
app/
├── build.gradle.kts          # Build configuration
├── src/main/
│   ├── AndroidManifest.xml   # App permissions, components
│   ├── java/com/pharmatech/morocco/
│   │   ├── MainActivity.kt   # Entry point
│   │   ├── PharmaTechApp.kt # Application class
│   │   ├── core/            # Shared utilities
│   │   ├── features/        # Feature modules
│   │   └── ui/              # Theme, navigation
│   ├── res/                 # Resources
│   └── assets/              # Excel files, fonts
```

### Useful Commands
```bash
# Build
./gradlew clean assembleDebug

# Install
./gradlew installDebug

# Run tests
./gradlew test

# Check dependencies
./gradlew :app:dependencies

# Lint check
./gradlew lint

# Generate APK
./gradlew assembleRelease
```

### Key Packages
- `androidx.compose.*` - Jetpack Compose
- `com.google.dagger.hilt.*` - Dependency injection
- `androidx.room.*` - Local database
- `com.squareup.retrofit2.*` - Networking
- `org.apache.poi.*` - Excel import
- `com.google.android.gms.maps.*` - Google Maps

---

## Remember

1. **Read before writing** - Understand existing code before modifying
2. **Test thoroughly** - Every change should be tested
3. **Document decisions** - Complex logic needs explanation
4. **Follow conventions** - Consistency is key
5. **Think about UX** - Every feature affects user experience
6. **Optimize wisely** - Don't optimize prematurely
7. **Handle errors** - Always assume operations can fail
8. **Keep it simple** - Complexity is the enemy of reliability

---

**When in doubt**, refer to:
1. PROJECT_STATUS.md - Current project state
2. ARCHITECTURE.md - System design
3. README.md - Project overview
4. Existing code - Follow established patterns

**CODEWORD**: "start conversion" = Switch to macOS environment mode
