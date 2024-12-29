package com.fittrackpro.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittrackpro.android.ui.states.ProfileUiState
import com.fittrackpro.android.ui.states.StatsUiState
import com.fittrackpro.android.ui.states.UiState
import com.fittrackpro.shared.domain.model.Profile
import com.fittrackpro.shared.domain.model.UserStats
import com.fittrackpro.shared.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(UiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _stats = MutableStateFlow<StatsUiState>(UiState.Loading)
    val stats: StateFlow<StatsUiState> = _stats.asStateFlow()

    init {
        loadProfile()
        loadStats()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                val profile = profileRepository.getProfile(1L) // TODO: Get from auth
                _uiState.value = if (profile != null) {
                    UiState.Success(profile)
                } else {
                    UiState.Error("Profile not found")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual repository call
                _stats.value = UiState.Success(UserStats())
            } catch (e: Exception) {
                _stats.value = UiState.Error(e.message ?: "Failed to load stats")
            }
        }
    }

    fun updateProfile(profile: Profile) {
        viewModelScope.launch {
            try {
                profileRepository.updateProfile(profile)
                _uiState.value = UiState.Success(profile)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to update profile")
            }
        }
    }
}
