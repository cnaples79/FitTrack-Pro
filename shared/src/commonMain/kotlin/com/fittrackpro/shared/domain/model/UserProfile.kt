package com.fittrackpro.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: Long,
    val name: String,
    val age: Int,
    val height: Double,
    val weight: Double,
    val gender: String,
    val activityLevel: String,
    val fitnessGoals: String
)
