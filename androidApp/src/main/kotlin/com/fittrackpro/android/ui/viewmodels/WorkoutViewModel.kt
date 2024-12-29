package com.fittrackpro.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittrackpro.android.ui.states.UiState
import com.fittrackpro.shared.domain.model.Workout
import com.fittrackpro.shared.domain.model.WorkoutType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.*

class WorkoutViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Workout>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Workout>>> = _uiState.asStateFlow()

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual repository call
                val workouts = listOf<Workout>() // Temporary empty list
                _uiState.value = UiState.Success(workouts)
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
                val workout = Workout(
                    id = 0, // This will be replaced by the database
                    userId = 1, // TODO: Get from auth
                    type = type,
                    duration = duration,
                    caloriesBurned = caloriesBurned,
                    distance = distance,
                    date = Clock.System.now().toEpochMilliseconds(),
                    notes = notes
                )
                // TODO: Add workout to repository
                loadWorkouts() // Reload workouts after adding
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
                val workout = Workout(
                    id = id,
                    userId = 1, // TODO: Get from auth
                    type = type,
                    duration = duration,
                    caloriesBurned = caloriesBurned,
                    distance = distance,
                    date = Clock.System.now().toEpochMilliseconds(),
                    notes = notes
                )
                // TODO: Update workout in repository
                loadWorkouts() // Reload workouts after updating
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to update workout")
            }
        }
    }

    fun deleteWorkout(workoutId: Long) {
        viewModelScope.launch {
            try {
                // TODO: Delete workout from repository
                loadWorkouts() // Reload workouts after deleting
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to delete workout")
            }
        }
    }
}
