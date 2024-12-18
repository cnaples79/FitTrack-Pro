package com.fittrackpro.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.fittrackpro.android.navigation.NavGraph
import com.fittrackpro.android.ui.theme.FitTrackProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitTrackProTheme {
                val navController = rememberNavController()
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(navController)
                        }
                    ) { paddingValues ->
                        NavGraph(
                            navController = navController,
                            modifier = Modifier.padding(paddingValues)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Workouts") },
            label = { Text("Workouts") },
            selected = currentRoute == Screen.Workouts.route,
            onClick = {
                navController.navigate(Screen.Workouts.route) {
                    launchSingleTop = true
                }
            }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Flag, contentDescription = "Goals") },
            label = { Text("Goals") },
            selected = currentRoute == Screen.Goals.route,
            onClick = {
                navController.navigate(Screen.Goals.route) {
                    launchSingleTop = true
                }
            }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == Screen.Profile.route,
            onClick = {
                navController.navigate(Screen.Profile.route) {
                    launchSingleTop = true
                }
            }
        )
    }
}