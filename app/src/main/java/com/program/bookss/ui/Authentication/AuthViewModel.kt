package com.program.bookss.ui.Authentication

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.program.bookss.domain.usecase.Authentication.AuthenticationUseCases
import com.program.bookss.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase : AuthenticationUseCases
): ViewModel() {

    val isUserAuthenticated get() = authUseCase.isUserAuthenticated()

    private val _signInState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val signInState : State<Response<Boolean>> = _signInState

    private val _signUpState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val signUpState : State<Response<Boolean>> = _signUpState

    private val _signInOutState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val signInOutState : State<Response<Boolean>> = _signInOutState

    private val _firebaseAuthState = mutableStateOf<Boolean>(false)
    val firebaseAuthState : State<Boolean> = _firebaseAuthState

    fun signOut(){
        viewModelScope.launch {
            authUseCase.fireBaseSignOut().collect{
                _signInOutState.value=it

                if (it ==Response.Success(true)){
                    _signInState.value = Response.Success(false)
                }
            }
        }
    }


    fun signIn(email:String,password :String){
        viewModelScope.launch {
            authUseCase.fireBaseSignIn(email, password).collect {
                _signInState.value = it
            }
        }
    }

    fun signUp(email:String,password :String,userName :String,role : String){
        viewModelScope.launch {
            authUseCase.fireBaseSignUp(email,password, userName,role).collect{
                _signUpState.value = it
            }
        }
    }

    fun getFireBaseAuthState(){
        viewModelScope.launch {
            authUseCase.fireBaseAuthState().collect{
                _firebaseAuthState.value = it
            }
        }
    }

}