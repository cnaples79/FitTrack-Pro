package com.fittrackpro.shared.data.repository

import com.fittrackpro.shared.FitTrackDatabase
import com.fittrackpro.shared.domain.model.UserProfile
import com.fittrackpro.shared.domain.repository.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull

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
                profile?.let { p ->
                    UserProfile(
                        id = p.id,
                        name = p.name,
                        age = p.age.toInt(),
                        height = p.height,
                        weight = p.weight,
                        gender = p.gender,
                        activityLevel = p.activity_level,
                        fitnessGoals = p.fitness_goals
                    )
                }
            }
    }
}
