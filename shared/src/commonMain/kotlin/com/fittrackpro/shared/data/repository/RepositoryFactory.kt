package com.fittrackpro.shared.data.repository

import com.fittrackpro.shared.FitTrackDatabase
import com.fittrackpro.shared.domain.repository.GoalRepository
import com.fittrackpro.shared.domain.repository.UserProfileRepository
import com.fittrackpro.shared.domain.repository.WorkoutRepository
import com.fittrackpro.shared.data.repository.impl.GoalRepositoryImpl
import com.fittrackpro.shared.data.repository.impl.UserProfileRepositoryImpl
import com.fittrackpro.shared.data.repository.impl.WorkoutRepositoryImpl

object RepositoryFactory {
    fun createGoalRepository(database: FitTrackDatabase, userId: Long): GoalRepository {
        return GoalRepositoryImpl(database, userId)
    }

    fun createUserProfileRepository(database: FitTrackDatabase, userId: Long): UserProfileRepository {
        return UserProfileRepositoryImpl(database, userId)
    }

    fun createWorkoutRepository(database: FitTrackDatabase, userId: Long): WorkoutRepository {
        return WorkoutRepositoryImpl(database, userId)
    }
}
