package com.fittrackpro.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import com.fittrackpro.shared.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    onDismissRequest: () -> Unit,
    profile: Profile,
    onSave: (Profile) -> Unit
) {
    var email by remember { mutableStateOf(profile.email) }
    var name by remember { mutableStateOf(profile.name) }
    var gender by remember { mutableStateOf(profile.gender) }
    var fitnessLevel by remember { mutableStateOf(profile.fitnessLevel) }
    var weeklyGoal by remember { mutableStateOf(profile.weeklyGoal.toString()) }
    var preferredWorkoutTypes by remember { mutableStateOf(profile.preferredWorkoutTypes) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Edit Profile") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = { }
                ) {
                    OutlinedTextField(
                        value = gender.name,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Gender") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) }
                    )

                    ExposedDropdownMenu(
                        expanded = false,
                        onDismissRequest = { }
                    ) {
                        Gender.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name) },
                                onClick = { gender = type }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = { }
                ) {
                    OutlinedTextField(
                        value = fitnessLevel.name,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Fitness Level") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) }
                    )

                    ExposedDropdownMenu(
                        expanded = false,
                        onDismissRequest = { }
                    ) {
                        FitnessLevel.values().forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level.name) },
                                onClick = { fitnessLevel = level }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = weeklyGoal,
                    onValueChange = { weeklyGoal = it },
                    label = { Text("Weekly Workout Goal") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Preferred Workout Types")
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WorkoutType.values().forEach { type ->
                        FilterChip(
                            selected = type in preferredWorkoutTypes,
                            onClick = {
                                preferredWorkoutTypes = if (type in preferredWorkoutTypes) {
                                    preferredWorkoutTypes - type
                                } else {
                                    preferredWorkoutTypes + type
                                }
                            },
                            label = { Text(type.name) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        profile.copy(
                            email = email,
                            name = name,
                            gender = gender,
                            fitnessLevel = fitnessLevel,
                            weeklyGoal = weeklyGoal.toIntOrNull() ?: 0,
                            preferredWorkoutTypes = preferredWorkoutTypes
                        )
                    )
                    onDismissRequest()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val rows = mutableListOf<List<Placeable>>()
        var currentRow = mutableListOf<Placeable>()
        var currentRowWidth = 0

        measurables.forEach { measurable ->
            val placeable = measurable.measure(constraints.copy(minWidth = 0))
            if (currentRowWidth + placeable.width > constraints.maxWidth) {
                rows.add(currentRow.toList())
                currentRow = mutableListOf()
                currentRowWidth = 0
            }
            currentRow.add(placeable)
            currentRowWidth += placeable.width
        }
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow.toList())
        }

        val height = rows.sumOf { row -> row.maxOf { it.height } }
        layout(constraints.maxWidth, height) {
            var y = 0
            rows.forEach { row ->
                var x = when (horizontalArrangement) {
                    Arrangement.Center -> (constraints.maxWidth - row.sumOf { it.width }) / 2
                    Arrangement.End -> constraints.maxWidth - row.sumOf { it.width }
                    else -> 0
                }
                row.forEach { placeable ->
                    placeable.place(x = x, y = y)
                    x += placeable.width
                }
                y += row.maxOf { it.height }
            }
        }
    }
}
