package com.fittrackpro.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittrackpro.shared.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile.asStateFlow()

    private val _stats = MutableStateFlow<UserStats>(UserStats())
    val stats: StateFlow<UserStats> = _stats.asStateFlow()

    init {
        loadProfile()
        loadStats()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual repository call
                _profile.value = createMockProfile()
                _uiState.value = ProfileUiState.Success
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual repository call
                _stats.value = createMockStats()
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Failed to load stats")
            }
        }
    }

    fun updateProfile(updatedProfile: UserProfile) {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual repository call
                _profile.value = updatedProfile
                _uiState.value = ProfileUiState.Success
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Failed to update profile")
            }
        }
    }

    private fun createMockProfile() = UserProfile(
        id = UUID.randomUUID().toString(),
        name = "John Doe",
        email = "john.doe@example.com",
        height = 175.0,
        weight = 70.0,
        age = 30,
        gender = Gender.MALE,
        fitnessLevel = FitnessLevel.INTERMEDIATE,
        preferredWorkoutTypes = listOf(
            WorkoutType.STRENGTH,
            WorkoutType.CARDIO,
            WorkoutType.HIIT
        ),
        weeklyGoal = 4
    )

    private fun createMockStats() = UserStats(
        totalWorkouts = 48,
        totalMinutes = 1440,
        totalCaloriesBurned = 12000,
        completedGoals = 8,
        currentStreak = 3,
        longestStreak = 7,
        workoutsByType = mapOf(
            WorkoutType.STRENGTH to 20,
            WorkoutType.CARDIO to 15,
            WorkoutType.HIIT to 8,
            WorkoutType.FLEXIBILITY to 5
        )
    )
}

data class UserStats(
    val totalWorkouts: Int = 0,
    val totalMinutes: Int = 0,
    val totalCaloriesBurned: Int = 0,
    val completedGoals: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val workoutsByType: Map<WorkoutType, Int> = emptyMap()
)

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    object Success : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}
