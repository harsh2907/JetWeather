package com.example.jetweather.ui.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jetweather.ui.presentation.screens.main_weather_screen.MainWeatherScreen
import com.example.jetweather.ui.presentation.screens.onboard.OnBoardingScreen

@Composable
fun WeatherNavigation(
    navController: NavHostController,
    isFirstTime:Boolean
    ) {
    val startRoute = if(isFirstTime) Screens.OnBoardingScreen.route else Screens.MainScreen.route

    NavHost(navController = navController, startDestination = startRoute){
        composable(route = Screens.OnBoardingScreen.route){
            OnBoardingScreen(navController = navController)
        }
        composable(route = Screens.MainScreen.route){
            MainWeatherScreen(navController = navController)
        }
    }
}

