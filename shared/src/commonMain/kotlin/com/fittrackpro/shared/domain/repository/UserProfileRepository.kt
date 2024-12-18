package com.fittrackpro.shared.domain.repository

import com.fittrackpro.shared.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    suspend fun getProfile(): UserProfile?
    suspend fun updateProfile(profile: UserProfile)
    suspend fun createProfile(profile: UserProfile)
    fun observeProfile(): Flow<UserProfile?>
}
