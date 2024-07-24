package com.program.bookss.data

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.program.bookss.domain.model.Book
import com.program.bookss.domain.repository.BookRepository
import com.program.bookss.utils.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseDatabase: FirebaseDatabase,
    private val auth: FirebaseAuth
): BookRepository {

    val user = auth.currentUser?.uid
    override fun uploadBook(book: Book, imageUri: Uri): Flow<Response<Boolean>> = flow{
        emit(Response.Loading)

        try {
            emit(Response.Loading)

            // Generate a unique filename for the image
            val filename = UUID.randomUUID().toString()
            val storageRef = firebaseStorage.reference.child("book_images/$filename")

            // Upload the image to Firebase Storage
            val uploadTask = storageRef.putFile(imageUri)
            uploadTask.await()
            // Get the download URL for the uploaded image
            val downloadUrl = storageRef.downloadUrl.await()

            // Set the image URL in the book object
            book.imageUrl = downloadUrl.toString()
            book.userId = user.toString()
            book.id = filename
            // Upload the book to Firebase Realtime Database
            val databaseRef = firebaseDatabase.reference.child("books").push()
            databaseRef.setValue(book).await()

            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An unknown error occurred"))
        }
    }

    override fun getBooks(): Flow<Response<List<Book>>> = callbackFlow  {
        trySend(Response.Loading).isSuccess

        // Referencia a la ubicación en Firebase donde se almacenan los libros
        val booksRef = firebaseDatabase.getReference("books")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val booksList = mutableListOf<Book>()
                for (child in snapshot.children) {
                    val book = child.getValue(Book::class.java)
                    if (book != null) {
                        book.id = child.key?:""
                        booksList.add(book)
                    }
                }
                // Emitir los libros cuando se obtienen
                trySend(Response.Success(booksList)).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                // Emitir un error si ocurre
                trySend(Response.Error(error.message)).isFailure
            }
        }
        // Añadir el listener para escuchar cambios en la base de datos
        booksRef.addValueEventListener(listener)
        // Remover el listener cuando el flujo se cierra
        awaitClose { booksRef.removeEventListener(listener) }
    }

    override fun deleteBook(bookId: String): Flow<Response<Boolean>> = flow{
        emit(Response.Loading)
        try {
            // Referencia al nodo del libro en Firebase Realtime Database
            val bookRef = firebaseDatabase.reference.child("books").child(bookId)

            // Obtén el libro para obtener la URL de la imagen
            val bookSnapshot = bookRef.get().await()
            val book = bookSnapshot.getValue(Book::class.java)

            // Elimina el libro de la base de datos
            bookRef.removeValue().await()

            // Si hay una imagen asociada, elimina la imagen del almacenamiento
            if (book?.storageId != null) {
                val imageRef = firebaseStorage.reference.child("book_images/${book.storageId}")
                imageRef.delete().await()
            }

            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "An unknown error occurred"))
        }

    }




}