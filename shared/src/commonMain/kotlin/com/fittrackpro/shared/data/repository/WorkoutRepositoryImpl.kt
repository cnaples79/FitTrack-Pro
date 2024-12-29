package com.fittrackpro.shared.data.repository

import com.fittrackpro.shared.FitTrackDatabase
import com.fittrackpro.shared.domain.model.Workout
import com.fittrackpro.shared.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.datetime.LocalDate
import kotlinx.coroutines.Dispatchers

class WorkoutRepositoryImpl(
    private val database: FitTrackDatabase
) : WorkoutRepository {

    override suspend fun getWorkouts(): List<Workout> {
        return database.workoutQueries.getAllWorkouts().executeAsList().map { workoutEntity ->
            Workout(
                id = workoutEntity.id,
                userId = workoutEntity.user_id,
                name = workoutEntity.name,
                type = workoutEntity.type,
                duration = workoutEntity.duration,
                caloriesBurned = workoutEntity.calories_burned,
                date = LocalDate.fromEpochDays(workoutEntity.date.toInt()),
                notes = workoutEntity.notes
            )
        }
    }

    override suspend fun getWorkout(id: Long): Workout? {
        return database.workoutQueries.getWorkout(id).executeAsOneOrNull()?.let { workout ->
            Workout(
                id = workout.id,
                userId = workout.user_id,
                name = workout.name,
                type = workout.type,
                duration = workout.duration,
                caloriesBurned = workout.calories_burned,
                date = LocalDate.fromEpochDays(workout.date.toInt()),
                notes = workout.notes
            )
        }
    }

    override suspend fun addWorkout(workout: Workout) {
        database.workoutQueries.insertWorkout(
            user_id = workout.userId,
            name = workout.name,
            type = workout.type,
            duration = workout.duration,
            calories_burned = workout.caloriesBurned,
            date = workout.date.toEpochDays().toLong(),
            notes = workout.notes
        )
    }

    override suspend fun updateWorkout(workout: Workout) {
        database.workoutQueries.updateWorkout(
            name = workout.name,
            type = workout.type,
            duration = workout.duration,
            calories_burned = workout.caloriesBurned,
            date = workout.date.toEpochDays().toLong(),
            notes = workout.notes,
            id = workout.id
        )
    }

    override suspend fun deleteWorkout(id: Long) {
        database.workoutQueries.deleteWorkout(id)
    }

    override suspend fun getWorkoutsByDateRange(start: LocalDate, end: LocalDate): List<Workout> {
        return database.workoutQueries.getWorkoutsByDateRange(
            user_id = 1, // TODO: Get current user ID from UserManager
            date = start.toEpochDays().toLong(),
            date_ = end.toEpochDays().toLong()
        ).executeAsList().map { workoutEntity ->
            Workout(
                id = workoutEntity.id,
                userId = workoutEntity.user_id,
                name = workoutEntity.name,
                type = workoutEntity.type,
                duration = workoutEntity.duration,
                caloriesBurned = workoutEntity.calories_burned,
                date = LocalDate.fromEpochDays(workoutEntity.date.toInt()),
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
                        name = workoutEntity.name,
                        type = workoutEntity.type,
                        duration = workoutEntity.duration,
                        caloriesBurned = workoutEntity.calories_burned,
                        date = LocalDate.fromEpochDays(workoutEntity.date.toInt()),
                        notes = workoutEntity.notes
                    )
                }
            }
    }
}
