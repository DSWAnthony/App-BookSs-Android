package com.program.bookss.domain.usecase.Authentication

import com.program.bookss.domain.repository.AuthRepository
import javax.inject.Inject

class FireBaseSignUp @Inject constructor(
    private val repository : AuthRepository
) {

    operator fun invoke(email :String,password:String,userName:String,role :String)
    =repository.fireBaseSignUp(email,password,userName,role)
}