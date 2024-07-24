package com.program.bookss.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.program.bookss.ui.Authentication.AuthViewModel
import com.program.bookss.ui.Authentication.BookViewModel
import com.program.bookss.ui.Authentication.UserViewModel
import com.program.bookss.ui.favorite.FavoriteScreen
import com.program.bookss.ui.home.BooksScreen
import com.program.bookss.ui.home.HomeScreen
import com.program.bookss.ui.profile.ProfileScreen
import com.program.bookss.ui.setting.SettingScreen
import com.program.bookss.utils.Response

@Composable
fun NavigationApp(
    navMainController: NavHostController,
    viewModel: AuthViewModel
) {
    val navigationController = rememberNavController()
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: MyAppRoute.Home

    val bookViewModel: BookViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()


    var role by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        userViewModel.getUserRole()
    }
    val userRoleResponse by userViewModel.userRole
    when (val response = userRoleResponse) {
        is Response.Success -> {
            role = response.data
        }
        else -> Unit
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Navigation(
                    navMainController = navMainController,
                    navController = navigationController,
                    bookViewModel = bookViewModel,
                    authViewModel = viewModel,
                    userViewModel = userViewModel,
                    role = role
                )
            }
        }

        // BottomNavigation
        MyAppBottomNavigation(
            selectedDestination = selectedDestination,
            navigateTopLevelDestination = { route ->
                navigationController.navigate(route)
            },
            role = role,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
                .zIndex(1f) // Asegúrate de que esté encima del contenido
        )

        // Dialog
    }
}

@Composable
fun Navigation(
    navMainController: NavHostController,
    navController: NavHostController,
    bookViewModel: BookViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    role: String
) {
    val booksState by bookViewModel.booksState.collectAsState()
    LaunchedEffect(Unit) {
        bookViewModel.getBooks()
    }

    NavHost(
        navController = navController,
        startDestination = MyAppRoute.Home,
        Modifier.fillMaxSize()
    ) {
        composable(MyAppRoute.Home) {
            HomeScreen(booksState, navController, bookViewModel,role)
        }
        composable(MyAppRoute.Favorite) {
            FavoriteScreen(navController, authViewModel)
        }
        composable(MyAppRoute.Person) {
            ProfileScreen(navController, userViewModel)
        }
        composable(MyAppRoute.Settings) {
            SettingScreen(navController, authViewModel,navMainController,role,userViewModel)
        }
        composable(MyAppRoute.Books) {
            BooksScreen(booksState, navController, role, bookViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppTopAppBar(
    role: String
) {
    TopAppBar(
        title = {
            Text(
                text = if (role.isNotEmpty()) "Bienvenido $role" else "Bienvenido",
                fontSize = 17.sp
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors()
    )
}

@Composable
fun MyAppBottomNavigation(
    selectedDestination: String,
    navigateTopLevelDestination: (String) -> Unit,
    role: String,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .height(60.dp)
            .padding(horizontal = 15.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(60.dp)),
        containerColor = Color.Black.copy(alpha = 0.2f),

        tonalElevation = 8.dp // Ajusta este valor según tus necesidades
    ) {
        val destinations = if (role == "admin" || role == "user") {
            TOP_LEVEL_DESTINATIONS
        } else {
            TOP_LEVEL_DESTINATIONS
            //TOP_LEVEL_DESTINATIONS.filter { it.route != MyAppRoute.Settings }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
        ) {
            destinations.forEach { destination ->
                NavigationBarItem(
                    selected = selectedDestination == destination.route,
                    onClick = {
                        if (selectedDestination != destination.route) {
                            navigateTopLevelDestination(destination.route)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = destination.selectedIcon,
                            contentDescription = stringResource(id = destination.iconTextId),
                            tint = if (selectedDestination == destination.route) Color.White else Color.Gray
                        )
                    },colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(.5f)
                    )
                )
            }
        }

    }
}


@Composable
fun LogoutDialog(onConfirmLogout: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar sesión") },
        text = { Text("¿Estás seguro que deseas cerrar sesión?") },
        confirmButton = {
            Button(
                onClick = onConfirmLogout
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

