package com.fittrackpro.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittrackpro.android.ui.states.UiState
import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.GoalType
import com.fittrackpro.shared.domain.model.GoalStatus
import com.fittrackpro.shared.domain.repository.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GoalViewModel : ViewModel(), KoinComponent {
    private val repository: GoalRepository by inject()
    private val _uiState = MutableStateFlow<UiState<List<Goal>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Goal>>> = _uiState.asStateFlow()

    init {
        loadGoals()
    }

    private fun loadGoals() {
        viewModelScope.launch {
            try {
                repository.getGoals().collect { goals ->
                    _uiState.value = UiState.Success(goals)
                }
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
                repository.addGoal(
                    title = name,
                    description = description,
                    type = type,
                    targetValue = targetValue,
                    deadline = deadline
                ).collect { goal ->
                    loadGoals() // Refresh the list after adding
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to add goal")
            }
        }
    }

    fun updateGoal(
        id: Long,
        name: String,
        description: String,
        targetValue: Double,
        type: GoalType,
        deadline: LocalDate
    ) {
        viewModelScope.launch {
            try {
                repository.updateGoal(
                    id = id,
                    title = name,
                    description = description,
                    type = type,
                    targetValue = targetValue,
                    deadline = deadline
                ).collect { goal ->
                    loadGoals() // Refresh the list after updating
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to update goal")
            }
        }
    }

    fun deleteGoal(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteGoal(id).collect { success ->
                    if (success) {
                        loadGoals() // Refresh the list after deleting
                    } else {
                        _uiState.value = UiState.Error("Failed to delete goal")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to delete goal")
            }
        }
    }
}
