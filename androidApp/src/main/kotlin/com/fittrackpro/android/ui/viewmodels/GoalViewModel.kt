package com.fittrackpro.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.GoalType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate

class GoalViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<GoalUiState>(GoalUiState.Loading)
    val uiState: StateFlow<GoalUiState> = _uiState.asStateFlow()

    private val _goals = MutableStateFlow<List<Goal>>(emptyList())
    val goals: StateFlow<List<Goal>> = _goals.asStateFlow()

    init {
        loadGoals()
    }

    private fun loadGoals() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual repository call
                val mockGoals = createMockGoals()
                _goals.value = mockGoals
                _uiState.value = GoalUiState.Success
            } catch (e: Exception) {
                _uiState.value = GoalUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun addGoal(goal: Goal) {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual repository call
                val currentGoals = _goals.value.toMutableList()
                currentGoals.add(goal)
                _goals.value = currentGoals
                _uiState.value = GoalUiState.Success
            } catch (e: Exception) {
                _uiState.value = GoalUiState.Error(e.message ?: "Failed to add goal")
            }
        }
    }

    fun updateGoalProgress(goalId: String, newProgress: Int) {
        viewModelScope.launch {
            try {
                val updatedGoals = _goals.value.map { goal ->
                    if (goal.id == goalId) {
                        goal.copy(
                            progress = newProgress,
                            completed = newProgress >= goal.target
                        )
                    } else goal
                }
                _goals.value = updatedGoals
            } catch (e: Exception) {
                _uiState.value = GoalUiState.Error(e.message ?: "Failed to update goal")
            }
        }
    }

    private fun createMockGoals(): List<Goal> {
        val today = Clock.System.now().toLocalDate(TimeZone.currentSystemDefault())
        val targetDate = today.plusDays(30)
        
        return listOf(
            Goal(
                id = "1",
                title = "Weekly Workouts",
                description = "Complete 5 workouts this week",
                targetDate = targetDate,
                type = GoalType.WORKOUT_COUNT,
                target = 5,
                progress = 2,
                completed = false
            ),
            Goal(
                id = "2",
                title = "Monthly Exercise Minutes",
                description = "Exercise for 1000 minutes this month",
                targetDate = targetDate,
                type = GoalType.WORKOUT_MINUTES,
                target = 1000,
                progress = 450,
                completed = false
            )
        )
    }
}

sealed class GoalUiState {
    object Loading : GoalUiState()
    object Success : GoalUiState()
    data class Error(val message: String) : GoalUiState()
}
