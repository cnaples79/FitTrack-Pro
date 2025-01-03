package com.fittrackpro.shared.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Goal(
    val id: Long = 0,
    val userId: Long,
    val title: String,
    val description: String,
    val type: GoalType,
    val targetValue: Double,
    val currentValue: Double = 0.0,
    val deadline: LocalDate,
    val status: GoalStatus = GoalStatus.NOT_STARTED,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
    val progress: Float = (currentValue / targetValue).toFloat().coerceIn(0f, 1f)
)
