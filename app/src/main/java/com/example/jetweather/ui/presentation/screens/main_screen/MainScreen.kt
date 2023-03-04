package com.example.jetweather.ui.presentation.screens.main_screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetweather.ui.presentation.navigation.MainNavigation
import com.example.jetweather.ui.presentation.screens.favorite_screen.SearchViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            val currentScreen = navController.currentBackStackEntryAsState().value?.destination?.route
            AnimatedVisibility(visible = bottomNavItems.map { it.route }.contains(currentScreen)) {
                CustomBottomNavBar(navController = navController)
            }
        }
    ) {
        MainNavigation(navController)
    }
}

