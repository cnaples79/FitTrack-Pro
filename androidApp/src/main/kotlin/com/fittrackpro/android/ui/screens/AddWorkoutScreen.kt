package com.fittrackpro.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fittrackpro.android.ui.viewmodels.WorkoutViewModel
import com.fittrackpro.shared.domain.model.Workout
import com.fittrackpro.shared.domain.model.WorkoutType
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(
    navController: NavController,
    viewModel: WorkoutViewModel = viewModel()
) {
    var workoutType by remember { mutableStateOf(WorkoutType.CARDIO) }
    var duration by remember { mutableStateOf("") }
    var caloriesBurned by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(Clock.System.now()) }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Workout") },
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
                value = workoutType.name,
                onValueChange = { },
                label = { Text("Workout Type") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) }
            )

            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = { }
            ) {
                ExposedDropdownMenu(
                    expanded = false,
                    onDismissRequest = { }
                ) {
                    WorkoutType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = { workoutType = type }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (minutes)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = caloriesBurned,
                onValueChange = { caloriesBurned = it },
                label = { Text("Calories Burned") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = distance,
                onValueChange = { distance = it },
                label = { Text("Distance") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
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
                    if (duration.isBlank() || caloriesBurned.isBlank()) {
                        showError = true
                        return@Button
                    }
                    
                    val workout = Workout(
                        id = 0L, // New workout, ID will be assigned by the database
                        userId = 1L, // TODO: Get from auth
                        type = workoutType.name,
                        duration = duration.toLongOrNull() ?: 0L,
                        caloriesBurned = caloriesBurned.toLongOrNull(),
                        distance = distance.toDoubleOrNull(),
                        date = date.toEpochMilliseconds(),
                        notes = notes.takeIf { it.isNotBlank() }
                    )
                    
                    viewModel.addWorkout(workout)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Workout")
            }
        }
    }
}
