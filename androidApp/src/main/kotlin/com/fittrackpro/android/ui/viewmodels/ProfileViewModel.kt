package com.fittrackpro.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittrackpro.android.ui.states.UiState
import com.fittrackpro.shared.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Profile>>(UiState.Loading)
    val uiState: StateFlow<UiState<Profile>> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual repository call
                val profile = Profile(
                    id = 1,
                    email = "user@example.com",
                    name = "John Doe",
                    gender = Gender.PREFER_NOT_TO_SAY,
                    fitnessLevel = FitnessLevel.INTERMEDIATE,
                    activityLevel = ActivityLevel.MODERATE_ACTIVE,
                    weeklyGoal = 3,
                    preferredWorkoutTypes = listOf(
                        WorkoutType.CARDIO,
                        WorkoutType.STRENGTH
                    ),
                    fitnessGoals = listOf(
                        "Improve overall fitness",
                        "Build muscle",
                        "Increase endurance"
                    )
                )
                _uiState.value = UiState.Success(profile)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    fun updateProfile(updatedProfile: Profile) {
        viewModelScope.launch {
            try {
                // TODO: Update profile in repository
                _uiState.value = UiState.Success(updatedProfile)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to update profile")
            }
        }
    }
}
