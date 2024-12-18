package com.fittrackpro.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittrackpro.shared.domain.model.Workout
import com.fittrackpro.shared.domain.model.WorkoutType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class WorkoutViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<WorkoutUiState>(WorkoutUiState.Loading)
    val uiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts.asStateFlow()

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual repository call
                val mockWorkouts = createMockWorkouts()
                _workouts.value = mockWorkouts
                _uiState.value = WorkoutUiState.Success
            } catch (e: Exception) {
                _uiState.value = WorkoutUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun addWorkout(workout: Workout) {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual repository call
                val currentWorkouts = _workouts.value.toMutableList()
                currentWorkouts.add(workout)
                _workouts.value = currentWorkouts
            } catch (e: Exception) {
                _uiState.value = WorkoutUiState.Error(e.message ?: "Failed to add workout")
            }
        }
    }

    private fun createMockWorkouts(): List<Workout> {
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        
        return listOf(
            Workout(
                id = "1",
                name = "Morning Run",
                type = WorkoutType.CARDIO,
                duration = 30,
                caloriesBurned = 300,
                date = localDateTime,
                notes = "Great morning run!"
            ),
            Workout(
                id = "2",
                name = "Weight Training",
                type = WorkoutType.STRENGTH,
                duration = 45,
                caloriesBurned = 200,
                date = localDateTime,
                notes = "Upper body focus"
            )
        )
    }
}

sealed class WorkoutUiState {
    object Loading : WorkoutUiState()
    object Success : WorkoutUiState()
    data class Error(val message: String) : WorkoutUiState()
}
