package com.program.bookss.domain.usecase.Book

data class BookUseCases (
    val uploadBook : UploadBook,
    val getBooks: GetBooks,
    val deleteBook: DeleteBook
)
