package com.fittrackpro.shared.domain.repository

import com.fittrackpro.shared.domain.model.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface WorkoutRepository {
    suspend fun getWorkouts(): List<Workout>
    suspend fun getWorkout(id: String): Workout?
    suspend fun addWorkout(workout: Workout)
    suspend fun updateWorkout(workout: Workout)
    suspend fun deleteWorkout(id: String)
    suspend fun getWorkoutsByDateRange(start: LocalDateTime, end: LocalDateTime): List<Workout>
    fun observeWorkouts(): Flow<List<Workout>>
}
