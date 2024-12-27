package com.fittrackpro.shared.data.repository

import com.fittrackpro.shared.data.FitTrackDatabase
import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoalRepositoryImpl(
    private val database: FitTrackDatabase
) : GoalRepository {

    override suspend fun getGoals(): List<Goal> = withContext(Dispatchers.Default) {
        database.goalQueries.getAllGoals().executeAsList().map { goalEntity ->
            Goal(
                id = goalEntity.id,
                userId = goalEntity.user_id,
                title = goalEntity.title,
                description = goalEntity.description,
                type = goalEntity.type,
                target = goalEntity.target,
                progress = goalEntity.progress,
                targetDate = goalEntity.target_date,
                startDate = goalEntity.start_date,
                endDate = goalEntity.end_date,
                status = goalEntity.status
            )
        }
    }

    override suspend fun getGoal(id: String): Goal? = withContext(Dispatchers.Default) {
        database.goalQueries.getGoalById(id.toLong()).executeAsOneOrNull()?.let { goalEntity ->
            Goal(
                id = goalEntity.id,
                userId = goalEntity.user_id,
                title = goalEntity.title,
                description = goalEntity.description,
                type = goalEntity.type,
                target = goalEntity.target,
                progress = goalEntity.progress,
                targetDate = goalEntity.target_date,
                startDate = goalEntity.start_date,
                endDate = goalEntity.end_date,
                status = goalEntity.status
            )
        }
    }

    override suspend fun addGoal(goal: Goal) = withContext(Dispatchers.Default) {
        database.goalQueries.insertGoal(
            user_id = goal.userId,
            title = goal.title,
            description = goal.description,
            type = goal.type,
            target = goal.target,
            progress = goal.progress,
            target_date = goal.targetDate,
            start_date = goal.startDate,
            end_date = goal.endDate,
            status = goal.status
        )
    }

    override suspend fun updateGoal(goal: Goal) = withContext(Dispatchers.Default) {
        database.goalQueries.updateGoal(
            title = goal.title,
            description = goal.description,
            type = goal.type,
            target = goal.target,
            target_date = goal.targetDate,
            end_date = goal.endDate,
            status = goal.status,
            id = goal.id
        )
    }

    override suspend fun updateGoalProgress(id: String, progress: Int, completed: Boolean) = withContext(Dispatchers.Default) {
        database.goalQueries.updateGoalProgress(
            progress = progress.toLong(),
            status = if (completed) "COMPLETED" else "IN_PROGRESS",
            id = id.toLong()
        )
    }

    override suspend fun deleteGoal(id: String) = withContext(Dispatchers.Default) {
        database.goalQueries.deleteGoal(id.toLong())
    }

    override fun observeGoals(): Flow<List<Goal>> {
        return database.goalQueries.getAllGoals()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { goalEntities ->
                goalEntities.map { goalEntity ->
                    Goal(
                        id = goalEntity.id,
                        userId = goalEntity.user_id,
                        title = goalEntity.title,
                        description = goalEntity.description,
                        type = goalEntity.type,
                        target = goalEntity.target,
                        progress = goalEntity.progress,
                        targetDate = goalEntity.target_date,
                        startDate = goalEntity.start_date,
                        endDate = goalEntity.end_date,
                        status = goalEntity.status
                    )
                }
            }
    }
}
