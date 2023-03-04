package com.example.jetweather.ui.presentation.screens.main_screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jetweather.ui.presentation.navigation.Screens
import com.example.jetweather.ui.theme.ColorPalate

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {

    object Home : BottomNavItem("Home", Icons.Outlined.Home, Screens.Home.route)
    object Favourite : BottomNavItem("Favourite", Icons.Outlined.Favorite, Screens.Favourite.route)
    object Setting : BottomNavItem("Setting", Icons.Outlined.ClearAll, Screens.Settings.route)

}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Favourite,
    BottomNavItem.Setting
)


@Composable
fun CustomBottomNavBar(
    navController: NavController
) {
    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
//            .padding(20.dp)
//            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(20.dp))
//            .graphicsLayer {
//                shape = RoundedCornerShape(20.dp)
//                clip = true
//            }
                ,
        elevation = 20.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.route
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        tint = if(isSelected) ColorPalate.BabyPink else Color.LightGray,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                selectedContentColor = ColorPalate.BabyPink,
                unselectedContentColor = Color.Gray,
                selected = isSelected,
                alwaysShowLabel = false,
                onClick = {

                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            //used pop up to avoid stack in bottom navigation
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}