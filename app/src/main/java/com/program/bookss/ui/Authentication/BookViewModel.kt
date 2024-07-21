package com.program.bookss.ui.Authentication

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.program.bookss.domain.model.Book
import com.program.bookss.domain.usecase.Book.BookUseCases
import com.program.bookss.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookUseCase: BookUseCases
): ViewModel(){

    private val _deleteState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val deleteState : State<Response<Boolean>> = _deleteState

    private val _uploadState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val uploadState: State<Response<Boolean>> = _uploadState

    private val _booksState = MutableStateFlow<Response<List<Book>>>(Response.Loading)
    val booksState: StateFlow<Response<List<Book>>> = _booksState

    init {
        getBooks()
    }


    fun uploadBook(book: Book, imageUri: Uri) {
        viewModelScope.launch {
            bookUseCase.uploadBook(book, imageUri).collect {
                _uploadState.value = it
            }
        }
    }

    fun getBooks() {
        viewModelScope.launch {
            bookUseCase.getBooks().collect {
                _booksState.value = it
            }
        }
    }

    fun deleteBook(bookId:String){
        viewModelScope.launch {
            bookUseCase.deleteBook(bookId =bookId).collect{
                _deleteState.value = it
            }
        }
    }


}