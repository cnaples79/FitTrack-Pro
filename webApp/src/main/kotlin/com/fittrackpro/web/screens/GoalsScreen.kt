package com.fittrackpro.web.screens

import com.fittrackpro.shared.domain.model.Goal
import com.fittrackpro.shared.presentation.GoalViewModel
import emotion.react.css
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.div
import web.cssom.*

val GoalsScreen = FC {
    var goals by useState<List<Goal>>(emptyList())
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
                +"Goals"
            }
            
            Button {
                variant = ButtonVariant.contained
                onClick = { showAddDialog = true }
                +"Add Goal"
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
            goals.forEach { goal ->
                Grid {
                    item = true
                    xs = 12
                    md = 6
                    
                    Card {
                        CardContent {
                            Typography {
                                variant = TypographyVariant.h6
                                +goal.title
                            }
                            Typography {
                                color = TypographyColor.textSecondary
                                +goal.type
                            }
                            div {
                                css {
                                    marginTop = 8.px
                                    marginBottom = 8.px
                                }
                                Typography {
                                    +"Progress: ${goal.progress}/${goal.target}"
                                }
                            }
                            LinearProgress {
                                variant = LinearProgressVariant.determinate
                                value = (goal.progress.toFloat() / goal.target.toFloat() * 100).toDouble()
                            }
                            if (!goal.description.isNullOrEmpty()) {
                                Typography {
                                    css {
                                        marginTop = 8.px
                                    }
                                    +goal.description
                                }
                            }
                        }
                        CardActions {
                            Button {
                                size = Size.small
                                +"Update Progress"
                            }
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
    
    // Add Goal Dialog
    if (showAddDialog) {
        Dialog {
            open = true
            onClose = { showAddDialog = false }
            
            DialogTitle {
                +"Add New Goal"
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
