package com.fittrackpro.shared.data

import com.fittrackpro.db.FitTrackDatabase
import com.fittrackpro.shared.data.repository.GoalRepositoryImpl
import com.fittrackpro.shared.data.repository.UserProfileRepositoryImpl
import com.fittrackpro.shared.data.repository.WorkoutRepositoryImpl
import com.fittrackpro.shared.domain.repository.GoalRepository
import com.fittrackpro.shared.domain.repository.UserProfileRepository
import com.fittrackpro.shared.domain.repository.WorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val driver = databaseDriverFactory.createDriver()
    private val database = FitTrackDatabase(driver)
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
            try {
                cleaner.cleanOldData()
            } catch (e: Exception) {
                // Log error if needed
            }
        }
    }

    fun cleanup() {
        databaseScope.launch {
            try {
                cleaner.cleanOldData()
            } catch (e: Exception) {
                // Log error if needed
            }
        }
    }

    companion object {
        private var instance: Database? = null

        fun getInstance(databaseDriverFactory: DatabaseDriverFactory): Database {
            return instance ?: Database(databaseDriverFactory).also {
                instance = it
            }
        }
    }
}
