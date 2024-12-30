package com.fittrackpro.shared.data.repository.impl

import com.fittrackpro.shared.FitTrackDatabase
import com.fittrackpro.shared.domain.model.UserProfile
import com.fittrackpro.shared.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull

class UserProfileRepositoryImpl(
    private val database: FitTrackDatabase,
    private val userId: Long
) : UserProfileRepository {

    override fun getProfile(): Flow<UserProfile?> = flow {
        val profile = database.userProfileQueries.getUserProfile(userId).executeAsOneOrNull()?.let { profile ->
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
        emit(profile)
    }

    override fun createProfile(profile: UserProfile): Flow<UserProfile> = flow {
        database.userProfileQueries.insertProfile(
            id = userId,
            name = profile.name,
            age = profile.age.toLong(),
            height = profile.height,
            weight = profile.weight,
            gender = profile.gender,
            activity_level = profile.activityLevel,
            fitness_goals = profile.fitnessGoals
        )
        emit(profile.copy(id = userId))
    }

    override fun updateProfile(profile: UserProfile): Flow<UserProfile> = flow {
        database.userProfileQueries.updateProfile(
            id = userId,
            name = profile.name,
            age = profile.age.toLong(),
            height = profile.height,
            weight = profile.weight,
            gender = profile.gender,
            activity_level = profile.activityLevel,
            fitness_goals = profile.fitnessGoals
        )
        emit(profile.copy(id = userId))
    }

    override fun observeProfile(): Flow<UserProfile?> {
        return database.userProfileQueries.getUserProfile(userId)
            .asFlow()
            .mapToOneOrNull()
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
