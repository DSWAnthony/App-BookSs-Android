package com.program.bookss.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.program.bookss.domain.model.Book
import com.program.bookss.ui.Authentication.BookViewModel
import com.program.bookss.ui.MyAppRoute
import com.program.bookss.ui.MyAppTopAppBar
import com.program.bookss.utils.Response

@Composable
fun HomeScreen(
    booksState: Response<List<Book>>,
    navController: NavHostController,
    bookViewModel: BookViewModel,
    role : String
){
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val uploadState by bookViewModel.uploadState
    var author by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            MyAppTopAppBar(role)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        showDialog = true
                    } else {
                        Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
                    }
                }, modifier = Modifier.padding(bottom = 70.dp),
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Photo")
            }
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                when (booksState) {
                    is Response.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    is Response.Success -> {
                        val booksList = booksState.data


                        Button(
                            onClick = {
                                navController.navigate(MyAppRoute.Books)
                                      },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(end = 15.dp)
                        ) {
                            Text(
                                text = "Ver Todo >",
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Default,
                                fontSize = 15.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                        LazyRow(modifier = Modifier.fillMaxWidth()) {
                            items(booksList) { book ->
                                if (book.state) {
                                    BookItem(book,false,bookViewModel)
                                }
                            }
                        }
                    }
                    is Response.Error -> {
                        Toast.makeText(context, "Error al cargar libros: ${(booksState).message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        if (showDialog) {
            BookUploadDialog(
                onDismiss = { showDialog = false },
                onImageSelected = {uri -> selectedImageUri = uri },
                onBookUploaded = { book ->
                    selectedImageUri?.let {
                        bookViewModel.uploadBook(book, it)
                    } ?: Toast.makeText(context, "Seleccione una imagen", Toast.LENGTH_SHORT).show()
                    showDialog = false
                },
                selectedImageUri = selectedImageUri,
                author = author,
                onAuthorChange = { author = it },
                title = title,
                onTitleChange = { title = it },
                description = description,
                onDescriptionChange = { description = it }
            )
        }

    }
        if (uploadState is Response.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(60.dp))
        }
}

@Composable
fun BookItem(book: Book,showEditButton: Boolean,bookViewModel: BookViewModel) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp) // Ajusta el ancho según el diseño deseado
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .width(200.dp) // Ajusta el ancho según el diseño deseado
        ) {
            CoilImage(
                imageUrl = book.imageUrl,
                contentDescription = book.title,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (showEditButton) {
               OptionsButtons(bookViewModel,book)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(color = Color.Black.copy(alpha = 0.2f))
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(Alignment.Center)
            )
        }
    }

}

@Composable
fun CoilImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context= LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .transformations(RoundedCornersTransformation(20f))
            .build(),
        contentDescription = contentDescription,
        modifier = modifier.padding(2.dp),
        contentScale = contentScale
    )
}


@Composable
fun BookUploadDialog(
    onDismiss: () -> Unit,
    onImageSelected: (Uri?) -> Unit,
    onBookUploaded: (Book) -> Unit,
    selectedImageUri: Uri?,
    author: String,
    onAuthorChange: (String) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri)
    }

    val isSwitchChecked = remember { mutableStateOf(false) }


    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    label = { Text("Autor") },
                    value = author,
                    onValueChange = onAuthorChange
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    label = { Text("Título") },
                    value = title,
                    onValueChange = onTitleChange
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    label = { Text("Descripción") },
                    value = description,
                    onValueChange = onDescriptionChange
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Agregar el Switch
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Publicar?")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = isSwitchChecked.value,
                        onCheckedChange = { isChecked ->
                            isSwitchChecked.value = isChecked
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Button(
                    onClick = { galleryLauncher.launch("img/*") }, // Cambia el comportamiento según sea necesario
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Seleccionar Imagen")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val book = Book(author = author, title = title, description = description)
                        if (isSwitchChecked.value){
                            book.state= true
                        }
                        onBookUploaded(book)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}

@Composable
fun OptionsButtons(
    bookViewModel: BookViewModel,
    book : Book
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 8.dp, y = (-7).dp) // Desplaza los botones fuera de la imagen
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = Color.Blue.copy(alpha = .6f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(5.dp)
            ) {
                IconButton(
                    onClick = {
                        println("Edit id  ${book.id}")
                    },
                    modifier = Modifier.size(20.dp) // Tamaño del botón
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp)) // Espacio entre los botones
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = Color.Red.copy(alpha = .6f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(5.dp)
            ) {
                IconButton(
                    onClick = {
                        println("Delete Id : ${book.id}")
                        bookViewModel.deleteBook(bookId = book.id)
                    },
                    modifier = Modifier.size(20.dp) // Tamaño del botón
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete",
                        tint = Color.White
                    )
                }
            }
        }
    }
}