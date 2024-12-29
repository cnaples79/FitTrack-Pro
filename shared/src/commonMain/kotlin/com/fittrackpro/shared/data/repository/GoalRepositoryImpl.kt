package com.fittrackpro.shared.data.repository

import com.fittrackpro.shared.FitTrackDatabase
import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.GoalStatus
import com.fittrackpro.shared.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.datetime.LocalDate
import kotlinx.coroutines.Dispatchers

class GoalRepositoryImpl(
    private val database: FitTrackDatabase
) : GoalRepository {
    override suspend fun getGoal(id: Long): Goal? {
        return database.goalQueries.getGoal(id).executeAsOneOrNull()?.let { goal ->
            Goal(
                id = goal.id,
                userId = goal.user_id,
                title = goal.title,
                description = goal.description ?: "",
                type = goal.type,
                targetValue = goal.target,
                currentValue = goal.progress,
                status = goal.status,
                startDate = LocalDate.fromEpochDays(goal.start_date.toInt()),
                endDate = LocalDate.fromEpochDays(goal.end_date.toInt()),
                createdAt = LocalDate.fromEpochDays(goal.created_at.toInt()),
                updatedAt = LocalDate.fromEpochDays(goal.updated_at.toInt())
            )
        }
    }

    override suspend fun getGoalsByUserId(userId: Long): List<Goal> {
        return database.goalQueries.getGoalsByUserId(userId).executeAsList().map { goal ->
            Goal(
                id = goal.id,
                userId = goal.user_id,
                title = goal.title,
                description = goal.description ?: "",
                type = goal.type,
                targetValue = goal.target,
                currentValue = goal.progress,
                status = goal.status,
                startDate = LocalDate.fromEpochDays(goal.start_date.toInt()),
                endDate = LocalDate.fromEpochDays(goal.end_date.toInt()),
                createdAt = LocalDate.fromEpochDays(goal.created_at.toInt()),
                updatedAt = LocalDate.fromEpochDays(goal.updated_at.toInt())
            )
        }
    }

    override suspend fun insertGoal(goal: Goal): Long {
        database.goalQueries.insertGoal(
            user_id = goal.userId,
            title = goal.title,
            description = goal.description,
            type = goal.type,
            target = goal.targetValue,
            progress = goal.currentValue,
            target_date = goal.startDate.toEpochDays().toLong(),
            start_date = goal.startDate.toEpochDays().toLong(),
            end_date = goal.endDate.toEpochDays().toLong(),
            status = goal.status,
            created_at = goal.createdAt.toEpochDays().toLong(),
            updated_at = goal.updatedAt.toEpochDays().toLong()
        )
        return database.goalQueries.lastInsertRowId().executeAsOne()
    }

    override suspend fun updateGoal(goal: Goal) {
        database.goalQueries.updateGoal(
            title = goal.title,
            description = goal.description,
            type = goal.type,
            target = goal.targetValue,
            progress = goal.currentValue,
            status = goal.status,
            start_date = goal.startDate.toEpochDays().toLong(),
            end_date = goal.endDate.toEpochDays().toLong(),
            updated_at = goal.updatedAt.toEpochDays().toLong(),
            id = goal.id
        )
    }

    override suspend fun updateGoalProgress(id: Long, currentValue: Double, status: GoalStatus, updatedAt: LocalDate) {
        database.goalQueries.updateGoalProgress(
            progress = currentValue,
            updated_at = updatedAt.toEpochDays().toLong(),
            id = id
        )
        
        database.goalQueries.updateGoalStatus(
            status = status,
            updated_at = updatedAt.toEpochDays().toLong(),
            id = id
        )
    }

    override suspend fun deleteGoal(id: Long) {
        database.goalQueries.deleteGoal(id)
    }

    override fun observeActiveGoals(userId: Long): Flow<List<Goal>> {
        return database.goalQueries.getActiveGoals(userId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { goals ->
                goals.map { goal ->
                    Goal(
                        id = goal.id,
                        userId = goal.user_id,
                        title = goal.title,
                        description = goal.description ?: "",
                        type = goal.type,
                        targetValue = goal.target,
                        currentValue = goal.progress,
                        status = goal.status,
                        startDate = LocalDate.fromEpochDays(goal.start_date.toInt()),
                        endDate = LocalDate.fromEpochDays(goal.end_date.toInt()),
                        createdAt = LocalDate.fromEpochDays(goal.created_at.toInt()),
                        updatedAt = LocalDate.fromEpochDays(goal.updated_at.toInt())
                    )
                }
            }
    }

    override fun observeCompletedGoals(userId: Long): Flow<List<Goal>> {
        return database.goalQueries.getCompletedGoals(userId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { goals ->
                goals.map { goal ->
                    Goal(
                        id = goal.id,
                        userId = goal.user_id,
                        title = goal.title,
                        description = goal.description ?: "",
                        type = goal.type,
                        targetValue = goal.target,
                        currentValue = goal.progress,
                        status = goal.status,
                        startDate = LocalDate.fromEpochDays(goal.start_date.toInt()),
                        endDate = LocalDate.fromEpochDays(goal.end_date.toInt()),
                        createdAt = LocalDate.fromEpochDays(goal.created_at.toInt()),
                        updatedAt = LocalDate.fromEpochDays(goal.updated_at.toInt())
                    )
                }
            }
    }
}
