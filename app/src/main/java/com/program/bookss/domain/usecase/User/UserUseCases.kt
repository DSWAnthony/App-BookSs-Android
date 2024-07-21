package com.program.bookss.domain.usecase.User

data class UserUseCases (
    val getUserDetails: GetUserDetails,
    val setUserDetails: SetUserDetails,
    val getRoleUser: GetRoleUser
)
