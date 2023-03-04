package com.example.jetweather.ui.presentation.screens.main_weather_screen

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.jetweather.MainActivity
import com.example.jetweather.domain.mapper.getForecastForDay
import com.example.jetweather.ui.presentation.screens.main_screen.MainViewModel
import com.example.jetweather.ui.presentation.screens.onboard.CustomDialogUI
import com.example.jetweather.ui.presentation.screens.onboard.sendToAppSetting
import com.example.jetweather.ui.theme.ColorPalate
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainWeatherScreen(
    navController: NavController,
    viewModel: MainViewModel
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val state = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    state.launchMultiplePermissionRequest()
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    PermissionsRequired(
        multiplePermissionsState = state,
        permissionsNotGrantedContent = {
            CustomAlertDialog()
        },
        permissionsNotAvailableContent = {
            CustomAlertDialog()
        }
    ) {
        LaunchedEffect(key1 = Unit) {
            viewModel.loadData()
        }

        val userDataState = viewModel.userData.collectAsState().value
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        val userLoc = userDataState.userData?.location
        val hourlyForecast = userDataState.userData?.hourlyForecast
        val dailyForecast = userDataState.userData?.dailyForecast

        val swipeRefreshState = rememberSwipeRefreshState(
            isRefreshing = (userDataState.isLoading || hourlyForecast.isNullOrEmpty()
                    || userLoc == null || userLoc.city.isEmpty()
                    || dailyForecast.isNullOrEmpty()
                    ) && userDataState.error.isEmpty()
        )


        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.loadData().also {
                    Toast.makeText(context, "Weather is up to date.", Toast.LENGTH_SHORT).show()
                }
            },
            indicator = { state, dis ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = dis,
                    backgroundColor = Color.Black,
                    contentColor = ColorPalate.BabyPink
                )
            }
        ) {
            Scaffold(
                scaffoldState = scaffoldState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = MainActivity.BOTTOM_PADDING.dp)
            ) {

                if (userDataState.error.isNotEmpty()) {
                    LaunchedEffect(key1 = Unit) {
                        scope.launch {
                            val action = scaffoldState.snackbarHostState.showSnackbar(
                                message = userDataState.error,
                                actionLabel = "Retry",
                                duration = SnackbarDuration.Long
                            )
                            if (action == SnackbarResult.ActionPerformed) {
                                viewModel.loadData()
                            }
                        }
                    }

                }

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (!hourlyForecast.isNullOrEmpty() && userLoc != null && userLoc.city.isNotEmpty() && !dailyForecast.isNullOrEmpty()) {
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInHorizontally(),
                            exit = slideOutHorizontally()
                        ) {
                            WeatherScreen(
                                hourlyForecasts = hourlyForecast.getForecastForDay(),
                                userLocation = userLoc,
                                dailyForecast = dailyForecast
                            )
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun CustomAlertDialog() {
    val context = LocalContext.current
    Dialog(onDismissRequest = { /*TODO*/ }) {
        CustomDialogUI() {
            context.sendToAppSetting()
        }
    }
}

@Composable
fun LoadingBox() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = ColorPalate.BabyPink)
    }
}