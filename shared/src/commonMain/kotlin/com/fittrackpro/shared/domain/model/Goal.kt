package com.fittrackpro.shared.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Goal(
    val id: Long,
    val userId: Long,
    val title: String,
    val description: String,
    val type: String,
    val target: Long,
    val progress: Long = 0,
    val targetDate: Long,
    val startDate: Long,
    val endDate: Long,
    val status: String
)

@Serializable
enum class GoalType {
    STEPS,
    DISTANCE,
    CALORIES,
    WORKOUT_MINUTES,
    CUSTOM
}

@Serializable
enum class GoalStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}
