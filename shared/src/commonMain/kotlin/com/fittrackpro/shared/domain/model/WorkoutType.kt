package com.fittrackpro.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class WorkoutType {
    CARDIO,
    STRENGTH,
    FLEXIBILITY,
    HIIT,
    YOGA,
    OTHER
}
