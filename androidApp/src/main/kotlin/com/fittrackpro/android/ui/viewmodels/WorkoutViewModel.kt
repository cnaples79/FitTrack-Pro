package com.fittrackpro.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittrackpro.android.ui.states.UiState
import com.fittrackpro.shared.domain.model.Workout
import com.fittrackpro.shared.domain.model.WorkoutType
import com.fittrackpro.shared.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WorkoutViewModel : ViewModel(), KoinComponent {
    private val repository: WorkoutRepository by inject()
    private val _uiState = MutableStateFlow<UiState<List<Workout>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Workout>>> = _uiState.asStateFlow()

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            try {
                repository.getWorkouts().collect { workouts ->
                    _uiState.value = UiState.Success(workouts)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load workouts")
            }
        }
    }

    fun addWorkout(
        type: String,
        duration: Long,
        caloriesBurned: Long?,
        distance: Double?,
        notes: String?
    ) {
        viewModelScope.launch {
            try {
                repository.addWorkout(
                    type = type,
                    duration = duration,
                    caloriesBurned = caloriesBurned,
                    distance = distance,
                    notes = notes
                ).collect { workout ->
                    loadWorkouts() // Refresh the list after adding
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to add workout")
            }
        }
    }

    fun updateWorkout(
        id: Long,
        type: String,
        duration: Long,
        caloriesBurned: Long?,
        distance: Double?,
        notes: String?
    ) {
        viewModelScope.launch {
            try {
                repository.updateWorkout(
                    id = id,
                    type = type,
                    duration = duration,
                    caloriesBurned = caloriesBurned,
                    distance = distance,
                    notes = notes
                ).collect { workout ->
                    loadWorkouts() // Refresh the list after updating
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to update workout")
            }
        }
    }

    fun deleteWorkout(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteWorkout(id).collect { success ->
                    if (success) {
                        loadWorkouts() // Refresh the list after deleting
                    } else {
                        _uiState.value = UiState.Error("Failed to delete workout")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to delete workout")
            }
        }
    }
}
