package com.program.bookss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.program.bookss.ui.Authentication.AuthViewModel
import com.program.bookss.ui.NavigationApp
import com.program.bookss.ui.theme.BookSsTheme
import com.program.bookss.utils.Screens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookSsTheme {
              val navController = rememberNavController()
                val authViewModel : AuthViewModel = hiltViewModel()
                BooksApp(navController,authViewModel)
            }
        }
    }
}

@Composable
fun BooksApp(
    navController: NavHostController,
    authViewModel: AuthViewModel
){

    NavHost(navController = navController, startDestination = Screens.SplashScreen.routes ){
        composable(Screens.LoginScreen.routes){
            LoginScreen(navController,authViewModel)
        }

        composable(Screens.SignUpScreen.routes){
            SignUpScreen(navController,authViewModel)
        }

        composable(Screens.SplashScreen.routes){
            SplashScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(Screens.NavigationApp.routes) {
            NavigationApp(
                navController = navController,
                viewModel = authViewModel,
            )
        }

    }
}

