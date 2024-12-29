package com.fittrackpro.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittrackpro.android.ui.states.UiState
import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.GoalType
import com.fittrackpro.shared.domain.model.GoalStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.*

class GoalViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Goal>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Goal>>> = _uiState.asStateFlow()

    init {
        loadGoals()
    }

    private fun loadGoals() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual repository call
                val goals = listOf<Goal>() // Temporary empty list
                _uiState.value = UiState.Success(goals)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load goals")
            }
        }
    }

    fun addGoal(
        name: String,
        description: String,
        targetValue: Double,
        type: GoalType,
        deadline: LocalDate
    ) {
        viewModelScope.launch {
            try {
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                val goal = Goal(
                    id = 0, // This will be replaced by the database
                    userId = 1, // TODO: Get from auth
                    title = name,
                    description = description,
                    targetValue = targetValue,
                    currentValue = 0.0,
                    type = type,
                    status = GoalStatus.NOT_STARTED,
                    startDate = now,
                    endDate = deadline,
                    createdAt = now,
                    updatedAt = now
                )
                // TODO: Add goal to repository
                loadGoals() // Reload goals after adding
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to add goal")
            }
        }
    }

    fun updateGoalProgress(goalId: Long, newValue: Double) {
        viewModelScope.launch {
            try {
                // TODO: Update goal progress in repository
                loadGoals() // Reload goals after updating
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to update goal progress")
            }
        }
    }

    fun deleteGoal(goalId: Long) {
        viewModelScope.launch {
            try {
                // TODO: Delete goal from repository
                loadGoals() // Reload goals after deleting
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to delete goal")
            }
        }
    }
}
