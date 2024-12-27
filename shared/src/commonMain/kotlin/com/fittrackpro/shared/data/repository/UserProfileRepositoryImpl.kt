package com.fittrackpro.shared.data.repository

import com.fittrackpro.shared.data.FitTrackDatabase
import com.fittrackpro.shared.domain.model.UserProfile
import com.fittrackpro.shared.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers

class UserProfileRepositoryImpl(
    private val database: FitTrackDatabase
) : UserProfileRepository {

    override suspend fun getProfile(): UserProfile? {
        return database.userProfileQueries.getProfile().executeAsOneOrNull()?.let { profile ->
            UserProfile(
                id = profile.id,
                name = profile.name,
                age = profile.age.toInt(),
                height = profile.height,
                weight = profile.weight,
                gender = profile.gender,
                activityLevel = profile.activity_level,
                fitnessGoals = profile.fitness_goals
            )
        }
    }

    override suspend fun createProfile(profile: UserProfile) {
        database.userProfileQueries.upsertProfile(
            id = profile.id,
            name = profile.name,
            age = profile.age.toLong(),
            height = profile.height,
            weight = profile.weight,
            gender = profile.gender,
            activity_level = profile.activityLevel,
            fitness_goals = profile.fitnessGoals
        )
    }

    override suspend fun updateProfile(profile: UserProfile) {
        database.userProfileQueries.upsertProfile(
            id = profile.id,
            name = profile.name,
            age = profile.age.toLong(),
            height = profile.height,
            weight = profile.weight,
            gender = profile.gender,
            activity_level = profile.activityLevel,
            fitness_goals = profile.fitnessGoals
        )
    }

    override fun observeProfile(): Flow<UserProfile?> {
        return database.userProfileQueries.getProfile()
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { profile ->
                profile?.let {
                    UserProfile(
                        id = it.id,
                        name = it.name,
                        age = it.age.toInt(),
                        height = it.height,
                        weight = it.weight,
                        gender = it.gender,
                        activityLevel = it.activity_level,
                        fitnessGoals = it.fitness_goals
                    )
                }
            }
    }
}
