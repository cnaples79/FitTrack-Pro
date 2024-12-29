package com.fittrackpro.shared.domain.repository

import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.GoalStatus
import com.fittrackpro.shared.domain.model.GoalType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface GoalRepository {
    fun getGoals(): Flow<List<Goal>>
    fun getGoalById(id: Long): Flow<Goal?>
    fun addGoal(
        title: String,
        description: String,
        type: GoalType,
        targetValue: Double,
        deadline: LocalDate
    ): Flow<Goal>
    
    fun updateGoal(
        id: Long,
        title: String,
        description: String,
        type: GoalType,
        targetValue: Double,
        deadline: LocalDate
    ): Flow<Goal?>
    
    fun updateGoalProgress(id: Long, currentValue: Double): Flow<Goal?>
    fun deleteGoal(id: Long): Flow<Boolean>
    fun getGoalsByType(type: GoalType): Flow<List<Goal>>
    fun getGoalsByStatus(status: GoalStatus): Flow<List<Goal>>
    
    fun addMockGoal(
        title: String,
        description: String,
        type: GoalType,
        targetValue: Double,
        currentValue: Double,
        deadline: LocalDate
    ): Flow<Goal>
    
    fun updateMockGoal(
        id: Long,
        title: String,
        description: String,
        type: GoalType,
        targetValue: Double,
        currentValue: Double,
        deadline: LocalDate
    ): Flow<Goal?>
}
