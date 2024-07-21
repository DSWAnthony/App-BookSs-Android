package com.program.bookss.domain.usecase.Book

import com.program.bookss.domain.repository.BookRepository
import javax.inject.Inject

class GetBooks @Inject constructor(
    private val repository : BookRepository
) {
    operator fun invoke() = repository.getBooks()
}