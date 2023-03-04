package com.example.jetweather.ui.presentation.screens.favorite_screen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.jetweather.MainActivity
import com.example.jetweather.R
import com.example.jetweather.domain.mapper.HourlyForecast
import com.example.jetweather.domain.models.entity.UserLocation
import com.example.jetweather.ui.presentation.navigation.Screens
import com.example.jetweather.ui.presentation.screens.main_weather_screen.OtherDetailRowItem
import com.example.jetweather.ui.theme.ColorPalate
import com.example.jetweather.ui.theme.Roboto
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FavouriteScreen(
    navController: NavHostController,
    viewModel: SearchViewModel
) {
    val bookmarkedWeathers by viewModel.savedPlaceState.collectAsState()
    val savedPlaces by viewModel.getAllSavedPlaces().collectAsState(initial = emptyList())
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = bookmarkedWeathers.isLoading)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = viewModel::getSavedWeather,
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
            modifier = Modifier.padding(bottom = MainActivity.BOTTOM_PADDING.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Searchbar { query ->
                    navController.navigate(Screens.SearchResult.route + "/$query") {
                        launchSingleTop = true
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

                AnimatedContent(targetState = bookmarkedWeathers) { weatherState ->
                    when {
                        weatherState.isEmpty || savedPlaces.isEmpty() -> {
                            ErrorBox(message = "No saved places yet.")
                        }

                        weatherState.error.isNotEmpty() -> {
                            ErrorBox(message = weatherState.error)
                        }

                        else -> {
                            val sortedList = weatherState.savedPlaces
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                items(sortedList) { savedPlace ->
                                    SavedPlaceCard(
                                        userLocation = savedPlace.location,
                                        hourlyForecast = savedPlace.forecast
                                    ) {
                                        showDialog(
                                            context = context,
                                            title = "Remove ${it.city}",
                                            message = "Do you want to remove ${it.city} from favorites"
                                        ) {
                                            viewModel.removePlace(it)
                                            scope.launch {
                                                val action =
                                                    scaffoldState.snackbarHostState.showSnackbar(
                                                        message = "Location removed successfully",
                                                        actionLabel = "Undo"
                                                    )
                                                if (action == SnackbarResult.ActionPerformed) {
                                                    viewModel.savePlace(it)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


fun showDialog(
    context: Context,
    title: String,
    message: String,
    onYes: () -> Unit
) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("Yes") { d, _ ->
            onYes()
            d.cancel()
        }
        .setNegativeButton("No") { d, _ -> d.cancel() }
        .create()
        .show()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Searchbar(
    modifier: Modifier = Modifier,
    onSearchPerformed: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        placeholder = {
            Text(
                text = "Search",
                color = Color.LightGray
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "search_icon",
                tint = Color.LightGray
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = ColorPalate.NavyBlue.copy(alpha = .8f),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedLabelColor = Color.Transparent,
            unfocusedLabelColor = Color.Transparent,
            cursorColor = ColorPalate.BabyPink,
        ),
        keyboardActions = KeyboardActions(onSearch = {
            onSearchPerformed(searchQuery.trim())
            keyboard?.hide()
        }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}

@Composable
fun ErrorBox(
    message: String
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            fontFamily = Roboto,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedPlaceCard(
    modifier: Modifier = Modifier,
    userLocation: UserLocation,
    hourlyForecast: HourlyForecast,
    onLongClick: (UserLocation) -> Unit
) {

    Card(
        backgroundColor = ColorPalate.LightNavyBlue,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(12.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongClick(userLocation)
                    }
                )
            }
    ) {
        ConstraintLayout(
            modifier = modifier
                .size(160.dp)
                .padding(4.dp)
        ) {
            val (temp, icon, city, country, bottomDetail) = createRefs()

            Text(buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 30.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                ) {
                    append(hourlyForecast.temperature.toString())
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = Roboto,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                ) {
                    append(hourlyForecast.hourlyUnits.temperature_180m)
                }

            }, modifier = Modifier.constrainAs(temp) {
                top.linkTo(parent.top, margin = 8.dp)
                start.linkTo(parent.start, margin = 8.dp)
            })

            Image(
                painter = painterResource(id = hourlyForecast.weatherType.iconRes),
                contentDescription = hourlyForecast.weatherType.weatherDesc,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(60.dp)
                    .constrainAs(icon) {
                        end.linkTo(parent.end, margin = 8.dp)
                        top.linkTo(parent.top, margin = 8.dp)
                    }
            )

            Text(
                text = userLocation.city,
                fontFamily = Roboto,
                color = Color.White,
                modifier = Modifier.constrainAs(city) {
                    top.linkTo(temp.bottom, margin = 8.dp)
                    start.linkTo(parent.start, margin = 8.dp)
                }
            )

            Text(
                text = "${userLocation.state}, ${userLocation.country}",
                fontFamily = Roboto,
                color = Color.LightGray,
                modifier = Modifier.constrainAs(country) {
                    top.linkTo(city.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 8.dp)
                }
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .constrainAs(bottomDetail) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OtherDetailRowItem(
                    icon = painterResource(id = R.drawable.ic_drop),
                    desc = "${hourlyForecast.precipitation}${hourlyForecast.hourlyUnits.precipitation}",
                    modifier = Modifier
                        .size(16.dp)
                        .padding(start = 8.dp),
                    style = TextStyle(
                        fontFamily = Roboto,
                        fontSize = 14.sp
                    )
                )
                OtherDetailRowItem(
                    icon = painterResource(id = R.drawable.ic_wind),
                    desc = "${hourlyForecast.wind}${hourlyForecast.hourlyUnits.windspeed_180m}",
                    modifier = Modifier
                        .size(16.dp),
                    style = TextStyle(
                        fontFamily = Roboto,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}