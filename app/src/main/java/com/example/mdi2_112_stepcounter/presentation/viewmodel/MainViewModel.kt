package com.example.mdi2_112_stepcounter.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mdi2_112_stepcounter.presentation.model.MainUiState

class MainViewModel : ViewModel() {
    var uiState by mutableStateOf(MainUiState())
        private set

    fun addStep() {
        uiState = uiState.copy(
            steps = uiState.steps + 1,
            calories = uiState.calories + 1
        )
    }

    fun increaseStepsGoal() {
        uiState = uiState.copy(
            stepsGoal = uiState.stepsGoal + 100
        )
    }

    fun decreaseStepsGoal() {
        uiState = uiState.copy(
            stepsGoal = uiState.stepsGoal - 100
        )
    }

    fun increaseCaloriesGoal() {
        uiState = uiState.copy(
            caloriesGoal = uiState.caloriesGoal + 50
        )
    }

    fun decreaseCaloriesGoal() {
        uiState = uiState.copy(
            caloriesGoal = uiState.caloriesGoal - 50
        )
    }

    fun updateHeartRate(heartRate: Int) {
        uiState = uiState.copy(heartRate = heartRate)
    }
}
