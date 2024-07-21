package com.program.bookss.domain.usecase.Authentication

import com.program.bookss.domain.repository.AuthRepository
import javax.inject.Inject

class FireBaseAuthState @Inject constructor(
    private val repository : AuthRepository
) {
    operator fun  invoke() = repository.getFirebaseAuthState()

}