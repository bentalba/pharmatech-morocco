# Contributing to PharmaTech Morocco

First off, thank you for considering contributing to PharmaTech Morocco! It's people like you that make this app better for everyone in the Moroccan healthcare community.

## Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code.

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check the issue list as you might find out that you don't need to create one. When you are creating a bug report, please include as many details as possible:

* **Use a clear and descriptive title**
* **Describe the exact steps to reproduce the problem**
* **Provide specific examples** to demonstrate the steps
* **Describe the behavior you observed** and what you expected
* **Include screenshots** if possible
* **Include device information** (Android version, device model)

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, please include:

* **Use a clear and descriptive title**
* **Provide a detailed description** of the suggested enhancement
* **Explain why this enhancement would be useful**
* **List any similar features** in other apps if applicable

### Pull Requests

* Fill in the required template
* Follow the Kotlin coding style
* Include appropriate test coverage
* Update documentation as needed
* End all files with a newline

## Development Process

### Setting Up Development Environment

1. Fork the repository
2. Clone your fork: `git clone https://github.com/yourusername/pharmatech-morocco.git`
3. Add upstream remote: `git remote add upstream https://github.com/original/pharmatech-morocco.git`
4. Create a branch: `git checkout -b feature/your-feature-name`

### Coding Standards

#### Kotlin Style Guide

Follow the official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// Good
class PharmacyRepository @Inject constructor(
    private val pharmacyDao: PharmacyDao,
    private val apiService: ApiService
) {
    fun getNearbyPharmacies(lat: Double, lng: Double): Flow<Resource<List<Pharmacy>>> {
        // Implementation
    }
}

// Bad
class PharmacyRepository @Inject constructor(private val pharmacyDao:PharmacyDao,private val apiService:ApiService){
    fun getNearbyPharmacies(lat:Double,lng:Double):Flow<Resource<List<Pharmacy>>>{
        // Implementation
    }
}
```

#### Compose Best Practices

```kotlin
// Good - State hoisting
@Composable
fun PharmacyScreen(
    viewModel: PharmacyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    PharmacyContent(
        pharmacies = state.pharmacies,
        onPharmacyClick = viewModel::onPharmacyClick
    )
}

@Composable
private fun PharmacyContent(
    pharmacies: List<Pharmacy>,
    onPharmacyClick: (String) -> Unit
) {
    // UI implementation
}
```

#### Documentation

* Add KDoc comments for public APIs
* Include usage examples for complex functions
* Document non-obvious code behavior

```kotlin
/**
 * Calculates medication adherence rate over a specified period.
 *
 * @param userId The user's unique identifier
 * @param days Number of days to analyze (default: 7)
 * @return Flow emitting adherence percentage (0-100)
 *
 * Example:
 * ```
 * viewModel.getAdherenceRate("user123", 30).collect { rate ->
 *     println("30-day adherence: $rate%")
 * }
 * ```
 */
fun getAdherenceRate(userId: String, days: Int = 7): Flow<Float>
```

### Testing

* Write unit tests for ViewModels and Repositories
* Write UI tests for critical user flows
* Aim for >80% code coverage
* Run tests before submitting PR: `./gradlew test`

```kotlin
@Test
fun `getAdherenceRate returns correct percentage`() = runTest {
    // Given
    val userId = "test_user"
    val expectedRate = 85.5f
    
    // When
    val result = repository.getAdherenceRate(userId).first()
    
    // Then
    assertEquals(expectedRate, result, 0.1f)
}
```

### Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: add barcode scanning for medications
fix: resolve crash when opening pharmacy details
docs: update README with Firebase setup instructions
refactor: extract common UI components
test: add tests for medication tracker
chore: update dependencies to latest versions
```

### Branch Naming

* `feature/` - New features: `feature/ar-medication-viewer`
* `fix/` - Bug fixes: `fix/login-crash`
* `refactor/` - Code refactoring: `refactor/repository-pattern`
* `docs/` - Documentation: `docs/api-documentation`
* `test/` - Tests: `test/tracker-unit-tests`

## Pull Request Process

1. **Update your branch** with latest upstream changes:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Ensure all tests pass**:
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

3. **Update documentation** if needed

4. **Create Pull Request** with:
   * Clear title describing the change
   * Detailed description of what and why
   * Screenshots for UI changes
   * Link to related issues

5. **Respond to review comments** promptly

6. **Squash commits** before merge if requested

## Project Structure Guidelines

When adding new features, follow the existing architecture:

```
features/
└── newfeature/
    ├── data/
    │   └── NewFeatureRepository.kt
    ├── domain/
    │   ├── models/
    │   └── usecases/
    └── presentation/
        ├── NewFeatureViewModel.kt
        ├── NewFeatureScreen.kt
        └── components/
```

## Questions?

Feel free to ask questions by:
* Opening an issue with the `question` label
* Reaching out on our Discord server
* Emailing: dev@pharmatech.ma

Thank you for contributing! 🙏

