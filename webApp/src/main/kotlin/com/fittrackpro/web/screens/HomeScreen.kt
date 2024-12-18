package com.fittrackpro.web.screens

import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.domain.model.Workout
import com.fittrackpro.shared.presentation.GoalViewModel
import com.fittrackpro.shared.presentation.WorkoutViewModel
import emotion.react.css
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.div
import web.cssom.*

val HomeScreen = FC {
    var workouts by useState<List<Workout>>(emptyList())
    var goals by useState<List<Goal>>(emptyList())
    var isLoading by useState(false)
    
    Grid {
        container = true
        spacing = responsive(3)
        
        Grid {
            item = true
            xs = 12
            Typography {
                variant = TypographyVariant.h4
                +"Dashboard"
            }
        }
        
        // Quick Stats
        Grid {
            item = true
            xs = 12
            md = 4
            
            Paper {
                sx {
                    padding = 16.px
                }
                Typography {
                    variant = TypographyVariant.h6
                    +"Recent Activity"
                }
                
                if (isLoading) {
                    CircularProgress()
                } else {
                    List {
                        workouts.take(5).forEach { workout ->
                            ListItem {
                                ListItemText {
                                    primary = ReactNode(workout.name)
                                    secondary = ReactNode("${workout.duration} min | ${workout.caloriesBurned} cal")
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Active Goals
        Grid {
            item = true
            xs = 12
            md = 4
            
            Paper {
                sx {
                    padding = 16.px
                }
                Typography {
                    variant = TypographyVariant.h6
                    +"Active Goals"
                }
                
                if (isLoading) {
                    CircularProgress()
                } else {
                    List {
                        goals.filter { !it.completed }.take(5).forEach { goal ->
                            ListItem {
                                ListItemText {
                                    primary = ReactNode(goal.title)
                                    secondary = ReactNode("Progress: ${goal.progress}/${goal.target}")
                                }
                                LinearProgress {
                                    variant = LinearProgressVariant.determinate
                                    value = (goal.progress.toFloat() / goal.target.toFloat() * 100).toDouble()
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Workout Summary
        Grid {
            item = true
            xs = 12
            md = 4
            
            Paper {
                sx {
                    padding = 16.px
                }
                Typography {
                    variant = TypographyVariant.h6
                    +"Workout Summary"
                }
                
                if (isLoading) {
                    CircularProgress()
                } else {
                    // Add workout summary chart here
                }
            }
        }
    }
}
