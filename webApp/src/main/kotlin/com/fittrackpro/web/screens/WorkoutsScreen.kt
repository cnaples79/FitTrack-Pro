package com.fittrackpro.web.screens

import com.fittrackpro.shared.domain.model.Workout
import com.fittrackpro.shared.presentation.WorkoutViewModel
import emotion.react.css
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.div
import web.cssom.*

val WorkoutsScreen = FC {
    var workouts by useState<List<Workout>>(emptyList())
    var isLoading by useState(false)
    var showAddDialog by useState(false)
    
    Grid {
        container = true
        spacing = responsive(3)
        
        Grid {
            item = true
            xs = 12
            sx {
                display = Display.flex
                justifyContent = JustifyContent.spaceBetween
                alignItems = AlignItems.center
            }
            
            Typography {
                variant = TypographyVariant.h4
                +"Workouts"
            }
            
            Button {
                variant = ButtonVariant.contained
                onClick = { showAddDialog = true }
                +"Add Workout"
            }
        }
        
        if (isLoading) {
            Grid {
                item = true
                xs = 12
                sx {
                    display = Display.flex
                    justifyContent = JustifyContent.center
                }
                CircularProgress()
            }
        } else {
            workouts.forEach { workout ->
                Grid {
                    item = true
                    xs = 12
                    md = 6
                    lg = 4
                    
                    Card {
                        CardContent {
                            Typography {
                                variant = TypographyVariant.h6
                                +workout.name
                            }
                            Typography {
                                color = TypographyColor.textSecondary
                                +workout.type
                            }
                            div {
                                css {
                                    marginTop = 8.px
                                }
                                Typography {
                                    +"Duration: ${workout.duration} minutes"
                                }
                                Typography {
                                    +"Calories: ${workout.caloriesBurned}"
                                }
                            }
                        }
                        CardActions {
                            Button {
                                size = Size.small
                                +"Edit"
                            }
                            Button {
                                size = Size.small
                                color = ButtonColor.error
                                +"Delete"
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Add Workout Dialog
    if (showAddDialog) {
        Dialog {
            open = true
            onClose = { showAddDialog = false }
            
            DialogTitle {
                +"Add New Workout"
            }
            
            DialogContent {
                // Add form fields here
            }
            
            DialogActions {
                Button {
                    onClick = { showAddDialog = false }
                    +"Cancel"
                }
                Button {
                    variant = ButtonVariant.contained
                    +"Save"
                }
            }
        }
    }
}
