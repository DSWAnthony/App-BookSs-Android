package com.program.bookss.utils

sealed class Screens(val routes :String){
    object SplashScreen :Screens("splash_screen")
    object LoginScreen :Screens("login_screen")
    object SignUpScreen :Screens("signup_screen")
    object NavigationApp :Screens("navigationApp")

}