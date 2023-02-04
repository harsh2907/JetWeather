package com.example.jetweather.ui.presentation.screens.onboard

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShareLocation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.jetweather.MainActivity
import com.example.jetweather.R
import com.example.jetweather.domain.location.hasLocationPermission
import com.example.jetweather.ui.presentation.navigation.Screens
import com.example.jetweather.ui.sharedpreferences.SharedPreferenceUtils
import com.example.jetweather.ui.theme.ColorPalate
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


// TODO: Ask for current location permission for weather
// TODO: Logic for one time opening using preferences 


@Composable
fun CustomDialogUI(modifier: Modifier = Modifier,
                   title: String = "Enable Location",
                   description: String = MainActivity.LOCATION_PERM_MESSAGE,
                   buttonText:String = "Okay",
                   onClick:()->Unit
) {
    Card(
        //shape = MaterialTheme.shapes.medium,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier
                .background(Color.White)
        ) {

            Image(
                imageVector = Icons.Rounded.ShareLocation,
                contentDescription = null, // decorative
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    color = Color.Red
                ),
                modifier = Modifier
                    .padding(top = 35.dp)
                    .height(70.dp)
                    .fillMaxWidth(),

                )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.h4,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
                Text(
                    text = description,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.body1,
                    color = Color.Black
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(ColorPalate.BabyPink),
                horizontalArrangement = Arrangement.SpaceAround
            ) {


                TextButton(onClick = onClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        buttonText,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
    }
}



 fun Context.sendToAppSetting(){
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri: Uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}



@Composable
fun OnBoardingScreen(
    navController: NavController
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Image(
            painter = painterResource(
                id = R.drawable.cloudy
            ),
            contentDescription = "windy_sunny_icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(40.dp)
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Discover the Weather\nin your City",
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            color = if (isSystemInDarkTheme()) Color.White else Color.Black
        )

        Text(
            text = "Get to know your weather maps and\nradar precipitation forecast.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            color = Color.Gray

        )

        Spacer(modifier = Modifier.padding(10.dp))

        Button(
            onClick = {
                val sharedPref = SharedPreferenceUtils(context as Activity)
                sharedPref.setFirstTimeDone()
                   navController.navigate(Screens.MainScreen.route){
                       popUpTo(0)
                   }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorPalate.BabyPink
            )
        ) {
            Text(
                text = "Get Started",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}