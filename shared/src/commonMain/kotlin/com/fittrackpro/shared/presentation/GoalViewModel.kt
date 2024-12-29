package com.fittrackpro.shared.presentation

import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.GoalStatus
import com.fittrackpro.shared.domain.repository.GoalRepository
import com.fittrackpro.shared.util.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import com.fittrackpro.shared.util.CommonFlow
import com.fittrackpro.shared.util.asCommonFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class GoalViewModel(
    private val repository: GoalRepository,
    private val dispatchers: Dispatchers
) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + dispatchers.main)
    private val _goals = MutableStateFlow<List<Goal>>(emptyList())
    val goals: CommonFlow<List<Goal>> = _goals.asCommonFlow()

    private val _activeGoals = MutableStateFlow<List<Goal>>(emptyList())
    val activeGoals: CommonFlow<List<Goal>> = _activeGoals.asCommonFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: CommonFlow<Boolean> = _isLoading.asCommonFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: CommonFlow<String?> = _error.asCommonFlow()

    init {
        loadGoals()
    }

    fun loadGoals() {
        viewModelScope.launch(dispatchers.io) {
            _isLoading.value = true
            try {
                repository.observeActiveGoals(1L).collectLatest { goals ->
                    _goals.value = goals
                    _activeGoals.value = filterGoals(goals)
                    _error.value = null
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun filterGoals(goals: List<Goal>): List<Goal> {
        return goals.filter { goal -> goal.status != GoalStatus.COMPLETED.name }
    }

    fun addGoal(goal: Goal) {
        viewModelScope.launch(dispatchers.io) {
            try {
                repository.insertGoal(goal)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateGoalProgress(id: Long, progress: Double, completed: Boolean) {
        viewModelScope.launch(dispatchers.io) {
            try {
                val status = if (completed) GoalStatus.COMPLETED else GoalStatus.IN_PROGRESS
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                repository.updateGoalProgress(id, progress, status, now)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteGoal(id: Long) {
        viewModelScope.launch(dispatchers.io) {
            try {
                repository.deleteGoal(id)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
