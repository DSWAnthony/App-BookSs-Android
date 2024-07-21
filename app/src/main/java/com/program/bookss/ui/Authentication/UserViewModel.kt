package com.program.bookss.ui.Authentication

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.program.bookss.domain.model.User
import com.program.bookss.domain.usecase.User.UserUseCases
import com.program.bookss.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val auth : FirebaseAuth,
    private val userUseCases: UserUseCases
): ViewModel() {

    private val userId = auth.currentUser?.uid
    private val _getUserData = mutableStateOf<Response<User?>>(Response.Success(null))
    val getUserData : State<Response<User?>> = _getUserData

    private val _setUserData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val setUserData : State<Response<Boolean>> = _setUserData

    private val _userRole = mutableStateOf<Response<String>>(Response.Loading)
    val userRole: State<Response<String>> = _userRole

    fun getUserInfo(){
        if (userId!=null){
            viewModelScope.launch {
                userUseCases.getUserDetails(userId = userId).collect{
                    _getUserData.value = it
                }
            }
        }
    }

    fun setUserInfo(name:String,userName:String,imgUrl:String){
        if (userId!=null){
            viewModelScope.launch {
                userUseCases.setUserDetails(
                    userId = userId,
                    name = name,
                    userName= userName,
                    imgUrl = imgUrl
                ).collect{
                    _setUserData.value = it
                }
            }
        }
    }

    fun getUserRole() {
        if (userId != null) {
            viewModelScope.launch {
                userUseCases.getRoleUser(userId = userId).collect {
                    _userRole.value = it
                }
            }
        }
    }

}