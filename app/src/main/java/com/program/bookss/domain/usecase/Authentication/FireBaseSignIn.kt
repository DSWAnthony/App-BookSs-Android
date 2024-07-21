package com.program.bookss.domain.usecase.Authentication

import com.program.bookss.domain.repository.AuthRepository
import javax.inject.Inject

class FireBaseSignIn @Inject constructor(
    private val repository : AuthRepository
) {
    operator fun invoke(email :String,pasword:String) = repository.fireBaseSignIn(email,pasword)

}