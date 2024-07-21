package com.program.bookss.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.program.bookss.domain.model.User
import com.program.bookss.domain.repository.AuthRepository
import com.program.bookss.utils.Constants
import com.program.bookss.utils.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepositoryImpl @Inject constructor(
    private val auth :FirebaseAuth,
    private val fireStore : FirebaseFirestore
): AuthRepository {

    var operationSuccessful : Boolean =false

    override fun isUserAuthenticatedInFireBase(): Boolean {
        return auth.currentUser!=null
    }

    override fun getFirebaseAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(auth.currentUser == null)
        }

        auth.addAuthStateListener(authStateListener)
        awaitClose{
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override fun fireBaseSignIn(email: String, password: String): Flow<Response<Boolean>> = flow {
        emit(Response.Loading) // Emite el estado de carga

        try {
            // SignIn con Firebase
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            if (authResult.user != null) {
                // Autenticación exitosa
                emit(Response.Success(true))
            } else {
                // Autenticación fallida
                emit(Response.Error("Authentication failed"))
            }
        } catch (e: Exception) {
            // Manejo de excepción
            emit(Response.Error(e.localizedMessage ?: "An unknown error occurred"))
        }
    }

    override fun fireBaseSignOut(): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            auth.signOut()
            emit(Response.Success(true))

        }
        catch (e:Exception){
            emit(Response.Error(e.localizedMessage?:"Ocurrio Un Error"))
        }
    }

    override fun fireBaseSignUp(
        email: String,
        password: String,
        userName: String,
        role : String
    ): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)
        try {
            val userCreated = suspendCoroutine<Boolean> { continuation ->
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { continuation.resume(true) }
                    .addOnFailureListener { continuation.resume(false) }
            }

            if (userCreated) {
                val userId = auth.currentUser?.uid!!
                val user = User(userName = userName, userId = userId, email = email, password = password, role = role)

                val userSaved = suspendCoroutine<Boolean> { continuation ->
                    fireStore.collection(Constants.COLLECTION_NAME_USERS)
                        .document(userId)
                        .set(user)
                        .addOnSuccessListener { continuation.resume(true) }
                        .addOnFailureListener { continuation.resume(false) }
                }

                if (userSaved) {
                    emit(Response.Success(true))
                } else {
                    emit(Response.Error("Fallo aL guardar datos de user"))
                }
            } else {
                emit(Response.Error("Fallo Al crear usuario"))
            }
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "Unknown error"))
        }
    }
}