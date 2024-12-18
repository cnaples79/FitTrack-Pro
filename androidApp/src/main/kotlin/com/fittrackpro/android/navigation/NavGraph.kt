package com.fittrackpro.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Workouts : Screen("workouts")
    object AddWorkout : Screen("add_workout")
    object Goals : Screen("goals")
    object AddGoal : Screen("add_goal")
    object Profile : Screen("profile")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Workouts.route) {
            WorkoutsScreen(navController)
        }
        composable(Screen.AddWorkout.route) {
            AddWorkoutScreen(navController)
        }
        composable(Screen.Goals.route) {
            GoalsScreen(navController)
        }
        composable(Screen.AddGoal.route) {
            AddGoalScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}
