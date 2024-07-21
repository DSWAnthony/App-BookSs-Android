package com.program.bookss.domain.usecase.User

import com.program.bookss.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDetails @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(userId:String) = repository.getUserDetails(userId = userId)
}