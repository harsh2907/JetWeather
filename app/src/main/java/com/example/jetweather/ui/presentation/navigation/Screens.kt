package com.example.jetweather.ui.presentation.navigation

sealed class Screens(val route:String) {
    object OnBoardingScreen:Screens("ONBOARDING_SCREEN")
    object MainScreen:Screens("MAIN_SCREEN")
}