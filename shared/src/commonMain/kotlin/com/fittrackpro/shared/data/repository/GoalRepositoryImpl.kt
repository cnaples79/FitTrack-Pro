package com.fittrackpro.shared.data.repository

import com.fittrackpro.db.FitTrackDatabase
import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.GoalType
import com.fittrackpro.shared.domain.repository.GoalRepository
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

class GoalRepositoryImpl(database: FitTrackDatabase) : GoalRepository {
    private val queries = database.goalQueries

    override suspend fun getGoals(): List<Goal> {
        return queries.getAllGoals().executeAsList().map { it.toGoal() }
    }

    override suspend fun getGoal(id: String): Goal? {
        return queries.getGoalById(id).executeAsOneOrNull()?.toGoal()
    }

    override suspend fun addGoal(goal: Goal) {
        queries.insertGoal(
            id = goal.id,
            title = goal.title,
            description = goal.description,
            target_date = goal.targetDate.toString(),
            type = goal.type.name,
            target = goal.target.toLong(),
            progress = goal.progress.toLong(),
            completed = if (goal.completed) 1L else 0L
        )
    }

    override suspend fun updateGoal(goal: Goal) {
        queries.updateGoal(
            title = goal.title,
            description = goal.description,
            target_date = goal.targetDate.toString(),
            type = goal.type.name,
            target = goal.target.toLong(),
            progress = goal.progress.toLong(),
            completed = if (goal.completed) 1L else 0L,
            id = goal.id
        )
    }

    override suspend fun updateGoalProgress(id: String, progress: Int, completed: Boolean) {
        queries.updateGoalProgress(
            progress = progress.toLong(),
            completed = if (completed) 1L else 0L,
            id = id
        )
    }

    override suspend fun deleteGoal(id: String) {
        queries.deleteGoal(id)
    }

    override suspend fun getActiveGoals(): List<Goal> {
        return queries.getActiveGoals(LocalDate.now().toString())
            .executeAsList()
            .map { it.toGoal() }
    }

    override fun observeGoals(): Flow<List<Goal>> {
        return queries.getAllGoals()
            .asFlow()
            .mapToList()
            .map { goals -> goals.map { it.toGoal() } }
    }

    private fun com.fittrackpro.db.Goal.toGoal(): Goal {
        return Goal(
            id = id,
            title = title,
            description = description,
            targetDate = target_date.toLocalDate(),
            type = GoalType.valueOf(type),
            target = target.toInt(),
            progress = progress.toInt(),
            completed = completed == 1L
        )
    }
}
