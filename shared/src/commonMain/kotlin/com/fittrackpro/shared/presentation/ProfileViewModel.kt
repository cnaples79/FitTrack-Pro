package com.fittrackpro.shared.presentation

import com.fittrackpro.shared.domain.model.UserProfile
import com.fittrackpro.shared.domain.repository.UserProfileRepository
import com.fittrackpro.shared.util.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import com.fittrackpro.shared.util.CommonFlow
import com.fittrackpro.shared.util.asCommonFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: UserProfileRepository,
    private val dispatchers: Dispatchers
) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + dispatchers.main)
    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: CommonFlow<UserProfile?> = _profile.asCommonFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: CommonFlow<Boolean> = _isLoading.asCommonFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: CommonFlow<String?> = _error.asCommonFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch(dispatchers.io) {
            _isLoading.value = true
            try {
                val userProfile = repository.getProfile()
                _profile.value = userProfile
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch(dispatchers.io) {
            try {
                repository.updateProfile(profile)
                _profile.value = profile
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun createProfile(profile: UserProfile) {
        viewModelScope.launch(dispatchers.io) {
            try {
                repository.createProfile(profile)
                _profile.value = profile
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
