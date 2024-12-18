package com.fittrackpro.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val height: Double? = null, // in centimeters
    val weight: Double? = null, // in kilograms
    val age: Int? = null,
    val gender: Gender? = null,
    val fitnessLevel: FitnessLevel = FitnessLevel.INTERMEDIATE,
    val preferredWorkoutTypes: List<WorkoutType> = emptyList(),
    val weeklyGoal: Int = 3 // number of workouts per week
)

@Serializable
enum class Gender {
    MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY
}

@Serializable
enum class FitnessLevel {
    BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
}
