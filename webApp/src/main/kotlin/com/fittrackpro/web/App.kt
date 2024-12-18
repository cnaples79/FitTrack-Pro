package com.fittrackpro.web

import com.fittrackpro.web.components.*
import emotion.react.css
import mui.material.*
import mui.material.styles.ThemeProvider
import mui.material.styles.createTheme
import react.*
import react.dom.html.ReactHTML.div
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter
import web.cssom.*

val theme = createTheme {
    palette {
        primary {
            main = "#1976d2"
        }
        secondary {
            main = "#dc004e"
        }
    }
}

val App = FC {
    ThemeProvider {
        this.theme = theme
        
        CssBaseline()
        
        BrowserRouter {
            div {
                css {
                    display = Display.flex
                    minHeight = 100.vh
                }
                
                AppDrawer()
                
                div {
                    css {
                        flexGrow = number(1.0)
                        padding = 24.px
                    }
                    
                    Routes {
                        Route {
                            path = "/"
                            element = HomeScreen.create()
                        }
                        Route {
                            path = "/workouts"
                            element = WorkoutsScreen.create()
                        }
                        Route {
                            path = "/goals"
                            element = GoalsScreen.create()
                        }
                        Route {
                            path = "/profile"
                            element = ProfileScreen.create()
                        }
                    }
                }
            }
        }
    }
}
