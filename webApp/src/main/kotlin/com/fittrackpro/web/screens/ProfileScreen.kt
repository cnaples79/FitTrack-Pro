package com.fittrackpro.web.screens

import com.fittrackpro.shared.domain.model.UserProfile
import com.fittrackpro.shared.presentation.ProfileViewModel
import emotion.react.css
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.div
import web.cssom.*

val ProfileScreen = FC {
    var profile by useState<UserProfile?>(null)
    var isLoading by useState(false)
    var isEditing by useState(false)
    
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
                +"Profile"
            }
            
            if (profile != null) {
                Button {
                    variant = ButtonVariant.contained
                    onClick = { isEditing = true }
                    +"Edit Profile"
                }
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
        } else if (profile == null) {
            Grid {
                item = true
                xs = 12
                Paper {
                    sx {
                        padding = 24.px
                        textAlign = TextAlign.center
                    }
                    Typography {
                        variant = TypographyVariant.h6
                        +"Create Your Profile"
                    }
                    Button {
                        variant = ButtonVariant.contained
                        sx {
                            marginTop = 16.px
                        }
                        onClick = { isEditing = true }
                        +"Get Started"
                    }
                }
            }
        } else {
            Grid {
                item = true
                xs = 12
                md = 6
                
                Paper {
                    sx {
                        padding = 24.px
                    }
                    Typography {
                        variant = TypographyVariant.h6
                        +"Personal Information"
                    }
                    List {
                        ListItem {
                            ListItemText {
                                primary = ReactNode("Name")
                                secondary = ReactNode(profile?.name ?: "")
                            }
                        }
                        ListItem {
                            ListItemText {
                                primary = ReactNode("Age")
                                secondary = ReactNode(profile?.age?.toString() ?: "")
                            }
                        }
                        ListItem {
                            ListItemText {
                                primary = ReactNode("Height")
                                secondary = ReactNode("${profile?.height} cm")
                            }
                        }
                        ListItem {
                            ListItemText {
                                primary = ReactNode("Weight")
                                secondary = ReactNode("${profile?.weight} kg")
                            }
                        }
                    }
                }
            }
            
            Grid {
                item = true
                xs = 12
                md = 6
                
                Paper {
                    sx {
                        padding = 24.px
                    }
                    Typography {
                        variant = TypographyVariant.h6
                        +"Fitness Preferences"
                    }
                    List {
                        ListItem {
                            ListItemText {
                                primary = ReactNode("Activity Level")
                                secondary = ReactNode(profile?.activityLevel ?: "")
                            }
                        }
                        ListItem {
                            ListItemText {
                                primary = ReactNode("Weekly Goal")
                                secondary = ReactNode("${profile?.weeklyGoal} workouts")
                            }
                        }
                    }
                    Typography {
                        variant = TypographyVariant.subtitle1
                        sx {
                            marginTop = 16.px
                            marginBottom = 8.px
                        }
                        +"Preferred Workout Types"
                    }
                    div {
                        css {
                            display = Display.flex
                            flexWrap = FlexWrap.wrap
                            gap = 8.px
                        }
                        profile?.preferredWorkoutTypes?.forEach { type ->
                            Chip {
                                label = ReactNode(type)
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Edit Profile Dialog
    if (isEditing) {
        Dialog {
            open = true
            onClose = { isEditing = false }
            maxWidth = "md"
            fullWidth = true
            
            DialogTitle {
                +(if (profile == null) "Create Profile" else "Edit Profile")
            }
            
            DialogContent {
                // Add form fields here
            }
            
            DialogActions {
                Button {
                    onClick = { isEditing = false }
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
