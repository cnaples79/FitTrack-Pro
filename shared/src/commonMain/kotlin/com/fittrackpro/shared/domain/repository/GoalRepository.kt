package com.fittrackpro.shared.domain.repository

import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.GoalStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface GoalRepository {
    suspend fun getGoal(id: Long): Goal?
    suspend fun getGoalsByUserId(userId: Long): List<Goal>
    suspend fun insertGoal(goal: Goal): Long
    suspend fun updateGoal(goal: Goal)
    suspend fun updateGoalProgress(id: Long, currentValue: Double, status: GoalStatus, updatedAt: LocalDate)
    suspend fun deleteGoal(id: Long)
    fun observeActiveGoals(userId: Long): Flow<List<Goal>>
    fun observeCompletedGoals(userId: Long): Flow<List<Goal>>
}
