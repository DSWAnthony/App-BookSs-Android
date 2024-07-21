package com.program.bookss

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.program.bookss.ui.Authentication.AuthViewModel
import com.program.bookss.ui.theme.Purple40
import com.program.bookss.utils.Response
import com.program.bookss.utils.Screens



@Composable
fun LoginScreen(navController: NavHostController,authViewModel: AuthViewModel){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    val signInState by authViewModel.signInState

    LaunchedEffect(signInState) {
        when (val response = signInState) {
            is Response.Success -> {
                if (response.data) {
                    Toast.makeText(context, "Inicio Sesión Correcto", Toast.LENGTH_SHORT).show()

                    navController.navigate(route = Screens.NavigationApp.routes) {
                        popUpTo(Screens.LoginScreen.routes){
                            inclusive=true
                        }
                    }
                    println("Login Successful: ${response.data}")
                }
            }
            is Response.Error -> {
                Toast.makeText(context, "Error al iniciar sesión: ${response.message}", Toast.LENGTH_SHORT).show()
                println("Login Error: ${response.message}")
            }
            else -> Unit
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Correo Electrónico") },
                value = email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = { email = it }
            )

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Contraseña") },
                value = password,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password = it }
            )

            Box(modifier = Modifier.padding(40.dp, 20.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        if (email.isNotEmpty() && password.isNotEmpty()) {
                            authViewModel.signIn(email, password)
                        } else {
                            Toast.makeText(context, "Existen Campos Vacios", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Iniciar Sesión".uppercase())
                }
            }

            Spacer(modifier = Modifier.height(25.dp))
            ClickableText(
                text = AnnotatedString("Olvidaste Tu Contraseña?"),
                onClick = {},
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                    color = Purple40
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "-------- o --------", style = TextStyle(color = Color.Gray))
            Spacer(modifier = Modifier.height(20.dp))

            Box {
                ClickableText(
                    text = AnnotatedString("No Tienes Cuenta ? Registrate"),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(top = 20.dp),
                    onClick = {
                        navController.navigate(route = Screens.SignUpScreen.routes) {
                            launchSingleTop = true
                        }
                    },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default,
                        textDecoration = TextDecoration.Underline,
                        color = Purple40
                    )
                )
            }
        }

        if (signInState is Response.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}