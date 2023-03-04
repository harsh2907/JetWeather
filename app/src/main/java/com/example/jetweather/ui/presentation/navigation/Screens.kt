package com.example.jetweather.ui.presentation.navigation

sealed class Screens(val route:String) {
    object OnBoardingScreen:Screens("ONBOARDING_SCREEN")
    object MainScreen:Screens("MAIN_SCREEN")
    object Home:Screens("Home")
    object Favourite:Screens("Favourite")
    object Settings:Screens("Settings")
    object SearchResult:Screens("SearchResult")




}