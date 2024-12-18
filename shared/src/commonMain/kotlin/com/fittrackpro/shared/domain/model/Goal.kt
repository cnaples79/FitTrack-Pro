package com.fittrackpro.shared.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Goal(
    val id: String,
    val title: String,
    val description: String,
    val targetDate: LocalDate,
    val type: GoalType,
    val target: Int, // The target value (e.g., number of workouts, minutes, etc.)
    val progress: Int, // Current progress towards the target
    val completed: Boolean = false
)

@Serializable
enum class GoalType {
    WORKOUT_COUNT, // Number of workouts to complete
    WORKOUT_MINUTES, // Total minutes of exercise
    CALORIE_BURN, // Total calories to burn
    STRENGTH_SESSIONS, // Number of strength training sessions
    CARDIO_SESSIONS // Number of cardio sessions
}
