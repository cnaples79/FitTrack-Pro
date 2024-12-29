package com.fittrackpro.shared.data

import app.cash.sqldelight.db.SqlDriver
import com.fittrackpro.shared.FitTrackDatabase
import com.fittrackpro.shared.Profile
import com.fittrackpro.shared.Goal
import com.fittrackpro.shared.data.adapters.EnumColumnAdapter
import com.fittrackpro.shared.data.adapters.ListColumnAdapter
import com.fittrackpro.shared.domain.model.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

import com.fittrackpro.shared.data.repository.GoalRepositoryImpl
import com.fittrackpro.shared.data.repository.UserProfileRepositoryImpl
import com.fittrackpro.shared.data.repository.WorkoutRepositoryImpl
import com.fittrackpro.shared.domain.repository.GoalRepository
import com.fittrackpro.shared.domain.repository.UserProfileRepository
import com.fittrackpro.shared.domain.repository.WorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.days

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val driver: SqlDriver = databaseDriverFactory.createDriver()
    private val database: FitTrackDatabase = createDatabase(driver)
    private val databaseScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val cleaner = DatabaseCleaner(database)

    val workoutRepository: WorkoutRepository = WorkoutRepositoryImpl(database)
    val goalRepository: GoalRepository = GoalRepositoryImpl(database)
    val userProfileRepository: UserProfileRepository = UserProfileRepositoryImpl(database)

    init {
        DatabaseMigration.migrateIfNeeded(driver)
        setupAutomaticCleanup()
    }

    private fun setupAutomaticCleanup() {
        databaseScope.launch {
            cleaner.cleanOldRecords()
        }
    }

    fun close() {
        driver.close()
    }
}

fun createDatabase(driver: SqlDriver): FitTrackDatabase {
    val json = Json { 
        ignoreUnknownKeys = true 
        isLenient = true
    }

    val typeAdapter = EnumColumnAdapter(GoalType.values())
    val statusAdapter = EnumColumnAdapter(GoalStatus.values())
    val genderAdapter = EnumColumnAdapter(Gender.values())
    val fitnessLevelAdapter = EnumColumnAdapter(FitnessLevel.values())
    val activityLevelAdapter = EnumColumnAdapter(ActivityLevel.values())
    val workoutTypesAdapter = ListColumnAdapter(ListSerializer(serializer<WorkoutType>()))
    val fitnessGoalsAdapter = ListColumnAdapter(ListSerializer(serializer<String>()))

    return FitTrackDatabase.invoke(
        driver = driver,
        ProfileAdapter = Profile.Adapter(
            genderAdapter = genderAdapter,
            fitnessLevelAdapter = fitnessLevelAdapter,
            activityLevelAdapter = activityLevelAdapter,
            preferredWorkoutTypesAdapter = workoutTypesAdapter,
            fitnessGoalsAdapter = fitnessGoalsAdapter
        ),
        GoalAdapter = Goal.Adapter(
            typeAdapter = typeAdapter,
            statusAdapter = statusAdapter
        )
    )
}
