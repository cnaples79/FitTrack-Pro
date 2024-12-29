package com.fittrackpro.shared.domain.repository

import com.fittrackpro.shared.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getProfile(userId: Long): Profile?
    suspend fun updateProfile(profile: Profile)
    fun observeProfile(userId: Long): Flow<Profile?>
}
