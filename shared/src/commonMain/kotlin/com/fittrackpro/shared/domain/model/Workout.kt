package com.fittrackpro.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val id: Long,
    val userId: Long,
    val type: String,
    val duration: Long,
    val caloriesBurned: Long?,
    val distance: Double?,
    val date: Long,
    val notes: String?
)
