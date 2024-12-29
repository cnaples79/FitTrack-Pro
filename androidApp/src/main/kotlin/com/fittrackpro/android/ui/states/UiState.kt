package com.fittrackpro.android.ui.states

import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.Profile
import com.fittrackpro.shared.domain.model.UserStats
import com.fittrackpro.shared.domain.model.Workout

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

typealias GoalUiState = UiState<List<Goal>>
typealias WorkoutUiState = UiState<List<Workout>>
typealias ProfileUiState = UiState<Profile>
typealias StatsUiState = UiState<UserStats>
