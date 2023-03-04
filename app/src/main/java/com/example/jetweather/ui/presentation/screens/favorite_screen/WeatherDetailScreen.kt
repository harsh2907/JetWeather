package com.example.jetweather.ui.presentation.screens.favorite_screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jetweather.domain.models.entity.UserLocation
import com.example.jetweather.ui.presentation.screens.main_weather_screen.LoadingBox
import com.example.jetweather.ui.theme.ColorPalate
import com.example.jetweather.ui.theme.Roboto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchResultScreen(
    query: String,
    navController: NavHostController,
    viewModel: SearchViewModel
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getLocation(query)
    }

    val searchState = viewModel.locationSearchResult.collectAsState().value
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        AnimatedContent(targetState = searchState) { state ->
            when {
                state.isLoading -> {
                    LoadingBox()
                }
                state.error.isNotEmpty() -> {
                    LaunchedEffect(key1 = Unit) {
                        scope.launch {
                            val action = scaffoldState.snackbarHostState.showSnackbar(
                                message = state.error,
                                actionLabel = "Retry"
                            )
                            if (action == SnackbarResult.ActionPerformed) {
                                viewModel.getLocation(query)
                            }
                        }
                    }
                }
                state.data.isEmpty() -> {
                    ErrorBox(message = "No result found.")
                }
                else -> {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(state.data) { item: UserLocation ->
                            LocationSearchResultItem(userLocation = item) {
                                scope.launch {
                                    if (viewModel.getPlaceByLat(item.lat.toString()) != null) {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(
                                                context,
                                                "${item.city} is already marked as favourite",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        viewModel.savePlace(it)
                                        Toast.makeText(
                                            context,
                                            "Location marked as favorite",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigateUp()
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


@Composable
fun LocationSearchResultItem(
    userLocation: UserLocation,
    onClick: (UserLocation) -> Unit
) {
    Card(
        elevation = 12.dp,
        shape = RoundedCornerShape(20.dp),
        backgroundColor = ColorPalate.LightNavyBlue,
        modifier = Modifier
            .padding(12.dp)
            .clickable { onClick(userLocation) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "${userLocation.city}, ${userLocation.state}, ${userLocation.country}",
                fontFamily = Roboto,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Lat: ${userLocation.lat}",
                fontFamily = Roboto,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Long: ${userLocation.long}",
                fontFamily = Roboto,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

