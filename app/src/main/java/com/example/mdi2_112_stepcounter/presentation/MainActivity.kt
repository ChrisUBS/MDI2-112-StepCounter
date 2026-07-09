package com.example.mdi2_112_stepcounter.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.example.mdi2_112_stepcounter.presentation.theme.MDI2112StepCounterTheme
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import androidx.compose.foundation.gestures.detectHorizontalDragGestures
//import androidx.compose.foundation.layout.Box
//import androidx.compose.ui.input.pointer.pointerInput

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MDI2112StepCounterTheme {
                WearFitnessApp()
            }
        }
    }
}

@Composable
fun WearFitnessApp() {
    var steps by remember { mutableIntStateOf(30) }
    var calories by remember { mutableIntStateOf(25) }
    var stepsGoal by remember { mutableIntStateOf(10000) }
    var caloriesGoal by remember { mutableIntStateOf(800) }

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
    HeartRateScreen()
    ModifyGoalScreen(
        stepsGoal = stepsGoal,
        caloriesGoal = caloriesGoal,
        onAddStep = { stepsGoal += 100 },
        onRemoveStep = { stepsGoal -= 100 },
        onAddCalories = { caloriesGoal += 50 },
        onRemoveCalories = { caloriesGoal -= 50 }
    )
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
fun HeartRateScreen() {
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
            color = Color.Red,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "78 BPM ❤️",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
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
        HeartRateScreen()
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