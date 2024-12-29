package com.fittrackpro.android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.GoalType
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AddGoalViewModel : ViewModel() {
    fun addGoal(
        title: String,
        description: String,
        targetValue: Double,
        type: GoalType,
        deadline: LocalDate
    ) {
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
                // TODO: Save goal to repository
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
}
