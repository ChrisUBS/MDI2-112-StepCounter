package com.example.mdi2_112_stepcounter.presentation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.core.content.ContextCompat
import com.example.mdi2_112_stepcounter.presentation.notifications.FitnessNotificationManager
import com.example.mdi2_112_stepcounter.presentation.navigation.ScreenRoutes
import com.example.mdi2_112_stepcounter.presentation.navigation.SwipeNavigationContainer
import com.example.mdi2_112_stepcounter.presentation.screens.DailyProgressScreen
import com.example.mdi2_112_stepcounter.presentation.screens.HeartRateScreen
import com.example.mdi2_112_stepcounter.presentation.screens.ModifyGoalScreen
import com.example.mdi2_112_stepcounter.presentation.viewmodel.MainViewModel

@Composable
fun WearFitnessApp(
    viewModel: MainViewModel,
    notificationManager: FitnessNotificationManager,
    hasHeartRateSensor: Boolean
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState

    var heartRateNotificationSent by remember { mutableStateOf(false) }
    var stepsNotificationSent by remember { mutableStateOf(false) }
    var notificationPermissionGranted by remember {
        mutableStateOf(
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        notificationPermissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !notificationPermissionGranted
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(uiState.heartRate, uiState.steps, notificationPermissionGranted) {
        if (
            uiState.heartRate >= 100 &&
            !heartRateNotificationSent &&
            notificationPermissionGranted
        ) {
            notificationManager.showNotification(
                notificationId = com.example.mdi2_112_stepcounter.presentation.notifications.NotificationIds.HEART_RATE_NOTIFICATION_ID,
                title = "Heart Rate Alert",
                message = "Your heart rate is ${uiState.heartRate} BPM or higher!"
            )
            heartRateNotificationSent = true
        }

        if (uiState.heartRate < 100) {
            heartRateNotificationSent = false
        }

        if (
            uiState.steps >= 1000 &&
            !stepsNotificationSent &&
            notificationPermissionGranted
        ) {
            notificationManager.showNotification(
                notificationId = com.example.mdi2_112_stepcounter.presentation.notifications.NotificationIds.STEPS_NOTIFICATION_ID,
                title = "Step Count Achieved!",
                message = "Congratulations! You've reached ${uiState.steps} steps."
            )
            stepsNotificationSent = true
        }

        if (uiState.steps < 1000) {
            stepsNotificationSent = false
        }
    }

    SwipeNavigationContainer { navController ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoutes.PROGRESS
        ) {
            composable(ScreenRoutes.PROGRESS) {
                DailyProgressScreen(
                    steps = uiState.steps,
                    calories = uiState.calories,
                    stepsGoal = uiState.stepsGoal,
                    caloriesGoal = uiState.caloriesGoal,
                    onAddStep = viewModel::addStep
                )
            }
            composable(ScreenRoutes.HEART) {
                HeartRateScreen(
                    heartRate = uiState.heartRate,
                    hasHeartRateSensor = hasHeartRateSensor
                )
            }
            composable(ScreenRoutes.GOALS) {
                ModifyGoalScreen(
                    stepsGoal = uiState.stepsGoal,
                    caloriesGoal = uiState.caloriesGoal,
                    onAddStep = viewModel::increaseStepsGoal,
                    onRemoveStep = viewModel::decreaseStepsGoal,
                    onAddCalories = viewModel::increaseCaloriesGoal,
                    onRemoveCalories = viewModel::decreaseCaloriesGoal
                )
            }
        }
    }
}
