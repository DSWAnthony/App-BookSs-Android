package com.program.bookss.domain.usecase.Authentication

import com.program.bookss.domain.repository.AuthRepository
import javax.inject.Inject

class FireBaseSignOut @Inject constructor(
    private val repository : AuthRepository
) {

    operator fun invoke() = repository.fireBaseSignOut()
}