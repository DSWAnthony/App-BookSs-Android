package com.program.bookss.domain.repository

import com.program.bookss.utils.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun isUserAuthenticatedInFireBase(): Boolean

    fun getFirebaseAuthState() : Flow<Boolean>
    //iniciar Sesion
    fun fireBaseSignIn(email: String,password :String): Flow<Response<Boolean>>

    fun fireBaseSignOut() : Flow<Response<Boolean>>
    //crear cuenta
    fun fireBaseSignUp(email: String,password: String,userName : String,role :String) : Flow<Response<Boolean>>
}
