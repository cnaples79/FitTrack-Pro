package com.fittrackpro.shared.data.repository

import com.fittrackpro.shared.FitTrackDatabase
import com.fittrackpro.shared.domain.model.Profile
import com.fittrackpro.shared.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers

class ProfileRepositoryImpl(
    private val database: FitTrackDatabase
) : ProfileRepository {
    override suspend fun getProfile(userId: Long): Profile? {
        return database.profileQueries.getProfile(userId).executeAsOneOrNull()?.let { profile ->
            Profile(
                id = profile.id,
                email = profile.email,
                name = profile.name,
                gender = profile.gender,
                fitnessLevel = profile.fitnessLevel,
                activityLevel = profile.activityLevel,
                weeklyGoal = profile.weeklyGoal.toInt(),
                preferredWorkoutTypes = profile.preferredWorkoutTypes,
                fitnessGoals = profile.fitnessGoals
            )
        }
    }

    override suspend fun updateProfile(profile: Profile) {
        database.profileQueries.upsertProfile(
            id = profile.id,
            email = profile.email,
            name = profile.name,
            gender = profile.gender,
            fitnessLevel = profile.fitnessLevel,
            activityLevel = profile.activityLevel,
            weeklyGoal = profile.weeklyGoal.toLong(),
            preferredWorkoutTypes = profile.preferredWorkoutTypes,
            fitnessGoals = profile.fitnessGoals
        )
    }

    override fun observeProfile(userId: Long): Flow<Profile?> {
        return database.profileQueries.observeProfile(userId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { profile ->
                profile?.let {
                    Profile(
                        id = it.id,
                        email = it.email,
                        name = it.name,
                        gender = it.gender,
                        fitnessLevel = it.fitnessLevel,
                        activityLevel = it.activityLevel,
                        weeklyGoal = it.weeklyGoal.toInt(),
                        preferredWorkoutTypes = it.preferredWorkoutTypes,
                        fitnessGoals = it.fitnessGoals
                    )
                }
            }
    }
}
