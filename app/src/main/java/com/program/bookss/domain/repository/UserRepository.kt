package com.program.bookss.domain.repository

import com.program.bookss.domain.model.User
import com.program.bookss.utils.Response
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserDetails(userId :String): Flow<Response<User>>

    fun setUserDetails(userId: String,
                       name:String,
                       userName:String,
                       imgUrl:String) :Flow<Response<Boolean>>

    fun getRoleUser(userId: String): Flow<Response<String>>
}