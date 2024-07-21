package com.program.bookss.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.program.bookss.domain.model.Book
import com.program.bookss.ui.Authentication.BookViewModel
import com.program.bookss.ui.MyAppRoute
import com.program.bookss.utils.Response


@Composable
fun BooksScreen(
    booksState: Response<List<Book>>,
    navController: NavHostController,
    role : String,
    bookViewModel: BookViewModel
){
    var showEditButton by remember { mutableStateOf(false) }
    val deleteState by bookViewModel.deleteState

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // Alinea verticalmente los elementos al centro
            modifier = Modifier.padding(start = 20.dp)
        ) {
            ClickableText(
                text = AnnotatedString("< Regresar"),
                onClick = {
                    navController.navigate(MyAppRoute.Home) {
                        popUpTo(MyAppRoute.Books) {
                            inclusive = true
                        }
                    }
                },
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Default,
                    color = Color.Gray
                )
            )
            if (role == "admin"){
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { showEditButton = !showEditButton },
                    modifier = Modifier.padding(end = 30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit"
                    )
                }
            }
        }

        when (booksState) {
            is Response.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is Response.Success -> {
                val booksList = booksState.data
                val filteredBooksList = if (role == "user") {
                    booksList.filter { it.state }
                } else {
                    booksList
                }
                LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.padding(6.dp)) {
                    items(filteredBooksList) { book ->
                        BookItem(book = book,showEditButton = showEditButton,bookViewModel)
                    }
                }
            }
            is Response.Error -> {
                Text(
                    text = "Error al cargar libros: ${(booksState as Response.Error).message}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    if (deleteState is Response.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}