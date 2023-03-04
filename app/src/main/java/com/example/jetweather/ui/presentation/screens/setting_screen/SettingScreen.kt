package com.example.jetweather.ui.presentation.screens.setting_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.jetweather.MainActivity
import com.example.jetweather.domain.mapper.HourlyForecast
import com.example.jetweather.domain.mapper.getForecastForDay
import com.example.jetweather.domain.models.entity.UserLocation
import com.example.jetweather.domain.utils.WeatherType
import com.example.jetweather.ui.presentation.screens.main_screen.MainViewModel
import com.example.jetweather.ui.sharedpreferences.SharedPreferenceUtils
import com.example.jetweather.ui.theme.ColorPalate
import com.example.jetweather.ui.theme.Roboto
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import java.time.LocalTime
import kotlin.text.Typography


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingScreen(
    viewModel: MainViewModel
) {
    val hourIndex = LocalTime.now().hour

    val userDataState = viewModel.userData.collectAsState().value
    val userData = userDataState.userData
    val hourlyForecast = userData?.hourlyForecast
    val userLoc = userData?.location

    val scaffoldState = rememberScaffoldState()

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = (userDataState.isLoading || hourlyForecast.isNullOrEmpty()
                || userLoc == null || userLoc.city.isEmpty()
                ) && userDataState.error.isEmpty()
    )
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
        Scaffold(scaffoldState = scaffoldState) {
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


            if (!hourlyForecast.isNullOrEmpty() && userLoc != null && userLoc.city.isNotEmpty() && hourlyForecast[0]?.get(
                    hourIndex
                ) != null
            ) {
                val forecast = hourlyForecast[0]?.get(hourIndex)!!
                AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    SettingScreenContent(
                        forecast,
                        userLoc
                    ){
                        viewModel.loadData()
                    }
                }
            }
        }
    }
}

@Composable
fun SettingScreenContent(
    hourlyForecast: HourlyForecast,
    userLocation: UserLocation,
    onSettingChanged:()->Unit
) {
    val weather = hourlyForecast.weatherType
    val temperature =
        "${hourlyForecast.temperature} ${hourlyForecast.hourlyUnits.temperature_180m}"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .scrollable(state = rememberScrollState(), orientation = Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.MyLocation,
                contentDescription = "location",
                tint = Color.LightGray,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "Your Location Now",
                color = Color.LightGray,
                style = MaterialTheme.typography.h6,
                fontFamily = Roboto
            )
        }

        Text(
            text = "${userLocation.city}, ${userLocation.state}, ${userLocation.country}",
            color = Color.White,
            fontFamily = Roboto,
            fontSize = 24.sp
        )

        Image(
            painter = painterResource(id = weather.iconRes),
            contentDescription = "weather",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            contentScale = ContentScale.FillHeight
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(bottom = 4.dp),
            backgroundColor = Color(0xff353361)
        ) {
            Text(
                text = weather.weatherDesc,
                modifier = Modifier.padding(8.dp),
                color = Color.White,
                fontFamily = Roboto,
                fontSize = 24.sp
            )
        }

        Text(
            text = temperature,
            color = Color.White,
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(12.dp),
            fontFamily = Roboto,
            fontWeight = FontWeight.W400
        )

        SettingsColumn(){
            onSettingChanged()
        }

    }
}


@Composable
fun SettingsColumn(
    onSettingChanged:()->Unit
) {
    val context = LocalContext.current

    val sharedPref = SharedPreferenceUtils(context)
    val tempOptionList = listOf("Celsius", "Fahrenheit")
    val windOptionList = listOf("km/h","m/s")
    var tempIndex by remember { mutableStateOf(sharedPref.getTemp()) }
    var windIndex by remember { mutableStateOf(sharedPref.getWind()) }


    var isTempOptionExpanded by remember { mutableStateOf(false) }
    var isWindOptionExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 10.dp, vertical = (MainActivity.BOTTOM_PADDING + 10).dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Temperature",
                style = MaterialTheme.typography.h6,
                fontFamily = Roboto,
                color = Color.White
            )

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .clickable {
                        isTempOptionExpanded = true
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tempOptionList[tempIndex],
                    style = MaterialTheme.typography.body1,
                    fontFamily = Roboto,
                    color = Color.LightGray
                )

                OptionsDropDownMenu(
                    isExpanded = isTempOptionExpanded,
                    items = tempOptionList,
                    onDismiss = {
                        isTempOptionExpanded = false
                    }) {
                    tempIndex = it
                    sharedPref.setTemp(it)
                    onSettingChanged()
                    isTempOptionExpanded = false
                }

                Spacer(modifier = Modifier.padding(horizontal = 6.dp))

                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.padding(12.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Wind",
                style = MaterialTheme.typography.h6,
                fontFamily = Roboto,
                color = Color.White,
            )

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .clickable {
                        isWindOptionExpanded = true
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = windOptionList[windIndex],
                    style = MaterialTheme.typography.body1,
                    fontFamily = Roboto,
                    color = Color.LightGray
                )

                OptionsDropDownMenu(
                    isExpanded = isWindOptionExpanded,
                    items = windOptionList,
                    onDismiss = {
                        isWindOptionExpanded = false
                    }) {
                    windIndex = it
                    sharedPref.setWind(it)
                    onSettingChanged()
                    isWindOptionExpanded = false
                }

                Spacer(modifier = Modifier.padding(horizontal = 6.dp))

                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}


@Composable
fun OptionsDropDownMenu(
    isExpanded: Boolean,
    items: List<String>,
    onDismiss: () -> Unit,
    onSelected: (Int) -> Unit
) {

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismiss
    ) {
        items.forEachIndexed { index, item ->
            DropdownMenuItem(onClick = {
                onSelected(index)
            }) {
                Text(text = item)
            }
        }
    }
}
