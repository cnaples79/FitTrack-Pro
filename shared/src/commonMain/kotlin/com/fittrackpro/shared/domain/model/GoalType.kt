package com.fittrackpro.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class GoalType {
    STEPS,
    DISTANCE,
    CALORIES,
    WORKOUT_COUNT,
    WEIGHT,
    MUSCLE_MASS,
    BODY_FAT,
    WATER_INTAKE,
    SLEEP_DURATION;

    companion object {
        fun fromString(value: String): GoalType {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                STEPS // Default value
            }
        }
    }
}
