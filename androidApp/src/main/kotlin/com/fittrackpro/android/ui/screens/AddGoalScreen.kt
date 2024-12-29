package com.fittrackpro.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fittrackpro.android.ui.viewmodels.GoalViewModel
import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.GoalStatus
import com.fittrackpro.shared.domain.model.GoalType
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalScreen(
    navController: NavController,
    viewModel: GoalViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    var goalType by remember { mutableStateOf(GoalType.WORKOUT_COUNT) }
    var targetDate by remember { mutableStateOf(
        Clock.System.now()
            .plus(30, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    ) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Goal") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Goal Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 3
            )

            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = { }
            ) {
                OutlinedTextField(
                    value = goalType.name.replace("_", " "),
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Goal Type") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) }
                )

                ExposedDropdownMenu(
                    expanded = false,
                    onDismissRequest = { }
                ) {
                    GoalType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name.replace("_", " ")) },
                            onClick = { goalType = type }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = target,
                onValueChange = { target = it },
                label = { 
                    Text(
                        when (goalType) {
                            GoalType.WORKOUT_COUNT -> "Number of Workouts"
                            GoalType.WORKOUT_MINUTES -> "Total Minutes"
                            GoalType.CALORIE_BURN -> "Total Calories"
                            GoalType.STRENGTH_SESSIONS -> "Number of Sessions"
                            GoalType.CARDIO_SESSIONS -> "Number of Sessions"
                        }
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = targetDate.toString(),
                onValueChange = { },
                label = { Text("Target Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                    }
                }
            )

            if (showError) {
                Text(
                    text = "Please fill in all required fields",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                onClick = {
                    if (title.isBlank() || description.isBlank() || target.isBlank()) {
                        showError = true
                        return@Button
                    }

                    val goal = Goal(
                        id = 0L, // This will be set by SQLDelight
                        userId = 1L, // Using default user ID
                        title = title,
                        description = description,
                        type = goalType,
                        targetValue = target.toDoubleOrNull() ?: 0.0,
                        currentValue = 0.0,
                        status = GoalStatus.IN_PROGRESS,
                        startDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
                        endDate = Clock.System.now()
                            .plus(30, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date,
                        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
                        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                    )
                    viewModel.addGoal(goal)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Goal")
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = targetDate.toEpochDays() * 24L * 60L * 60L * 1000L
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            targetDate = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .date
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
