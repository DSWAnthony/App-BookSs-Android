package com.program.bookss.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.program.bookss.ui.Authentication.UserViewModel
import com.program.bookss.utils.Response

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userViewModel: UserViewModel
){
    val localContext = LocalContext.current
    userViewModel.getUserInfo()
    when(val response = userViewModel.getUserData.value){
        is Response.Loading ->{
            CircularProgressIndicator()
        }
        is Response.Success ->{
            //Log.d("Successful ",response.data.toString())
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                IconButton(onClick = {
                    navController.popBackStack()
                },modifier = Modifier.padding(start = 10.dp, top = 20.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Sharp.ArrowBack, contentDescription = "regresar")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(modifier = Modifier.size(200.dp)) {
                        if (response.data?.imageUrl.isNullOrEmpty() || response.data?.imageUrl =="") {
                            Image(
                                painter = painterResource(id =R.drawable.user_default),
                                contentDescription = "userDefault",
                                modifier = Modifier.size(200.dp)
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
                                    .size(200.dp)
                                    .clip(CircleShape)
                            )
                        }

                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(10.dp)
                                .background(
                                    Color.Blue.copy(.4f),
                                    shape = CircleShape
                                )
                                .clip(CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "foto de perfil",
                                tint = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))

                DataRow(label = "Nombre", value = response.data?.userName?:"")
                Separator()
                DataRow(label = "Correo", value = response.data?.email?:"")
                Separator()
                DataRow(label = "Contraseña", value = response.data?.password?:"")
                Separator()
                DataRow(label = "Rol", value = response.data?.role?:"")
            }
        }
        is Response.Error -> {
            Text(text = "Error Al traer La Info")
        }
    }

}

@Composable
fun Separator(){
    HorizontalDivider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 20.dp)
    )
}

@Composable
fun DataRow(
    label :String,
    value :String
){
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(start = 25.dp)) {
            Text(
                text = label,
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                fontSize = 17.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.W400,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

    }
}