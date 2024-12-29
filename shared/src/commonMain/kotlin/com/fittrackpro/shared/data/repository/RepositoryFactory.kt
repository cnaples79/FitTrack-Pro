package com.fittrackpro.shared.data.repository

import GoalRepositoryImpl
import com.fittrackpro.shared.FitTrackDatabase
import com.fittrackpro.shared.domain.repository.GoalRepository
import com.fittrackpro.shared.domain.repository.UserProfileRepository
import com.fittrackpro.shared.domain.repository.WorkoutRepository

object RepositoryFactory {
    fun createGoalRepository(database: FitTrackDatabase, userId: Long): GoalRepository {
        return GoalRepositoryImpl(database, userId)
    }

    fun createUserProfileRepository(database: FitTrackDatabase): UserProfileRepository {
        return UserProfileRepositoryImpl(database)
    }

    fun createWorkoutRepository(database: FitTrackDatabase): WorkoutRepository {
        return WorkoutRepositoryImpl(database)
    }
}
