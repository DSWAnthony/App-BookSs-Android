package com.program.bookss.ui.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.program.bookss.R
import com.program.bookss.ui.Authentication.AuthViewModel
import com.program.bookss.ui.Authentication.UserViewModel
import com.program.bookss.ui.LogoutDialog
import com.program.bookss.ui.MyAppRoute
import com.program.bookss.utils.Response
import com.program.bookss.utils.Screens

@Composable
fun SettingScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    navMainController: NavHostController,
    role :String,
    userViewModel: UserViewModel
) {
    var showDialogLogout by remember { mutableStateOf(false) }
    val localContext = LocalContext.current
    val onLogoutConfirmed: () -> Unit = {
        authViewModel.signOut()
        navMainController.navigate(Screens.LoginScreen.routes) {
            popUpTo(Screens.NavigationApp.routes) { inclusive = true }
        }
    }
    userViewModel.getUserInfo()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {


        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Setting",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Cursive,
                modifier = Modifier.padding(start = 15.dp, top = 30.dp)
            )
        }

        Spacer(modifier =Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable {
                    navController.navigate(MyAppRoute.Person)
                },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

          when(val response = userViewModel.getUserData.value){
              is Response.Success ->{
                  if (response.data?.imageUrl.isNullOrEmpty() || response.data?.imageUrl =="") {
                      Image(
                          painter = painterResource(id =R.drawable.user_default),
                          contentDescription = "userDefault",
                          modifier = Modifier.size(60.dp)
                              .clip(CircleShape)
                      )

                  }else{
                      AsyncImage(
                          model = ImageRequest.Builder(localContext)
                              .data(
                                  response.data?.imageUrl
                              )
                              .crossfade(true)
                              .transformations(CircleCropTransformation())
                              .build(),
                          contentDescription = null,
                          contentScale = ContentScale.Crop,
                          modifier = Modifier
                              .size(60.dp)
                              .clip(CircleShape)
                      )
                  }
                    Text(
                        text = response.data?.userName?:"",
                        fontSize = 17.sp,
                        fontFamily = FontFamily.Default,
                        modifier = Modifier.padding(start = 20.dp)
                    )
              }
              is Response.Error ->{
                  Text(text = "Error al cargar la imagen")
              }
              else -> Unit
          }
        }
        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 20.dp)
        )

        RowItem(
            text = "Cambiar Contraseña",
            icon = Icons.Default.Person,
            onClick = {

                println("cambio")
            }
        )

        if (role == "admin"){
        Spacer(modifier = Modifier.height(10.dp))

        RowItem(
            text = "General",
            icon = Icons.Default.Build,
            onClick = { println("General") }
        )
        }

        Spacer(modifier = Modifier.height(10.dp))

        RowItem(
            text = "Ayuda",
            icon = Icons.Default.Info,
            onClick = { println("ayuda") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        RowItem(
            text = "Política y Privacidad",
            icon = Icons.Default.Info,
            onClick = { println("ayuda") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        RowItem(
            text = "Cerrar sesión",
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            onClick = {
                showDialogLogout = true
                println("Cerro Sesion")
            }
        )

    }
    if (showDialogLogout) {
        LogoutDialog(
            onConfirmLogout = {
                   onLogoutConfirmed()
                showDialogLogout = false
            },
            onDismiss = { showDialogLogout = false }
        )
    }
}

@Composable
fun RowItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .border(
                BorderStroke(1.dp, color = Color.LightGray),
                shape = RoundedCornerShape(20.dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            fontFamily = FontFamily.Default,
            modifier = Modifier.padding(start = 20.dp)
        )
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier
                .padding(end = 20.dp)
                .size(25.dp)
        )
    }

}