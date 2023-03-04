package com.example.jetweather.ui.presentation.screens.main_weather_screen

import com.example.jetweather.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetweather.data.utils.to12HourFormat
import com.example.jetweather.domain.models.entity.UserLocation
import com.example.jetweather.domain.mapper.DayWiseForecast
import com.example.jetweather.domain.mapper.HourlyForecast
import com.example.jetweather.domain.utils.WeatherType
import com.example.jetweather.ui.theme.ColorPalate
import com.example.jetweather.ui.theme.Roboto


@Composable
fun WeatherScreen(
    userLocation: UserLocation,
    hourlyForecasts: List<HourlyForecast>,
    dailyForecast: List<DayWiseForecast>
) {

    val forecast = hourlyForecasts[0]
    val weather = forecast.weatherType
    val temperature = "${forecast.temperature}${forecast.hourlyUnits.temperature_180m}"
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TemperatureComponent(
                temperature = temperature,
                location = userLocation.city,
                weatherType = weather
            )
            Image(
                painter = painterResource(id = weather.iconRes),
                contentDescription = weather.weatherDesc,
                modifier = Modifier.padding(10.dp)
            )

        }

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        OtherDetailsRow(hourlyForecast = forecast)

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            items(hourlyForecasts) {
                HourlyForecastItem(forecast = it)
            }
        }
        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

        Text(
            text = "7 days forecast",
            fontFamily = Roboto,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.padding(6.dp).align(Alignment.Start)
        )

        dailyForecast.forEach {
            DailyForecastItem(weather = it)
            Spacer(modifier = Modifier.padding(bottom = 6.dp))
        }

    }
}


@Composable
fun HourlyForecastItem(
    forecast: HourlyForecast
) {
    val weather = forecast.weatherType
    Box(
        modifier = Modifier
            .size(90.dp, 120.dp)
            .padding(horizontal = 8.dp)
            .background(color = ColorPalate.LightNavyBlue, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = forecast.time.to12HourFormat(),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 4.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
        )

        Image(
            painter = painterResource(id = weather.iconRes),
            contentDescription = weather.weatherDesc,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Text(
            text = "${forecast.temperature} ${forecast.hourlyUnits.temperature_180m}",
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
                .align(Alignment.BottomCenter),
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )


    }
}


@Composable
fun TemperatureComponent(
    temperature: String,
    location: String,
    weatherType: WeatherType
) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .height(180.dp)
            .background(ColorPalate.NavyBlue),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = location,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.TopCenter),
            fontFamily = Roboto,
            color = Color.White,
            fontWeight = FontWeight.Light
        )

        Text(
            text = temperature,
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(horizontal = 8.dp),
            fontFamily = Roboto,
            color = Color.White
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp),
            backgroundColor = Color(0xff353361)
        ) {
            Text(
                text = weatherType.weatherDesc,
                modifier = Modifier.padding(6.dp),
                color = Color.White,
                fontFamily = Roboto
            )
        }
    }
}


@Composable
fun OtherDetailsRow(
    hourlyForecast: HourlyForecast
) {
    val precipitation = "${hourlyForecast.precipitation}${hourlyForecast.hourlyUnits.precipitation}"
    val humidity =
        "${hourlyForecast.relativeHumidity}${hourlyForecast.hourlyUnits.relativehumidity_2m}"
    val wind = "${hourlyForecast.wind}${hourlyForecast.hourlyUnits.windspeed_180m}"
    val pressure = "${hourlyForecast.pressure}${hourlyForecast.hourlyUnits.pressure_msl}"

    Row(
        Modifier
            .fillMaxWidth()
            .background(ColorPalate.NavyBlue),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OtherDetailRowItem(
            icon = painterResource(id = R.drawable.ic_drop),
            desc = precipitation,
            modifier = Modifier.size(20.dp)
        )
        OtherDetailRowItem(
            icon = rememberVectorPainter(image = Icons.Rounded.Thermostat),
            desc = humidity,
            modifier = Modifier.size(20.dp)
        )
        OtherDetailRowItem(icon = painterResource(id = R.drawable.ic_wind), desc = wind, modifier = Modifier.size(20.dp))
        OtherDetailRowItem(icon = painterResource(id = R.drawable.ic_pressure), desc = pressure, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun OtherDetailRowItem(
    icon: Painter,
    desc: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.body1
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            painter = icon, contentDescription = desc, tint = Color(0xff6686b6),
            modifier = modifier
        )
        Spacer(modifier = Modifier.padding(start = 4.dp))
        Text(
            text = desc,
            fontFamily = Roboto,
            color = Color.White,
            style = style
        )
    }
}


@Composable
fun DailyForecastItem(
    weather: DayWiseForecast
) {

    val temperature =
        "${weather.forecast.maxTemperature}/${weather.forecast.minTemperature}${weather.forecast.dailyUnits.temperature_2m_max}"

    Row(
        Modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = weather.day,
            fontSize = 14.sp,
            fontFamily = Roboto,
            color = Color.White,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            textAlign = TextAlign.Start
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = weather.forecast.weatherType.iconRes),
                contentDescription = weather.forecast.weatherType.weatherDesc,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.width(60.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 3.dp))
            Text(
                text = weather.forecast.weatherType.weatherDesc,
                fontSize = 12.sp,
                fontFamily = Roboto,
                color = Color.White
            )
        }
        Text(
            text = temperature,
            fontSize = 14.sp,
            fontFamily = Roboto,
            color = Color.White,
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(1f),
            textAlign = TextAlign.End
        )

    }
}