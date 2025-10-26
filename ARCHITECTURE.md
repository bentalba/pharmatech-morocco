# Architecture Documentation

## Overview

PharmaTech Morocco follows **Clean Architecture** principles combined with **MVVM (Model-View-ViewModel)** pattern. This architecture ensures separation of concerns, testability, and maintainability.

## Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                        │
│  ┌─────────────┐  ┌──────────────┐  ┌──────────────────┐   │
│  │   Compose   │→→│  ViewModel   │→→│  State & Events  │   │
│  │   Screens   │←←│   (State)    │←←│                  │   │
│  └─────────────┘  └──────────────┘  └──────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                            │
│  ┌──────────────┐  ┌─────────────────┐  ┌──────────────┐   │
│  │  Use Cases   │  │  Domain Models  │  │ Repository   │   │
│  │  (Business)  │  │                 │  │ Interfaces   │   │
│  └──────────────┘  └─────────────────┘  └──────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                       Data Layer                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │ Repositories │  │  Room DB     │  │  Retrofit API    │  │
│  │  (Impl)      │  │  (Local)     │  │  (Remote)        │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │   Firebase   │  │  DataStore   │  │  Shared Prefs    │  │
│  │   Services   │  │  (Settings)  │  │  (Legacy)        │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Layer Responsibilities

### Presentation Layer
**Location**: `features/*/presentation/`

**Components**:
- **Compose Screens** (`*Screen.kt`) - UI definition
- **ViewModels** (`*ViewModel.kt`) - State management
- **State Classes** - UI state representation
- **Events** - User interaction events

**Responsibilities**:
- Render UI based on state
- Handle user input
- Observe ViewModel state
- Navigation

**Example**:
```kotlin
@HiltViewModel
class PharmacyViewModel @Inject constructor(
    private val repository: PharmacyRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(PharmacyState())
    val state: StateFlow<PharmacyState> = _state.asStateFlow()
    
    fun searchPharmacies(query: String) {
        viewModelScope.launch {
            repository.searchPharmacies(query).collect { resource ->
                _state.update { it.copy(
                    pharmacies = resource.data ?: emptyList(),
                    isLoading = resource is Resource.Loading
                )}
            }
        }
    }
}
```

### Domain Layer
**Location**: `features/*/domain/`

**Components**:
- **Use Cases** - Business logic operations
- **Domain Models** - Business entities
- **Repository Interfaces** - Data contracts

**Responsibilities**:
- Define business rules
- Coordinate data operations
- Independent of frameworks
- Pure Kotlin (no Android dependencies)

**Example**:
```kotlin
interface PharmacyRepository {
    fun getNearbyPharmacies(lat: Double, lng: Double): Flow<Resource<List<Pharmacy>>>
    fun searchPharmacies(query: String): Flow<Resource<List<Pharmacy>>>
    fun toggleFavorite(userId: String, pharmacyId: String): Flow<Resource<Boolean>>
}
```

### Data Layer
**Location**: `features/*/data/`

**Components**:
- **Repository Implementations** - Data source coordination
- **Data Sources** - Room, Retrofit, Firebase
- **DTOs** - Data transfer objects
- **Mappers** - Entity ↔ Domain model conversion

**Responsibilities**:
- Fetch data from network/database
- Cache data locally
- Provide offline support
- Handle data synchronization

**Example**:
```kotlin
class PharmacyRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: PharmacyDao,
    private val networkMonitor: NetworkMonitor
) : PharmacyRepository {
    
    override fun getNearbyPharmacies(lat: Double, lng: Double) = flow {
        emit(Resource.Loading())
        
        // Emit cached data first
        val cached = dao.getPharmacies().first()
        if (cached.isNotEmpty()) emit(Resource.Success(cached))
        
        // Fetch fresh data if online
        if (networkMonitor.isConnected.first()) {
            val response = api.getPharmacies(lat, lng)
            response.body()?.let { pharmacies ->
                dao.insertPharmacies(pharmacies.map { it.toEntity() })
                emit(Resource.Success(pharmacies))
            }
        }
    }
}
```

## Core Components

### Dependency Injection (Hilt)

