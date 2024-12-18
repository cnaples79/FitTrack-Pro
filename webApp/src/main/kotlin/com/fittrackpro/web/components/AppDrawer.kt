package com.fittrackpro.web.components

import emotion.react.css
import mui.icons.material.*
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.div
import react.router.dom.NavLink
import react.router.useNavigate
import web.cssom.*

val AppDrawer = FC {
    val navigate = useNavigate()
    
    Drawer {
        variant = DrawerVariant.permanent
        sx {
            width = 240.px
            flexShrink = number(0.0)
        }
        
        Toolbar {
            Typography {
                variant = TypographyVariant.h6
                +"FitTrack Pro"
            }
        }
        
        Divider()
        
        List {
            ListItem {
                disablePadding = true
                ListItemButton {
                    onClick = { navigate("/") }
                    ListItemIcon { Home() }
                    ListItemText { +"Home" }
                }
            }
            
            ListItem {
                disablePadding = true
                ListItemButton {
                    onClick = { navigate("/workouts") }
                    ListItemIcon { FitnessCenter() }
                    ListItemText { +"Workouts" }
                }
            }
            
            ListItem {
                disablePadding = true
                ListItemButton {
                    onClick = { navigate("/goals") }
                    ListItemIcon { Flag() }
                    ListItemText { +"Goals" }
                }
            }
            
            ListItem {
                disablePadding = true
                ListItemButton {
                    onClick = { navigate("/profile") }
                    ListItemIcon { Person() }
                    ListItemText { +"Profile" }
                }
            }
        }
    }
}
