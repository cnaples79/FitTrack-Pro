package com.fittrackpro.shared.data.repository

import com.fittrackpro.db.FitTrackDatabase
import com.fittrackpro.shared.domain.model.Workout
import com.fittrackpro.shared.domain.model.WorkoutType
import com.fittrackpro.shared.domain.repository.WorkoutRepository
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime

class WorkoutRepositoryImpl(database: FitTrackDatabase) : WorkoutRepository {
    private val queries = database.workoutQueries

    override suspend fun getWorkouts(): List<Workout> {
        return queries.getAllWorkouts().executeAsList().map { it.toWorkout() }
    }

    override suspend fun getWorkout(id: String): Workout? {
        return queries.getWorkoutById(id).executeAsOneOrNull()?.toWorkout()
    }

    override suspend fun addWorkout(workout: Workout) {
        queries.insertWorkout(
            id = workout.id,
            name = workout.name,
            type = workout.type.name,
            duration = workout.duration.toLong(),
            calories_burned = workout.caloriesBurned.toLong(),
            date = workout.date.toString(),
            notes = workout.notes
        )
    }

    override suspend fun updateWorkout(workout: Workout) {
        queries.updateWorkout(
            name = workout.name,
            type = workout.type.name,
            duration = workout.duration.toLong(),
            calories_burned = workout.caloriesBurned.toLong(),
            date = workout.date.toString(),
            notes = workout.notes,
            id = workout.id
        )
    }

    override suspend fun deleteWorkout(id: String) {
        queries.deleteWorkout(id)
    }

    override suspend fun getWorkoutsByDateRange(
        start: LocalDateTime,
        end: LocalDateTime
    ): List<Workout> {
        return queries.getWorkoutsByDateRange(
            start.toString(),
            end.toString()
        ).executeAsList().map { it.toWorkout() }
    }

    override fun observeWorkouts(): Flow<List<Workout>> {
        return queries.getAllWorkouts()
            .asFlow()
            .mapToList()
            .map { workouts -> workouts.map { it.toWorkout() } }
    }

    private fun com.fittrackpro.db.Workout.toWorkout(): Workout {
        return Workout(
            id = id,
            name = name,
            type = WorkoutType.valueOf(type),
            duration = duration.toInt(),
            caloriesBurned = calories_burned.toInt(),
            date = date.toLocalDateTime(),
            notes = notes
        )
    }
}
