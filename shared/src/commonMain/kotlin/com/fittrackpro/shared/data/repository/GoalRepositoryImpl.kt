package com.fittrackpro.shared.data.repository.impl

import com.fittrackpro.shared.FitTrackDatabase
import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.GoalStatus
import com.fittrackpro.shared.domain.model.GoalType
import com.fittrackpro.shared.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class GoalRepositoryImpl(
    private val database: FitTrackDatabase,
    private val userId: Long
) : GoalRepository {
    private val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    override fun getGoals(): Flow<List<Goal>> = flow {
        val goals = database.goalQueries.getGoalsByUserId(userId).executeAsList().map { goal ->
            Goal(
                id = goal.id,
                title = goal.title,
                description = goal.description ?: "",
                type = goal.type,
                targetValue = goal.target,
                currentValue = goal.progress,
                deadline = LocalDate.fromEpochDays(goal.target_date.toInt()),
                status = goal.status,
                createdAt = LocalDate.fromEpochDays(goal.created_at.toInt()),
                updatedAt = LocalDate.fromEpochDays(goal.updated_at.toInt())
            )
        }
        emit(goals)
    }

    override fun getGoalById(id: Long): Flow<Goal?> = flow {
        val goal = database.goalQueries.getGoal(id, userId).executeAsOneOrNull()?.let { goal ->
            Goal(
                id = goal.id,
                title = goal.title,
                description = goal.description ?: "",
                type = goal.type,
                targetValue = goal.target,
                currentValue = goal.progress,
                deadline = LocalDate.fromEpochDays(goal.target_date.toInt()),
                status = goal.status,
                createdAt = LocalDate.fromEpochDays(goal.created_at.toInt()),
                updatedAt = LocalDate.fromEpochDays(goal.updated_at.toInt())
            )
        }
        emit(goal)
    }

    override fun addGoal(
        title: String,
        description: String,
        type: GoalType,
        targetValue: Double,
        deadline: LocalDate
    ): Flow<Goal> = flow {
        val targetDate = deadline.toEpochDays().toLong()
        val createdAt = now.toEpochDays().toLong()
        
        database.goalQueries.insertGoal(
            user_id = userId,
            title = title,
            description = description,
            type = type,
            target = targetValue,
            progress = 0.0,
            target_date = targetDate,
            start_date = createdAt,
            end_date = targetDate,
            status = GoalStatus.NOT_STARTED,
            created_at = createdAt,
            updated_at = createdAt
        )
        
        val goalId = database.goalQueries.lastInsertRowId().executeAsOne()
        val goal = Goal(
            id = goalId,
            title = title,
            description = description,
            type = type,
            targetValue = targetValue,
            currentValue = 0.0,
            deadline = deadline,
            status = GoalStatus.NOT_STARTED,
            createdAt = now,
            updatedAt = now
        )
        emit(goal)
    }

    override fun updateGoal(
        id: Long,
        title: String,
        description: String,
        type: GoalType,
        targetValue: Double,
        deadline: LocalDate
    ): Flow<Goal> = flow {
        val currentGoal = database.goalQueries.getGoal(id, userId).executeAsOne()
        val updatedAt = now.toEpochDays().toLong()
        val targetDate = deadline.toEpochDays().toLong()
        
        database.goalQueries.updateGoal(
            title = title,
            description = description,
            type = type,
            target = targetValue,
            progress = currentGoal.progress,
            status = currentGoal.status,
            start_date = currentGoal.start_date,
            end_date = targetDate,
            updated_at = updatedAt,
            id = id,
            user_id = userId
        )
        
        val goal = Goal(
            id = id,
            title = title,
            description = description,
            type = type,
            targetValue = targetValue,
            currentValue = currentGoal.progress,
            deadline = deadline,
            status = currentGoal.status,
            createdAt = LocalDate.fromEpochDays(currentGoal.created_at.toInt()),
            updatedAt = now
        )
        emit(goal)
    }

    override fun updateGoalProgress(id: Long, newValue: Double): Flow<Goal> = flow {
        val currentGoal = database.goalQueries.getGoal(id, userId).executeAsOne()
        val updatedAt = now.toEpochDays().toLong()
        val newStatus = when {
            newValue >= currentGoal.target -> GoalStatus.COMPLETED
            newValue > 0 -> GoalStatus.IN_PROGRESS
            else -> GoalStatus.NOT_STARTED
        }
        
        // First update the progress
        database.goalQueries.updateGoalProgress(
            progress = newValue,
            updated_at = updatedAt,
            id = id,
            user_id = userId
        )
        
        // Then update the status if it changed
        if (newStatus != currentGoal.status) {
            database.goalQueries.updateGoalStatus(
                status = newStatus,
                updated_at = updatedAt,
                id = id,
                user_id = userId
            )
        }
        
        val goal = Goal(
            id = id,
            title = currentGoal.title,
            description = currentGoal.description ?: "",
            type = currentGoal.type,
            targetValue = currentGoal.target,
            currentValue = newValue,
            deadline = LocalDate.fromEpochDays(currentGoal.target_date.toInt()),
            status = newStatus,
            createdAt = LocalDate.fromEpochDays(currentGoal.created_at.toInt()),
            updatedAt = now
        )
        emit(goal)
    }

    override fun deleteGoal(id: Long): Flow<Boolean> = flow {
        database.goalQueries.deleteGoal(id, userId)
        emit(true)
    }

    override fun getGoalsByType(type: GoalType): Flow<List<Goal>> = flow {
        val goals = database.goalQueries.getGoalsByUserId(userId)
            .executeAsList()
            .filter { it.type == type }
            .map { goal ->
                Goal(
                    id = goal.id,
                    title = goal.title,
                    description = goal.description ?: "",
                    type = goal.type,
                    targetValue = goal.target,
                    currentValue = goal.progress,
                    deadline = LocalDate.fromEpochDays(goal.target_date.toInt()),
                    status = goal.status,
                    createdAt = LocalDate.fromEpochDays(goal.created_at.toInt()),
                    updatedAt = LocalDate.fromEpochDays(goal.updated_at.toInt())
                )
            }
        emit(goals)
    }

    override fun getGoalsByStatus(status: GoalStatus): Flow<List<Goal>> = flow {
        val goals = database.goalQueries.getGoalsByUserId(userId)
            .executeAsList()
            .filter { it.status == status }
            .map { goal ->
                Goal(
                    id = goal.id,
                    title = goal.title,
                    description = goal.description ?: "",
                    type = goal.type,
                    targetValue = goal.target,
                    currentValue = goal.progress,
                    deadline = LocalDate.fromEpochDays(goal.target_date.toInt()),
                    status = goal.status,
                    createdAt = LocalDate.fromEpochDays(goal.created_at.toInt()),
                    updatedAt = LocalDate.fromEpochDays(goal.updated_at.toInt())
                )
            }
        emit(goals)
    }

    override fun addMockGoal(
        title: String,
        description: String,
        type: GoalType,
        targetValue: Double,
        currentValue: Double,
        deadline: LocalDate
    ): Flow<Goal> = flow {
        val goal = Goal(
            id = 0,
            title = title,
            description = description,
            type = type,
            targetValue = targetValue,
            currentValue = currentValue,
            deadline = deadline,
            status = if (currentValue >= targetValue) GoalStatus.COMPLETED else GoalStatus.IN_PROGRESS,
            createdAt = now,
            updatedAt = now
        )
        
        val deadlineEpochDays = goal.deadline.toEpochDays().toLong()
        val nowEpochDays = now.toEpochDays().toLong()
        
        database.goalQueries.insertGoal(
            user_id = 0,
            id = userId,
            title = title,
            description = description,
            type = type,
            target = targetValue,
            progress = currentValue,
            target_date = nowEpochDays,
            start_date = deadlineEpochDays,
            end_date = deadlineEpochDays,
            status = goal.status,
            created_at = nowEpochDays,
            updated_at = nowEpochDays
        )
        
        val insertedId = database.goalQueries.lastInsertRowId().executeAsOne()
        emit(goal.copy(id = insertedId))
    }

    override fun updateMockGoal(
        id: Long,
        title: String,
        description: String,
        type: GoalType,
        targetValue: Double,
        currentValue: Double,
        deadline: LocalDate
    ): Flow<Goal?> = flow {
        val existingGoal = database.goalQueries.getGoal(id, userId).executeAsOneOrNull()
        if (existingGoal != null) {
            val status = if (currentValue >= targetValue) GoalStatus.COMPLETED else GoalStatus.IN_PROGRESS
            val updatedGoal = Goal(
                id = id,
                title = title,
                description = description,
                type = type,
                targetValue = targetValue,
                currentValue = currentValue,
                deadline = deadline,
                status = status,
                createdAt = LocalDate.fromEpochDays(existingGoal.created_at.toInt()),
                updatedAt = now
            )
            
            val deadlineEpochDays = deadline.toEpochDays().toLong()
            val updatedAtEpochDays = now.toEpochDays().toLong()
            
            database.goalQueries.updateGoal(
                title = title,
                description = description,
                type = type,
                target = targetValue,
                progress = currentValue,
                status = status,
                start_date = existingGoal.start_date,
                end_date = deadlineEpochDays,
                updated_at = updatedAtEpochDays,
                id = id,
                user_id = userId
            )
            
            emit(updatedGoal)
        } else {
            emit(null)
        }
    }
}
