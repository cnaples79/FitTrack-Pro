package com.fittrackpro.shared.domain.repository

import com.fittrackpro.shared.domain.model.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    suspend fun getGoals(): List<Goal>
    suspend fun getGoal(id: String): Goal?
    suspend fun addGoal(goal: Goal)
    suspend fun updateGoal(goal: Goal)
    suspend fun updateGoalProgress(id: String, progress: Int, completed: Boolean)
    suspend fun deleteGoal(id: String)
    suspend fun getActiveGoals(): List<Goal>
    fun observeGoals(): Flow<List<Goal>>
}
