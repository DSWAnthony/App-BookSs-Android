package com.program.bookss.ui.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.program.bookss.ui.Authentication.AuthViewModel
import com.program.bookss.ui.Authentication.UserViewModel
import com.program.bookss.utils.Response

@Composable
fun ProfileScreen(navController: NavHostController,userViewModel: UserViewModel){

    val context = LocalContext.current
    userViewModel.getUserInfo()
    when(val response = userViewModel.getUserData.value){
        is Response.Loading ->{
            Toast.makeText(context,"Esto esta Cargando",Toast.LENGTH_SHORT).show()
        }
        is Response.Success ->{
            Log.d("Successful ",response.data.toString())
        }
        is Response.Error -> {
            Toast.makeText(context,"No se pudo traer info",Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Profile")
    }
}