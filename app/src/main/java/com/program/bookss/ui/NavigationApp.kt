package com.program.bookss.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.program.bookss.domain.model.Book
import com.program.bookss.ui.Authentication.AuthViewModel
import com.program.bookss.ui.Authentication.BookViewModel
import com.program.bookss.ui.Authentication.UserViewModel
import com.program.bookss.ui.favorite.FavoriteScreen
import com.program.bookss.ui.home.BooksScreen
import com.program.bookss.ui.home.HomeScreen
import com.program.bookss.ui.profile.ProfileScreen
import com.program.bookss.ui.setting.SettingScreen
import com.program.bookss.utils.Response
import com.program.bookss.utils.Screens


@Composable
fun NavigationApp(
    navController: NavHostController,
    viewModel: AuthViewModel,
) {

    val nav = rememberNavController()
    val navigationActions = remember(nav) { MainAppNavigationActions(nav) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: MyAppRoute.Home

    var showDialog by remember { mutableStateOf(false) }
    val bookViewModel : BookViewModel = hiltViewModel()
    val userViewModel : UserViewModel = hiltViewModel()

    val onLogoutConfirmed: () -> Unit = {
        viewModel.signOut()
        navController.navigate(Screens.LoginScreen.routes) {
            popUpTo(Screens.NavigationApp.routes) { inclusive = true }
        }
    }

    var role by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        userViewModel.getUserRole()
    }
    val userRoleResponse  by userViewModel.userRole
    when (val response = userRoleResponse ) {
        is Response.Success -> {
            role = response.data
        }
        else -> Unit
    }

    Scaffold(
        topBar = { MyAppTopAppBar(onLogoutClick = { showDialog = true },role) },

    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            if (showDialog) {
                LogoutDialog(
                    onConfirmLogout = {
                        onLogoutConfirmed()
                        showDialog = false
                    },
                    onDismiss = { showDialog = false }
                )
            }
            Navigation(
                navController = nav,
                bookViewModel = bookViewModel,
                viewModel = viewModel,
                userViewModel= userViewModel,
                selectedDestination = selectedDestination,
                navigateTopLevelDestination = navigationActions::navigateTo,
                role = role
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppTopAppBar(
    onLogoutClick: () -> Unit,
    role: String
) {

    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text =  if (role.isNotEmpty()) "Bienvenido $role" else "Bienvenido",
                        fontSize = 17.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(),
        actions = {
            IconButton(onClick = onLogoutClick) {
                Icon(Icons.Outlined.ExitToApp, contentDescription = "Cerrar sesión")
            }
        }
    )
}

@Composable
fun Navigation(
    navController: NavHostController,
    bookViewModel: BookViewModel,
    viewModel: AuthViewModel,
    userViewModel: UserViewModel,
    selectedDestination: String,
    navigateTopLevelDestination: (MyAppTopLevelDestination) -> Unit,
    role :String
){

    val booksState by bookViewModel.booksState.collectAsState()
    LaunchedEffect(Unit) {
        bookViewModel.getBooks()
    }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = MyAppRoute.Home
            ) {
                composable(MyAppRoute.Home) {
                    HomeScreen(booksState,navController,bookViewModel)
                }
                composable(MyAppRoute.Favorite) {
                    FavoriteScreen(navController, viewModel)
                }
                composable(MyAppRoute.Person) {
                    ProfileScreen(navController,userViewModel)
                }
                composable(MyAppRoute.Settings) {
                    SettingScreen(navController, viewModel)
                }
                composable(MyAppRoute.Books){
                    BooksScreen(booksState,navController,role,bookViewModel)
                }

            }
            MyAppBottomNavigation(
                selectedDestination = selectedDestination,
                navigateTopLevelDestination = navigateTopLevelDestination,
                role = role
            )
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

@Composable
fun MyAppBottomNavigation(
    selectedDestination: String,
    navigateTopLevelDestination: (MyAppTopLevelDestination) -> Unit,
    role: String
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        val filteredDestinations = if (role == "admin") {
            TOP_LEVEL_DESTINATIONS
        } else {
            TOP_LEVEL_DESTINATIONS.filter { it.route != MyAppRoute.Settings }
        }

        filteredDestinations.forEach { destination ->
            NavigationBarItem(
                selected = selectedDestination == destination.route,
                onClick = { navigateTopLevelDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = stringResource(id = destination.iconTextId)
                    )
                }
            )
        }
    }
}