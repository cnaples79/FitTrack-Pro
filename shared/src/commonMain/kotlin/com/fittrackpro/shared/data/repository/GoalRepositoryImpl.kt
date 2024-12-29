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
        val goal = Goal(
            id = 0,
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
        
        val deadlineEpochDays = goal.deadline.toEpochDays().toLong()
        val nowEpochDays = now.toEpochDays().toLong()
        
        database.goalQueries.insertGoal(
            userId,
            title,
            description,
            type,
            targetValue,
            0.0,
            deadlineEpochDays,
            nowEpochDays,
            deadlineEpochDays,
            GoalStatus.NOT_STARTED,
            nowEpochDays,
            nowEpochDays
        )
        
        val insertedId = database.goalQueries.lastInsertRowId().executeAsOne()
        emit(goal.copy(id = insertedId))
    }

    override fun updateGoal(
        id: Long,
        title: String,
        description: String,
        type: GoalType,
        targetValue: Double,
        deadline: LocalDate
    ): Flow<Goal?> = flow {
        val existingGoal = database.goalQueries.getGoal(id, userId).executeAsOneOrNull()
        if (existingGoal != null) {
            val updatedGoal = Goal(
                id = id,
                title = title,
                description = description,
                type = type,
                targetValue = targetValue,
                currentValue = existingGoal.progress,
                deadline = deadline,
                status = existingGoal.status,
                createdAt = LocalDate.fromEpochDays(existingGoal.created_at.toInt()),
                updatedAt = now
            )
            
            val deadlineEpochDays = deadline.toEpochDays().toLong()
            val updatedAtEpochDays = now.toEpochDays().toLong()
            
            database.goalQueries.updateGoal(
                title,
                description,
                type,
                targetValue,
                existingGoal.progress,
                existingGoal.status,
                existingGoal.start_date,
                deadlineEpochDays,
                updatedAtEpochDays,
                id,
                userId
            )
            
            emit(updatedGoal)
        } else {
            emit(null)
        }
    }

    override fun updateGoalProgress(id: Long, currentValue: Double): Flow<Goal?> = flow {
        val existingGoal = database.goalQueries.getGoal(id, userId).executeAsOneOrNull()
        if (existingGoal != null) {
            val status = if (currentValue >= existingGoal.target) GoalStatus.COMPLETED else GoalStatus.IN_PROGRESS
            val updatedAt = now.toEpochDays().toLong()
            
            database.goalQueries.updateGoalProgress(
                currentValue,
                updatedAt,
                id,
                userId
            )
            
            // Update status if needed
            if (status != existingGoal.status) {
                database.goalQueries.updateGoalStatus(
                    status,
                    updatedAt,
                    id,
                    userId
                )
            }
            
            val updatedGoal = Goal(
                id = existingGoal.id,
                title = existingGoal.title,
                description = existingGoal.description ?: "",
                type = existingGoal.type,
                targetValue = existingGoal.target,
                currentValue = currentValue,
                deadline = LocalDate.fromEpochDays(existingGoal.target_date.toInt()),
                status = status,
                createdAt = LocalDate.fromEpochDays(existingGoal.created_at.toInt()),
                updatedAt = now
            )
            
            emit(updatedGoal)
        } else {
            emit(null)
        }
    }

    override fun deleteGoal(id: Long): Flow<Boolean> = flow {
        val existingGoal = database.goalQueries.getGoal(id, userId).executeAsOneOrNull()
        if (existingGoal != null) {
            database.goalQueries.deleteGoal(id, userId)
            emit(true)
        } else {
            emit(false)
        }
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
            userId,
            title,
            description,
            type,
            targetValue,
            currentValue,
            deadlineEpochDays,
            nowEpochDays,
            deadlineEpochDays,
            if (currentValue >= targetValue) GoalStatus.COMPLETED else GoalStatus.IN_PROGRESS,
            nowEpochDays,
            nowEpochDays
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
                title,
                description,
                type,
                targetValue,
                currentValue,
                status,
                existingGoal.start_date,
                deadlineEpochDays,
                updatedAtEpochDays,
                id,
                userId
            )
            
            emit(updatedGoal)
        } else {
            emit(null)
        }
    }
}
