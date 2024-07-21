package com.program.bookss.domain.usecase.Book

import com.program.bookss.domain.repository.BookRepository
import javax.inject.Inject

class DeleteBook @Inject constructor(
    private val repository: BookRepository
) {
    operator fun invoke(bookId:String)= repository.deleteBook(bookId)
}