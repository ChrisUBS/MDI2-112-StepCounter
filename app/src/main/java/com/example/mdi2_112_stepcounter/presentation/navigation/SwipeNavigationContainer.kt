package com.example.mdi2_112_stepcounter.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

object ScreenRoutes {
    const val PROGRESS = "progress"
    const val HEART = "heart"
    const val GOALS = "goals"
}

@Composable
fun SwipeNavigationContainer(
    content: @Composable (NavHostController) -> Unit
) {
    val navController = rememberNavController()
    val routes = listOf(ScreenRoutes.PROGRESS, ScreenRoutes.HEART, ScreenRoutes.GOALS)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ScreenRoutes.PROGRESS
    val currentIndex = routes.indexOf(currentRoute)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(currentRoute) {
                var totalDrag = 0f
                detectHorizontalDragGestures(
                    onDragStart = {
                        totalDrag = 0f
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        totalDrag += dragAmount
                    },
                    onDragEnd = {
                        if (totalDrag < -60 && currentIndex < routes.lastIndex) {
                            navController.navigate(routes[currentIndex + 1]) {
                                launchSingleTop = true
                            }
                        }

                        if (totalDrag > 60 && currentIndex > 0) {
                            navController.navigate(routes[currentIndex - 1]) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        content(navController)
    }
}
