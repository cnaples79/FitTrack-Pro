package com.fittrackpro.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class GoalStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    FAILED;

    companion object {
        fun fromProgress(progress: Float): GoalStatus {
            return when {
                progress <= 0f -> NOT_STARTED
                progress < 1f -> IN_PROGRESS
                else -> COMPLETED
            }
        }
    }
}
