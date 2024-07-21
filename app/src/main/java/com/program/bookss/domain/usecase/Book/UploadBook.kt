package com.program.bookss.domain.usecase.Book

import android.net.Uri
import com.program.bookss.domain.model.Book
import com.program.bookss.domain.repository.BookRepository
import javax.inject.Inject

class UploadBook @Inject constructor(
    private val repository: BookRepository
) {

    operator fun invoke(book:Book,imgUri: Uri) = repository.uploadBook(book,imgUri)
}