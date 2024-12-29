package com.fittrackpro.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class GoalType {
    WORKOUT_COUNT,
    WORKOUT_MINUTES,
    CALORIE_BURN,
    STRENGTH_SESSIONS,
    CARDIO_SESSIONS
}
