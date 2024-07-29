package com.example.eventplanner

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eventplanner.ui.theme.EventPlannerTheme

class MainActivity : ComponentActivity() {
    private val sensorModel: SensorModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EventPlannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // beim Schütteln in den AddEvent Screen wechseln
                    sensorModel.onShake = {
                        navController.navigate("addEvent")
                    }

                    // Navigation zwischen den einzelnen Views
                    NavHost(navController, startDestination = "home") {
                        composable("home") { MainScreen(navController, context = LocalContext.current) }
                        composable("addEvent") { AddEventScreen(navController) }
                        composable("myEvents") { MyEventsScreen(context = LocalContext.current) }
                        composable("calendar") { CalendarScreen() }
                    }
                }
            }
        }
    }
}