package com.fittrackpro.shared.domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val id: String,
    val name: String,
    val type: WorkoutType,
    val duration: Int, // in minutes
    val caloriesBurned: Int,
    val date: LocalDateTime,
    val notes: String? = null
)

@Serializable
enum class WorkoutType {
    STRENGTH,
    CARDIO,
    FLEXIBILITY,
    HIIT,
    YOGA,
    OTHER
}
