package com.program.bookss.domain.usecase.Authentication

data class AuthenticationUseCases(
    val isUserAuthenticated: IsUserAuthenticated,
    val fireBaseAuthState: FireBaseAuthState,
    val fireBaseSignIn: FireBaseSignIn,
    val fireBaseSignOut: FireBaseSignOut,
    val fireBaseSignUp: FireBaseSignUp

)
