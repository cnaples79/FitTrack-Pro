package com.fittrackpro.shared.data.repository.impl

import com.fittrackpro.shared.FitTrackDatabase
import com.fittrackpro.shared.domain.model.Workout
import com.fittrackpro.shared.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.datetime.LocalDateTime
import kotlinx.coroutines.Dispatchers

class WorkoutRepositoryImpl(
    private val database: FitTrackDatabase,
    private val userId: Long
) : WorkoutRepository {

    override suspend fun getWorkouts(): List<Workout> {
        return database.workoutQueries.getWorkoutsByUserId(userId).executeAsList().map { workout ->
            Workout(
                id = workout.id,
                userId = workout.user_id,
                type = workout.type,
                duration = workout.duration,
                caloriesBurned = workout.calories_burned,
                distance = workout.distance,
                date = workout.date,
                notes = workout.notes
            )
        }
    }

    override suspend fun getWorkout(id: String): Workout? {
        return database.workoutQueries.getWorkoutById(id.toLong(), userId).executeAsOneOrNull()?.let { workout ->
            Workout(
                id = workout.id,
                userId = workout.user_id,
                type = workout.type,
                duration = workout.duration,
                caloriesBurned = workout.calories_burned,
                distance = workout.distance,
                date = workout.date,
                notes = workout.notes
            )
        }
    }

    override suspend fun addWorkout(workout: Workout) {
        database.workoutQueries.insertWorkout(
            user_id = userId,
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
            id = workout.id,
            user_id = userId,
            type = workout.type,
            duration = workout.duration,
            calories_burned = workout.caloriesBurned,
            distance = workout.distance,
            date = workout.date,
            notes = workout.notes
        )
    }

    override suspend fun deleteWorkout(id: String) {
        database.workoutQueries.deleteWorkout(id.toLong())
    }

    override suspend fun getWorkoutsByDateRange(start: LocalDateTime, end: LocalDateTime): List<Workout> {
        return database.workoutQueries.getWorkoutsByDateRange(
            user_id = userId,
            date = start.date.toEpochDays().toLong(),
            date_ = end.date.toEpochDays().toLong()
        ).executeAsList().map { workout ->
            Workout(
                id = workout.id,
                userId = workout.user_id,
                type = workout.type,
                duration = workout.duration,
                caloriesBurned = workout.calories_burned,
                distance = workout.distance,
                date = workout.date,
                notes = workout.notes
            )
        }
    }

    override fun observeWorkouts(): Flow<List<Workout>> {
        return database.workoutQueries.getWorkoutsByUserId(userId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { workouts ->
                workouts.map { workout ->
                    Workout(
                        id = workout.id,
                        userId = workout.user_id,
                        type = workout.type,
                        duration = workout.duration,
                        caloriesBurned = workout.calories_burned,
                        distance = workout.distance,
                        date = workout.date,
                        notes = workout.notes
                    )
                }
            }
    }
}