**File**: `core/di/AppModule.kt`

Provides:
- Database instance (singleton)
- Network client (Retrofit)
- Repository implementations
- WorkManager integration

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, PharmaTechDatabase::class.java, "pharmatech.db")
            .fallbackToDestructiveMigration()
            .build()
    
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
}
```

### State Management

**Pattern**: Unidirectional Data Flow (UDF)

```
User Action → Event → ViewModel → Update State → Recompose UI
                         ↓
                   Side Effect → Repository → Database/Network
```

**Implementation**:
```kotlin
// State
data class PharmacyState(
    val pharmacies: List<Pharmacy> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Events
sealed class PharmacyEvent {
    data class Search(val query: String) : PharmacyEvent()
    data class SelectPharmacy(val id: String) : PharmacyEvent()
    object Refresh : PharmacyEvent()
}

// ViewModel
fun onEvent(event: PharmacyEvent) {
    when (event) {
        is PharmacyEvent.Search -> searchPharmacies(event.query)
        is PharmacyEvent.SelectPharmacy -> selectPharmacy(event.id)
        PharmacyEvent.Refresh -> refreshPharmacies()
    }
}
```

## Data Flow Examples

### Reading Data (Offline-First)

```
Screen → ViewModel → Repository → Local DB → UI
                          ↓
                     Network API → Local DB → UI
```

1. UI requests data via ViewModel
2. Repository checks local database first
3. If data exists, emit immediately
4. Fetch from network in background
5. Update database with fresh data
6. Emit updated data to UI

### Writing Data

```
Screen → ViewModel → Repository → Local DB + Network API → Sync
```

1. UI triggers save action
2. ViewModel validates and processes
3. Repository saves to local DB immediately
4. Background sync to network when online
5. Conflict resolution if needed

## Navigation

**Pattern**: Type-safe Compose Navigation

```kotlin
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Pharmacy : Screen("pharmacy")
    object PharmacyDetail : Screen("pharmacy/{id}") {
        fun createRoute(id: String) = "pharmacy/$id"
    }
}

@Composable
fun PharmaTechNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.PharmacyDetail.route) { backStackEntry ->
            val pharmacyId = backStackEntry.arguments?.getString("id")
            PharmacyDetailScreen(pharmacyId, navController)
        }
    }
}
```

## Error Handling

**Pattern**: Resource wrapper for API responses

```kotlin
sealed class Resource<T> {
    class Success<T>(val data: T?) : Resource<T>()
    class Error<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
}
```

Usage in Repository:
```kotlin
fun getData() = flow {
    emit(Resource.Loading())
    try {
        val result = api.fetchData()
        emit(Resource.Success(result))
    } catch (e: Exception) {
        emit(Resource.Error(e.message ?: "Unknown error"))
    }
}
```

## Testing Strategy

### Unit Tests
- **ViewModels**: Test state changes and business logic
- **Repositories**: Test data operations with mock dependencies
- **Use Cases**: Test domain logic in isolation

### Integration Tests
- **Database**: Test Room queries and relationships
- **API**: Test Retrofit endpoints with MockWebServer

### UI Tests
- **Compose**: Test user interactions and navigation
- **Accessibility**: Verify screen reader support

## Performance Optimizations

1. **Lazy Loading**: Load data on demand
2. **Pagination**: Limit database/network queries
3. **Image Caching**: Coil with disk/memory cache
4. **Database Indexing**: Optimize frequent queries
5. **WorkManager**: Efficient background tasks
6. **ProGuard/R8**: Code shrinking and obfuscation

## Security Measures

1. **Network**: HTTPS only, certificate pinning
2. **Storage**: Encrypted DataStore for sensitive data
3. **Authentication**: JWT tokens with refresh mechanism
4. **Permissions**: Runtime permission checks
5. **Code Obfuscation**: ProGuard rules for release

## Future Improvements

- [ ] Implement MVI (Model-View-Intent) for complex flows
- [ ] Add GraphQL for efficient data fetching
- [ ] Implement offline mutations queue
- [ ] Add end-to-end encryption for health data
- [ ] Implement multi-module architecture for better build times

---

**Last Updated**: October 26, 2025

