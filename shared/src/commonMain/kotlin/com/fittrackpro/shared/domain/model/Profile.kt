package com.fittrackpro.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: Long,
    val email: String,
    val name: String,
    val gender: Gender,
    val fitnessLevel: FitnessLevel,
    val activityLevel: ActivityLevel,
    val weeklyGoal: Int,
    val preferredWorkoutTypes: List<WorkoutType>,
    val fitnessGoals: List<String>
)

@Serializable
enum class Gender {
    MALE,
    FEMALE,
    OTHER,
    PREFER_NOT_TO_SAY
}

@Serializable
enum class FitnessLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    EXPERT
}

@Serializable
enum class ActivityLevel {
    SEDENTARY,
    LIGHTLY_ACTIVE,
    MODERATELY_ACTIVE,
    VERY_ACTIVE,
    EXTREMELY_ACTIVE
}
