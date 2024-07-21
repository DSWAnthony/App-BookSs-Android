package com.program.bookss.data

import com.google.firebase.firestore.FirebaseFirestore
import com.program.bookss.domain.model.User
import com.program.bookss.domain.repository.UserRepository
import com.program.bookss.utils.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
): UserRepository {
    private var operationSuccessful = false

    override fun getUserDetails(userId: String): Flow<Response<User>> = callbackFlow{
        Response.Loading
        val snapShotListener = firebaseFirestore.collection("users")
            .document(userId)
            .addSnapshotListener{snapshot,error ->
                val response = if (snapshot != null) {
                    val userInfo = snapshot.toObject(User::class.java)
                    Response.Success<User>(userInfo!!)
                }
                else{
                    Response.Error(error?.message?:error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose{
            snapShotListener.remove()
        }
    }

    override fun setUserDetails(
        userId: String,
        name: String,
        userName: String,
        imgUrl: String)  :Flow<Response<Boolean>> = flow{

            try {
                val userObj = mutableMapOf<String,String>()
                userObj["name"] = name
                userObj["userName"] = userName
                userObj["imgUrl"] = imgUrl

                firebaseFirestore.collection("users").document(userId).update(userObj as Map<String,Any>)
                    .addOnSuccessListener {

                    }.await()

                if (operationSuccessful){
                    emit(Response.Success(operationSuccessful))
                }else{
                    emit(Response.Error("No se Edito Correctamente"))
                }
            }
            catch (e :Exception){
                Response.Error(e.localizedMessage?:"An  Unexpected Error")
            }
    }

    override fun getRoleUser(userId: String): Flow<Response<String>> = callbackFlow {
        Response.Loading
        val documentReference = firebaseFirestore.collection("users").document(userId)
        val snapShotListener = documentReference.addSnapshotListener { snapshot, error ->
            val response = if (snapshot != null && snapshot.exists()) {
                val role = snapshot.getString("role")
                Response.Success(role ?: "")
            } else {
                Response.Error(error?.message ?: "An Unexpected Error")
            }
            trySend(response).isSuccess
        }
        awaitClose {
            snapShotListener.remove()
        }
    }


}