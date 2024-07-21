package com.program.bookss.domain.usecase.User

import com.program.bookss.domain.repository.UserRepository
import javax.inject.Inject

class GetRoleUser @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId : String)=repository.getRoleUser(userId)

}
