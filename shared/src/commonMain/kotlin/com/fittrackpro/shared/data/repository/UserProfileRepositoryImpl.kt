package com.fittrackpro.shared.data.repository

import com.fittrackpro.db.FitTrackDatabase
import com.fittrackpro.shared.domain.model.*
import com.fittrackpro.shared.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserProfileRepositoryImpl(database: FitTrackDatabase) : UserProfileRepository {
    private val queries = database.userProfileQueries

    override suspend fun getProfile(): UserProfile? {
        val profile = queries.getProfile().executeAsOneOrNull() ?: return null
        val workoutTypes = queries.getPreferredWorkoutTypes(profile.id)
            .executeAsList()
            .map { WorkoutType.valueOf(it) }

        return UserProfile(
            id = profile.id,
            name = profile.name,
            email = profile.email,
            height = profile.height,
            weight = profile.weight,
            age = profile.age?.toInt(),
            gender = profile.gender?.let { Gender.valueOf(it) },
            fitnessLevel = FitnessLevel.valueOf(profile.fitness_level),
            preferredWorkoutTypes = workoutTypes,
            weeklyGoal = profile.weekly_goal.toInt()
        )
    }

    override suspend fun updateProfile(profile: UserProfile) {
        queries.transaction {
            queries.updateProfile(
                name = profile.name,
                email = profile.email,
                height = profile.height,
                weight = profile.weight,
                age = profile.age?.toLong(),
                gender = profile.gender?.name,
                fitness_level = profile.fitnessLevel.name,
                weekly_goal = profile.weeklyGoal.toLong(),
                id = profile.id
            )

            // Update preferred workout types
            queries.deletePreferredWorkoutTypes(profile.id)
            profile.preferredWorkoutTypes.forEach { type ->
                queries.insertPreferredWorkoutType(profile.id, type.name)
            }
        }
    }

    override suspend fun createProfile(profile: UserProfile) {
        queries.transaction {
            queries.insertProfile(
                id = profile.id,
                name = profile.name,
                email = profile.email,
                height = profile.height,
                weight = profile.weight,
                age = profile.age?.toLong(),
                gender = profile.gender?.name,
                fitness_level = profile.fitnessLevel.name,
                weekly_goal = profile.weeklyGoal.toLong()
            )

            profile.preferredWorkoutTypes.forEach { type ->
                queries.insertPreferredWorkoutType(profile.id, type.name)
            }
        }
    }

    override fun observeProfile(): Flow<UserProfile?> = flow {
        emit(getProfile())
    }
}
