package com.program.bookss.domain.repository

import android.net.Uri
import com.program.bookss.domain.model.Book
import com.program.bookss.utils.Response
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun uploadBook(book: Book, imageUri: Uri): Flow<Response<Boolean>>

    fun getBooks(): Flow<Response<List<Book>>>

    fun deleteBook(bookId :String) : Flow<Response<Boolean>>
}