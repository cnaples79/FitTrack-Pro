package com.fittrackpro.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserStats(
    val totalWorkouts: Int = 0,
    val totalMinutes: Int = 0,
    val totalCaloriesBurned: Int = 0,
    val weeklyWorkouts: Int = 0,
    val weeklyMinutes: Int = 0,
    val weeklyCaloriesBurned: Int = 0,
    val monthlyWorkouts: Int = 0,
    val monthlyMinutes: Int = 0,
    val monthlyCaloriesBurned: Int = 0,
    val streakDays: Int = 0,
    val longestStreak: Int = 0
)
