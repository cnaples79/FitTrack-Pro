package com.fittrackpro.shared.presentation

import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.repository.GoalRepository
import com.fittrackpro.shared.util.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import com.fittrackpro.shared.util.CommonFlow
import com.fittrackpro.shared.util.asCommonFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

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
                repository.getGoals().collect { goals ->
                    _goals.value = goals
                    _activeGoals.value = goals.filter { !it.completed }
                    _error.value = null
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addGoal(goal: Goal) {
        viewModelScope.launch(dispatchers.io) {
            try {
                repository.addGoal(goal)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateGoalProgress(id: String, progress: Int, completed: Boolean) {
        viewModelScope.launch(dispatchers.io) {
            try {
                repository.updateGoalProgress(id, progress, completed)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteGoal(id: String) {
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
