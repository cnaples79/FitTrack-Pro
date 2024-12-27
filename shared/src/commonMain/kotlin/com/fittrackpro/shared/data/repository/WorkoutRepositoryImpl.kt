package com.fittrackpro.shared.data.repository

import com.fittrackpro.shared.data.FitTrackDatabase
import com.fittrackpro.shared.domain.model.Workout
import com.fittrackpro.shared.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.datetime.LocalDateTime
import kotlinx.coroutines.Dispatchers

class WorkoutRepositoryImpl(
    private val database: FitTrackDatabase
) : WorkoutRepository {
    override suspend fun getWorkouts(): List<Workout> {
        return database.workoutQueries.getAllWorkouts().executeAsList().map { workoutEntity ->
            Workout(
                id = workoutEntity.id,
                userId = workoutEntity.user_id,
                type = workoutEntity.type,
                duration = workoutEntity.duration,
                caloriesBurned = workoutEntity.calories_burned,
                distance = workoutEntity.distance,
                date = workoutEntity.date,
                notes = workoutEntity.notes
            )
        }
    }

    override suspend fun getWorkout(id: String): Workout? {
        return database.workoutQueries.getWorkoutById(id.toLong()).executeAsOneOrNull()?.let { workoutEntity ->
            Workout(
                id = workoutEntity.id,
                userId = workoutEntity.user_id,
                type = workoutEntity.type,
                duration = workoutEntity.duration,
                caloriesBurned = workoutEntity.calories_burned,
                distance = workoutEntity.distance,
                date = workoutEntity.date,
                notes = workoutEntity.notes
            )
        }
    }

    override suspend fun addWorkout(workout: Workout) {
        database.workoutQueries.insertWorkout(
            user_id = workout.userId,
            type = workout.type,
            duration = workout.duration,
            calories_burned = workout.caloriesBurned,
            distance = workout.distance,
            date = workout.date,
            notes = workout.notes
        )
    }

    override suspend fun updateWorkout(workout: Workout) {
        database.workoutQueries.updateWorkout(
            type = workout.type,
            duration = workout.duration,
            calories_burned = workout.caloriesBurned,
            distance = workout.distance,
            notes = workout.notes,
            id = workout.id
        )
    }

    override suspend fun deleteWorkout(id: String) {
        database.workoutQueries.deleteWorkout(id.toLong())
    }

    override suspend fun getWorkoutsByDateRange(start: LocalDateTime, end: LocalDateTime): List<Workout> {
        return database.workoutQueries.getWorkoutsByDateRange(
            user_id = 1, // TODO: Get current user ID from UserManager
            date = start.toEpochMilliseconds(),
            date_ = end.toEpochMilliseconds()
        ).executeAsList().map { workoutEntity ->
            Workout(
                id = workoutEntity.id,
                userId = workoutEntity.user_id,
                type = workoutEntity.type,
                duration = workoutEntity.duration,
                caloriesBurned = workoutEntity.calories_burned,
                distance = workoutEntity.distance,
                date = workoutEntity.date,
                notes = workoutEntity.notes
            )
        }
    }

    override fun observeWorkouts(): Flow<List<Workout>> {
        return database.workoutQueries.getAllWorkouts()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { workoutEntities ->
                workoutEntities.map { workoutEntity ->
                    Workout(
                        id = workoutEntity.id,
                        userId = workoutEntity.user_id,
                        type = workoutEntity.type,
                        duration = workoutEntity.duration,
                        caloriesBurned = workoutEntity.calories_burned,
                        distance = workoutEntity.distance,
                        date = workoutEntity.date,
                        notes = workoutEntity.notes
                    )
                }
            }
    }

    private fun LocalDateTime.toEpochMilliseconds(): Long {
        val localDateTime = this
        val year = localDateTime.year
        val month = localDateTime.monthNumber
        val day = localDateTime.dayOfMonth
        val hour = localDateTime.hour
        val minute = localDateTime.minute
        val second = localDateTime.second
        val nanosecond = localDateTime.nanosecond

        // Simplified conversion to epoch milliseconds
        // Note: This is a rough approximation and doesn't account for time zones
        val daysInMonth = intArrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var totalDays = 0

        // Add days for years since 1970
        for (y in 1970 until year) {
            totalDays += if (y % 4 == 0 && (y % 100 != 0 || y % 400 == 0)) 366 else 365
        }

        // Add days for months in current year
        for (m in 1 until month) {
            totalDays += if (m == 2 && (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))) 29 else daysInMonth[m]
        }

        // Add days in current month
        totalDays += day - 1

        // Convert to milliseconds
        var totalMillis = totalDays * 24L * 60L * 60L * 1000L
        totalMillis += hour * 60L * 60L * 1000L
        totalMillis += minute * 60L * 1000L
        totalMillis += second * 1000L
        totalMillis += nanosecond / 1_000_000L

        return totalMillis
    }
}
