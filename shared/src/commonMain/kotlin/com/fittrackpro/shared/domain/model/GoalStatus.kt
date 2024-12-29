package com.fittrackpro.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class GoalStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}
