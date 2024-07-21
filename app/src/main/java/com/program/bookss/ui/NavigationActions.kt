package com.program.bookss.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.program.bookss.R


class MainAppNavigationActions(private val navController: NavHostController) {
    fun navigateTo(destination: MyAppTopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
        }
    }
}

data class MyAppTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val iconTextId: Int
)

val TOP_LEVEL_DESTINATIONS = listOf(
    MyAppTopLevelDestination(
        route = MyAppRoute.Home,
        selectedIcon = Icons.Default.Home,
        iconTextId = R.string.home
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Favorite,
        selectedIcon = Icons.Default.Favorite,
        iconTextId = R.string.favorite
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Person,
        selectedIcon = Icons.Default.Person,
        iconTextId = R.string.person
    ),
    MyAppTopLevelDestination(
        route = MyAppRoute.Settings,
        selectedIcon = Icons.Default.Settings,
        iconTextId = R.string.setting
    )

)

object MyAppRoute {
    const val Home = "home"
    const val Person = "person"
    const val Favorite = "favorite"
    const val Settings = "settings"

    const val Books = "books"
}
