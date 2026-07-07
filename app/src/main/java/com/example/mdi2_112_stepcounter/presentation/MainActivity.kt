package com.example.mdi2_112_stepcounter.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.example.mdi2_112_stepcounter.presentation.theme.MDI2112StepCounterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MDI2112StepCounterTheme {
                StepCounterScreen()
            }
        }
    }
}

@Composable
fun StepCounterScreen() {
    var steps by remember { mutableIntStateOf(30) }
    val calories = 120
    val stepsGoal = 10000
    val caloriesGoal = 500

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Steps:",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = steps.toString(),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Calories:",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "$calories kcal",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Daily Goal:",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "$stepsGoal Steps / $caloriesGoal kcal",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { steps++ }
        ) {
            Text(
                text = "Add Step",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StepCounterScreenPreview() {
    MDI2112StepCounterTheme {
        StepCounterScreen()
    }
}