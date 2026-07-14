package com.example.mdi2_112_stepcounter.presentation.model

data class MainUiState(
    val steps: Int = 30,
    val calories: Int = 25,
    val stepsGoal: Int = 10_000,
    val caloriesGoal: Int = 800,
    val heartRate: Int = 72
)
