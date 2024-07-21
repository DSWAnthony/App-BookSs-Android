package com.program.bookss.domain.usecase.User

import com.program.bookss.domain.repository.UserRepository
import javax.inject.Inject

class SetUserDetails @Inject constructor(
    private val repository: UserRepository
) {

    operator fun invoke(userId:String,name:String,userName:String,imgUrl:String) =
        repository.setUserDetails(userId = userId,name, userName, imgUrl)
}