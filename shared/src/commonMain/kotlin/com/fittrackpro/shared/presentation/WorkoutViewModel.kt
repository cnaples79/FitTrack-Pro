package com.fittrackpro.shared.presentation

import com.fittrackpro.shared.domain.model.Workout
import com.fittrackpro.shared.domain.repository.WorkoutRepository
import com.fittrackpro.shared.util.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.fittrackpro.shared.util.CommonFlow
import com.fittrackpro.shared.util.asCommonFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WorkoutViewModel(
    private val repository: WorkoutRepository,
    private val dispatchers: Dispatchers
) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + dispatchers.main)
    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: CommonFlow<List<Workout>> = _workouts.asCommonFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: CommonFlow<Boolean> = _isLoading.asCommonFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: CommonFlow<String?> = _error.asCommonFlow()

    init {
        loadWorkouts()
    }

    fun loadWorkouts() {
        viewModelScope.launch(dispatchers.io) {
            _isLoading.value = true
            try {
                repository.getWorkouts().collect { workouts ->
                    _workouts.value = workouts
                    _error.value = null
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addWorkout(workout: Workout) {
        viewModelScope.launch(dispatchers.io) {
            try {
                repository.addWorkout(workout)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateWorkout(workout: Workout) {
        viewModelScope.launch(dispatchers.io) {
            try {
                repository.updateWorkout(workout)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteWorkout(id: String) {
        viewModelScope.launch(dispatchers.io) {
            try {
                repository.deleteWorkout(id)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
