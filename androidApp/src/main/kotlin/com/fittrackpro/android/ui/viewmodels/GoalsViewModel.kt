package com.fittrackpro.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittrackpro.android.ui.states.UiState
import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.GoalType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class GoalsViewModel : ViewModel() {
    private val _goals = MutableStateFlow<UiState<List<Goal>>>(UiState.Loading)
    val goals: StateFlow<UiState<List<Goal>>> = _goals.asStateFlow()

    init {
        loadGoals()
    }

    private fun loadGoals() {
        viewModelScope.launch {
            try {
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                // TODO: Replace with actual repository call
                val mockGoals = listOf(
                    Goal(
                        id = 1,
                        title = "Daily Steps",
                        description = "Walk 10,000 steps every day",
                        type = GoalType.STEPS,
                        targetValue = 10000.0,
                        currentValue = 7500.0,
                        deadline = now.plus(kotlinx.datetime.DatePeriod(days = 7)),
                        createdAt = now,
                        updatedAt = now
                    ),
                    Goal(
                        id = 2,
                        title = "Weight Loss",
                        description = "Lose 5kg in 3 months",
                        type = GoalType.WEIGHT,
                        targetValue = 5.0,
                        currentValue = 2.0,
                        deadline = now.plus(kotlinx.datetime.DatePeriod(months = 3)),
                        createdAt = now,
                        updatedAt = now
                    )
                )
                _goals.value = UiState.Success(mockGoals)
            } catch (e: Exception) {
                _goals.value = UiState.Error(e.message ?: "Failed to load goals")
            }
        }
    }

    fun addGoal(title: String, description: String, targetValue: Double, type: GoalType, deadline: LocalDate) {
        viewModelScope.launch {
            try {
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                val newGoal = Goal(
                    id = System.currentTimeMillis(),
                    title = title,
                    description = description,
                    type = type,
                    targetValue = targetValue,
                    deadline = deadline,
                    createdAt = now,
                    updatedAt = now
                )
                val currentGoals = (_goals.value as? UiState.Success)?.data ?: emptyList()
                _goals.value = UiState.Success(currentGoals + newGoal)
            } catch (e: Exception) {
                _goals.value = UiState.Error(e.message ?: "Failed to add goal")
            }
        }
    }

    fun updateGoalProgress(goalId: Long, newValue: Double) {
        viewModelScope.launch {
            try {
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                val currentGoals = (_goals.value as? UiState.Success)?.data ?: return@launch
                val updatedGoals = currentGoals.map { goal ->
                    if (goal.id == goalId) {
                        goal.copy(currentValue = newValue, updatedAt = now)
                    } else {
                        goal
                    }
                }
                _goals.value = UiState.Success(updatedGoals)
            } catch (e: Exception) {
                _goals.value = UiState.Error(e.message ?: "Failed to update goal progress")
            }
        }
    }
}
