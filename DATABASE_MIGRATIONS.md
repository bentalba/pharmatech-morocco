# Database Migration Strategy for PharmaTech Morocco

## Current Status

**WARNING:** The app currently uses `.fallbackToDestructiveMigration()` in `AppModule.kt:85`, which **DELETES ALL USER DATA** on schema changes. This is **NOT ACCEPTABLE** for production.

## Migration Implementation Guide

### Step 1: Remove Destructive Migration

In `app/src/main/java/com/pharmatech/morocco/core/di/AppModule.kt`, replace:

```kotlin
fun provideDatabase(@ApplicationContext context: Context): PharmaTechDatabase {
    return Room.databaseBuilder(
        context,
        PharmaTechDatabase::class.java,
        "pharmatech_database"
    )
        .fallbackToDestructiveMigration() // ❌ REMOVE THIS
        .build()
}
```

With:

```kotlin
fun provideDatabase(@ApplicationContext context: Context): PharmaTechDatabase {
    return Room.databaseBuilder(
        context,
        PharmaTechDatabase::class.java,
        "pharmatech_database"
    )
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // ✅ Add migrations
        .build()
}
```

### Step 2: Define Migration Objects

Create migration objects in a new file: `app/src/main/java/com/pharmatech/morocco/core/database/migrations/DatabaseMigrations.kt`

```kotlin
package com.pharmatech.morocco.core.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration from version 1 to version 2
 * Example: Adding a new column to an existing table
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Example: Add a new column 'lastSynced' to pharmacies table
        database.execSQL(
            "ALTER TABLE pharmacies ADD COLUMN lastSynced INTEGER"
        )
    }
}

/**
 * Migration from version 2 to version 3
 * Example: Creating a new table
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Example: Create a new table for user preferences
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS user_preferences (
                userId TEXT NOT NULL PRIMARY KEY,
                language TEXT NOT NULL,
                notificationsEnabled INTEGER NOT NULL,
                darkModeEnabled INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}
```

### Step 3: Update Database Version

In `app/src/main/java/com/pharmatech/morocco/core/database/PharmaTechDatabase.kt`, update the version:

```kotlin
@Database(
    entities = [
        UserEntity::class,
        PharmacyEntity::class,
        MedicationEntity::class,
        // ... other entities
    ],
    version = 2, // ✅ Increment this when schema changes
    exportSchema = true // ✅ Enable schema export for migration testing
)
abstract class PharmaTechDatabase : RoomDatabase() {
    // DAOs...
}
```

### Step 4: Enable Schema Export

In `app/build.gradle.kts`, add:

```kotlin
android {
    defaultConfig {
        // ...

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }
}

kapt {
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}
```

This creates JSON schema files in `app/schemas/` that can be version-controlled and used for testing migrations.

### Step 5: Test Migrations

Create migration tests in `androidTest`:

```kotlin
package com.pharmatech.morocco.core.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pharmatech.morocco.core.database.migrations.MIGRATION_1_2
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        PharmaTechDatabase::class.java
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        var db = helper.createDatabase(TEST_DB, 1).apply {
            // Insert test data for version 1
            execSQL("INSERT INTO pharmacies VALUES (...)")
            close()
        }

        // Re-open with version 2
        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

        // Verify migration succeeded
        // Query and check data
    }

    companion object {
        private const val TEST_DB = "migration-test"
    }
}
```

## Common Migration Scenarios

### Adding a Column

```kotlin
val MIGRATION_X_Y = object : Migration(X, Y) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE table_name ADD COLUMN new_column_name TEXT DEFAULT 'default_value'"
        )
    }
}
```

### Renaming a Column

```kotlin
val MIGRATION_X_Y = object : Migration(X, Y) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // SQLite doesn't support RENAME COLUMN directly
        // Need to create new table, copy data, drop old table

        // 1. Create new table with correct schema
        database.execSQL("CREATE TABLE table_name_new (...)")

        // 2. Copy data from old table
        database.execSQL("INSERT INTO table_name_new SELECT ... FROM table_name")

        // 3. Drop old table
        database.execSQL("DROP TABLE table_name")

        // 4. Rename new table
        database.execSQL("ALTER TABLE table_name_new RENAME TO table_name")
    }
}
```

### Changing Column Type

```kotlin
val MIGRATION_X_Y = object : Migration(X, Y) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Similar to renaming - create new table, copy data with CAST if needed
        database.execSQL("CREATE TABLE table_name_new (...)")
        database.execSQL("INSERT INTO table_name_new SELECT id, CAST(old_column AS INTEGER) ... FROM table_name")
        database.execSQL("DROP TABLE table_name")
        database.execSQL("ALTER TABLE table_name_new RENAME TO table_name")
    }
}
```

### Adding a New Table

```kotlin
val MIGRATION_X_Y = object : Migration(X, Y) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE new_table (
                id TEXT NOT NULL PRIMARY KEY,
                column1 TEXT NOT NULL,
                column2 INTEGER NOT NULL
            )
            """.trimIndent()
        )

        // Create indices if needed
        database.execSQL("CREATE INDEX index_new_table_column1 ON new_table(column1)")
    }
}
```

### Removing a Table

```kotlin
val MIGRATION_X_Y = object : Migration(X, Y) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS old_table")
    }
}
```

## Migration Checklist

Before releasing a version with schema changes:

- [ ] Increment database version in `@Database` annotation
- [ ] Create Migration object for version N to N+1
- [ ] Add migration to `addMigrations()` in AppModule
- [ ] Write migration test
- [ ] Run migration test on emulator
- [ ] Test migration on device with existing data
- [ ] Export new schema to `app/schemas/`
- [ ] Document migration in changelog
- [ ] Test app functionality after migration
- [ ] Verify no data loss

## Emergency Rollback Plan

If a migration fails in production:

1. **Option 1: Deploy fixed migration**
   - Create new migration that reverts changes
   - Increment version again
   - Deploy hotfix

2. **Option 2: Allow destructive migration temporarily**
   - Add `.fallbackToDestructiveMigration()` back
   - Warn users they'll lose data
   - Deploy, then fix properly in next version

## Current Database Version

- **Version:** 1 (initial version)
- **Last Updated:** 2025-10-26
- **Status:** No migrations implemented yet - using destructive migration

## When to Migrate

Migrate when you:
- Add/remove a table
- Add/remove a column
- Change column type
- Change column constraints (NOT NULL, DEFAULT, etc.)
- Add/remove indices
- Change primary keys or foreign keys

## Best Practices

1. **Never skip versions** - Always provide migrations for consecutive versions
2. **Test thoroughly** - Use MigrationTestHelper to verify migrations work
3. **Keep migrations simple** - Each migration should do one thing
4. **Document changes** - Comment what each migration does
5. **Export schemas** - Keep schema history in version control
6. **Handle nullability** - Use DEFAULT values when adding NOT NULL columns
7. **Preserve data** - Never drop columns that contain user data without backup
8. **Version control** - Commit schema exports with code changes

## Future: Multi-Step Migrations

When a user hasn't updated in a while, they may need to migrate through multiple versions:

```kotlin
.addMigrations(
    MIGRATION_1_2,
    MIGRATION_2_3,
    MIGRATION_3_4,
    MIGRATION_4_5
)
```

Room will automatically chain these migrations (1→2→3→4→5) as needed.

---

**Next Steps:**
1. Create `DatabaseMigrations.kt` file
2. Update `AppModule.kt` to remove destructive migration
3. Set up migration testing framework
4. Plan and document future schema changes
