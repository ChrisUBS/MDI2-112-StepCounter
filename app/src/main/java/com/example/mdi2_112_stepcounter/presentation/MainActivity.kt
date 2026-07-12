package com.example.mdi2_112_stepcounter.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.example.mdi2_112_stepcounter.presentation.theme.MDI2112StepCounterTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService

const val CHANNEL_ID = "fitness_alerts"
const val HEART_RATE_NOTIFICATION_ID = 1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)
        setContent {
            MDI2112StepCounterTheme {
                WearFitnessApp()
            }
        }
    }
}

private fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        CHANNEL_ID,
        "Fitness Alerts",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "Heart-rate and activity reminders"
    }

    val notificationManager = getSystemService(context, NotificationManager::class.java)
    notificationManager?.createNotificationChannel(channel)
}

@Composable
fun WearFitnessApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    var steps by remember { mutableIntStateOf(30) }
    var heartRate by remember { mutableIntStateOf(78) }
    var calories by remember { mutableIntStateOf(25) }
    var stepsGoal by remember { mutableIntStateOf(10000) }
    var caloriesGoal by remember { mutableIntStateOf(800) }

    var heartRateNotificationSent by remember {
        mutableStateOf(false)
    }
    var notificationPermissionGranted by remember {
        mutableStateOf(
            Build.VERSION.SDK_INT <
                    Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        notificationPermissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !notificationPermissionGranted
        ) {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    LaunchedEffect(
        heartRate,
        notificationPermissionGranted
    ) {
        if (
            heartRate >= 100 &&
            !heartRateNotificationSent &&
            notificationPermissionGranted
        ) {
            showNotification(
                context = context,
                notificationId = HEART_RATE_NOTIFICATION_ID,
                title = "Heart Rate Alert",
                message ="Your heart rate is $heartRate BPM or higher!"
            )
            heartRateNotificationSent = true
        }

        if (heartRate < 100) {
            heartRateNotificationSent = false
        }
    }

    SwipeNavigationContainer(
        navController = navController
    ) {
        NavHost(
            navController = navController,
            startDestination = "progress"
        ) {

            composable("progress") {
                DailyProgressScreen(
                    steps = steps,
                    calories = calories,
                    stepsGoal = stepsGoal,
                    caloriesGoal = caloriesGoal,
                    onAddStep = {
                        steps++
                        calories++
                    }
                )
            }

            composable("heart") {
                HeartRateScreen(
                    heartRate = heartRate,
                    onAddToHeartRate = { heartRate++ },
                    onRemoveFromHeartRate = { heartRate-- }
                )
            }

            composable("goals") {
                ModifyGoalScreen(
                    stepsGoal = stepsGoal,
                    caloriesGoal = caloriesGoal,
                    onAddStep = { stepsGoal += 100 },
                    onRemoveStep = { stepsGoal -= 100 },
                    onAddCalories = { caloriesGoal += 50 },
                    onRemoveCalories = { caloriesGoal -= 50 }
                )
            }
        }
    }
}

@Composable
fun SwipeNavigationContainer(
    navController: NavHostController,
     content: @Composable () -> Unit
) {
    val routes = listOf("progress", "heart", "goals")
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: "progress"
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
                        // Move Forward
                        if (
                            totalDrag < -60 &&
                            currentIndex < routes.lastIndex
                        ) {
                            navController.navigate(
                                routes[currentIndex + 1]
                            ) {
                                launchSingleTop = true
                            }
                        }
                        // Move Backward
                        if (
                            totalDrag > 60 &&
                            currentIndex > 0
                        ) {
                            navController.navigate(
                                routes[currentIndex - 1]
                            ) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun DailyProgressScreen(
    steps: Int,
    calories: Int,
    stepsGoal: Int,
    caloriesGoal: Int,
    onAddStep: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Daily Progress",
            color = Color.Green,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Steps:",
            color = Color.Cyan,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "${steps.toString()} / $stepsGoal",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Calories:",
            color = Color.Magenta,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "$calories / $caloriesGoal kcal",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAddStep
        ) {
            Text(
                text = "Add Step",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun HeartRateScreen(
    heartRate: Int,
    onAddToHeartRate: () -> Unit,
    onRemoveFromHeartRate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Heart Rate",
            color = Color.Yellow,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onRemoveFromHeartRate() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "-",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "$heartRate BPM ❤️",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { onAddToHeartRate() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun ModifyGoalScreen(
    stepsGoal: Int,
    caloriesGoal: Int,
    onAddStep: () -> Unit,
    onRemoveStep: () -> Unit,
    onAddCalories: () -> Unit,
    onRemoveCalories: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Modify Goal",
            color = Color.Yellow,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Steps",
            color = Color.Cyan,
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onRemoveStep() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "-",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "$stepsGoal",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { onAddStep() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Calories",
            color = Color.Cyan,
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onRemoveCalories() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "-",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "$caloriesGoal",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { onAddCalories() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

fun showNotification(
    context: Context,
    notificationId: Int,
    title: String,
    message: String
) {
    if (
        Build.VERSION.SDK_INT >=
        Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    val notification =
        NotificationCompat.Builder(
            context,
            CHANNEL_ID
        ).setSmallIcon(
            android.R.drawable.ic_dialog_info
        )
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

    NotificationManagerCompat
        .from(context)
        .notify(notificationId, notification)
}

@Preview(showBackground = true)
@Composable
fun StepCounterScreenPreview() {
    MDI2112StepCounterTheme {
        WearFitnessApp()
    }
}

@Preview(showBackground = true)
@Composable
fun HeartRateScreenPreview() {
    MDI2112StepCounterTheme {
        HeartRateScreen(
            heartRate = 78,
            onAddToHeartRate = {},
            onRemoveFromHeartRate = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ModifyGoalScreenPreview() {
    MDI2112StepCounterTheme {
        ModifyGoalScreen(
            stepsGoal = 10000,
            caloriesGoal = 800,
            onAddStep = {},
            onRemoveStep = {},
            onAddCalories = {},
            onRemoveCalories = {}
        )
    }
}